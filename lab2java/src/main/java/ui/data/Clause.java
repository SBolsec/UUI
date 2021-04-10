package ui.data;

import java.util.*;
import java.util.stream.Collectors;

public class Clause {
    /** Literals that make up this clause */
    List<Literal> literals;
    /** Flags whether the clause has been resolved */
    boolean resolved = false;

    /**
     * Constructor which sets set of literals.
     * @param literals set of literals
     */
    public Clause(List<Literal> literals) {
        this.literals = literals;
    }

    /**
     * Constructor which takes string representation of clause.
     * @param input string representation of clause
     * @throws IllegalArgumentException if input is empty or null
     */
    public Clause(String input) {
        if (input == null || input.isEmpty())
            throw new IllegalArgumentException("Input string was empty or null");

        this.literals = Arrays.stream(input.toLowerCase().split("\\s+v\\s+"))
                .map(s -> new Literal(s))
                .collect(Collectors.toList());
    }

    /**
     * Returns unmodifiable list of literals.
     * @return unmodifiable list of literals
     */
    public List<Literal> getLiterals() {
        return Collections.unmodifiableList(literals);
    }

    /**
     * Returns whether this clause is resolved
     * @return true if this clause is resolved, false otherwise
     */
    public boolean isResolved() {
        return resolved;
    }

    /**
     * Sets resolved flag
     * @param resolved flag to set
     */
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    /**
     * Checks whether two clauses are equal.
     * @param o object to test
     * @return true if clauses are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clause clause = (Clause) o;
        return Objects.equals(literals, clause.literals);
    }

    /**
     * Returns hash code.
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(literals);
    }

    /**
     * Returns string representation of clause.
     * @return string representation of clause
     */
    @Override
    public String toString() {
        return literals.stream()
                .sorted((a,b) -> a.getName().compareTo(b.getName()))
                .map(a -> a.toString())
                .collect(Collectors.joining(" v "));
    }
}
