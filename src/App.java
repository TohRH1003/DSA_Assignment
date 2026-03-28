import algorithm.*;
import datastructures.*;
import io.*;
import java.util.*;
import model.*;

// Main entry point for the assignment project.
public class App {
    public static void main(String[] args) { // Currently is just for testing purpose
        testGreedy();
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
}
