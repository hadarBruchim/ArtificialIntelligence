
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
/**
 * ids algorithm
 * @author hadas
 *
 */
public class IDS extends Algo {
    private int[] initialState;
    private int[] goal;
    private int size;
    private Stack<State> stack;
    //private HashSet<State> closeList;   -- option with close list
    
    /**
     * constructor
     * @param initialState
     * @param goalState
     * @param sizeBoard
     */
    public IDS(int[] initialState, int[] goalState,int sizeBoard) {
        this.goal = goalState;
        this.size = sizeBoard;
        this.initialState = initialState;
        this.stack = new Stack<State>();
        //this.closeList = new HashSet<>();   -- option with close list
    }

    @Override
    public Solution searchSolution() {
        Solution solution=null;
        int depth=0;
        this.stack.clear();
        while(solution == null){
            solution=depthLimit(depth);
            depth++;
        }
        return solution;
    }

    /**
     * The method return the dfs algorithm in specific depth
     * @param depth
     * @return solution
     */
    public Solution depthLimit(int depth) {
        int numOfV = 0;
        State initial = new State(initialState, '0', this.size,null, 0);
        this.stack.push(initial);
        while(!this.stack.isEmpty()){
            State currentState = this.stack.pop();
            numOfV+=1;
            if(compareStates(currentState.getArrayOfNums(), this.goal)){
                return new Solution(numOfV, oppositeWay(wayToGoal(currentState)), currentState.depthOfState);
            }
            if(currentState.getDepth() < depth){
                currentState.MakeSuccessors();
                List<State> list = currentState.getSuccessors(); 
                for (int i = list.size()-1; i >= 0; i--) {
                    this.stack.push(list.get(i));
                }
            }
        }
        return null;
    }
}


