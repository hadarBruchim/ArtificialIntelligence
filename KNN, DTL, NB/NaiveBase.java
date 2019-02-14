import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NaiveBase extends Algo{
    private List<Attribute> naive_train;
    private List<Attribute> naive_test;
    private Attribute pred_test;
    private Attribute prad_train;
    private double accuracy;
    
    /**
     * Constructor NaiveBase
     * @param listOfAttr
     * @param listOfAttr_test
     * @param prad_test1
     * @param prad_train1
     */
    public NaiveBase(List<Attribute> listOfAttr, List<Attribute> listOfAttr_test,
            Attribute prad_test1,Attribute prad_train1){
        naive_train=listOfAttr;
        naive_test=listOfAttr_test;
        pred_test=prad_test1;
        prad_train=prad_train1;
        accuracy=0;
    }
    
    /**
     * The method calculate the NaiveBase for the test
     * @return
     */
    public List<String> calculate() {
        List<String>naiveResult=new ArrayList<String>();
        HashMap<String,Integer>map=new HashMap<String,Integer>();

        for(int i=0; i<prad_train.getAllData().size();i++){
            if(!map.containsKey(prad_train.getSpecificData(i))){
                map.put(prad_train.getSpecificData(i),1);
            }else{
                map.put(prad_train.getSpecificData(i), map.get(prad_train.getSpecificData(i)) + 1);
            }
        }
        
        List<Pair2<String,Double>>prior=new ArrayList<Pair2<String,Double>>();;
        for ( Map.Entry<String,Integer> entry : map.entrySet()) {
            double num=(double)entry.getValue()/prad_train.getAllData().size();
            prior.add(new Pair2<String,Double>(entry.getKey(),num));
        }
        
        int numOfExample=naive_test.get(0).getAllData().size();
        naiveBaseProcess(naiveResult, map, prior, numOfExample);    
        calculateAccuracy(naiveResult,pred_test.getAllData());
        return naiveResult;
    }

    private void naiveBaseProcess(List<String> naiveResult, HashMap<String, Integer> map,
            List<Pair2<String, Double>> prior, int numOfExample) {
        List<String> testValue;
        for(int j=0; j<numOfExample; j++){
            testValue=new ArrayList<String>();
            for(int m=0; m<naive_test.size();m++){
                testValue.add(naive_test.get(m).getSpecificData(j));
            }

            List<Pair2<String,List<Pair2<String,Double>>>>probList=new ArrayList<Pair2<String,List<Pair2<String,Double>>>>();
            List<Pair2<String,Double>>helper;
            for (Map.Entry<String,Integer> entry : map.entrySet()) {
                helper=new ArrayList<Pair2<String,Double>>();
                for(int p=0;p<testValue.size()-1;p++){    
                    int sumOfClassifi=calcTheProb(entry.getKey(),testValue.get(p),naive_train.get(p));
                    int kSecond=naive_train.get(p).getTags().size();
                    /*smoothing*/
                    helper.add(new Pair2<String,Double>(testValue.get(p),(double)(sumOfClassifi+1)/(entry.getValue()+kSecond)));
                }
                Pair2<String,List<Pair2<String,Double>>> specificProb=new Pair2<String,List<Pair2<String,Double>>>(entry.getKey(),helper);
                probList.add(specificProb);
            }
            int numOfClassifi=prad_train.getTags().size();
            HashMap<String,Double>mapResualt=new HashMap<String,Double>(numOfClassifi);
            double sum=0;
            for(int k=0;k<numOfClassifi; k++){
                if(prior.get(k).getKey().equals(probList.get(k).getKey())) {
                    sum=prior.get(k).getValue();
                    for(int p=0;p<testValue.size()-1;p++) {    
                        sum*=probList.get(k).getValue().get(p).getValue();
                    }
                    mapResualt.put(prior.get(k).getKey(),sum);
                    sum=0;
                }
            }
            double maxVal=0;
            String maxValString=null;
            
            for ( Map.Entry<String,Double> entry : mapResualt.entrySet()) {
                String key = entry.getKey();
                double tab = entry.getValue();
                if(tab>maxVal){
                    maxVal=tab;
                    maxValString=key;
                }
            }
            naiveResult.add(maxValString); 
        }
    }

    /**
     * The method calculate probability
     * @param keyClassifi
     * @param attr
     * @param attribute
     * @return
     */
    private int calcTheProb(String keyClassifi, String attr, Attribute attribute) {
        int counter=0;
        for(int i=0; i<prad_train.getAllData().size();i++){
            if(prad_train.getSpecificData(i).equals(keyClassifi)){
                if(attribute.getSpecificData(i).equals(attr))
                    counter++;
            }
        }
        return counter;
    }
    
}
