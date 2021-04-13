package ui.data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a clause which consists of a set of literals.
 */
public class Clause {
    /** Literals that make up this clause */
    Set<Literal> literals;

    /**
     * Constructor which sets set of literals.
     * @param literals set of literals
     */
    public Clause(Set<Literal> literals) {
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

        // create set of literals from given string
        this.literals = Arrays.stream(input.toLowerCase().trim().split("\\s+v\\s+"))
                .map(Literal::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns unmodifiable set of literals.
     * @return unmodifiable set of literals
     */
    public Set<Literal> getLiterals() {
        return Collections.unmodifiableSet(literals);
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
                .sorted(Comparator.comparing(Literal::getName))
                .map(Literal::toString)
                .collect(Collectors.joining(" v "));
    }
}
