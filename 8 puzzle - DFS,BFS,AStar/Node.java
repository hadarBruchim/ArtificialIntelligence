
/**
 * Node
 * @author hadas
 *
 */
public class Node {
    int y;
	int x;
	int val;
	
	/**
	 * constructor
	 * @param x
	 * @param y
	 * @param value
	 */
	public Node(int x, int y, int value) {
		this.x = x;
		this.y = y;
		this.val = value;
	}
	/**
	 * getter method
	 * @return x
	 */
	public int getX() {
		return x;
	}
	
	   /**
     * getter method
     * @return y
     */
    public int getY() {
        return y;
    }
	
    /**
     * getter method
     * @return val
     */
    public int getVal() {
        return val;
    }
    
	/**
	 * setter method
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * setter method
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * setter method
	 * @param val
	 */
	public void setVal(int val) {
		this.val = val;
	}
	
}
