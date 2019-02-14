import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Knn extends Algo{
    private List<Attribute> knn_train;
    private List<Attribute> knn_test;
    private Attribute pred_test;
    private int k;
    private double accuracy;
    
    /**
     * Constructor of Knn
     * @param listOfAttr
     * @param listOfAttr_test
     * @param prad_test
     * @param kNumber
     */
    public Knn(List<Attribute> listOfAttr, List<Attribute> listOfAttr_test,
            Attribute prad_test,int kNumber){
        knn_train=listOfAttr;
        knn_test=listOfAttr_test;
        pred_test=prad_test;
        k=kNumber;
        accuracy=0;
    }
    
    /**
     * calculate Knn Algo
     * @return List<String>
     */
    public List<String> Hamming(){
        int counter=0;
        List<String>KnnResult=new ArrayList<String>(); // the prediction values of all  tests
        List<String>testValue;
        int numOfExample=knn_test.get(0).getAllData().size(); //number of tests
        for(int j=0; j<numOfExample; j++){
            testValue=new ArrayList<String>();
            for(int m=0; m<knn_test.size();m++){
                
            /*get all the attribute of specific example test*/
                testValue.add(knn_test.get(m).getSpecificData(j));
            }

            counter=0;
            /*number of trains*/
            int numOfTrain=knn_train.get(0).getAllData().size();    
            List<String>trainValue;
            List<Pair2<Integer,String>>listDist=new ArrayList<Pair2<Integer,String>>();
            for(int r=0; r<numOfTrain; r++){
                trainValue=new ArrayList<String>();
                for(int m=0; m<knn_train.size();m++){
                    
                  /*get all the attribute of specific example train*/
                    trainValue.add(knn_train.get(m).getSpecificData(r));
                }
                
                /*calculate humming distance*/
                for(int m=0; m<knn_train.size()-1;m++){
                    String help1=testValue.get(m);
                    String help2=trainValue.get(m);
                    if(!help1.equals(help2)){
                        counter++;
                    }
                }
                listDist.add(new Pair2<Integer,String>(counter,trainValue.get(knn_train.size()-1)));
                counter=0;
            }
            
           /*sort the list by the key value*/
           listDist.sort(new Comparator<Pair2<Integer, String>>() {
                @Override
                public int compare(Pair2<Integer, String> o1, Pair2<Integer, String> o2) {
                    if (o1.getKey() < o2.getKey()) {
                        return -1;
                    } else if (o1.getKey().equals(o2.getKey())) {
                        return 0; 
                    } else {
                        return 1;
                    }
                }
            });
           
           /*get k minimum distance*/
           List<String>finalDescision= new ArrayList<String>();
           for(int p=0;p<k;p++){
               finalDescision.add(listDist.get(p).getValue());
           }
           
           /*count the number of each classification*/
           HashMap<String,Integer>map=new HashMap<String,Integer>();
           int count=1;
           for(int p=0;p<k;p++){
               if(!map.containsKey(finalDescision.get(p))){
                   map.put(finalDescision.get(p),count);
               }else{
                   map.put(finalDescision.get(p), map.get(finalDescision.get(p)) + 1);
               }
           }
           
           /*choose the max classification*/
           String maxValString = maxValueInKnn(map);
           
           /*list of results*/
           KnnResult.add(maxValString);
        }
        
        /*calculate Accuracy to knn*/
        calculateAccuracy(KnnResult,pred_test.getAllData());
        
        /*return the final list*/
        return KnnResult;
    }

    private String maxValueInKnn(HashMap<String, Integer> map) {
        int maxVal=0;
           String maxValString=null;
           
           for ( Map.Entry<String,Integer> entry : map.entrySet()) {
               String key = entry.getKey();
               int tab = entry.getValue();
               if(tab>maxVal){
                   maxVal=tab;
               maxValString=key;
               }
           }
        return maxValString;
    }

}
