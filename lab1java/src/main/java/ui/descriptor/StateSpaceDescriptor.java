package ui.descriptor;

import ui.Utils;
import ui.data.State;
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
    private State initialState;
    /** Set of final states */
    private Set<State> finalStates;
    /** Transitions between states */
    private Map<State, List<Transition>> transitions;

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
        initialState = new State(lines.get(0));
        finalStates = Arrays.stream(lines.get(1).split(" "))
                .map(s -> new State(s))
                .collect(Collectors.toSet());

        transitions = new HashMap<>();
        for (int i = 0, n = lines.size(); i < n; i++) {
            if (i == 0 || i == 1)
                continue;

            String[] l = lines.get(i).split(" ");
            String state = l[0].substring(0, l[0].length() - 1); // remove the colon

            List<Transition> t = new ArrayList<>();
            for (int j = 1; j < l.length; j++) {
                String[] s = l[j].split(",");
                t.add(new Transition(new State(s[0]), Double.parseDouble(s[1])));
            }
            Collections.sort(t, (t1, t2) -> t1.getState().compareTo(t2.getState()));

            this.transitions.put(new State(state), t);
        }
    }

    /**
     * Returns the initial state.
     * @return initial state
     */
    public State getInitialState() {
        return initialState;
    }

    /**
     * Returns unmodifiable set of final states.
     * @return set of final states
     */
    public Set<State> getFinalStates() {
        return Collections.unmodifiableSet(finalStates);
    }

    /**
     * Returns unmodifiable map of transitions.
     * @return map of transitions
     */
    public Map<State, List<Transition>> getTransitions() {
        return Collections.unmodifiableMap(transitions);
    }

    /**
     * Return set of successors based on given state.
     */
    public final Function<State, Set<Transition>> SUCCESSOR = state ->
            getTransitions().get(state).stream()
                .map(s -> (Transition) s)
                .collect(Collectors.toSet());

    /**
     * Checks whether the given state is in the set of final states.
     */
    public final Predicate<State> GOAL = state -> getFinalStates().contains(state);
}
