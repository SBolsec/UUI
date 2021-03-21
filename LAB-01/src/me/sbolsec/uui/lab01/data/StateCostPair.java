package me.sbolsec.uui.lab01.data;

public class StateCostPair<S> {
    private S state;
    private double cost;

    public StateCostPair(S state, double cost) {
        super();
        this.state = state;
        this.cost = cost;
    }

    public S getState() {
        return state;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return String.format("<%s, %.1f>", state, cost);
    }
}
