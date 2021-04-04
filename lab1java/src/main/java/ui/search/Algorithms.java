package ui.search;

import ui.data.Transition;
import ui.node.HeuristicNode;
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
    public static SearchResult bfs(String s0, Function<String, List<Transition>> succ, Predicate<String> goal) {
        List<Node> open = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        // with this set we sacrifice some memory for the sake of a lot of speed
        Set<String> openSet = new HashSet<>();

        open.add(new Node(s0));
        openSet.add(s0);

        int statesVisited = 0;

        while (!open.isEmpty()) {
            Node n = open.remove(0);
            statesVisited++;
            if (goal.test(n.getState())) {
                return new SearchResult(Optional.of(n), statesVisited);
            }
            visited.add(n.getState());

            for (Transition m : succ.apply(n.getState())) {
                // check if state was already visited
                if (visited.contains(m.getState()))
                    continue;

                // check if state is in open
                if (openSet.contains(m.getState()))
                    continue;

                open.add(new Node(n, m.getState(), m.getCost() + n.getCost()));
                openSet.add(m.getState());
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
    public static SearchResult ucs(String s0, Function<String, Set<Transition>> succ, Predicate<String> goal) {
        Queue<Node> open = new PriorityQueue<>(Node.BY_COST.thenComparing(Node.BY_NAME));
        Set<String> visited = new HashSet<>();
        // with this set we sacrifice some memory for the sake of a lot of speed
        Set<String> openSet = new HashSet<>();

        open.add(new Node(s0));
        openSet.add(s0);

        int statesVisited = 0;

        while (!open.isEmpty()) {
            Node n = open.poll();
            openSet.remove(n.getState());
            statesVisited++;
            if (goal.test(n.getState())) {
                return new SearchResult(Optional.of(n), statesVisited);
            }
            visited.add(n.getState());

            for (Transition m : succ.apply(n.getState())) {
                // continue if this state has already been visited
                if (visited.contains(m.getState())) {
                    continue;
                }

                // if this state is not in open then there is no need to search for it in open
                if (!openSet.contains(m.getState())) {
                    open.offer(new Node(n, m.getState(), m.getCost() + n.getCost()));
                    openSet.add(m.getState());
                    continue;
                }

                // check if there is a cheaper node for this state in open
                // if there is a node that is more expensive, remove it
                double cost = m.getCost() + n.getCost();
                boolean seenCheaper = false;
                Iterator<Node> it = open.iterator();
                while (it.hasNext()) {
                    Node o = it.next();
                    if (o.getState().equals(m.getState())) {
                        if (o.getCost() <= cost) {
                            seenCheaper = true;
                            break;
                        } else {
                            it.remove();
                            break;
                        }
                    }
                }

                if (!seenCheaper) {
                    open.offer(new Node(n, m.getState(), m.getCost() + n.getCost()));
                    openSet.add(m.getState());
                }
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
    public static SearchResult astar(String s0, Function<String, Set<Transition>> succ, Predicate<String> goal, Function<String, Double> h) {
        Queue<HeuristicNode> open = new PriorityQueue<>(HeuristicNode.BY_TOTAL_COST.thenComparing(HeuristicNode.BY_NAME));
        Set<String> closed = new HashSet<>();
        // with this set we sacrifice some memory for the sake of a lot of speed
        Set<String> openSet = new HashSet<>();

        open.add(new HeuristicNode(s0, h.apply(s0)));
        openSet.add(s0);

        int statesVisited = 0;

        while (!open.isEmpty()) {
            HeuristicNode n = open.poll();
            openSet.remove(n.getState());
            statesVisited++;
            if (goal.test(n.getState())) {
                return new SearchResult(Optional.of(n), statesVisited);
            }
            closed.add(n.getState());

            Set<Transition> successors = succ.apply(n.getState());
            for (Transition m : successors) {
                // check if closed contains this state
                if (closed.contains(m.getState())) {
                    continue;
                }

                double cost = m.getCost() + n.getCost();
                double totalCost = cost + h.apply(m.getState());
                String state = m.getState();

                // if the state is not in open, add it and continue with the loop
                // no need to check if there is cheaper in open
                if (!openSet.contains(state)) {
                    open.offer(new HeuristicNode(n, state, cost, totalCost));
                    openSet.add(state);
                    continue;
                }

                // Iterator has to be used because removing an element while looping is not possible
                boolean seenCheaper = false;
                Iterator<HeuristicNode> it = open.iterator();
                while (it.hasNext()) {
                    HeuristicNode o = it.next();
                    if (o.getState().equals(state)) {
                        if (o.getTotalCost() <= totalCost) {
                            seenCheaper = true;
                            break;
                        } else {
                            it.remove();
                            break;
                        }
                    }
                }

                if (!seenCheaper) {
                    open.offer(new HeuristicNode(n, state, cost, totalCost));
                    openSet.add(state);
                }
            }
        }
        return new SearchResult(Optional.empty(), statesVisited);
    }
}
