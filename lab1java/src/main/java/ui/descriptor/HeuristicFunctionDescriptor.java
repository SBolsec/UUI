package ui.descriptor;

import ui.Utils;
import ui.data.State;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that models heuristic functions.
 */
public class HeuristicFunctionDescriptor {
    /** Map that stores heuristics for all states */
    private Map<State, Double> heuristics;

    /**
     * Constructor which initializes the heuristics from provided file.
     * @param filePath path to file from which to initialize heuristics
     * @throws IOException if there is a problem with reading the file
     * @throws IllegalArgumentException if there is a problem with the file path
     */
    public HeuristicFunctionDescriptor(String filePath) throws IOException {
        // check provided file path
        Path path = Utils.checkProvidedPath(filePath);

        // load the file while removing comments
        List<String> lines = Files.readAllLines(path)
                .stream()
                .filter(line -> !line.startsWith("#"))
                .map(String::trim)
                .collect(Collectors.toList());

        heuristics = new HashMap<>();

        // parse the lines
        for (int i = 0, n = lines.size(); i < n; i++) {
            String[] s = lines.get(i).split(" ");
            // remove the colon from the state name
            heuristics.put(new State(s[0].substring(0, s[0].length() - 1)), Double.parseDouble(s[1]));
        }
    }

    /**
     * Returns the heuristics
     * @return map of heuristics
     */
    public Map<State, Double> getHeuristics() {
        return Collections.unmodifiableMap(heuristics);
    }
}
