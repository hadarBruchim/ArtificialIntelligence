import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * A star algorithm
 * @author hadas
 *
 */
public class AStar extends Algo {
    private int[] initial;
    private int[] goal;
    private int size;
    private PriorityQueue<HeuristicState> queue;
    private Comparator<HeuristicState> HeuristicComperator;
    //private HashSet<State> closeList; -- option with close list


    /**
     * constructor
     * @param initialState
     * @param goalState
     * @param sizeBoard
     */
    public AStar(int[] initialState, int[] goalState, int sizeBoard) {
        this.initial = initialState;
        this.goal = goalState;
        this.size = sizeBoard;
        //this.closeList = new HashSet<State>(); -- option with close list
        new Comparator<HeuristicState>() {
            @Override
            public int compare(HeuristicState s1, HeuristicState s2) {
                int bool;
                if((bool=s1.getfValue() - s2.getfValue())!=0)
                    return bool;
                else
                    return s1.getTime() - s2.getTime();
            }
        };
        this.queue = new PriorityQueue<HeuristicState>(HeuristicComperator);
    }
    
    @Override
    public Solution searchSolution() {
        int numOfV = 0,timeInOpenList = 0;
        HeuristicState initiaHeur = new HeuristicState(initial, '0', this.size, null);
        initiaHeur.setTime(timeInOpenList);    
        this.queue.add(initiaHeur);
        timeInOpenList++;
        while(!this.queue.isEmpty()){
            HeuristicState currentState = this.queue.poll();
            numOfV+=1;
            if(compareStates(currentState.getArrayOfNums(), goal)){
                String way = wayToGoal(currentState);
                int cost = way.length();
                return new Solution(numOfV, oppositeWay(wayToGoal(currentState)), cost);
            }
            currentState.MakeSuccessors();
            for (State state: currentState.successorsList){
                HeuristicState helperHeur= new HeuristicState(state.arrayOfNums, state.parentMove, state.size, currentState);
                helperHeur.setFValue();
                helperHeur.setTime(timeInOpenList); //for the comparable
                this.queue.add(helperHeur);
                timeInOpenList++;
            }
        }
        return null;
    }
}
