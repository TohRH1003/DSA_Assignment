import algorithm.*;
import datastructures.*;
import io.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import model.*;

public class App {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=== JOB SEQUENCING (ASSIGNMENT SCHEDULING) ===\n");

        // Choose data source
        List<Assignment> assignments = loadAssignments();
        if (assignments == null || assignments.isEmpty()) {
            System.out.println("No assignments loaded. Exiting.");
            return;
        }

        // Store assignments
        AssignmentStore store = new ArrayAssignmentStore();
        store.addAll(assignments);

        // Print the assignment
        assignments.forEach(System.out::println);

        // Step 3: Choose algorithm
        int algoChoice = chooseAlgorithm();

        // Step 4: Solve
        ScheduleResult result = null;

        // Measure execution time (used for comparing algorithm performance)
        long startTime = System.nanoTime(); // APPENDED

        if (algoChoice == 1) {
            result = GreedyProfitBased.assignmentSequence(store);
        } else if (algoChoice == 2) {
            result = BackTracking.findOptimalSchedule(store);
        } else if (algoChoice == 3) {
            result = BruteForce.assignmentSequence(store); // APPENDED
        } else if (algoChoice == 4) {
            result = EDFSchedule.assignmentSequence(store);
        } else {
            System.out.println("Invalid algorithm choice.");
            return;
        }

        // End timing after algorithm execution
        long endTime = System.nanoTime(); // APPEDNDED

        // Total execution time in nanoseconds
        long duration = endTime - startTime;

        // Step 5: Display result
        displayResult(result);

        // Show performance comparison
        System.out.println("\nExecution Time: " + duration + "ns (" + (duration / 1_000_000.0) + "ms)"); // APPENDED
    }

    private static List<Assignment> loadAssignments() {
        System.out.println("Please choose the data source:");
        System.out.println("  1. Read from CSV file");
        System.out.println("  2. Generate random assignments");
        System.out.print("Your choice: ");
        int choice = readInt();
        List<Assignment> assignments = null;

        if (choice == 1) {
            String path = "assignments.csv";
            AssignmentFileReader reader = new AssignmentFileReader();

            try {
                return reader.readFromCsv(Path.of(path));

            } catch (IOException e) {
                System.out.println("Error reading file: " + e.getMessage());
                return null;
            }

        } else if (choice == 2) {

            boolean validInput = false;

            do {

                try {
                    System.out.print("Number of assignments: ");
                    int count = readInt();

                    System.out.print("Min deadline: ");
                    int minDead = readInt();

                    System.out.print("Max deadline: ");
                    int maxDead = readInt();

                    System.out.print("Min marks: ");
                    int minMark = readInt();

                    System.out.print("Max marks: ");
                    int maxMark = readInt();

                    RandomAssignmentGenerator gen = new RandomAssignmentGenerator();
                    assignments = gen.generate(count, minDead, maxDead, minMark, maxMark);

                    validInput = true;
                }

                catch (Exception e) {
                    System.out.println(e.getMessage());

                }

            } while (!validInput);

        } else {

            System.out.println("Invalid choice.");
            return null;
        }

        return assignments;
    }

    private static int chooseAlgorithm() {
        System.out.println("\nAvailable algorithms:");
        System.out.println("  1. Greedy (Profit-based)");
        System.out.println("  2. Backtracking (Exact)");
        System.out.println("  3. Brute Force (Profit-based)");
        System.out.println("  4. Earliest Deadline First (Exact)");
        System.out.print("Select algorithm: ");
        return readInt();
    }

    private static void displayResult(ScheduleResult result) {
        System.out.println("\n=== SCHEDULING RESULT ===\n");

        List<ScheduledAssignment> selected = result.getSelectedJobs();
        if (selected.isEmpty()) {
            System.out.println("No jobs selected.");
        } else {
            System.out.println("Selected assignments (execution order):");
            System.out.println("Day\tID\tDeadline\tMarks\tTitle");
            for (ScheduledAssignment sa : selected) {
                Assignment a = sa.getAssignment();
                System.out.printf("%d\t%s\t%d\t\t%d\t%s%n",
                        sa.getScheduledDay(),
                        a.getId(),
                        a.getDeadline(),
                        a.getMarks(),
                        a.getTitle());
            }
            System.out.println("\nTotal marks: " + result.getTotalMarks());
        }

        System.out.println("\nUnselected assignments:");
        List<Assignment> unselected = result.getUnselectedJobs();
        if (unselected.isEmpty()) {
            System.out.println("  None");
        } else {
            for (Assignment a : unselected) {
                System.out.printf("  %s (%s, deadline=%d, marks=%d)%n",
                        a.getId(), a.getTitle(), a.getDeadline(), a.getMarks());
            }
        }
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Enter again: ");
            }
        }
    }
}