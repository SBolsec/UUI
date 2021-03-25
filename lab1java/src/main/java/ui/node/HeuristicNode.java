package ui.node;

import ui.data.State;

import java.util.Comparator;

/**
 * Models a node used in heuristic algorithms
 */
public class HeuristicNode extends Node {
    /** Total cost for this state */
    private double totalCost;

    /**
     * Initializes node with given state and totalCost, sets parent to null and cost to 0.
     * @param state state
     * @param totalCost total cost
     */
    public HeuristicNode(State state, double totalCost) {
        this(null, state, 0, totalCost);
    }

    /**
     * Constructor.
     * @param parent parent node
     * @param state state
     * @param cost cost
     * @param totalCost total cost
     */
    public HeuristicNode(Node parent, State state, double cost, double totalCost) {
        super(parent, state, cost);
        this.totalCost = totalCost;
    }

    /**
     * Returns the total cost
     * @return totalCost
     */
    public double getTotalCost() {
        return totalCost;
    }

    public static final Comparator<HeuristicNode> BY_TOTAL_COST = (n1, n2) -> {
        if (n1 == null && n2 == null) return 0;
        if (n1 == null) return 1;
        if (n2 == null) return -1;
        return Double.compare(n1.getTotalCost(), n2.getTotalCost());
    };
}
