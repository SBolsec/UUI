package me.sbolsec.uui.lab01.node;

public class SearchResult<S> {
    private BasicNode<S> node;
    private int statesVisited;

    public SearchResult(BasicNode<S> node, int statesVisited) {
        super();
        this.node = node;
        this.statesVisited = statesVisited;
    }

    public BasicNode<S> getNode() {
        return node;
    }

    public int getStatesVisited() {
        return statesVisited;
    }
}
