
/**
 * HeuristicState
 * @author hadas
 *
 */
public class HeuristicState extends State implements Comparable<HeuristicState> {
    private int fValue;
	private int gValueState;
	private int hValueState;
	private int[][] goalState;
	private int timeCreate;

	/**
	 * constructor
	 * @param arrayOfNums
	 * @param parent
	 * @param size
	 * @param state
	 */
	public HeuristicState(int[] arrayOfNums, char parent, int size, State state) {
		super(arrayOfNums, parent, size, state);
        this.fValue = 0;
        this.gValueState = 0;
		this.hValueState = 0;
		this.goalState = new int[size][size];
	}

	/**
	 * get time
	 * @return time
	 */
	public int getTime(){
	    return this.timeCreate;
	}

	/**
	 * calculate the Heuristic function
	 */
	public void calcHeuristicManhattan() {
		createCurrentState();
		initGoalState();
		int placeX, placeY;
		for (int i = 0; i < super.size; i++) {
			for (int j = 0; j < super.size; j++) {
				int val = this.current[i][j].getVal(); 
				if(val != goalState[i][j] && val!=0) {
                    placeY = (val-1)%size;
					placeX = (val-1)/size;
				}else {
					continue;
				}
				hValueState += (Math.abs(i-placeX) + Math.abs(j - placeY));
			}
		}
	}

    /**
     * @return the gValue
     */
    public int getgValue() {
        return gValueState;
    }

    /**
     * @return the hValue
     */
    public int gethValue() {
        return hValueState;
    }

    /**
     * @return the fValue
     */
    public int getfValue() {
        return fValue;
    }
    
    /**
     * @return the matrix
     */
    public int[][] getMatrix() {
        return goalState;
    }

    /**
     * set time in open list
     * @param num
     */
    public void setTime(int num){
        this.timeCreate=num;
    }
    

    /**
     * @param gValue
     */
    public void setgValue(int gValue) {
        this.gValueState = gValue;
    }

    /**
     * @param hValue 
     */
    public void sethValue(int hValue) {
        this.hValueState = hValue;
    }

    /**
     * calculate and set f value
     * @param fValue the fValue to set
     */
    public void setFValue() {
        calcHeuristicManhattan();
        if (this.cameFrom != null) {
            this.gValueState = ((HeuristicState) this.cameFrom).gValueState + 1;
            this.fValue = this.hValueState + this.gValueState;
        } else {
            this.gValueState = 0; //the goal
        }
    }
    

    /**
     * goal state
     */
    public void initGoalState() {
        int numbers = size * size;
        int temp = 1;
        for (int i = 0; i < super.size; i++) {
            for (int j = 0; j < super.size; j++) {
                if (temp <= numbers) {
                    goalState[i][j] = temp;
                    temp++;
                }
            }
        }
        goalState[size-1][size-1] = 0; //the last number
    }

    /**
     * @param matrix
     */
    public void setMatrix(int[][] matrix) {
        this.goalState = matrix;
    }

    @Override
    public int compareTo(HeuristicState arg0) {
        int bool;
        if((bool=this.getfValue() - arg0.getfValue())!=0)
        return bool;
        else
            return this.getTime() - arg0.getTime();
    }

}
