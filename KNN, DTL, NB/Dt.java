import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class Dt extends Algo{
    private List<Attribute> dt_train;
    private List<Attribute> dt_test;
    private Attribute pred_test;
    private Attribute prad_train;
    private Node finalTree;
    
    /**
     * Constructor of Dt
     * @param listOfAttr
     * @param listOfAttr_test
     * @param prad_test
     * @param prad
     * @param title 
     */
    public Dt(List<Attribute> listOfAttr, List<Attribute> listOfAttr_test, Attribute prad_test, Attribute prad) {
        dt_train=listOfAttr;
        dt_test=listOfAttr_test;
        pred_test=prad_test;
        prad_train=prad;
        }
    
    /**
     * The method run the test on the tree we build
     * @return list with the result of the tests
     */
    public List<String> testTheTree(){
        List<String>dtResult=new ArrayList<String>();
        List<String> nameAttribute=new ArrayList<String>();
        for(int i=0; i<dt_train.size();i++){
            nameAttribute.add(dt_train.get(i).getVal());
        }

        List<Pair2<String,String>>testValue;
        
        /*number of tests to check*/
        int numOfExample=dt_test.get(0).getAllData().size();
        
        for(int j=0; j<numOfExample; j++){
            testValue=new ArrayList<Pair2<String,String>>();
            for(int m=0; m<dt_test.size();m++){

            /*get all the attribute of specific example test*/
                testValue.add(new Pair2<String,String>(dt_test.get(m).getVal(),dt_test.get(m).getSpecificData(j)));
            }
            
            /*check what the final classification*/
            String ans=checkResultTree(testValue,finalTree,testValue.size()-1);
            dtResult.add(ans);
        }
        /*calculate Accuracy of Dt*/
        calculateAccuracy(dtResult,pred_test.getAllData());
        return dtResult;
    }
           
    /**
     * The method run on the tree and check value to specific example 
     * recursive method
     * @param testValue
     * @param root
     * @param i - number of attribute to check
     * @return the classification
     */
    private String checkResultTree(List<Pair2<String, String>> testValue, Node root, int i) {
        String str=null;
        String rootValue= root.getAttrName();
        String feature=null;
        /*find place of specific string*/
        for (Pair2<String, String> temp : testValue) {
            if(temp.getKey().equals(rootValue)){
                feature=temp.getValue();
                break;
            }
        }
        if(i==0 || root.getMaps()==null){
            return root.getAttrName();
        }
        else{
            for ( String key : root.getMaps().keySet() ) {
                if(key.equals(feature))
                    str=checkResultTree(testValue, root.getMaps().get(key),i-1);
            }
        }    
        return str;
    }

    /**
     * The method build a tree from examples given
     * @return tree
     */
    public Node buildTree() {
        Node tree= new Node();
        tree.setFather(null);
        prad_train.getTags().sort(String::compareToIgnoreCase);
        Node func = id3(dt_train,dt_train,prad_train,null,null,null,tree);
        setFinalTree(func);
        return func;
    }
    
    /**
     * The method update the examples list according to a specific attribute
     * @param dt_train2
     * @param attributeName
     * @param specificFeature
     * @return update list
     */
    private List<Attribute> updateSet(List<Attribute> dt_train2, String attributeName, String specificFeature) {
        List<Attribute> newList= dt_train2;
        List<Integer>numList=new ArrayList<Integer>();
        int placeOfBest=-1;
        for(int i=0; i<dt_train2.size(); i++){
            if(dt_train2.get(i).getVal()==attributeName){
                placeOfBest= i;
                break;
            }
        }
        List<Attribute>help=new ArrayList<Attribute>();
        if(placeOfBest!=-1){
            for(int k=0; k<dt_train2.get(placeOfBest).getAllData().size(); k++){
                if(newList.get(placeOfBest).getSpecificData(k).equals(specificFeature))
                    numList.add(k);
            }        
            for(int i=0; i<dt_train2.size(); i++){
               Attribute a=new Attribute(dt_train2.get(i).getVal(), null, dt_train2.get(i).getTags());
               List<String> newArr = new ArrayList<String>();
               for(int j=0; j<numList.size(); j++){
                  int helpme=numList.get(j);
                  newArr.add(newList.get(i).getSpecificData(helpme));
                }
               a.setAllData(newArr);
               help.add(a);
            }
        }
        return help;
    }

    /**
     * The id3 method
     * recursive method
     * @param examples
     * @param classification
     * @param bestAttr
     * @param specificFeature
     * @param defult
     * @param fatherNode
     * @return node
     */
    private Node id3(List<Attribute> examples,List<Attribute> attributes, Attribute classification,
            String bestAttr,String specificFeature, String defult, Node fatherNode) {
        
        /*Stop conditions*/
        if(classification.getAllData().size()==0){
            return new Node(defult,null,fatherNode);
        }    
         /*All example have same classification*/
        List<Pair2<String,Integer>>numberOfYesOrNo=new ArrayList<Pair2<String,Integer>>();
        for(int i=0; i<classification.getTags().size();i++){
            String yesOrNO=classification.getTags().get(i);
            int number=calcNumberOfTags(yesOrNO,classification);
            numberOfYesOrNo.add(new Pair2<String,Integer>(yesOrNO,number));
        }
        
        /*Stop conditions - mode*/
        if(numberOfYesOrNo.get(0).getValue()==0){
            return new Node(numberOfYesOrNo.get(1).getKey(),null,fatherNode);
        }
        if(numberOfYesOrNo.get(1).getValue()==0){
            return new Node(numberOfYesOrNo.get(0).getKey(),null,fatherNode);
        }
        
        /*calculate who is the maximum yes or no*/
        String defultMaxString = thwMode(numberOfYesOrNo);

        /*calculate entropy*/
        int sizeOfTrain=classification.getAllData().size();
        double entropyDesicion=calcEntropyMain(numberOfYesOrNo,sizeOfTrain);
        
        /*calculate gain*/
        List<Pair2<String,Double>>Allgains = new ArrayList<>();

        for(int i=0; i<examples.size()-1; i++){
            double gain=calcGain(entropyDesicion,examples.get(i),sizeOfTrain,examples,classification,bestAttr);
            Allgains.add(new Pair2<String,Double>(examples.get(i).getVal(), gain));
        }
        
        /*choose max gain*/
        Pair2<String,Double> maxgain = maxGain(Allgains);
        
        int placeOfBest = IndexBest(examples, maxgain);
        
        /*Stop conditions = mode*/
        if(placeOfBest==-1){
            return new Node(defult,null,fatherNode);
        }
        
        Node newNode= new Node();
        newNode.setAttrName(maxgain.getKey());
        newNode.setFather(fatherNode);
        
        Attribute best=null;
        if(placeOfBest!=-1){
            best=examples.get(placeOfBest);
        }
        
         if(placeOfBest!=-1){
            best.getTags().sort(String::compareToIgnoreCase);
            for(int k=0; k<best.getTags().size(); k++){
                List<Attribute> afterSet = updateSet(examples,maxgain.getKey(),best.getTags().get(k));
                Node subTree=id3(afterSet,attributes, afterSet.get(afterSet.size()-1), maxgain.getKey(),
                            best.getTags().get(k), defultMaxString,newNode);
                newNode.addToMap(best.getTags().get(k),subTree);
            }
         }
        return newNode;
    }
    
    /**
     * The method return the max between yes or no
     * @param numberOfYesOrNo
     * @return string
     */
    private String thwMode(List<Pair2<String, Integer>> numberOfYesOrNo) {
        String defultMaxString = null;
        int maxNum=0;
        for (Pair2<String, Integer> temp : numberOfYesOrNo) {
            if(temp.getValue()>maxNum){
                defultMaxString=temp.getKey();
                maxNum=temp.getValue();
            }
        }
        return defultMaxString;
    }

    /**
     * The method return the place in the list of attribute
     * @param examples
     * @param maxgain
     * @return integer
     */
    private int IndexBest(List<Attribute> examples, Pair2<String, Double> maxgain) {
        int placeOfBest=-1;
        for(int i=0; i<examples.size(); i++){
            if(examples.get(i).getVal()==maxgain.getKey()){
                placeOfBest= i;
                break;
            }
        }
        return placeOfBest;
    }

    /**
     * The method calculate entropy
     * @param tags
     * @param sizeOfTrain
     * @return the entropy
     */
    private double calcEntropyMain(List<Pair2<String, Integer>> tags, int sizeOfTrain) {
        double entropy=0;
        for(int i=0; i<tags.size();i++){
            double val=(double)(tags.get(i).getValue());
            double prob=val/sizeOfTrain;
            if(prob==0)
                continue;
            double calc=(-(prob)*(Math.log(prob)/Math.log(2)));
            entropy+=calc;
            }
        return entropy;
    }
    
    /**
     * The method calculate gain
     * @param MainEntropy
     * @param attr
     * @param sizeOfTrain
     * @param dt_train2
     * @param prad_train2
     * @return gain
     */
    private double calcGain(double MainEntropy,Attribute attr,int sizeOfTrain, List<Attribute> dt_train2,
            Attribute prad_train2,String bestAttr) {
        double entropy=MainEntropy;
        String option1=prad_train2.getTags().get(0);
        String option2=prad_train2.getTags().get(1);
        for(int i=0; i<attr.getTags().size();i++){
            int numberOfPic=calcNumberOfTags(attr.getTags().get(i),attr);
            int numOfFeature1=calculateWithClassi(attr.getTags().get(i),attr,prad_train2,option1);
            int numOfFeature2=calculateWithClassi(attr.getTags().get(i),attr,prad_train2,option2);
            if(numberOfPic==0){
                entropy=entropy-0;
                continue;
            }
            double prob1=(double)numOfFeature1/(double)numberOfPic;
            double prob2=(double)numOfFeature2/(double)numberOfPic;
            if(numOfFeature1==0 || numOfFeature2==0){
                entropy=entropy-0;
                continue;
            }else{
            double calc1=(-(prob1)*(Math.log(prob1)/Math.log(2)));
            double calc2=(-(prob2)*(Math.log(prob2)/Math.log(2)));
            double help=((double)numberOfPic/(double)sizeOfTrain)*(calc1+calc2);
            
            entropy=entropy-help;
            }
        }
        return entropy;
    }
    
    /**
     * calculate With Classi
     * @param str
     * @param attr
     * @param prad_train
     * @param option
     * @return
     */
    private int calculateWithClassi(String str, Attribute attr, Attribute prad_train, String option) {
        int count=0;
        for(int i=0; i<attr.getAllData().size();i++){
            if(attr.getSpecificData(i).equals(str))
                if(prad_train.getSpecificData(i).equals(option))
                count++;
        }
        return count;
    }

    /**
     * The method return the max gain
     * @param gains
     * @return the max gain
     */
    private Pair2<String,Double> maxGain(List<Pair2<String,Double>> gains){
        double maxVal=0;
        String maxValString=null;
        for (Pair2<String, Double> pair2 : gains) {
            String key = pair2.getKey();
            double value = pair2.getValue();
            if(value>maxVal){
                maxVal=value;
                maxValString=key;
            }
        }
        return new Pair2<String,Double>(maxValString,maxVal);
    }
    
    /**
     * The method calculate number of tags in specific attribute
     * @param str
     * @param prad_train
     * @return number of tags
     */
    private int calcNumberOfTags(String str, Attribute prad_train) {
        int count=0;
        for(int i=0; i<prad_train.getAllData().size();i++){
            if(prad_train.getSpecificData(i).equals(str))
                count++;
        }
        return count;
    }

    /**
     * @return the finalTree
     */
    public Node getFinalTree() {
        return finalTree;
    }

    /**
     * @param finalTree the finalTree to set
     */
    public void setFinalTree(Node finalTree) {
        this.finalTree = finalTree;
    }

    /**
     * The method print the final tree
     * @throws FileNotFoundException
     */
    public void printTree() throws FileNotFoundException {
        File output = new File("C:/Users/hadas/workspace/Ai_ex2/output_tree.txt");
        //File output = new File("output_tree.txt");

        FileOutputStream foutput = new FileOutputStream(output);
        BufferedWriter writeToFile = new BufferedWriter(new OutputStreamWriter(foutput));
        String str= print(finalTree,1);
        try {
            /*delete the last "\n" */
            if (str != null)
                str = str.substring(0, str.length() - 1);
            
            writeToFile.write(str);
            writeToFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        
    }

    /**
     * The print method - recursive
     * @param finalTree2
     * @param tab
     * @return string of the tree
     */
    private String print(Node finalTree2,int tab) {
        String str="";
        List<String> sortedKeys = finalTree2.getMaps().keySet().stream().sorted().collect(Collectors.toList());
        if(finalTree2.getMaps()!=null)     
        for ( String key : sortedKeys ) {
            for(int i=0; i<tab-1; i++)
                str+='\t';
            if(finalTree2.getMaps().get(key).getAttrName().equals(prad_train.getTags().get(0))||
                    finalTree2.getMaps().get(key).getAttrName().equals(prad_train.getTags().get(1))
                    ){
                if(tab==1){
                str += finalTree2.getAttrName() + "=" + key + ":" + finalTree2.getMaps().get(key).getAttrName()+ '\n';
                }else{
                 str += "|" + finalTree2.getAttrName() + "=" + key + ":" + finalTree2.getMaps().get(key).getAttrName()+ '\n';
                }
            }else{
                if(tab==1){
                    str += finalTree2.getAttrName() + "=" + key + "\n";
                }
                else{
                    str += "|" + finalTree2.getAttrName() + "=" + key + '\n';
                }
                str += print(finalTree2.getMaps().get(key), tab + 1);
            }
        }
        return str;
    }
    
}
