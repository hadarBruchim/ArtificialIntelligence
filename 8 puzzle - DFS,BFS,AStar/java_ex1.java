
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class java_ex1 {
	public static void main(String[] args) {
		
		String algoType, boardSizeString, listOfNumbers;
		try {

		    File file = new File("C:/Users/hadas/workspace/HelpProj/src/input.txt");
	        //File file = new File("input.txt");
			BufferedReader buffer = new BufferedReader(new FileReader(file));
			
			
			algoType = buffer.readLine(); // the algorithm type
			boardSizeString = buffer.readLine(); // the board size
			listOfNumbers = buffer.readLine(); // the board initial state
			
			String[] arrayOfString = listOfNumbers.split("-"); //split string to array
			int[] goalState = makeSolutionState(arrayOfString.length); //create goal state to compare with
			int boardSize = Integer.parseInt(boardSizeString); //convert string to integer
			
			int sizeOfNumbers = arrayOfString.length;
			int[] arrayOfNum = new int[sizeOfNumbers]; //convert array of string to number
			for (int i = 0; i < sizeOfNumbers; i++) {
				arrayOfNum[i] = Integer.parseInt(arrayOfString[i]);
			}

			switch (algoType) {
			case "1":
				Algo ids = new IDS(arrayOfNum, goalState, boardSize);
                Solution solutionIds = ids.searchSolution();
                solutionIds.output();
                // for self check: System.out.print(solutionIds.getWayBack()+" "+solutionIds.getNodes()+" "+solutionIds.cost);
				break;
			case "2":
				Algo bfs = new BFS(arrayOfNum, goalState, boardSize);
				Solution solutionBfs = bfs.searchSolution();
				solutionBfs.output();
				// for self check: System.out.print(solutionBfs.getWayBack()+" "+solutionBfs.getNodes()+" "+solutionBfs.cost);
				break;
			case "3":
				 Algo aStar= new AStar(arrayOfNum, goalState, boardSize);
				 Solution solutionAStar = aStar.searchSolution();
	             solutionAStar.output();
	             // for self check: System.out.print(solutionAStar.getWayBack()+" "+solutionAStar.getNodes()+" "+solutionAStar.cost);
	             break;
			default:
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * create a goal state according length input
	 * @param len
	 * @return goal state
	 */
	public static int[] makeSolutionState(int len) {
		int[] goalPuzzle = new int[len];
		for (Integer j = 1; j <= len; j++) {
			if (j == len) {
				goalPuzzle[j - 1] = 0; //the last number
				break;
			}
			goalPuzzle[j - 1] = j;
		}
		return goalPuzzle;
	}

}
