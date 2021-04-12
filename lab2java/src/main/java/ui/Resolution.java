package ui;

import ui.data.Clause;
import ui.data.ClausePair;
import ui.data.Literal;
import ui.data.Node;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that stores resolution algorithms
 */
public class Resolution {

    public static Map<Clause, Set<Clause>> resolved = new HashMap<>();
    public static Set<Node> nodes = new HashSet<>();

    public static void resolutionByRefutation(List<Clause> initialClauses, Clause last) {
        Set<Clause> clauses = new HashSet<>();
        Set<Clause> sos = new HashSet<>();
        Set<Clause> negatedFinalClause = negateClause(last);
        sos.addAll(negatedFinalClause);

        initialClauses.forEach(c -> {
            // check for tautology, do not add tautology
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
        clauses.forEach(c -> resolved.put(c, new HashSet<>()));
        sos.forEach(c -> resolved.put(c, new HashSet<>()));

        while (true) {
            boolean somethingAdded = false;
            for (ClausePair pair : selectClauses(clauses, sos)) {
                Set<Clause> resolvents = plResolve(pair.getC1(), pair.getC2());

                for (Clause c : resolvents) {
                    if (c.getLiterals().contains(Literal.NIL)) {
                        // Find node that has NIL
                        List<Node> resNodes = new ArrayList<>();
                        for (Node node : nodes) {
                            if (node.getClause().getLiterals().contains(Literal.NIL)) {
                                resNodes.add(node);
                                break;
                            }
                        }
                        // Find all the nodes that lead to NIL
                        Queue<Node> pathNodes = new PriorityQueue<>((a,b) -> a.toString().compareTo(b.toString()));
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
                        List<Clause> pathClauses = new ArrayList<>();
                        List<Node> firstResolved = new ArrayList<>();
                        // find all the clauses that were known from the start
                        Set<Node> premise = new HashSet<>();
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
                        StringBuilder sb = new StringBuilder();
                        int i = 0;
                        for (i = 0; i < pathClauses.size(); i++) {
                            sb.append(i+1).append(". ").append(pathClauses.get(i)).append("\n");
                        }
                        sb.append("===============\n");

                        resNodes.removeAll(premise);
                        pathClauses.addAll(firstResolved.stream().map(a -> a.getClause()).collect(Collectors.toList()));
                        for (int j = 0; j < firstResolved.size(); j++) {
                            Node node = firstResolved.get(j);
                            sb.append(i+1).append(". ").append(node.getClause()).append(" (");
                            sb.append(pathClauses.indexOf(node.getParent1())+1).append(", ");
                            sb.append(pathClauses.indexOf(node.getParent2())+1).append(")\n");
                            i++;
                        }


                        outer: while (!resNodes.isEmpty()) {
                            Iterator<Node> it = resNodes.iterator();
                            while (it.hasNext()) {
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
                        sb.append("===============");
                        System.out.println(sb.toString());
                        System.out.println("[CONCLUSION]: " + last + " is true");

                        return;
                    }

                    Iterator<Clause> it = clauses.iterator();
                    while (it.hasNext()) {
                        Clause clause = it.next();
                        if (clause.getLiterals().containsAll(c.getLiterals())) {
                            it.remove();
                        }
                    }
                    it = sos.iterator();
                    while (it.hasNext()) {
                        Clause clause = it.next();
                        if (clause.getLiterals().containsAll(c.getLiterals())) {
                            it.remove();
                        }
                    }

                    sos.add(c);
                    somethingAdded = true;
                }
            }

            if (!somethingAdded) {
                System.out.println("[CONCLUSION]: " + last + " is unknown");
                return;
            }
        }
    }

    /**
     * Returns negated clauses created from given clause.
     * @param clause clause which to negate
     * @return negated clauses
     */
    public static Set<Clause> negateClause(Clause clause) {
        Set<Clause> result = new HashSet<>();
        for (Literal l : clause.getLiterals()) {
            result.add(new Clause(new HashSet<>(Arrays.asList(new Literal(l.isNegated() ? l.getName() : "~" + l.getName())))));
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
    public static Set<ClausePair> selectClauses(Set<Clause> clauses, Set<Clause> sos) {
        Set<ClausePair> toResolve = new HashSet<>();
        // try to find something to resolve in the initialClauses
        for (Clause s : sos) {
            Set<Clause> r = resolved.get(s);
            for (Literal l1 : s.getLiterals()) {
                for (Clause c : clauses) {
                    if (r != null && r.contains(c)) continue;
                    long count = c.getLiterals().stream()
                            .filter(literal -> literal.getName().equals(l1.getName()) && literal.isNegated() != l1.isNegated())
                            .count();
                    if (count == 0) continue; // skip (a, a), we want (a, ~a)
                    toResolve.add(new ClausePair(c, s));
                }
            }
        }
        // try to find something to resolve in sos
        for (Clause s : sos) {
            Set<Clause> r = resolved.get(s);
            for (Literal l1 : s.getLiterals()) {
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
     * Resolves the given clauses and returns the resolvent
     * @param c1 first parent clause
     * @param c2 second prent clause
     * @return resolvent of the given clauses
     */
    public static Set<Clause> plResolve(Clause c1, Clause c2) {
        Set<Clause> result = new HashSet<>();

        for (Literal literal : c1.getLiterals()) {
            if (c2.getLiterals().stream().filter(l -> l.getName().equals(literal.getName()) && l.isNegated() != literal.isNegated()).count() > 0) {
                Clause c = resolve(c1, c2, literal);
                if (c != null)
                    result.add(c);
            }
        }

        return result;
    }

    public static Clause resolve(Clause c1, Clause c2, Literal literal) {
        Set<Literal> res = new HashSet<>();

        res.addAll(c1.getLiterals());
        res.addAll(c2.getLiterals());
        res.remove(literal);
        res.remove(new Literal(literal.isNegated() ? literal.getName() : "~" + literal.getName()));

        if (res.isEmpty()) {
            Clause nil = new Clause(new HashSet<>(Arrays.asList(Literal.NIL)));
            nodes.add(new Node(nil, c1, c2));
            return nil;
        }

        Clause result = new Clause(res);

        Set<Clause> toAdd = new HashSet<>();
        toAdd.add(c1);
        toAdd.add(c2);
        toAdd.add(result);
        if (resolved.get(c1) != null)
            toAdd.addAll(resolved.get(c1));
        if (resolved.get(c2) != null)
            toAdd.addAll(resolved.get(c2));
        resolved.put(result, toAdd);

        resolved.get(c1).add(c2);
        resolved.get(c2).add(c1);

        // check for tautology
        for (Literal l1 : res) {
            for (Literal l2 : res) {
                if (l1.equals(l2)) continue;
                if (l1.getName().equals(l2.getName()) && l1.isNegated() != l2.isNegated()) {
                    return null;
                }
            }
        }

        nodes.add(new Node(result, c1, c2));
        return result;
    }
}
