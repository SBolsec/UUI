package ui;

import ui.data.Clause;
import ui.data.ClausePair;
import ui.data.Literal;
import ui.data.Node;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that conducts the resolution procedure and
 * holds all te supporting data structures.
 */
public class Resolution {
    /** Map that stores a set of resolved clauses for each clause */
    private Map<Clause, Set<Clause>> resolved;
    /** Set of nodes used to trace back the resolution procedure */
    private Set<Node> nodes;

    /**
     * Starts the resolution procedure.
     * @param initialClauses list of initial clauses
     * @param last clause which the procedure wants to test
     */
    public boolean resolutionByRefutation(List<Clause> initialClauses, Clause last) {
        // initialize data structures
        resolved = new HashMap<>(); // map of resolved clauses that will keep track of everything that has been tried
        nodes = new HashSet<>(); // set of nodes used to trace back the resolution procedure

        Set<Clause> clauses = initializeClauses(initialClauses); // set of initial clauses

        Set<Clause> negatedFinalClause = negateClause(last); // clauses generated by negating the last clause
        Set<Clause> sos = new HashSet<>(negatedFinalClause); // initialize the sos set

        // initialize the map of resolved clauses
        clauses.forEach(c -> resolved.put(c, new HashSet<>(Collections.singletonList(c))));
        sos.forEach(c -> resolved.put(c, new HashSet<>(Collections.singletonList(c))));

        while (true) {
            boolean somethingAdded = false; // flag that is used to stop the outer loop
            for (ClausePair pair : selectClauses(clauses, sos)) {
                Set<Clause> resolvents = plResolve(pair.getC1(), pair.getC2());

                for (Clause c : resolvents) {
                    // if NIL is found, the procedure is done and the output is generated
                    if (c.getLiterals().contains(Literal.NIL)) {
                        printResult(initialClauses, last, negatedFinalClause);
                        return true;
                    }

                    // removing redundant clauses using the absorption equivalence: F && (F || G) === F
                    Iterator<Clause> it = clauses.iterator(); // removing from starting clauses
                    while (it.hasNext()) {
                        Clause clause = it.next();
                        if (clause.getLiterals().containsAll(c.getLiterals())) {
                            it.remove();
                        }
                    }
                    it = sos.iterator(); // removing from sos
                    while (it.hasNext()) {
                        Clause clause = it.next();
                        if (clause.getLiterals().containsAll(c.getLiterals())) {
                            it.remove();
                        }
                    }

                    sos.add(c); // add the new clause to sos
                    somethingAdded = true; // set the flag so the loop doesn't end
                }
            }

            if (!somethingAdded) { // if nothing was added, the procedure stops
                printError(initialClauses, last, negatedFinalClause);
                return false;
            }
        }
    }

    /**
     * Generates set of starting clauses from the given list of initial clauses.
     * @param initialClauses list of initial clauses
     * @return set of starting clauses
     */
    private Set<Clause> initializeClauses(List<Clause> initialClauses) {
        Set<Clause> clauses = new HashSet<>();
        initialClauses.forEach(c -> { // check for tautology, do not add tautology
            boolean add = true;
            outer:
            for (Literal l1 : c.getLiterals()) {
                for (Literal l2 : c.getLiterals()) {
                    if (l1.equals(l2)) continue;
                    if (l1.getName().equals(l2.getName()) && l1.isNegated() != l2.isNegated()) {
                        add = false;
                        break outer;
                    }
                }
            }
            if (add) clauses.add(c);
        });
        // removing redundant clauses using the absorption equivalence: F && (F || G) === F
        Iterator<Clause> it = clauses.iterator();
        while (it.hasNext()) {
            Clause clause = it.next();
            for (Clause c : clauses) {
                if (clause.equals(c)) continue; // do not remove yourself
                if (clause.getLiterals().containsAll(c.getLiterals())) {
                    it.remove();
                    break;
                }
            }
        }
        return clauses;
    }

    /**
     * Returns set of negated clauses created from given clause.
     * @param clause clause which to negate
     * @return set of negated clauses
     */
    private Set<Clause> negateClause(Clause clause) {
        Set<Clause> result = new HashSet<>();
        for (Literal l : clause.getLiterals()) {
            result.add(new Clause(new HashSet<>(Collections.singletonList(new Literal(l.isNegated() ? l.getName() : "~" + l.getName())))));
        }
        return result;
    }

