package ui.data;

import java.util.Objects;

/**
 * Clause pair that holds two clauses.
 */
public class ClausePair {
    /** First clause of this clause pair */
    private final Clause c1;
    /** Second clause of this clause pair */
    private final Clause c2;

    /**
     * Constructor.
     * @param c1 first clause
     * @param c2 second clause
     */
    public ClausePair(Clause c1, Clause c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    /**
     * Returns the first clause of this pair.
     * @return first clause of this pair
     */
    public Clause getC1() {
        return c1;
    }

    /**
     * Returns the second clause of this pair.
     * @return second clause of this pair
     */
    public Clause getC2() {
        return c2;
    }

    /**
     * Checks whether two pairs are equal.
     * @param o object to test
     * @return true if pairs are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClausePair that = (ClausePair) o;
        return Objects.equals(c1, that.c1) && Objects.equals(c2, that.c2);
    }

    /**
     * Returns hash code.
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(c1, c2);
    }

    @Override
    public String toString() {
        return "(" + c1 + ", " + c2 + ")";
    }
}
