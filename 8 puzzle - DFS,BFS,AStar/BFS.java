//import java.util.HashSet;   -- option with close list
import java.util.LinkedList;
import java.util.Queue;

/**
 * bfs algorithm
 * @author hadas
 *
 */
public class BFS extends Algo {
    private int[] goal;
	private LinkedList<State> queueState;
	private int[] initialState;
	private int size;
	// private HashSet<State> closeList;  -- option with close list
	
	/**
	 * constructor
	 * @param initialState
	 * @param goalState
	 * @param sizeBoard
	 */
	public BFS(int[] initialState, int[] goalState, int sizeBoard) {
	    this.initialState = initialState;
		this.goal = goalState;
		this.size = sizeBoard;
		this.queueState = new LinkedList<State>();
		//this.closeList = new HashSet<>();   -- option with close list
	}

	@Override
	public Solution searchSolution() {
	    int numOfV = 0;
		State initial = new State(initialState, '0', this.size, null);
		this.queueState.add(initial);
		while (!this.queueState.isEmpty()) {
			State currentState = this.queueState.poll();
	        numOfV+=1;
			if (compareStates(currentState.getArrayOfNums(),goal)) {			    
				return new Solution(numOfV,oppositeWay(wayToGoal(currentState)), 0);
			}
			//this.closeList.add(currentState);   -- option with close list
			currentState.MakeSuccessors();
			for (State s: currentState.successorsList){
					this.queueState.add(s);
			}
		}
		return null;	
	}
}
