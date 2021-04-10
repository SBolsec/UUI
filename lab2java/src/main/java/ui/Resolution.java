package ui;

import ui.data.Clause;
import ui.data.ClausePair;
import ui.data.Literal;

import java.util.*;

/**
 * Class that stores resolution algorithms
 */
public class Resolution {

    public static void resolutionByRefutation(List<Clause> clauses, Clause last) {
        List<Clause> sos = new ArrayList<>();
        sos.addAll(negateClause(last));

        StringBuilder sb1 = new StringBuilder(); // stores the output
        int i = 1;
        for (Clause c : clauses) sb1.append(i++).append(". ").append(c).append("\n");
        for (Clause c : sos) sb1.append(i++).append(". ").append(c).append("\n");
        sb1.append("===============");
        StringBuilder sb2 = new StringBuilder();

        while (true) {
            ClausePair pair = selectClauses(clauses, sos);
            if (pair == null) {
                System.out.println(sb1.toString());
                System.out.println("[CONCLUSION]: " + last + " is unknown");
                return;
            }
            List<Literal> resolvents = plResolve(pair.getC1(), pair.getC2());
            Clause newClause = new Clause(resolvents);

            sb2.append(i++).append(". ").append(newClause).append(" (")
                .append(clauses.indexOf(pair.getC1())+1)
                .append(", ")
                .append(sos.indexOf(pair.getC2())+1+clauses.size())
                .append(")\n");

            if (resolvents.contains(Literal.NIL)) {
                sb2.append("===============");
                System.out.println(sb1.toString());
                System.out.println(sb2.toString());
                System.out.println("[CONCLUSION]: " + last + " is true");
                return;
            }

            sos.add(new Clause(resolvents));
        }
    }

    /**
     * Returns negated clauses created from given clause.
     * @param clause clause which to negate
     * @return negated clauses
     */
    public static List<Clause> negateClause(Clause clause) {
        List<Clause> result = new ArrayList<>();
        for (Literal l : clause.getLiterals()) {
            result.add(new Clause(Arrays.asList(new Literal(l.isNegated() ? l.getName() : "~" + l.getName()))));
        }
        return result;
    }

    /**
     * Select pairs of clauses which to resolve from which one is always from sos.
     * First clause in pair is from initial clauses, second is from sos.
     * @param initialClauses initial clauses
     * @param sos clauses in sos
     * @return set of clauses to resolve
     */
    public static ClausePair selectClauses(List<Clause> initialClauses, List<Clause> sos) {
        // try to find something to resolve in the initialClauses
        for (Clause s : sos) {
            if (s.isResolved()) continue;
            for (Literal l1 : s.getLiterals()) {
                for (Clause c : initialClauses) {
                    if (c.isResolved()) continue;
                    if (!c.getLiterals().contains(l1)) continue;
                    Literal l2 = c.getLiterals().get(c.getLiterals().indexOf(l1));
                    if (l1.isNegated() && l2.isNegated()) continue; // skip (a, a), we want (a, ~a)
                    return new ClausePair(c, s);
                }
            }
        }
        // try to find something to resolve in sos
        for (Clause s : sos) {
            if (s.isResolved()) continue;
            for (Literal l1 : s.getLiterals()) {
                for (Clause c : sos) {
                    if (s.equals(c)) continue;
                    if (c.isResolved()) continue;
                    if (!c.getLiterals().contains(l1)) continue;
                    Literal l2 = c.getLiterals().get(c.getLiterals().indexOf(l1));
                    if (l1.isNegated() && l2.isNegated()) continue; // skip (a, a), we want (a, ~a)
                    return new ClausePair(c, s);
                }
            }
        }
        return null;
    }

    /**
     * Resolves the given clauses and returns the resolvent
     * @param c1 first parent clause
     * @param c2 second prent clause
     * @return resolvent of the given clauses
     */
    public static List<Literal> plResolve(Clause c1, Clause c2) {
        List<Literal> result = new ArrayList<>();

        // find which literal to remove
        Literal toRemove = null;
        for (Literal l1 : c1.getLiterals()) {
            if (c2.getLiterals().contains(l1)) {
                toRemove = l1;
                break;
            }
        }

        // add all literals to result
        result.addAll(c1.getLiterals());
        result.addAll(c2.getLiterals());
        // remove literal which is being resolved
        result.remove(toRemove); // remove literal from first clause
        result.remove(toRemove); // remove literal from second clause
        // set flag that clauses are resolved
        c1.setResolved(true);
        c2.setResolved(true);

        // if the result is empty, add the NIL literal
        if (result.isEmpty())
            result.add(Literal.NIL);

        return result;
    }
}
