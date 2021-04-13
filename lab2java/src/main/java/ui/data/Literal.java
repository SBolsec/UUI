package ui.data;

import java.util.Objects;

/**
 * One literal that is part of clauses.
 * Example: a, ~b
 */
public class Literal {
    /** Name of the literal */
    private final String name;
    /** Flag whether the literal is negated */
    private final boolean negated;
    /** NIL literal */
    public static final Literal NIL = new Literal("NIL");

    /**
     * Constructor which sets the literal and determines whether it is negated.
     * @param name name of the literal
     */
    public Literal(String name) {
        this(name.startsWith("~") ? name.substring(1) : name, name.startsWith("~"));
    }

    /**
     * Constructor.
     * @param name name of the literal
     * @param negated true if the literal is negated, false otherwise
     */
    public Literal(String name, boolean negated) {
        this.name = name;
        this.negated = negated;
    }

    /**
     * Returns the name of the literal.
     * @return name of the literal
     */
    public String getName() {
        return name;
    }

    /**
     * Returns whether the literal is negated.
     * @return true if the literal is negated, false otherwise
     */
    public boolean isNegated() {
        return negated;
    }

    /**
     * Returns string representation of literal.
     * @return string representation of literal
     */
    @Override
    public String toString() {
        return (negated ? "~" : "") + name;
    }

    /**
     * Checks whether two literal are equal.
     * @param o object to test
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return negated == literal.negated && Objects.equals(name, literal.name);
    }

    /**
     * Returns hash code.
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, negated);
    }
}
