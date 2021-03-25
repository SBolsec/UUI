package ui;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Helper functions.
 */
public class Utils {

    /**
     * Checks whether the provided file path exists, is a regular file and checks
     * for read permission. If everything is satisfied, it returns the path, else
     * it throws an exception.
     * @param filePath file path to be checked
     * @return path to specified file
     * @throws IllegalArgumentException if there is an error with the provided path
     */
    public static Path checkProvidedPath(String filePath) {
        // check provided file path
        Path path = Path.of(filePath);
        if (!Files.exists(path))
            throw new IllegalArgumentException("Provided file path does not exist!");
        if (!Files.isRegularFile(path))
            throw new IllegalArgumentException("Provided file path is not a regular file!");
        if (!Files.isReadable(path))
            throw new IllegalArgumentException("Do not have read permission for provided file!");

        return path;
    }
}
