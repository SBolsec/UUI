package ui;

import ui.data.Clause;
import ui.data.ClausesList;
import ui.data.Command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Cookbook that supports commands.
 */
public class CookBook {
    /** Initial clauses */
    private final List<Clause> clauses;
    /** Commands which to perform */
    private final List<Command> commands;
    /** Resolution which will be used */
    private final Resolution resolution;

    /**
     * Constructor.
     * @param clausesFile path to file which contains initial clauses
     * @param commandsFile path to file which contains commands
     * @throws IOException if there is an error with the file path
     * @throws IllegalArgumentException if action on a command was invalid
     */
    public CookBook(String clausesFile, String commandsFile) throws IOException {
        // load the initial clauses
        ClausesList clausesList = new ClausesList(clausesFile);
        clauses = clausesList.getClauses();

        // load the commands
        Path commandsPath = Utils.checkFilePath(commandsFile);
        List<String> lines = Files.readAllLines(commandsPath);
        commands = new ArrayList<>();
        for (String s : lines) {
            commands.add(new Command(s));
        }

        // create resolution which will be used
        resolution = new Resolution();
    }

    /**
     * Starts the procedure of processing the input commands.
     */
    public void run() {
        // Print initial clauses read from file
        System.out.println("Constructed with knowledge:");
        clauses.forEach(System.out::println);

        // Process the commands
        for (Command command : commands) {
            System.out.println("\nUser's command: " + command.getCommandString());

            switch (command.getType()) {
                case ADD_CLAUSE -> {
                    clauses.add(command.getClause());
                    System.out.println("Added " + command.getClause());
                }
                case REMOVE_CLAUSE -> {
                    clauses.remove(command.getClause());
                    System.out.println("Removed " + command.getClause());
                }
                case VALIDATE_CLAUSE -> resolution.resolutionByRefutation(clauses, command.getClause());
            }
        }
    }
}
