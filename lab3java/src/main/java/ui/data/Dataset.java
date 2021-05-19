package ui.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Dataset {
    private Set<String> attributes = new TreeSet<>();
    private String targetVariable;

    private Map<String, Set<String>> attributeValues = new HashMap<>();
    private Set<String> targetValues = new TreeSet<>();

    private List<DataEntry> data = new ArrayList<>();

    public Dataset(Path path) throws IOException {
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
            List<Datapoint> datapoints = new ArrayList<>();
            String[] values = line.split(",");
            targetValues.add(values[n-1]);
            for (int i = 0; i < n-1; i++) {
                attributeValues.get(attributes.get(i)).add(values[i]);
                datapoints.add(new Datapoint(attributes.get(i), values[i]));
            }
            datapoints.add(new Datapoint(targetVariable, values[n-1]));
            data.add(new DataEntry(datapoints));
        }
    }

    public Set<String> getAttributes() {
        return attributes;
    }

    public String getTargetVariable() {
        return targetVariable;
    }

    public Map<String, Set<String>> getAttributeValues() {
        return attributeValues;
    }

    public Set<String> getTargetValues() {
        return targetValues;
    }

    public List<DataEntry> getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dataset dataset = (Dataset) o;
        return Objects.equals(attributes, dataset.attributes) && Objects.equals(targetVariable, dataset.targetVariable) && Objects.equals(attributeValues, dataset.attributeValues) && Objects.equals(targetValues, dataset.targetValues) && Objects.equals(data, dataset.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes, targetVariable, attributeValues, targetValues, data);
    }
}
