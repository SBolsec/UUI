package ui.descriptor;

import ui.Utils;
import ui.data.Transition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Class that models the state space.
 */
public class StateSpaceDescriptor {
    /** Initial state */
    private String initialState;
    /** Set of final states */
    private Set<String> finalStates;
    /** Transitions between states */
    private Map<String, Set<Transition>> transitions;

    /**
     * Constructor which initializes the state space from a file.
     * @param filePath path to file from which to initialize state space
     * @throws IOException if there is a problem with the provided path
     * @throws IllegalArgumentException if there is a problem with the provided path
     */
    public StateSpaceDescriptor(String filePath) throws IOException {
        // check provided file path
        Path path = Utils.checkProvidedPath(filePath);

        // load the file while removing comments
        List<String> lines = Files.readAllLines(path)
                .stream()
                .filter(line -> !line.startsWith("#"))
                .map(String::trim)
                .collect(Collectors.toList());

        if (lines.size() < 3)
            throw new IllegalArgumentException("File must contain at least 3 lines: initial state, final states and transitions!");

        // parse the lines
        initialState = lines.get(0);
        finalStates = Arrays.stream(lines.get(1).split(" ")).collect(Collectors.toCollection(HashSet::new));

        transitions = new HashMap<>();
        for (int i = 0, n = lines.size(); i < n; i++) {
            if (i == 0 || i == 1)
                continue;

            String[] l = lines.get(i).split(" ");
            String state = l[0].substring(0, l[0].length() - 1); // remove the colon

            Set<Transition> t = new HashSet<>();
            for (int j = 1; j < l.length; j++) {
                String[] s = l[j].split(",");
                t.add(new Transition(s[0], Double.parseDouble(s[1])));
            }
            this.transitions.put(state, t);
        }
    }

    /**
     * Returns the initial state.
     * @return initial state
     */
    public String getInitialState() {
        return initialState;
    }

    /**
     * Returns unmodifiable set of final states.
     * @return set of final states
     */
    public Set<String> getFinalStates() {
        return finalStates;
    }

    /**
     * Returns unmodifiable map of transitions.
     * @return map of transitions
     */
    public Map<String, Set<Transition>> getTransitions() {
        return transitions;
    }

    /**
     * Return set of successors based on given state sorted by state name.
     */
    public final Function<String, List<Transition>> SUCCESSOR_BY_NAME = state ->
            getTransitions().get(state).stream()
                    .sorted(Transition.BY_NAME)
                    .collect(Collectors.toList());

    public final Function<String, Set<Transition>> SUCCESSOR = state ->
            getTransitions().get(state);

    /**
     * Checks whether the given state is in the set of final states.
     */
    public final Predicate<String> GOAL = state -> getFinalStates().contains(state);
}
