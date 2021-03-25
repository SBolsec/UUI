package ui.search;

import ui.node.Node;

import java.util.Optional;

/**
 * Result of running a search algorithm
 */
public class SearchResult {
    /** Node that was the result */
    private Optional<Node> node;
    /** Number of visited states */
    private int statesVisited;

    /**
     * Constructor.
     * @param node result node
     * @param statesVisited number of visited states
     */
    public SearchResult(Optional<Node> node, int statesVisited) {
        this.node = node;
        this.statesVisited = statesVisited;
    }

    /**
     * Result node
     * @return result node
     */
    public Optional<Node> getNode() {
        return node;
    }

    /**
     * Returns number of visited states
     * @return number of visited states
     */
    public int getStatesVisited() {
        return statesVisited;
    }
}
