package ui.heuristic;

import ui.data.Transition;
import ui.descriptor.HeuristicFunctionDescriptor;
import ui.descriptor.StateSpaceDescriptor;
import ui.search.Algorithms;
import ui.search.SearchResult;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Holds methods which check different aspects of a heuristic function
 */
public class Checker {

    /**
     * Checks whether provided heuristic function is optimistic
     */
    public static void checkOptimistic(String pathToStateSpaceDescriptor, String pathToHeuristicFunctionDescriptor) throws IOException {
        System.out.println("# HEURISTIC-OPTIMISTIC " + pathToHeuristicFunctionDescriptor);

        StateSpaceDescriptor ssd = new StateSpaceDescriptor(pathToStateSpaceDescriptor);
        HeuristicFunctionDescriptor hfd = new HeuristicFunctionDescriptor(pathToHeuristicFunctionDescriptor);

        boolean optimistic = true;
        Map<String, Double> sortedMap = new TreeMap<>(hfd.getHeuristics());
        for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
            // find the real cost using ucs
            SearchResult result = Algorithms.ucs(entry.getKey(), ssd.SUCCESSOR, ssd.GOAL);
            double realCost = result.getNode().get().getCost();

            // form the output string
            StringBuilder sb = new StringBuilder();
            sb.append("[CONDITION]: ");
            if (entry.getValue() <= realCost) {
                sb.append("[OK] ");
            } else {
                optimistic = false;
                sb.append("[ERR] ");
            }
            sb.append("h(").append(entry.getKey()).append(") <= h*: ");
            sb.append(String.format("%.1f", entry.getValue()));
            sb.append(" <= ");
            sb.append(String.format("%.1f", realCost));

            System.out.println(sb.toString());
        }

        System.out.println("[CONCLUSION]: Heuristic is" + (optimistic ? "" : " not") + " optimistic.");
    }

    /**
     * Checks whether provided heuristic function is consistent
     */
    public static void checkConsistent(String pathToStateSpaceDescriptor, String pathToHeuristicFunctionDescriptor) throws IOException {
        System.out.println("# HEURISTIC-CONSISTENT " + pathToHeuristicFunctionDescriptor);

        StateSpaceDescriptor ssd = new StateSpaceDescriptor(pathToStateSpaceDescriptor);
        HeuristicFunctionDescriptor hfd = new HeuristicFunctionDescriptor(pathToHeuristicFunctionDescriptor);

        boolean consistent = true;
        Map<String, Double> sortedMap = new TreeMap<>(hfd.getHeuristics());
        for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
            Set<Transition> transitions = new TreeSet<>(Transition.BY_NAME);
            transitions.addAll(ssd.getTransitions().get(entry.getKey()));
            for (Transition t : transitions) {
                double h = hfd.HEURISTIC.apply(t.getState());

                // form the output string
                StringBuilder sb = new StringBuilder();
                sb.append("[CONDITION]: ");
                if (entry.getValue() <= h + t.getCost()) {
                    sb.append("[OK] ");
                } else {
                    consistent = false;
                    sb.append("[ERR] ");
                }
                sb.append("h(").append(entry.getKey()).append(") <= h(").append(t.getState()).append(") + c: ");
                sb.append(String.format("%.1f", entry.getValue()));
                sb.append(" <= ");
                sb.append(String.format("%.1f", hfd.HEURISTIC.apply(t.getState())));
                sb.append(" + ");
                sb.append(String.format("%.1f", t.getCost()));

                System.out.println(sb.toString());
            }
        }

        System.out.println("[CONCLUSION]: Heuristic is" + (consistent ? "" : " not") + " consistent.");
    }
}
