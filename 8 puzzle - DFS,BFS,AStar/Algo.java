/**
 * class that contain general method to all the algorithm
 * @author hadas
 *
 */
public abstract class Algo {
    private String wayStringToGoal;
    
    /**
     * the method return solution according to the algorithm type
     * @return the correct solution
     */
    public abstract Solution searchSolution();
    
    /**
     * return the trace of the solution to the goal
     * @param goalState
     * @return way
     */
    public String wayToGoal(State goalState){
    	wayStringToGoal = "";
    	while(goalState.cameFrom != null) {
    		if(goalState.parentMove != '0') {
    			wayStringToGoal += goalState.parentMove;
    			goalState = goalState.cameFrom;
    		}		
    	}	
    	return wayStringToGoal;
    }
    
    /**
     * change the trace in reverse
     * @param backTrace
     * @return opposite Way
     */
    public String oppositeWay(String backTrace) {
        String result = new StringBuffer(backTrace).reverse().toString();
        return result;
    }
    
    /**
     * the method compare between 2 states and return boolean answer 
     * @param currentState
     * @param goal
     * @return true if equals
     */
    public boolean compareStates(int[] currentState, int[] goal) {
        for (int i = 0; i < currentState.length; i++) {
            if (currentState[i] != goal[i]) {
                return false;
            }
        }
        return true;
    }
    
/*    public int justSay(){
    int x=0;
    String s="hadas";
    for(int i=0; i< s.length(); i++){
        if(s.charAt(i)=='a'){
            x=x+1;
        }
    }
    return x;
  }*/
    
}
