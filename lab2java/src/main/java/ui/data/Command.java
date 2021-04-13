package ui.data;

import java.util.Objects;

/**
 * Models a command in the cookbook
 */
public class Command {
    /** Clause of the command */
    private final Clause clause;
    /** What the command should do */
    private final Action type;
    /** String representation of command */
    private final String commandString;

    /**
     * Constructor.
     * @param command command in string format
     */
    public Command(String command) {
        commandString = command.toLowerCase();
        clause = new Clause(command.trim().substring(0, command.length()-2));
        type = switch (command.charAt(command.length()-1)) {
            case '?' -> Action.VALIDATE_CLAUSE;
            case '+' -> Action.ADD_CLAUSE;
            case '-' -> Action.REMOVE_CLAUSE;
            default -> null;
        };
        if (type == null)
            throw new IllegalArgumentException("Action not supported! Action was: " + command.charAt(command.length()-1));
    }

    /**
     * Returns the clause.
     * @return clause
     */
    public Clause getClause() {
        return clause;
    }

    /**
     * Returns the action type.
     * @return action type
     */
    public Action getType() {
        return type;
    }

    /**
     * Returns the command string.
     * @return command string
     */
    public String getCommandString() {
        return commandString;
    }

    /**
     * Checks whether two commands are equal.
     * @param o object to test
     * @return true if commands are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(clause, command.clause) && type == command.type;
    }

    /**
     * Returns hash code.
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(clause, type);
    }
}
