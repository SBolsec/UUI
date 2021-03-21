package me.sbolsec.uui.lab01.node;

public class CostNode<S> extends BasicNode<S> implements Comparable<CostNode<S>> {
    protected double cost;

    public CostNode(S state, BasicNode<S> parent, double cost) {
        super(state, parent);
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public CostNode<S> getParent() {
        return (CostNode<S>) super.getParent();
    }

    @Override
    public int compareTo(CostNode<S> o) {
        return Double.compare(this.cost, o.cost);
    }
}
