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
        Path file = Utils.checkFilePath(filePath);

        List<String> lines = Files.readAllLines(file);
        for (int i = 0, n = lines.size(); i < n; i++) {
            if (lines.get(i).trim().startsWith("#")) continue;
            clauses.add(new Clause(lines.get(i).trim()));
        }
    }

    /**
     * Returns list of clauses
     * @return list of clauses
     */
    public List<Clause> getClauses() {
        return clauses;
    }
}
