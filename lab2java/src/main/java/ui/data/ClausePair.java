package ui.data;

/**
 * Clause pair that holds two clauses.
 */
public class ClausePair {
    /** First clause of this clause pair */
    private Clause c1;
    /** Second clause of this clause pair */
    private Clause c2;

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
}