    /**
     * Select pairs of clauses which to resolve from which one is always from sos.
     * First clause in pair is from initial clauses, second is from sos.
     * @param clauses initial clauses
     * @param sos clauses in sos
     * @return set of clauses to resolve
     */
    private Set<ClausePair> selectClauses(Set<Clause> clauses, Set<Clause> sos) {
        Set<ClausePair> toResolve = new HashSet<>();

        // one of the clauses to resolve has to be from sos
        for (Clause s : sos) {
            Set<Clause> r = resolved.get(s);

            for (Literal l1 : s.getLiterals()) {
                // try to find something to resolve in the initialClauses
                for (Clause c : clauses) {
                    if (r != null && r.contains(c)) continue;
                    long count = c.getLiterals().stream()
                            .filter(literal -> literal.getName().equals(l1.getName()) && literal.isNegated() != l1.isNegated())
                            .count();
                    if (count == 0) continue; // skip (a, a), we want (a, ~a)
                    toResolve.add(new ClausePair(c, s));
                }
                // try to find something to resolve in the sos
                for (Clause c : sos) {
                    if (r != null && r.contains(c)) continue;
                    if (s.equals(c)) continue;
                    long count = c.getLiterals().stream()
                            .filter(literal -> literal.getName().equals(l1.getName()) && literal.isNegated() != l1.isNegated())
                            .count();
                    if (count == 0) continue; // skip (a, a), we want (a, ~a)
                    toResolve.add(new ClausePair(c, s));
                }
            }
        }

        return toResolve;
    }

    /**
     * Resolves the given clauses and returns a set of resolvent clauses
     * @param c1 first parent clause
     * @param c2 second prent clause
     * @return set of resolvent clauses of the given parent clauses
     */
    private Set<Clause> plResolve(Clause c1, Clause c2) {
        Set<Clause> result = new HashSet<>();

        // iterate over every literal in the first clause
        for (Literal literal : c1.getLiterals()) {
            // if the second clause contains the negated literal from the first clause, resolve it
            if (c2.getLiterals().stream().anyMatch(l -> l.getName().equals(literal.getName()) && l.isNegated() != literal.isNegated())) {
                Clause c = resolve(c1, c2, literal);
                if (c != null) // if result is null there was a tautology
                    result.add(c); // else there was a concrete result so we add it
            }
        }

        return result;
    }

    /**
     * Resolves the given clauses using the given literal.
     * @param c1 first clause
     * @param c2 second clause
     * @param literal literal which to resolve
     * @return resolved clause
     */
    private Clause resolve(Clause c1, Clause c2, Literal literal) {
        Set<Literal> res = new HashSet<>(); // set of resolved literals

        res.addAll(c1.getLiterals()); // add all literals of first clause
        res.addAll(c2.getLiterals()); // add all literals of second clause
        res.remove(literal); // remove given literal
        // remove negated version of given literal
        res.remove(new Literal(literal.isNegated() ? literal.getName() : "~" + literal.getName()));

        // if everything was resolved return clause containing NIL
        if (res.isEmpty()) {
            Clause nil = new Clause(new HashSet<>(Collections.singletonList(Literal.NIL)));
            nodes.add(new Node(nil, c1, c2)); // create a node for this resolution
            return nil;
        }

        Clause result = new Clause(res); // create clause using resolved literals

        // update the according sets of resolved clauses to stop the same clauses from resolving each other
        Set<Clause> toAdd = new HashSet<>();
        toAdd.add(c1);
        toAdd.add(c2);
        toAdd.add(result);
        if (resolved.get(c1) != null)
            toAdd.addAll(resolved.get(c1));
        if (resolved.get(c2) != null)
            toAdd.addAll(resolved.get(c2));
        resolved.put(result, toAdd);

        resolved.get(c1).add(c2); // add c2 to the set of resolved clauses for c1
        resolved.get(c2).add(c1); // add c1 to the set of resolved clauses for c2

        // check for tautology
        for (Literal l1 : res) {
            for (Literal l2 : res) {
                if (l1.equals(l2)) continue;
                if (l1.getName().equals(l2.getName()) && l1.isNegated() != l2.isNegated()) {
                    return null;
                }
            }
        }

        nodes.add(new Node(result, c1, c2)); // create a node for this resolution
        return result;
    }

