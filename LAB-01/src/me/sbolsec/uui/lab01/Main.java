package me.sbolsec.uui.lab01;

import me.sbolsec.uui.lab01.data.HeuristicFunctionDescriptor;
import me.sbolsec.uui.lab01.data.State;
import me.sbolsec.uui.lab01.data.StateSpaceDescriptor;
import me.sbolsec.uui.lab01.node.BasicNode;
import me.sbolsec.uui.lab01.node.CostNode;
import me.sbolsec.uui.lab01.node.HeuristicNode;
import me.sbolsec.uui.lab01.node.SearchResult;

import java.io.IOException;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws IOException {
        String path1 = "/home/sbolsec/Documents/lab1_files/maps/istra.txt";
        String path2 = "/home/sbolsec/Documents/lab1_files/maps/istra_heuristic.txt";

        StateSpaceDescriptor ssd = new StateSpaceDescriptor(path1);
        HeuristicFunctionDescriptor hfd = new HeuristicFunctionDescriptor(path2);

        System.out.println("=== BFS ===");
        Optional<SearchResult<State>> res1 = SearchAlgorithms.breadthFirstSearch(
                ssd.getInitialState(), ssd.successor, ssd.goal);
        printResult(res1);

        System.out.println("\n=== Better BFS ===");
        Optional<SearchResult<State>> res2 = SearchAlgorithms.breadthFirstSearchBetter(
                ssd.getInitialState(), ssd.successor, ssd.goal);
        printResult(res2);

        System.out.println("\n=== Uniform cost search ===");
        Optional<SearchResult<State>> res3 = SearchAlgorithms.uniformCostSearch(
                ssd.getInitialState(), ssd.successor, ssd.goal);
        printResult(res3);

        System.out.println("\n=== A* ===");
        Optional<SearchResult<State>> res4 = SearchAlgorithms.aStarSearch(
                ssd.getInitialState(), ssd.successor, ssd.goal,
                (state) -> hfd.getHeuristics().get(state.getName())
        );
        printResult(res4);

        System.out.println("\n=== A* with visited ===");
        Optional<SearchResult<State>> res5 = SearchAlgorithms.aStarSearchWithVisited(
                ssd.getInitialState(), ssd.successor, ssd.goal,
                (state) -> hfd.getHeuristics().get(state.getName())
        );
        printResult(res5);
    }

    public static void printResult(Optional<SearchResult<State>> result) {
        if (result.isEmpty()) {
            System.out.println("[FOUND_SOLUTION]: no");
            return;
        }

        SearchResult<State> res = result.get();
        CostNode<State> node = (CostNode<State>) res.getNode();

        System.out.println("[FOUND_SOLUTION]: yes");
        System.out.println("[STATES_VISITED]: " +  res.getStatesVisited());
        System.out.println("[PATH_LENGTH]: " + node.getDepth());
        System.out.println("[TOTAL_COST]: " + node.getCost());
        System.out.println("[PATH]: " + BasicNode.nodePath(node));
    }
}
