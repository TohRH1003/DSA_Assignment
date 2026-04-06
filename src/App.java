import algorithm.*;
import datastructures.*;
import io.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import model.*;

// Main entry point for the assignment project.
public class App {
    public static void main(String[] args) { // Currently is just for testing purpose
        testGreedy2();
    }

    public static void testGreedy() {
        // Create a random assignment generator
        RandomAssignmentGenerator generator = new RandomAssignmentGenerator();
        // Generate a list of random assignments
        List<Assignment> assignments = generator.generate(5, 1, 3, 10, 100);
        AssignmentStore store = new ArrayAssignmentStore();
        store.addAll(assignments);

        ScheduleResult result = GreedyProfitBased.assignmentSequence(store);

        System.out.println("total mark");
        System.out.println(result.getTotalMarks());

        System.out.println("\nSelected");
        for (int i = 0; i < result.getSelectedJobs().size(); i++) {
            System.out.println(result.getSelectedJobs().get(i).getAssignment());
        }

        System.out.println("\nUnselected");
        for (int i = 0; i < result.getUnselectedJobs().size(); i++) {
            System.out.println(result.getUnselectedJobs().get(i));
        }

    }

    public static void testGreedy2() {
        Path csvPath = Path.of("assignments.csv");
        AssignmentFileReader reader = new AssignmentFileReader();

        List<Assignment> assignments;
        try {
            assignments = reader.readFromCsv(csvPath);
        } catch (IOException e) {
            System.out.println("Failed to read CSV file: " + csvPath);
            System.out.println(e.getMessage());
            return;
        }

        if (assignments.isEmpty()) {
            System.out.println("No assignment data found in: " + csvPath);
            return;
        }

        AssignmentStore store = new ArrayAssignmentStore();
        store.addAll(assignments);

        ScheduleResult result = GreedyProfitBased.assignmentSequence(store);

        System.out.println("CSV file");
        System.out.println(csvPath.toAbsolutePath());

        System.out.println("\nLoaded assignments");
        for (Assignment assignment : assignments) {
            System.out.println(assignment);
        }

        System.out.println("\nTotal mark");
        System.out.println(result.getTotalMarks());

        System.out.println("\nSelected assignments");
        for (int i = 0; i < result.getSelectedJobs().size(); i++) {
            System.out.println(result.getSelectedJobs().get(i).getAssignment());
        }

        System.out.println("\nUnselected assignments");
        for (int i = 0; i < result.getUnselectedJobs().size(); i++) {
            System.out.println(result.getUnselectedJobs().get(i));
        }
    }
}