    /**
     * Prints resolution procedure.
     * @param initialClauses list of initialClauses
     * @param last final clause
     * @param negatedFinalClause clauses generated from the final clause
     */
    private void printResult(List<Clause> initialClauses, Clause last, Set<Clause> negatedFinalClause) {
        // Find node that has NIL
        List<Node> resNodes = new ArrayList<>();
        for (Node node : nodes) {
            if (node.getClause().getLiterals().contains(Literal.NIL)) {
                resNodes.add(node);
                break;
            }
        }

        // Find all the nodes that are on the resolution procedure
        Queue<Node> pathNodes = new PriorityQueue<>(Comparator.comparing(Node::toString));
        pathNodes.offer(resNodes.get(0));
        while (!pathNodes.isEmpty()) {
            Node n = pathNodes.poll();
            for (Node node : nodes) {
                if (node.getClause().equals(n.getParent1()) || node.getClause().equals(n.getParent2())) {
                    resNodes.add(node);
                    pathNodes.offer(node);
                }
            }
        }

        // find all the clauses that were known from the start
        List<Clause> pathClauses = new ArrayList<>(); // stores clauses that are on the procedure path
        List<Node> firstResolved = new ArrayList<>(); // stores nodes that have both parent clauses in the initial clauses set
        Set<Node> premise = new HashSet<>(); // stores nodes that will be removed for the next step
        for (Node node : resNodes) {
            if ((initialClauses.contains(node.getParent1()) || negatedFinalClause.contains(node.getParent1())) &&
                    initialClauses.contains(node.getParent2()) || negatedFinalClause.contains(node.getParent2())) {
                if (!pathClauses.contains(node.getParent1()))
                    pathClauses.add(node.getParent1());
                if (!pathClauses.contains(node.getParent2()))
                    pathClauses.add(node.getParent2());
                if (!firstResolved.contains(node.getClause()))
                    firstResolved.add(node);
                premise.add(node);
            }
            if (initialClauses.contains(node.getParent1()) || negatedFinalClause.contains(node.getParent1())) {
                if (!pathClauses.contains(node.getParent1()))
                    pathClauses.add(node.getParent1());
            }
            if (initialClauses.contains(node.getParent2()) || negatedFinalClause.contains(node.getParent2())) {
                if (!pathClauses.contains(node.getParent2()))
                    pathClauses.add(node.getParent2());
            }
        }

        // StringBuilder that stores the output string
        StringBuilder sb = new StringBuilder();
        int i;
        // Add all the clauses that were known in the initial clauses to the output string
        for (i = 0; i < pathClauses.size(); i++) {
            sb.append(i+1).append(". ").append(pathClauses.get(i)).append("\n");
        }
        sb.append("===============\n");

        // remove all nodes that are fully processed
        resNodes.removeAll(premise);
        // add all of the clauses that were resolved using clauses from the initial clauses
        pathClauses.addAll(firstResolved.stream().map(Node::getClause).collect(Collectors.toList()));
        // add all of the previously mentioned clauses to the output string
        for (Node node : firstResolved) {
            sb.append(i + 1).append(". ").append(node.getClause()).append(" (");
            sb.append(pathClauses.indexOf(node.getParent1()) + 1).append(", ");
            sb.append(pathClauses.indexOf(node.getParent2()) + 1).append(")\n");
            i++;
        }

        // iterate over all of the remaining nodes
        outer: while (!resNodes.isEmpty()) {
            Iterator<Node> it = resNodes.iterator();
            while (it.hasNext()) {
                // both parent clauses of the node are already in the list of completed clauses, add the resolved clause as well
                Node node = it.next();
                if (pathClauses.contains(node.getParent1()) && pathClauses.contains(node.getParent2())) {
                    pathClauses.add(node.getClause());
                    it.remove();
                    sb.append(i+1).append(". ").append(node.getClause()).append(" (");
                    sb.append(pathClauses.indexOf(node.getParent1())+1).append(", ");
                    sb.append(pathClauses.indexOf(node.getParent2())+1).append(")\n");
                    i++;
                    continue outer;
                }
            }
        }
        // print the generated output string of the resolution process and the conclusion
        sb.append("===============");
        System.out.println(sb.toString());
        System.out.println("[CONCLUSION]: " + last + " is true");
    }

    /**
     * Prints error message.
     * @param initialClauses set of initial clauses
     * @param last last clause
     * @param negatedFinalClause set of negated clauses generated from last clause
     */
    private void printError(List<Clause> initialClauses, Clause last, Set<Clause> negatedFinalClause) {
        int i = 1;
        for (Clause c : initialClauses) {
            System.out.println(i++ + ". " + c);
        }
        for (Clause c : negatedFinalClause) {
            System.out.println(i++ + ". " + c);
        }
        System.out.println("===============");
        System.out.println("[CONCLUSION]: " + last + " is unknown");
    }
}
