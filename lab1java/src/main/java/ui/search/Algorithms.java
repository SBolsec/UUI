package ui.search;

import ui.data.State;
import ui.data.Transition;
import ui.node.Node;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Holds implementations of search algorithms
 */
public class Algorithms {

    /**
     * Breadth first search
     * @param s0 initial state
     * @param succ successor function
     * @param goal goal function
     * @return result of search
     */
    public static SearchResult bfs(State s0, Function<State, Set<Transition>> succ, Predicate<State> goal) {
        List<Node> open = new LinkedList<>();
        open.add(new Node(s0));
        Set<State> visited = new HashSet<>();

        int statesVisited = 0;

        while (!open.isEmpty()) {
            Node n = open.remove(0);
            statesVisited++;
            if (goal.test(n.getState())) {
                return new SearchResult(Optional.of(n), statesVisited);
            }
            visited.add(n.getState());
            for (Transition m : succ.apply(n.getState()).stream().sorted(Transition.BY_NAME).collect(Collectors.toSet())) {
                if (!visited.contains(m.getState())) {
                    open.add(new Node(n, m.getState(), m.getCost() + n.getCost()));
                }
            }
        }
        return new SearchResult(Optional.empty(), statesVisited);
    }

    /**
     * Uniform cost search
     * @param s0 initial state
     * @param succ successor function
     * @param goal goal function
     * @return result of search
     */
    public static SearchResult ucs(State s0, Function<State, Set<Transition>> succ, Predicate<State> goal) {
        Queue<Node> open = new PriorityQueue<>(Node.BY_COST.thenComparing(Node.BY_NAME));
        open.add(new Node(s0));
        Set<State> visited = new HashSet<>();

        int statesVisited = 0;

        while (!open.isEmpty()) {
            Node n = open.remove();
            statesVisited++;
            if (goal.test(n.getState())) {
                return new SearchResult(Optional.of(n), statesVisited);
            }
            visited.add(n.getState());
            for (Transition m : succ.apply(n.getState())) {
                if (!visited.contains(m.getState()) && !open.stream().map(node -> node.getState()).collect(Collectors.toSet()).contains(m.getState())) {
                    open.add(new Node(n, m.getState(), m.getCost() + n.getCost()));
                }
            }
        }
        return new SearchResult(Optional.empty(), statesVisited);
    }
}
