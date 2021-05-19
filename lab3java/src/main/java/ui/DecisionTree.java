package ui;

import ui.data.DataEntry;
import ui.data.Datapoint;
import ui.data.Dataset;
import ui.node.LeafNode;
import ui.node.TreeNode;

import java.util.*;

public class DecisionTree {

    private TreeNode rootNode;
    private Dataset trainSet;

    public DecisionTree() {
    }

    public void fit(Dataset dataset) {
        trainSet = dataset;
        rootNode = id3(dataset.getData(), dataset.getData(), dataset.getAttributes(), dataset.getTargetVariable(), dataset);
        System.out.println("[BRANCHES]:");
        rootNode.printBranches();
    }

    public void predict(Dataset dataset) {
        if (rootNode == null) {
            System.out.println("Model needs to be trained first!");
            return;
        }

        List<String> targetValues = new ArrayList<>(trainSet.getTargetValues());
        int n = targetValues.size();
        int[][] confusionMatrix = new int[n][n];
        int correct = 0;

        StringBuilder predictions = new StringBuilder();
        for (DataEntry entry : dataset.getData()) {
            String prediction = getPrediction(rootNode, entry, trainSet.getData());
            predictions.append(prediction).append(" ");

            String correctValue = entry.getDatapoint(trainSet.getTargetVariable()).getValue();
            if (prediction.equals(correctValue)) correct++;

            int x = targetValues.indexOf(correctValue);
            int y = targetValues.indexOf(prediction);
            confusionMatrix[x][y]++;
        }

        double accuracy = (double) correct / dataset.getData().size();

        System.out.println("[PREDICTIONS]: " + predictions.toString().trim());
        System.out.format("[ACCURACY]: %.5f\n", accuracy);
        System.out.println("[CONFUSION_MATRIX]:");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(confusionMatrix[i][j]);
                if (j != n-1) sb.append(" ");
                else sb.append("\n");
            }
        }
        System.out.print(sb.toString());
    }

    private String getPrediction(TreeNode node, DataEntry entry, List<DataEntry> fallback) {
        String attribute = node.getAttribute();

        if (attribute == null) { // Leaf node
            return node.getValue();
        }

        String value = entry.getDatapoint(attribute).getValue();
        TreeNode childNode = node.getChildren().get(value);

        if (childNode == null) return mostFrequentValue(fallback, trainSet.getTargetVariable());

        List<DataEntry> newFallback = new ArrayList<>(fallback);
        newFallback.removeIf(e -> !e.getDatapoint(attribute).equals(value));

        return getPrediction(childNode, entry, newFallback);
    }

    private TreeNode id3(List<DataEntry> d, List<DataEntry> dParent, Set<String> x, String targetVariable, Dataset dataset) {
        if (d == null || d.isEmpty()) {
            String v = mostFrequentValue(dParent, targetVariable);
            return new LeafNode(v);
        }

        String v = mostFrequentValue(d, targetVariable);
        List<DataEntry> dv = new ArrayList<>(d);
        dv.removeIf(entry -> !entry.getDatapoint(targetVariable).getValue().equals(v));

        if (x == null || x.isEmpty() || d.size() == dv.size()) {
            return new LeafNode(v);
        }

        String feature = mostDiscriminativeFeature(d, x, dataset);
        TreeNode node = new TreeNode(feature);

        Set<String> newX = new HashSet<>(x);
        newX.remove(feature);

        for (String f : dataset.getAttributeValues().get(feature)) {
            List<DataEntry> newD = new ArrayList<>(d);
            newD.removeIf(e -> !e.getDatapoint(feature).getValue().equals(f));

            TreeNode t = id3(newD, d, newX, targetVariable, dataset);
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
            Datapoint point = e.getDatapoint(targetVariable);
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
     * Returns the entropy of the dataset.
     * @param data data on which to calculate entropy
     * @param targetVariable target variable
     * @return calculated entropy
     */
    private double entropy(List<DataEntry> data, String targetVariable) {
        double res = 0;

        Map<String, Integer> occurrences = new HashMap<>();
        for (DataEntry entry : data) {
            Datapoint point = entry.getDatapoint(targetVariable);
            occurrences.compute(point.getValue(), (k, v) -> v == null ? 1 : v+1);
        }
        double n = (double) data.size();
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
    private double informationGain(List<DataEntry> data, String attribute, Dataset dataset) {
        double res = entropy(data, dataset.getTargetVariable());

        double n = (double) data.size();
        for (String v : dataset.getAttributeValues().get(attribute)) {
            List<DataEntry> entries = new ArrayList<>(data);
            entries.removeIf(e -> !e.getDatapoint(attribute).getValue().equals(v));

            double entropy = entropy(entries, dataset.getTargetVariable());
            res -= (entries.size() / n * entropy);
        }

        return res;
    }

    private String mostDiscriminativeFeature(List<DataEntry> d, Set<String> features, Dataset dataset) {
        String x = null;
        double max = 0;

        StringBuilder sb = new StringBuilder();
        for (String feature : features) {
            double infGain = informationGain(d, feature, dataset);
            if (x == null || infGain > max || (infGain == max && x.compareTo(feature) > 1)) {
                max = infGain;
                x = feature;
            }
            sb.append("IG(").append(feature).append(")=").append(infGain).append(" ");
        }
        System.out.println(sb.toString());

        return x;
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
