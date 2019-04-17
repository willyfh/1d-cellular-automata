import java.io.*;
import java.util.*;

/**
 * @author Willy Fitra Hendria
 */
public class OneDCellularAutomata {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		int[] cells = new int[84]; // initialize empty cells
		Map<String, Integer> transitionMap = new HashMap<>(); // transition table
		int radius = 0; // radius input
		String rule = ""; // rule input (Wolfram notation)
		String startCond = ""; // starting condition (S:Seed or R:Random)

		// read radius input
		do {
			System.out.println("Please input the radius (1 or 2):");
			radius = Integer.parseInt(scanner.nextLine());
		} while (radius != 1 && radius != 2);
		
		// read rule input
		do {
			System.out.println("Please input the rule in Wolfram notation (eg. 00011110):");
			System.out.println("(for radius=1, should contains 8 digits. for radius=2, 32 digits)");
			rule = scanner.nextLine();
		} while (rule.length() != (int)Math.pow(2, 2*radius+1) || !rule.matches("[01]+"));
		
		// read starting condition input
		do {
			System.out.println("Please input the starting condition (S for Seed or R for Random):");
			startCond = scanner.nextLine();
		} while (!startCond.equalsIgnoreCase("S") && !startCond.equalsIgnoreCase("R"));
		
		generateTransitionTable(transitionMap, radius, rule);

		initializeStartingCondition(cells, startCond);
		
		generateCA(cells, radius, rule, transitionMap);
	}
	
	/**
	 * Initialize starting condition by a given paramenter (S : Seed or R : Random)
	 */
	public static void initializeStartingCondition(int[] cells, String startCond) {
		if (startCond.equalsIgnoreCase("S")){ // Seed
			cells[42] = 1;
		} else if (startCond.equalsIgnoreCase("R")) { // Random
			for (int j=2; j<cells.length-2; j++) {
				double probability = Math.random();
				if (probability < 0.5) {
					cells[j] = 0;
				} else {
					cells[j] = 1;
				}
			}
		}
	}
	
	/**
	 * Generate Cellular Automata until t=30
	 */
	public static void generateCA(int[] cells, int radius, String rule, Map<String, Integer> transitionMap) {
		for (int i=0; i<=30; i++) { // until t=30
			System.out.print("t=" + i +",");
			System.out.print("\t");
			for (int j=0; j<84; j++) {
				System.out.print(cells[j]);
			}
			System.out.println();
			cells = generateNextCells(cells, radius, rule, transitionMap);
		}
	}
	
	/**
	 * Generate next cells (t+1);
	 */
	public static int[] generateNextCells(int[] cells, int radius, String rule,Map<String, Integer> transitionMap) {
		int[] nextCells = new int[84];
		for (int j=2; j<82; j++) {
			String state = ""; // current state
			for (int k=j-radius; k<=j+radius; k++) {
				state += cells[k];
			}
			nextCells[j] = transitionMap.get(state); // get next state from transition table
		}
		return nextCells;
	}
	
	/**
	 * Generate transition table based on radius and rule (Wolfram Notation) input
	 *
	 * @param transitionMap key: state of neigborhood, value: next state
	 */
	public static void generateTransitionTable(Map<String, Integer> transitionMap, int radius, String rule) {
		int N = 2*radius+1; // number of neighborhood
		int S = (int)Math.pow(2, N);// total possible states for neighborhood.
		int[] flag = new int[N];
		for (int i=0; i<S; i++) {
			String state = "";
			for (int j=1; j<=N; j++) {
				if (i%(S/Math.pow(2, j))==0){
					flag[j-1] = flag[j-1]==0 ? 1:0;
				}
				state += flag[j-1];
			}
			transitionMap.put(state, Character.getNumericValue(rule.charAt(i)));
		}		
	}
}