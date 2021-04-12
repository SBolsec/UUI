package ui.data;

import java.util.Objects;

public class Node {
    private Clause clause;
    private Clause parent1;
    private Clause parent2;

    public Node(Clause clause, Clause parent1, Clause parent2) {
        this.clause = clause;
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    public Clause getClause() {
        return clause;
    }

    public Clause getParent1() {
        return parent1;
    }

    public Clause getParent2() {
        return parent2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(clause, node.clause) && Objects.equals(parent1, node.parent1) && Objects.equals(parent2, node.parent2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clause, parent1, parent2);
    }

    @Override
    public String toString() {
        return "{" + clause + "} (" + parent1 + ", " + parent2 +')';
    }
}
