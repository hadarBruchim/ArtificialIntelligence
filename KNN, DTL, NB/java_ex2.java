import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class java_ex2 {
public static void main(String[] args) {
        
    try{
        Path path = Paths.get("C:/Users/hadas/workspace/Ai_ex2/src/train.txt");
        //Path path = Paths.get("train.txt");
        long lineCount = Files.lines(path).count();
        
        File file = new File("C:/Users/hadas/workspace/Ai_ex2/src/train.txt");
        //File file = new File("train.txt");
        BufferedReader buffer=new BufferedReader(new FileReader(file));
        String [][] data_train= convertFileToData(buffer,(int)lineCount);

        List<Attribute> listOfAttr= attribute(data_train);
        Attribute prad=listOfAttr.get(listOfAttr.size()-1);
        
        Path path_test = Paths.get("C:/Users/hadas/workspace/Ai_ex2/src/test.txt");
        //Path path_test = Paths.get("test.txt");
        long lineCount_test = Files.lines(path_test).count();
        
        File file_test = new File("C:/Users/hadas/workspace/Ai_ex2/src/test.txt");
        //File file_test = new File("test.txt");
        BufferedReader buffer_test=new BufferedReader(new FileReader(file_test));
        String [][] data_test= convertFileToData(buffer_test,(int)lineCount_test);
       // List<String>Title= title(data_test);
        List<Attribute> listOfAttr_test= attribute(data_test);
        Attribute prad_test=listOfAttr_test.get(listOfAttr_test.size()-1);
        
       /*KNN*/
        Knn knn_Algo= new Knn(listOfAttr,listOfAttr_test,prad_test,5);
        List<String>KnnResult=knn_Algo.Hamming();
        double knnAccuracy=knn_Algo.getAccuracy();
        //System.out.println(knnAccuracy);
        
        /*Naive base*/
        NaiveBase naiveBase_algo=new NaiveBase(listOfAttr,listOfAttr_test,prad_test,prad);
        List<String>naiveResult=naiveBase_algo.calculate();
        double naiveAccuracy=naiveBase_algo.getAccuracy();
        //System.out.println(naiveAccuracy);
        
        /*Dt*/
        Dt decisionTree_algo= new Dt(listOfAttr,listOfAttr_test,prad_test,prad);
        Node buildDtTree=decisionTree_algo.buildTree();
        List<String>decisionTreeResult=decisionTree_algo.testTheTree();
        double decisionTreeAccuracy=decisionTree_algo.getAccuracy();
        //System.out.println(decisionTreeAccuracy);
        decisionTree_algo.printTree();
        
        createOutputFile(prad_test, KnnResult, knnAccuracy, naiveResult, naiveAccuracy, decisionTreeResult,
                decisionTreeAccuracy);
        
} catch (IOException e) {
    e.printStackTrace();
}
}

private static void createOutputFile(Attribute prad_test, List<String> KnnResult, double knnAccuracy,
        List<String> naiveResult, double naiveAccuracy, List<String> decisionTreeResult, double decisionTreeAccuracy)
        throws FileNotFoundException, IOException {
    File output = new File("C:/Users/hadas/workspace/Ai_ex2/output.txt");
    //File output = new File("output.txt");
    FileOutputStream foutput = new FileOutputStream(output);
    BufferedWriter writeToFile = new BufferedWriter(new OutputStreamWriter(foutput));
    writeToFile.write("Num\tDt\tKNN\tnaiveBase\t\n");
    for(int i=0;i<prad_test.getAllData().size();i++){
        try {
            writeToFile.write((i+1)+"\t"+decisionTreeResult.get(i)+"\t"+KnnResult.get(i)+"\t"+naiveResult.get(i)+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    /*accuracy:*/
    double valueKnn = roundResualt(knnAccuracy);
    double valueNaive = roundResualt(naiveAccuracy);
    double valueDt = roundResualt(decisionTreeAccuracy);
    writeToFile.write("\t"+valueDt+"\t"+valueKnn + "\t" +valueNaive);
    writeToFile.close();
}

private static double roundResualt(double knnAccuracy) {
    double valueKnn = Math.ceil(knnAccuracy*100) / 100;
    return valueKnn;
}

/*private static List<String> title(String[][] data_test) {
    List<String> titles = new ArrayList<>();
    for(int i=0; i<data_test[0].length; i++){
        titles.add(data_test[0][i]);
    }
    return titles;
}*/

/**
 * The structure of the data
 * @param data
 * @return List<Attribute> 
 */
private static List<Attribute> attribute(String[][] data) {
    List<List<String>> columns=new ArrayList<List<String>>();
    Set<Pair2<String,String>>infoSet=new HashSet<Pair2<String,String>>(); 
    int[]numberTag=new int[data[0].length]; //list of number of  tags in each columns
    int count=0,countAll=0,max=0;
    
    for(int i=0; i<data[0].length;i++){ // Column
        List<String>col=new ArrayList<String>();
        
        for(int j=1; j<data.length;j++){ // rows
            Pair2<String,String> help=new Pair2<String, String>((data[0][i]),(data[j][i]));       
            infoSet.add(help); // add type of tag - to set. no duplicate
            col.add(data[j][i]); // add to list with specific Attribute
        }
        columns.add(col); //all the data in specific Attribute
        countAll=infoSet.size(); //number of features
        if(i!=0){
            count+=numberTag[i-1];
        numberTag[i]=infoSet.size()-count;
        if(numberTag[i]>max)
            max=numberTag[i];
        }else{
            numberTag[i]=countAll;
            max=numberTag[i];
        }
    }
    
    String[][] feature=new String[max][numberTag.length]; // by column

    List<Attribute> infoFinal=new ArrayList<Attribute>();
    int line=0;
    for(int i=0; i<numberTag.length;i++){
        List<String>tags=new ArrayList<String>();
        for(Pair2<String,String> object : infoSet) {
            String key=(String)object.getKey();
            String value=(String)object.getValue();
            if(key==data[0][i]){
                tags.add(value);
                feature[line][i]=value;
                line++;
            }
        }

        /*create new attribute*/
        Attribute att=new Attribute(data[0][i],columns.get(i),tags);
        /*list of attribute*/
        infoFinal.add(att);
        line=0;
    }
    return infoFinal;
}

/**
 *The method convert File To Data
 * @param buffer
 * @param lineCount
 * @return
 * @throws IOException
 */
private static String[][] convertFileToData(BufferedReader buffer, int lineCount) throws IOException {
    String line;
    String title=buffer.readLine();
    String [] titleFic= title.split("\t");
    String [][] data= new String[(int) lineCount][titleFic.length];
    data[0]=titleFic;
    int i=1;
    while ((line = buffer.readLine()) != null) {
        data[i]=line.split("\t");
        i++;
    }
    return data;
}

}

