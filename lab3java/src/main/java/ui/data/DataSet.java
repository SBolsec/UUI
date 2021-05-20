package ui.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Data set created from file on disk.
 */
public class DataSet {
    /** Set of all attributes (features) (except target variable). */
    private final Set<String> attributes;
    /** Target variable. */
    private final String targetVariable;

    /** Values each attribute can adopt. */
    private final Map<String, Set<String>> attributeValues = new HashMap<>();
    /** Values the target value can adopt. */
    private final Set<String> targetValues = new TreeSet<>();

    /** List of all the lines read from the disk. */
    private final List<DataEntry> data = new ArrayList<>();

    public DataSet(Path path) throws IOException {
        if (!Files.exists(path) || !Files.isRegularFile(path) || !Files.isReadable(path))
            throw new IllegalArgumentException("Path is invalid.");

        List<String> lines = Files.readAllLines(path);

        // parse first line with attributes
        List<String> attributes = new ArrayList<>();
        String[] attrs = lines.get(0).split(",");
        int n = attrs.length;
        targetVariable = attrs[n-1];
        for (int i = 0; i < n-1; i++) {
            attributes.add(attrs[i]);
            attributeValues.put(attrs[i], new TreeSet<>());
        }
        this.attributes = new TreeSet<>(attributes);
        lines.remove(0);

        // parse all other lines
        for (String line : lines) {
            List<DataPoint> dataPoints = new ArrayList<>();
            String[] values = line.split(",");
            targetValues.add(values[n-1]);
            for (int i = 0; i < n-1; i++) {
                attributeValues.get(attributes.get(i)).add(values[i]);
                dataPoints.add(new DataPoint(attributes.get(i), values[i]));
            }
            dataPoints.add(new DataPoint(targetVariable, values[n-1]));
            data.add(new DataEntry(dataPoints));
        }
    }

    /**
     * Returns all attributes.
     * @return all attributes
     */
    public Set<String> getAttributes() {
        return Collections.unmodifiableSet(attributes);
    }

    /**
     * Returns target variable.
     * @return target variable
     */
    public String getTargetVariable() {
        return targetVariable;
    }

    /**
     * Returns attribute values for all attributes.
     * @return attribute values for all attributes
     */
    public Map<String, Set<String>> getAttributeValues() {
        return Collections.unmodifiableMap(attributeValues);
    }

    /**
     * Returns values that the target value can adopt.
     * @return values that the target value can adopt
     */
    public Set<String> getTargetValues() {
        return Collections.unmodifiableSet(targetValues);
    }

    /**
     * Returns data.
     * @return data
     */
    public List<DataEntry> getData() {
        return Collections.unmodifiableList(data);
    }

    /**
     * Checks if two object are equal.
     * @param o object to test
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSet dataset = (DataSet) o;
        return Objects.equals(attributes, dataset.attributes) && Objects.equals(targetVariable, dataset.targetVariable) && Objects.equals(attributeValues, dataset.attributeValues) && Objects.equals(targetValues, dataset.targetValues) && Objects.equals(data, dataset.data);
    }

    /**
     * Returns hash code.
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(attributes, targetVariable, attributeValues, targetValues, data);
    }
}
