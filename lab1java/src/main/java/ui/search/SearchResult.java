package ui.search;

import ui.Utils;
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

    /**
     * Returns a string representation of the search result.
     * @return string representation of search result
     */
    @Override
    public String toString() {
        if (getNode().isEmpty()) {
            return "[FOUND_SOLUTION]: no\n";
        }
        StringBuilder sb = new StringBuilder();
        Node node = getNode().get();
        sb.append("[FOUND_SOLUTION]: yes\n");
        sb.append("[STATES_VISITED]: ").append(getStatesVisited()).append("\n");
        sb.append("[PATH_LENGTH]: ").append(node.getDepth()).append("\n");
        sb.append("[TOTAL_COST]: ").append(String.format("%.1f\n", node.getCost()));
        sb.append("[PATH]: " + Utils.getPathAsString(node.getPath()));
        return sb.toString();
    }
}
