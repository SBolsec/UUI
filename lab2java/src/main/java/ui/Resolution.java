package ui;

import ui.data.Clause;
import ui.data.ClausePair;
import ui.data.Literal;

import java.util.*;

/**
 * Class that stores resolution algorithms
 */
public class Resolution {

    public static Map<Clause, Set<Clause>> resolved = new HashMap<>();

    public static void resolutionByRefutation(List<Clause> initialClauses, Clause last) {
        Set<Clause> clauses = new HashSet<>();
        Set<Clause> sos = new HashSet<>();
        sos.addAll(negateClause(last));

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
                        System.out.println("[CONCLUSION]: " + last + " is true");
                        return;
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
            return new Clause(new HashSet<>(Arrays.asList(Literal.NIL)));
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

        return result;
    }
}
