package ui.data;

import java.util.Objects;

/**
 * Class that models the state.
 */
public class State {
    /** Name of the state */
    private String name;

    /**
     * Constructor.
     * @param name name of the state
     */
    public State(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the state.
     * @return name of the state
     */
    public String getName() {
        return name;
    }

    /**
     * Checks whether two states are equal.
     * @param o state to check
     * @return true if equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(name, state.name);
    }

    /**
     * Returns the hash code of the state.
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Returns the state name.
     * @return state name
     */
    @Override
    public String toString() {
        return name;
    }
}
