package ui.node;

import ui.data.State;
import ui.data.Transition;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Models a node in the search tree
 */
public class Node {
    /** Parent node */
    protected Node parent;
    /** State of the node */
    protected Transition transition;

    /**
     * Initializes node with no parent and sets cost to 0.
     * @param state state
     */
    public Node(State state) {
        this(null, state, 0);
    }

    /**
     * Constructor.
     * @param parent parent node
     * @param state state
     * @param cost cost
     */
    public Node(Node parent, State state, double cost) {
        this.parent = parent;
        this.transition = new Transition(state, cost);
    }

    /**
     * Return the parent node
     * @return parent node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Returns the transition
     * @return transition
     */
    public Transition getTransition() {
        return transition;
    }

    /**
     * Returns the state
     * @return state
     */
    public State getState() {
        return transition.getState();
    }

    /**
     * Returns the cost
     * @return cost
     */
    public double getCost() {
        return transition.getCost();
    }

    /**
     * Checks whether the objects are equal
     * @param o object to test
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(parent, node.parent) && Objects.equals(transition, node.transition);
    }

    /**
     * Returns hash code
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(parent, transition);
    }

    /**
     * Returns the depth
     * @return depth
     */
    public int getDepth() {
        int depth = 1;
        Node node = this;
        while (node.getParent() != null) {
            node = node.getParent();
            depth++;
        }
        return depth;
    }

    /**
     * Returns path from start to this node
     * @return path from start to this node
     */
    public List<Node> getPath() {
        List<Node> path = new ArrayList<>();

        path.add(this);
        Node node = getParent();
        while (node != null) {
            path.add(0, node);
            node = node.getParent();
        }

        return path;
    }

    /**
     * Comparator by the state name.
     */
    public static final Comparator<Node> BY_NAME = (n1, n2) -> {
        if (n1 == null && n2 == null) return 0;
        if (n1 == null) return 1;
        if (n2 == null) return -1;
        return n1.getState().getName().compareTo(n2.getState().getName());
    };

    /**
     * Comparator by the cost.
     */
    public static final Comparator<Node> BY_COST= (n1, n2) -> {
        if (n1 == null && n2 == null) return 0;
        if (n2 == null) return -1;
        if (n1 == null) return 1;
        return Double.compare(n1.getCost(), n2.getCost());
    };
}
