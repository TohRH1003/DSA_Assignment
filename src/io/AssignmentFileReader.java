package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import model.Assignment;

// Reads assignment data from a CSV file.
public class AssignmentFileReader {
    public List<Assignment> readFromCsv(Path filePath) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        List<Assignment> assignments = new ArrayList<>();

        if (lines.isEmpty()) {
            return assignments;
        }

        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index).trim();
            if (line.isEmpty()) {
                continue;
            }

            if (index == 0 && line.toLowerCase().startsWith("id,")) {
                continue;
            }

            String[] parts = line.split(",");
            if (parts.length < 4) {
                throw new IOException("Invalid CSV row: " + line);
            }

            assignments.add(
                new Assignment(
                    parts[0].trim(),
                    parts[1].trim(),
                    Integer.parseInt(parts[2].trim()),
                    Integer.parseInt(parts[3].trim())
                )
            );
        }

        return assignments;
    }
}
