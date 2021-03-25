package ui.node;

import ui.data.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Models a node in the search tree
 */
public class Node {
    /** Parent node */
    private Node parent;
    /** State of the node */
    private State state;
    /** Cost of the node */
    private double cost;

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
        this.state = state;
        this.cost = cost;
    }

    /**
     * Return the parent node
     * @return parent node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Returns the state
     * @return state
     */
    public State getState() {
        return state;
    }

    /**
     * Returns the cost
     * @return cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * Checks whether nodes are equal
     * @param o node to check
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Double.compare(node.cost, cost) == 0 && Objects.equals(parent, node.parent) && Objects.equals(state, node.state);
    }

    /**
     * Returns the hash code of this node
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(parent, state, cost);
    }

    /**
     * Returns the depth
     * @return depth
     */
    public int getDepth() {
        int depth = 1;
        Node node = getParent();
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
}
