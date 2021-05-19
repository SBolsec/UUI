package ui.data;

import java.util.List;

public class DataEntry {
    private List<Datapoint> datapoints;

    public DataEntry(List<Datapoint> datapoints) {
        this.datapoints = datapoints;
    }

    public List<Datapoint> getDatapoints() {
        return datapoints;
    }

    public Datapoint getDatapoint(String attribute) {
        for (Datapoint d : datapoints) {
            if (d.getAttribute().equals(attribute))
                return d;
        }
        return null;
    }
}
