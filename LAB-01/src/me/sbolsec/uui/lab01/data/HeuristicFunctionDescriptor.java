package me.sbolsec.uui.lab01.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class HeuristicFunctionDescriptor {
    private Map<String, Double> heuristics = new HashMap<>();

    public HeuristicFunctionDescriptor(String filePath) throws IOException {
        // check provided file path
        Path path = Path.of(filePath);
        if (!Files.exists(path))
            throw new IllegalArgumentException("Provided file path does not exist!");
        if (!Files.isRegularFile(path))
            throw new IllegalArgumentException("Provided file path is not a regular file!");
        if (!Files.isReadable(path))
            throw new IllegalArgumentException("Do not have read permission for provided file!");

        // load the file while removing comments
        List<String> lines = Files.readAllLines(path)
                .stream()
                .filter(line -> !line.startsWith("#"))
                .map(String::trim)
                .collect(Collectors.toList());

        for (int i = 0, n = lines.size(); i < n; i++) {
            String[] s = lines.get(i).split(" ");
            heuristics.put(s[0].substring(0, s[0].length() - 1), Double.parseDouble(s[1]));
        }
    }

    public Map<String, Double> getHeuristics() {
        return Collections.unmodifiableMap(heuristics);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeuristicFunctionDescriptor that = (HeuristicFunctionDescriptor) o;
        return Objects.equals(heuristics, that.heuristics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heuristics);
    }
}
