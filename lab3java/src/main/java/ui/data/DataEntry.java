package ui.data;

import java.util.List;

/**
 * Represents one line from the training or testing set.
 */
public class DataEntry {
    /** List of data points this data entry consists of. */
    private final List<DataPoint> dataPoints;

    /**
     * Constructor.
     * @param dataPoints data points of this entry
     */
    public DataEntry(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    /**
     * Returns data point with given attribute
     * @param attribute attribute used to retrieve data point
     * @return data point with given attribute or null if not found
     */
    public DataPoint getDatapoint(String attribute) {
        for (DataPoint d : dataPoints) {
            if (d.getAttribute().equals(attribute))
                return d;
        }
        return null;
    }
}
