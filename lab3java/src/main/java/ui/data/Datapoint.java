package ui.data;

import java.util.Objects;

public class Datapoint {
    private String attribute;
    private String value;

    public Datapoint(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Datapoint datapoint = (Datapoint) o;
        return Objects.equals(attribute, datapoint.attribute) && Objects.equals(value, datapoint.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attribute, value);
    }

    @Override
    public String toString() {
        return "Datapoint{" +
                "attribute='" + attribute + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
