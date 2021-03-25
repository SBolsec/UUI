package ui;

/**
 * Starting point of program.
 */
public class Solution {
	/** Search algorithm to be used */
	private static SearchAlgorithm algorithm;
	/** Path to file from which to initialize state space descriptor */
	private static String pathToStateSpaceDescriptor;
	/** Path to file from which to initialize heuristic function descriptor */
	private static String pathToHeuristicFunctionDescriptor;
	/** Flag which signalizes whether to check if the given heuristic is optimistic */
	private static Boolean checkOptimistic;
	/** Flag which signalizes whether to check if the given heuristic is consistent */
	private static Boolean checkConsistent;

	/**
	 * Parses the command line arguments and initializes the state of the program.
	 * @param args command line arguments
	 * @throws IllegalArgumentException if there is an error in the arguments
	 */
	public static void parseArgs(String ... args) {
		for (int i = 0; i < args.length; i++) {
			outer:
			switch (args[i].toLowerCase()) {
				case "--alg":
					if (algorithm != null) throw new IllegalArgumentException("Algorithm was already set!");
					if (i+1 < args.length) throw new IllegalArgumentException("No algorithm was provided!");
					i++;
					switch (args[i].toLowerCase()) {
						case "bfs":
							algorithm = SearchAlgorithm.BFS; break outer;
						case "ucs":
							algorithm = SearchAlgorithm.UCS; break outer;
						case "astar":
							algorithm = SearchAlgorithm.ASTAR; break outer;
						default: throw new IllegalArgumentException("Algorithm not supported!\nSupported algorithms are:\n\t1. bfs\n\t2. ucs\n\t 3. astar");
					}
				case "--s":
					if (pathToStateSpaceDescriptor != null) throw new IllegalArgumentException("Path to state space descriptor was already set!");
					if (i+1 < args.length) throw new IllegalArgumentException("No path to state space descriptor was provided!");
					pathToStateSpaceDescriptor = args[i+1];
					i++;
					break;
				case "--h":
					if (pathToHeuristicFunctionDescriptor != null) throw new IllegalArgumentException("Path to heuristic descriptor was already set!");
					if (i+1 < args.length) throw new IllegalArgumentException("No path to heuristic descriptor was provided!");
					pathToHeuristicFunctionDescriptor = args[i+1];
					i++;
					break;
				case "--check-optimistic":
					if (checkOptimistic != null) throw new IllegalArgumentException("Check optimistic flag was already set!");
					checkOptimistic = true;
					break;
				case "--check-consistent":
					if (checkConsistent != null) throw new IllegalArgumentException("Check consistent flag was already set!");
					checkConsistent = true;
					break;
				default:
					throw new IllegalArgumentException("Unsupported argument: " + args[i]);
			}
		}
		if (checkOptimistic == null) checkOptimistic = false;
		if (checkConsistent == null) checkConsistent = false;
	}

	/**
	 * Starting point.
	 * @param args command line arguments
	 * @throws IllegalArgumentException if there is an error with the arguments
	 */
	public static void main(String ... args) {
		parseArgs(args);

		if (algorithm != null) {
			if (pathToStateSpaceDescriptor == null) throw new IllegalArgumentException("Path to state space descriptor was not provided!");
			switch (algorithm) {
				case BFS:
					// TODO: bfs
					break;
				case UCS:
					// TODO: ucs
					break;
				case ASTAR:
					if (pathToHeuristicFunctionDescriptor == null) throw new IllegalArgumentException("Path to heuristic descriptor was not provided!");
					// TODO: astar
					break;
				default:
					throw new IllegalArgumentException("Unsupported search algorithm!");
			}
		}

		if (checkOptimistic) {
			if (pathToStateSpaceDescriptor == null) throw new IllegalArgumentException("Path to state space descriptor was not provided!");
			if (pathToHeuristicFunctionDescriptor == null) throw new IllegalArgumentException("Path to heuristic descriptor was not provided!");
			// TODO: check optimistic
		}

		if (checkConsistent) {
			if (pathToStateSpaceDescriptor == null) throw new IllegalArgumentException("Path to state space descriptor was not provided!");
			if (pathToHeuristicFunctionDescriptor == null) throw new IllegalArgumentException("Path to heuristic descriptor was not provided!");
			// TODO: check consistent
		}
	}

}
