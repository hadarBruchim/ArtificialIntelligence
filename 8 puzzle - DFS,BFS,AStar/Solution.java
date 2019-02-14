import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * Solution
 * @author hadas
 *
 */
public class Solution {
	private int nodes;
	private String goalWay;
	int costForAlgo;
	
	/**
	 * constructor
	 * @param nodes
	 * @param wayBack
	 * @param cost
	 */
	public Solution(int nodes, String wayBack, int cost) {
		this.nodes = nodes;
		this.goalWay = wayBack;
		this.costForAlgo = cost;
	}
	
	/**
	 * getter method
	 * @return nodes
	 */
	public int getNodes() {
		return nodes;
	}
	/**
	 * getter method
	 * @return depth
	 */
	public int getDepth() {
        return costForAlgo;
    }
	/**
	 * get way back to goal
	 * @return goal way string
	 */
	public String getWayBack() {
		return goalWay;
	}
	/**
	 * getter method
	 * @return cost
	 */
	public int getCost() {
		return costForAlgo;
	}
	
	/**
	 * output function
	 * @throws IOException
	 */
	 public void output() throws IOException{
	     File output = new File("output.txt");
         FileOutputStream foutput = new FileOutputStream(output);
         BufferedWriter writeToFile = new BufferedWriter(new OutputStreamWriter(foutput));
         try {
             writeToFile.write(getWayBack() + " " + getNodes() + " " + getCost());
         } catch (IOException e) {
             e.printStackTrace();
         }
         try {
             writeToFile.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
	    }
}