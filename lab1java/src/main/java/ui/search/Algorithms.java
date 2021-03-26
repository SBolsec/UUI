package ui.search;

import ui.data.State;
import ui.data.Transition;
import ui.node.HeuristicNode;
import ui.node.Node;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

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
            for (Transition m : succ.apply(n.getState())) {
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
                if (visited.contains(m.getState())) {
                    continue;
                }

                // check if open contains this state
                // Iterator has to be used because removing an element while looping is not possible
                boolean seenCheeper = false;
                Iterator<Node> it = open.iterator();
                while (it.hasNext()) {
                    Node o = it.next();
                    if (o.getState().equals(m.getState())) {
                        if (o.getCost() <= m.getCost() + n.getCost()) {
                            seenCheeper = true;
                        } else {
                            it.remove();
                        }
                    }
                }

                if (!seenCheeper)
                    open.add(new Node(n, m.getState(), m.getCost() + n.getCost()));
            }
        }
        return new SearchResult(Optional.empty(), statesVisited);
    }

    /**
     * A* search algorithm
     * @param s0 initial state
     * @param succ successor function
     * @param goal goal function
     * @param h heuristic function
     * @return result of search
     */
    public static SearchResult astar(State s0, Function<State, Set<Transition>> succ, Predicate<State> goal, Function<State, Double> h) {
        Queue<HeuristicNode> open = new PriorityQueue<>(HeuristicNode.BY_TOTAL_COST.thenComparing(HeuristicNode.BY_NAME));
        open.add(new HeuristicNode(s0, h.apply(s0)));
        Set<State> closed = new HashSet<>();

        int statesVisited = 0;

        while (!open.isEmpty()) {
            HeuristicNode n = open.remove();
            statesVisited++;
            if (goal.test(n.getState())) {
                return new SearchResult(Optional.of(n), statesVisited);
            }
            closed.add(n.getState());

            for (Transition m : succ.apply(n.getState())) {
                // check if closed contains this state
                if (closed.contains(m.getState())) {
                    continue;
                }

                double cost = m.getCost() + n.getCost();
                double totalCost = cost + h.apply(m.getState());

                // check if open contains this state
                // Iterator has to be used because removing an element while looping is not possible
                boolean seenCheeper = false;
                Iterator<HeuristicNode> it = open.iterator();
                while (it.hasNext()) {
                    HeuristicNode o = it.next();
                    if (o.getState().equals(m.getState())) {
                        if (o.getTotalCost() <= totalCost) {
                            seenCheeper = true;
                        } else {
                            it.remove();
                        }
                    }
                }

                if (!seenCheeper)
                    open.add(new HeuristicNode(n, m.getState(), cost, totalCost));
            }
        }
        return new SearchResult(Optional.empty(), statesVisited);
    }
}
