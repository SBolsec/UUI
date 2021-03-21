package me.sbolsec.uui.lab01;

import me.sbolsec.uui.lab01.data.StateCostPair;
import me.sbolsec.uui.lab01.node.CostNode;
import me.sbolsec.uui.lab01.node.HeuristicNode;
import me.sbolsec.uui.lab01.node.SearchResult;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class SearchAlgorithms {
    public static <S> Optional<SearchResult<S>> breadthFirstSearch(S s0, Function<S, Set<StateCostPair<S>>> succ, Predicate<S> goal) {
        int statesVisited = 1;

        Deque<CostNode<S>> open = new LinkedList<>();
        open.add(new CostNode<>(s0, null, 0.0));

        while (!open.isEmpty()) {
            CostNode<S> n = open.removeFirst();
            if (goal.test(n.getState())) return Optional.of(new SearchResult<>(n, statesVisited));
            for (StateCostPair<S> child : succ.apply(n.getState())) {
                statesVisited++;
                open.addLast(new CostNode(child.getState(), n, n.getCost() + child.getCost()));
            }
        }

        return Optional.empty();
    }

    public static <S> Optional<SearchResult<S>> breadthFirstSearchBetter(S s0, Function<S, Set<StateCostPair<S>>> succ, Predicate<S> goal) {
        int statesVisited = 1;
        Deque<CostNode<S>> open = new LinkedList<>();

        CostNode<S> n0 = new CostNode<>(s0, null, 0.0);
        if (goal.test(s0)) return Optional.of(new SearchResult<>(n0, statesVisited));

        open.add(n0);

        while (!open.isEmpty()) {
            CostNode<S> n = open.removeFirst();
            for (StateCostPair<S> child : succ.apply(n.getState())) {
                statesVisited++;
                CostNode<S> childNode = new CostNode<>(child.getState(), n, n.getCost() + child.getCost());
                if (goal.test(child.getState())) return Optional.of(new SearchResult<>(childNode, statesVisited));
                open.addLast(childNode);
            }
        }

        return Optional.empty();
    }

    public static <S> Optional<SearchResult<S>> uniformCostSearch(S s0, Function<S, Set<StateCostPair<S>>> succ, Predicate<S> goal) {
        int statesVisited = 1;
        Queue<CostNode<S>> open = new PriorityQueue<>();
        open.add(new CostNode<>(s0, null, 0.0));

        while (!open.isEmpty()) {
            CostNode<S> n = open.remove();
            if (goal.test(n.getState())) return Optional.of(new SearchResult<>(n, statesVisited));
            for (StateCostPair<S> child : succ.apply(n.getState())) {
                statesVisited++;
                open.add(new CostNode<>(child.getState(), n, n.getCost() + child.getCost()));
            }
        }

        return Optional.empty();
    }

    public static <S> Optional<SearchResult<S>> aStarSearch(S s0, Function<S, Set<StateCostPair<S>>> succ, Predicate<S> goal, Function<S, Double> heuristic) {
        int statesVisited = 0;
        Queue<HeuristicNode<S>> open = new PriorityQueue<>(HeuristicNode.COMPARE_BY_TOTAL);
        open.add(new HeuristicNode<>(s0, null, 0.0, heuristic.apply(s0)));

        while (!open.isEmpty()) {
            HeuristicNode<S> n = open.remove();
            statesVisited++;
            if (goal.test(n.getState())) return Optional.of(new SearchResult<>(n, statesVisited));
            for (StateCostPair<S> child : succ.apply(n.getState())) {
                double cost = n.getCost() + child.getCost();
                double total = cost + heuristic.apply(child.getState());
                open.add(new HeuristicNode<>(child.getState(), n, cost, total));
            }
        }

        return Optional.empty();
    }

    public static <S> Optional<SearchResult<S>> aStarSearchWithVisited(S s0, Function<S, Set<StateCostPair<S>>> succ, Predicate<S> goal, Function<S, Double> heuristic) {
        int statesVisited = 0;
        Queue<HeuristicNode<S>> open = new PriorityQueue<>(HeuristicNode.COMPARE_BY_TOTAL);
        open.add(new HeuristicNode<>(s0, null, 0.0, heuristic.apply(s0)));
        Set<S> visited = new HashSet<>();

        while (!open.isEmpty()) {
            HeuristicNode<S> n = open.remove();
            statesVisited++;
            if (goal.test(n.getState())) return Optional.of(new SearchResult<>(n, statesVisited));
            visited.add(n.getState());
            for (StateCostPair<S> child : succ.apply(n.getState())) {
                if (visited.contains(child.getState())) continue;
                double cost = n.getCost() + child.getCost();
                double total = cost + heuristic.apply(child.getState());
                boolean openHasCheaper = false;
                Iterator<HeuristicNode<S>> it = open.iterator();
                while (it.hasNext()) {
                    HeuristicNode<S> m = it.next();
                    if (!m.getState().equals(child.getState())) continue;
                    if (m.getTotalEstimatedCost() <= total) {
                        openHasCheaper = true;
                    } else {
                        it.remove();
                    }
                    break;
                }
                if (!openHasCheaper) {
                    open.add(new HeuristicNode<>(child.getState(), n, cost, total));
                }
            }
        }

        return Optional.empty();
    }
}
