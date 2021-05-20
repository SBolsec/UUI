package ui;

import ui.data.DataSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Starting point of program.
 */
public class Solution {

	/**
	 * Starting point of program.
	 * @param args command line arguments
	 * @throws IOException if there is an error while working with files
	 */
	public static void main(String ... args) throws IOException {
		// Check number of command line arguments
		if (args.length < 2 || args.length > 4) {
			System.err.println("Invalid arguments!");
			System.exit(1);
		}

		Path trainPath = Path.of(args[0]);
		Path testPath = Path.of(args[1]);
		Integer limit = null;

		// If 3 arguments are given try to parse number from third argument
		if (args.length == 3) {
			try {
				limit = Integer.parseInt(args[2]);
			} catch (Exception e) {
				System.err.println("Third argument should be an integer.");
				System.exit(1);
			}
		}

		// Check given file paths
		if (!checkPath(trainPath) && !checkPath(testPath)) {
			System.err.println("Provided path is invalid.");
			System.exit(1);
		}

		// Load training and testing dataset
		DataSet trainDataSet = new DataSet(trainPath);
		DataSet testDataSet = new DataSet(testPath);

		// Create decision tree, train it and test it with testing dataset
		DecisionTree decisionTree = new DecisionTree(limit);
		decisionTree.fit(trainDataSet);
		decisionTree.predict(testDataSet);
	}

	/**
	 * Checks if given path is valid.
	 * @param path path to check
	 * @return true if path is valid, false otherwise
	 */
	private static boolean checkPath(Path path) {
		return Files.exists(path) && Files.isRegularFile(path) && Files.isReadable(path);
	}

}
