package ui;

import ui.data.Clause;
import ui.data.ClausesList;

import java.io.IOException;
import java.util.List;

/**
 * Starting point of the solution.
 */
public class Solution {

	/**
	 * Starting point of the program.
	 * @param args command line arguments
	 */
	public static void main(String ... args) throws IOException {
		if (args.length < 2 || args.length > 3) {
			System.err.println("Invalid arguments!");
			System.exit(1);
		}

		switch (args[0].toLowerCase()) {
			case "resolution" -> {
				// check arguments
				if (args.length != 2) {
					System.err.println("Invalid arguments!");
					System.exit(1);
				}
				// load clauses from file
				ClausesList cl = new ClausesList(args[1]);
				List<Clause> clauses = cl.getClauses();
				// start the resolution
				Resolution resolution = new Resolution();
				resolution.resolutionByRefutation(clauses.subList(0, clauses.size() - 1), clauses.get(clauses.size() - 1));
			}
			case "cooking" -> {
				// check arguments
				if (args.length != 3) {
					System.err.println("Invalid arguments!");
					System.exit(1);
				}
				CookBook cookbook = new CookBook(args[1], args[2]);
				cookbook.run();
			}
			default -> {
				System.err.println("Invalid argument! Supported arguments are: resolution, cooking");
				System.exit(1);
			}
		}
	}

}
