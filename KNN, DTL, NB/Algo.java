import java.util.List;

public abstract class Algo {
    private double accuracy;

    /**
     * Calculate the Accuracy of the algorithm
     * @param knnResult
     * @param allData
     */
    public void calculateAccuracy(List<String> knnResult, List<String> allData) {
        int counter = 0;
        for(int i=0; i<knnResult.size(); i++){
            if(knnResult.get(i).equals(allData.get(i)))
                counter++;
        }
        int n=knnResult.size();
        this.accuracy=(double)counter/n;
    }
    
    /**
     * Get the Accuracy of the specific algorithm
     * @return
     */
    public double getAccuracy(){
        return accuracy;
    }
}
