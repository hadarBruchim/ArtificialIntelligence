import java.util.ArrayList;
import java.util.List;

public class Attribute {
    private String val;
    private List<String>allData;
    private List<String>tags;
    
    /**
     * Constructor of Attribute
     * @param value
     * @param allData1
     * @param tags1
     */
    public Attribute(String value,List<String>allData1,List<String>tags1){
        val=value;
        allData=allData1;
        tags=tags1;
    }
    
    /**
     * The method calculate the number of specific tag
     * @param tag
     * @return number
     */
    public int getNumberOfSpecificTag(String tag){
        int count =0;
        for(int j=0; j<allData.size(); j++){
            if(tag.equals(allData.get(j)))
                    count++;
        }
        return count;
    }
    
    /**
     * The method return the number of each tag in this attribure
     * @return list
     */
    public List<Pair2<String,Integer>> numberOfTag(){
        List<Pair2<String,Integer>> result=new ArrayList<Pair2<String,Integer>>();
        for(int i=0; i<tags.size();i++){
            result.add(new Pair2<String,Integer>(tags.get(i),getNumberOfSpecificTag(tags.get(i))));
        }
        return result;
    }
    
    /**
     * Get method - specific data
     * @param i
     * @return string
     */
    public String getSpecificData(int i){
        return allData.get(i);
    }
    
    /**
     * @return the val
     */
    public String getVal() {
        return val;
    }
    /**
     * @return the allData
     */
    public List<String> getAllData() {
        return allData;
    }
    /**
     * @return the tags
     */
    public List<String> getTags() {
        return tags;
    }
    /**
     * @param val the val to set
     */
    public void setVal(String val) {
        this.val = val;
    }
    /**
     * @param allData the allData to set
     */
    public void setAllData(List<String> allData) {
        this.allData = allData;
    }
    /**
     * @param tags the tags to set
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }   
}
