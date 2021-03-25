package ui.data;

import java.util.Objects;

/**
 * Class that models a transition between states.
 */
public class Transition {
    /** Destination state */
    private State state;
    /** Cost of transition */
    private double cost;

    /**
     * Constructor.
     * @param state destination state
     * @param cost cost of transition
     */
    public Transition(State state, double cost) {
        this.state = state;
        this.cost = cost;
    }

    /**
     * Returns the destination state.
     * @return destination state
     */
    public State getState() {
        return state;
    }

    /**
     * Returns the transition cost.
     * @return cost of transition
     */
    public double getCost() {
        return cost;
    }

    /**
     * Checks whether two transitions are equal.
     * @param o transition to check
     * @return true if equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;
        return Double.compare(that.cost, cost) == 0 && Objects.equals(state, that.state);
    }

    /**
     * Returns the hash code of the state.
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(state, cost);
    }

    /**
     * Returns string representation of the transition.
     * @return string representation of the transition
     */
    @Override
    public String toString() {
        return String.format("(%s, %.1f)", state, cost);
    }
}
