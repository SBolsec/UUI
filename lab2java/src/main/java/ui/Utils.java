package ui;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class that stores helper methods.
 */
public class Utils {

    /**
     * Checks whether the provided file path is valid.
     * Returns the path if its valid, otherwise throws exception.
     * @param filePath file path to check
     * @return path to file
     * @throws IllegalArgumentException if there is a problem with the file path
     */
    public static Path checkFilePath(String filePath) {
        Path path = Path.of(filePath);

        if (!Files.exists(path))
            throw new IllegalArgumentException("File does not exist! It was: " + filePath);
        if (!Files.isReadable(path))
            throw new IllegalArgumentException("File is not readable! It was: " + filePath);
        if (!Files.isRegularFile(path))
            throw new IllegalArgumentException("File is not regular file! It was: " + filePath);

        return path;
    }
}
