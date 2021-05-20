package ui;

import ui.data.DataEntry;
import ui.data.DataPoint;
import ui.data.DataSet;
import ui.node.LeafNode;
import ui.node.TreeNode;

import java.util.*;

/**
 * Decision tree that uses the ID3 algorithm.
 */
public class DecisionTree {
    /** Limit of the depth of the tree to be used while training model. */
    private final Integer limit;
    /** Reference to root node of constructed decision tree. */
    private TreeNode rootNode;
    /** Dataset that was used to train the model. */
    private DataSet trainSet;

    /**
     * Default constructor.
     */
    public DecisionTree() {
        limit = null;
    }

    /**
     * Constructor that sets the limit while training the model.
     * @param limit limit of the depth of the decision tree
     */
    public DecisionTree(Integer limit) {
        this.limit = limit;
    }

    /**
     * Trains the model with the given dataset.
     * @param dataset training dataset
     */
    public void fit(DataSet dataset) {
        trainSet = dataset;
        rootNode = id3(dataset.getData(), dataset.getData(), dataset.getAttributes(), dataset.getTargetVariable(), dataset, limit);
        System.out.println("[BRANCHES]:");
        rootNode.printBranches();
    }

    /**
     * Makes a prediction for every entry in the given dataset using the previously constructed tree.
     * @param dataset test dataset
     */
    public void predict(DataSet dataset) {
        if (rootNode == null) {
            System.out.println("Model needs to be trained first!");
            return;
        }

        // Preparing data structures
        List<String> targetValues = new ArrayList<>(trainSet.getTargetValues());
        int n = targetValues.size();
        int[][] confusionMatrix = new int[n][n];
        int correct = 0;
        StringBuilder predictions = new StringBuilder();

        // Make prediction for every entry in dataset
        for (DataEntry entry : dataset.getData()) {
            // Make prediction
            String prediction = getPrediction(rootNode, entry, trainSet.getData());
            predictions.append(prediction).append(" ");


            // Increment number of correct predictions if it was correct
            String correctValue = entry.getDatapoint(trainSet.getTargetVariable()).getValue();
            if (prediction.equals(correctValue)) correct++;

            // Increment cell in confusion matrix
            int x = targetValues.indexOf(correctValue);
            int y = targetValues.indexOf(prediction);
            confusionMatrix[x][y]++;
        }

        double accuracy = (double) correct / dataset.getData().size();

        System.out.println("[PREDICTIONS]: " + predictions.toString().trim());
        System.out.format("[ACCURACY]: %.5f\n", accuracy);
        System.out.println("[CONFUSION_MATRIX]:");

        // Prepare confusion matrix for printing
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(confusionMatrix[i][j]);
                if (j != n-1) sb.append(" ");
                else sb.append("\n");
            }
        }
        System.out.print(sb);
    }

    /**
     * Recursively traverses the decision tree to make a prediction for the given data entry.
     * @param node current node of the tree (root node at beginning)
     * @param entry data entry for which to make a prediction
     * @param fallback used to make a prediction if there is no node for the given value
     * @return prediction made by traversing the decision tree
     */
    private String getPrediction(TreeNode node, DataEntry entry, List<DataEntry> fallback) {
        String attribute = node.getAttribute();

        if (attribute == null) { // Leaf node
            return node.getValue();
        }

        // Get next node to visit
        String value = entry.getDatapoint(attribute).getValue();
        TreeNode childNode = node.getChildren().get(value);

        // If next node is null, that means that the value has not been seen while constructing the decision tree.
        // In that case return the most frequent value from the fallback dataset.
        if (childNode == null) return mostFrequentValue(fallback, trainSet.getTargetVariable());

        // Limit the fallback dataset for the next recursive call
        List<DataEntry> newFallback = new ArrayList<>(fallback);
        newFallback.removeIf(e -> !e.getDatapoint(attribute).getValue().equals(value));

        return getPrediction(childNode, entry, newFallback);
    }

    /**
     * Constructs decision tree using given parameters.
     * @param d data entries from which to make next node
     * @param dParent data entries of the parent
     * @param x set of attributes (features) that have not been resolved yet
     * @param y target variable
     * @param dataset structure that provides helper methods
     * @param depth used to stop the tree at the specified depth if provided
     * @return root node of constructed decision tree
     */
    private TreeNode id3(List<DataEntry> d, List<DataEntry> dParent, Set<String> x, String y, DataSet dataset, Integer depth) {
        // End recursion if depth limit is reached (if it's provided)
        if (depth != null && depth == 0) {
            String v = mostFrequentValue(d == null || d.isEmpty() ? dParent : d, y);
            return new LeafNode(v);
        }

        // If there are no data entries left, use the parent data entries to find the most
        // frequent value and create a leaf node with it
        if (d == null || d.isEmpty()) {
            String v = mostFrequentValue(dParent, y);
            return new LeafNode(v);
        }

        // Find most frequent value and filter data entries that contain it
        String v = mostFrequentValue(d, y);
        List<DataEntry> dv = new ArrayList<>(d);
        dv.removeIf(entry -> !entry.getDatapoint(y).getValue().equals(v));

        // If there is no features left to select or the previous filtering didn't
        // change the data entries, return a leaf node
        if (x == null || x.isEmpty() || d.size() == dv.size()) {
            return new LeafNode(v);
        }

        // Find most discriminative feature and create a new node with it
        String feature = mostDiscriminativeFeature(d, x, dataset);
        TreeNode node = new TreeNode(feature);

        // Prepare new set of attributes which will be used in next recursive calls
        // to determine the most discriminative feature. Remove the feature that was
        // just selected from this new set so it can't be chosen again.
        Set<String> newX = new HashSet<>(x);
        newX.remove(feature);

        // Loop over all values from selected feature, create nodes from them and
        // add those nodes as children of the node that is currently being constructed.
        for (String f : dataset.getAttributeValues().get(feature)) {
            // Filter data entries that contain specific value
            List<DataEntry> newD = new ArrayList<>(d);
            newD.removeIf(e -> !e.getDatapoint(feature).getValue().equals(f));

            // Recursively call this method to generate node
            TreeNode t = id3(newD, d, newX, y, dataset, depth != null ? depth-1 : null);
            node.addChild(f, t);
        }

        return node;
    }

    /**
     * Returns the most frequent value of the target variable in given dataset.
     * @param d dataset
     * @param targetVariable target variable
     * @return most frequent value of target variable in given dataset
     */
    private String mostFrequentValue(List<DataEntry> d, String targetVariable) {
        Map<String, Integer> occurrences = new HashMap<>();
        for (DataEntry e : d) {
            DataPoint point = e.getDatapoint(targetVariable);
            occurrences.compute(point.getValue(), (k, v) -> v == null ? 1 : v+1);
        }

        String v = null;
        int max = 0;
        for (Map.Entry<String, Integer> e : occurrences.entrySet()) {
            if (v == null || e.getValue() > max || (e.getValue() == max && v.compareTo(e.getKey()) > 0)) {
                v = e.getKey();
                max = e.getValue();
            }
        }

        return v;
    }

    /**
     * Return the feature with the highest information gain.
     * @param d dataset on which to calculate information gain
     * @param features features for which to calculate information gain
     * @param dataset used to get helper functions
     * @return feature with the highest information gain
     */
    private String mostDiscriminativeFeature(List<DataEntry> d, Set<String> features, DataSet dataset) {
        String x = null;
        double max = 0;

        StringBuilder sb = new StringBuilder();
        for (String feature : features) {
            // Calculate information gain for given feature
            double infGain = informationGain(d, feature, dataset);

            // Find feature with maximum information gain
            // If two features have equal information gains, max is determined by feature name
            if (x == null || infGain > max || (infGain == max && x.compareTo(feature) > 0)) {
                max = infGain;
                x = feature;
            }
            sb.append("IG(").append(feature).append(")=").append(infGain).append(" ");
        }
        System.out.println(sb);

        return x;
    }

    /**
     * Returns the entropy of the dataset.
     * @param data data on which to calculate entropy
     * @param targetVariable target variable
     * @return calculated entropy
     */
    private double entropy(List<DataEntry> data, String targetVariable) {
        double res = 0;

        // Count number of occurrences for each value
        Map<String, Integer> occurrences = new HashMap<>();
        for (DataEntry entry : data) {
            DataPoint point = entry.getDatapoint(targetVariable);
            occurrences.compute(point.getValue(), (k, v) -> v == null ? 1 : v+1);
        }

        // Calculate entropy
        double n = data.size();
        for (Integer i : occurrences.values()) {
            double val = i / n;
            res -= val * log2(val);
        }

        return res;
    }

    /**
     * Calculates the information gain of a specific attribute in the given dataset.
     * @param data list of data entries
     * @param attribute attribute for which to calculate
     * @param dataset dataset
     * @return information gain of given attribute in the given dataset
     */
    private double informationGain(List<DataEntry> data, String attribute, DataSet dataset) {
        double res = entropy(data, dataset.getTargetVariable());

        double n = data.size();
        for (String v : dataset.getAttributeValues().get(attribute)) {
            List<DataEntry> entries = new ArrayList<>(data);
            entries.removeIf(e -> !e.getDatapoint(attribute).getValue().equals(v));

            // Calculate the entropy of a specific value of the given attribute
            double entropy = entropy(entries, dataset.getTargetVariable());

            res -= (entries.size() / n * entropy);
        }

        return res;
    }

    /**
     * Calculates log2 of given number.
     * @param n number at which to calculate log2
     * @return result of calculation
     */
    private static double log2(double n) {
        return (Math.log(n) / Math.log(2));
    }
}
