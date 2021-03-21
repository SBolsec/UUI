package me.sbolsec.uui.lab01.node;

import java.util.Comparator;

public class HeuristicNode<S> extends CostNode<S> {
    private double totalEstimatedCost;

    public HeuristicNode(S state, HeuristicNode<S> parent, double cost, double totalEstimatedCost) {
        super(state, parent, cost);
        this.totalEstimatedCost = totalEstimatedCost;
    }

    public double getTotalEstimatedCost() {
        return totalEstimatedCost;
    }

    @Override
    public HeuristicNode<S> getParent() {
        return (HeuristicNode<S>) super.getParent();
    }

    @Override
    public String toString() {
        return String.format("(%s, %.1f, %.1f)", state, cost, totalEstimatedCost);
    }

    public static final Comparator<HeuristicNode<?>> COMPARE_BY_COST =
            (n1, n2) -> Double.compare(n1.getCost(), n2.getCost());

    public static final Comparator<HeuristicNode<?>> COMPARE_BY_TOTAL =
            (n1, n2) -> Double.compare(n1.getTotalEstimatedCost(), n2.getTotalEstimatedCost());

    public static final Comparator<HeuristicNode<?>> COMPARE_BY_HEURISTICS =
            (n1, n2) -> Double.compare(n1.getTotalEstimatedCost() - n1.getCost(),
                    n2.getTotalEstimatedCost() - n2.getCost());
}
