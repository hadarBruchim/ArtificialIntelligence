import java.util.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;;

/**
 * state
 * @author hadas
 *
 */
public class State {
	public State cameFrom;
	public Node[][] current;
	public List<State> successorsList = new ArrayList<>();
	public int depthOfState;
	public int size;
	public int[] arrayOfNums;
    public char parentMove;

	public State(int[] arrayOfNums, char parent, int size, State state) {
		this.parentMove = parent;
		this.size = size;
		this.cameFrom = state;
		this.arrayOfNums = arrayOfNums;
		this.current = new Node[size][size];
	}
	
	/**
	 * Ids constructor.
	 * @param arrayOfNums
	 * @param parent
	 * @param size
	 * @param state
	 * @param depth1
	 */
	public State(int[] arrayOfNums, char parent, int size, State state,int depth1) {
        this.parentMove = parent;
        this.size = size;
        this.cameFrom = state;
        this.arrayOfNums = arrayOfNums;
        this.depthOfState=depth1;
        this.current = new Node[size][size];
    }
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(arrayOfNums);
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof State) {
			return Arrays.equals(this.arrayOfNums, ((State) other).arrayOfNums);
		}
		return false;
	}
	
	/**
	 * get array
	 * @return arrayOfNums
	 */
	public int[] getArrayOfNums() {
		return arrayOfNums;
	}
	
	/**
	 * make Current State.
	 */
	public void createCurrentState() {
		int length = this.arrayOfNums.length, counter = 0;
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				if (counter < length) {
					this.current[i][j] = new Node(i, j, this.arrayOfNums[counter]);
					counter++;
				}
			}
		}
	}

	/**
	 * find zero cell.
	 * @return state
	 */
	public Node findTheZero() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (this.current[i][j].getVal()== 0) {
					return this.current[i][j];
				}
			}
		}
		return null;
	}


	/**
	 * create state successors
	 */
	public void MakeSuccessors() {
		successorsList.clear();
		createCurrentState();
		Node other,nodeZero = findTheZero();
		int[] newArray;
		
        if (nodeZero.x + 1 <= size - 1) {
            other = new Node(nodeZero.x + 1, nodeZero.y, this.current[nodeZero.x + 1][nodeZero.y].getVal());
            newArray = convertToArray(nodeZero, other);
            successorsList.add(new State(newArray, 'U', size, this,this.depthOfState+1));
        }
		
		if (nodeZero.x - 1 >= 0) {
			other = new Node(nodeZero.x - 1, nodeZero.y, this.current[nodeZero.x - 1][nodeZero.y].getVal());
			newArray = convertToArray(nodeZero, other);
			successorsList.add(new State(newArray, 'D', size, this,this.depthOfState+1));
		}

        if (nodeZero.y + 1 <= size - 1) {
            other = new Node(nodeZero.x, nodeZero.y + 1, this.current[nodeZero.x][nodeZero.y + 1].getVal());
            newArray = convertToArray(nodeZero, other);
            successorsList.add(new State(newArray, 'L', size, this,this.depthOfState+1));
        }
		
		if (nodeZero.y - 1 >= 0) {
			other = new Node(nodeZero.x, nodeZero.y - 1, this.current[nodeZero.x][nodeZero.y - 1].getVal());
			newArray = convertToArray(nodeZero, other);
			successorsList.add(new State(newArray, 'R', size, this,this.depthOfState+1));
		}
	}
	
	/**
	 * matrix to array
	 * @param zero
	 * @param other
	 * @return int[] newArray
	 */
	public int[] convertToArray(Node zero, Node other) {
		int[] newArray = new int[this.arrayOfNums.length];
		int temp = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (temp < newArray.length) {
					if (this.current[i][j].val == zero.val) {
						newArray[temp] = other.getVal();
					} else if (this.current[i][j].val == other.val) {
						newArray[temp] = zero.getVal();
					} else {
						newArray[temp] = this.current[i][j].getVal();
					}
					temp++;
				}
			}
		}
		return newArray;
	}

	/**
	 * get successors
	 * @return list
	 */
	public List<State> getSuccessors() {
		return successorsList;
	}

	/**
	 * get Depth
	 * @return depth
	 */
	public int getDepth(){
	    return this.depthOfState;
	}

}
