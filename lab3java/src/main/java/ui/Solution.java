package ui;

import ui.data.Dataset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Solution {

	public static void main(String ... args) throws IOException {
		if (args.length < 2 || args.length > 4) {
			System.err.println("Invalid arguments!");
			System.exit(1);
		}

		Path trainPath = Path.of(args[0]);
		Path testPath = Path.of(args[1]);
		Integer limit = null;

		if (args.length == 3) {
			try {
				limit = Integer.parseInt(args[2]);
			} catch (Exception e) {
				System.err.println("Third argument should be an integer.");
				System.exit(1);
			}
		}

		if (!checkPath(trainPath) && !checkPath(testPath)) {
			System.err.println("Provided path is invalid.");
			System.exit(1);
		}

		Dataset trainDataset = new Dataset(trainPath);
		Dataset testDataset = new Dataset(testPath);

		DecisionTree decisionTree = new DecisionTree(limit);
		decisionTree.fit(trainDataset);
		decisionTree.predict(testDataset);
	}

	private static boolean checkPath(Path path) {
		return Files.exists(path) && Files.isRegularFile(path) && Files.isReadable(path);
	}

}
