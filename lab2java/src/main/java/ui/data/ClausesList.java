package ui.data;

import ui.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parses clauses from file and stores them.
 */
public class ClausesList {
    /** List of clauses */
    List<Clause> clauses = new ArrayList<>();

    /**
     * Constructor that reads the clauses from a file.
     * @param filePath file from which to read clauses.
     * @throws IllegalAccessError if there is a problem with the file path
     * @throws IOException if there is a problem while reading the file
     */
    public ClausesList(String filePath) throws IOException {
        // check provided file path
        Path file = Utils.checkFilePath(filePath);

        // read all lines except comments and create list of clauses
        List<String> lines = Files.readAllLines(file);
        for (String line : lines) {
            if (line.trim().startsWith("#")) continue;
            clauses.add(new Clause(line.trim()));
        }
    }

    /**
     * Returns list of clauses
     * @return list of clauses
     */
    public List<Clause> getClauses() {
        return clauses;
    }

    /**
     * Returns string representation of all clauses.
     * @return string representation of all clauses
     */
    @Override
    public String toString() {
        return clauses.stream().map(Clause::toString).collect(Collectors.joining(", ", "(", ")"));
    }
}
