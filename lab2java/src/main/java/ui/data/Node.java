package ui.data;

import java.util.Objects;

/**
 * Node that is used to determine the resolution procedure.
 */
public class Node {
    /** Clause that was resolved */
    private final Clause clause;
    /** First parent clause  */
    private final Clause parent1;
    /** Second parent clause */
    private final Clause parent2;

    /**
     * Constructor.
     * @param clause resolved clause
     * @param parent1 first parent clause
     * @param parent2 second parent clause
     */
    public Node(Clause clause, Clause parent1, Clause parent2) {
        this.clause = clause;
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    /**
     * Returns the clause that was resolved.
     * @return resolved clause
     */
    public Clause getClause() {
        return clause;
    }

    /**
     * Returns first parent clause.
     * @return first parent clause
     */
    public Clause getParent1() {
        return parent1;
    }

    /**
     * Returns second parent clause.
     * @return second parent clause
     */
    public Clause getParent2() {
        return parent2;
    }

    /**
     * Checks whether two nodes are equal.
     * @param o object to test
     * @return true if nodes are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(clause, node.clause) && Objects.equals(parent1, node.parent1) && Objects.equals(parent2, node.parent2);
    }

    /**
     * Returns hash code.
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(clause, parent1, parent2);
    }

    /**
     * Returns string representation of the node.
     * @return string representation of the node
     */
    @Override
    public String toString() {
        return "{" + clause + "} (" + parent1 + ", " + parent2 +')';
    }
}
