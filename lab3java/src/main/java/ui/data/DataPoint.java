package ui.data;

import java.util.Objects;

/**
 * Connects attributes and values.
 */
public class DataPoint {
    /** Attribute of this data point. */
    private final String attribute;
    /** Value of this data point. */
    private final String value;

    public DataPoint(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * Returns attribute.
     * @return attribute
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Returns value.
     * @return value
     */
    public String getValue() {
        return value;
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
        DataPoint datapoint = (DataPoint) o;
        return Objects.equals(attribute, datapoint.attribute) && Objects.equals(value, datapoint.value);
    }

    /**
     * Returns hash code.
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(attribute, value);
    }

    /**
     * Returns string representation of object.
     * @return string representation of object.
     */
    @Override
    public String toString() {
        return "Datapoint{" +
                "attribute='" + attribute + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
