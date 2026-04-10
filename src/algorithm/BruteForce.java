package algorithm;

import datastructures.*;
import java.util.*;
import model.*;

public class BruteForce {
    public static ScheduleResult assignmentSequence(AssignmentStore assignments) {

        // Start execution time (measuring only brute force algorithm)
        long startTime = System.nanoTime();

        // Step 1: Handle empty input
        if (assignments == null || assignments.size() == 0) {
            return new ScheduleResult(new ArrayList<>(), new ArrayList<>(), 0);
        }
        
        // Convert AssignmentStore into list for easier processing
        List<Assignment> allAssignments = assignments.toList();
        int n = allAssignments.size();

        // Store the best result found
        int maxMarks = 0;
        List<Assignment> bestSubset = new ArrayList<>();

        // Step 2: Generate ALL possible subsets using bitmask
        // Total subsets = 2^n
        for (int mask = 0; mask < (1 << n); mask++) {
            List<Assignment> subset = new ArrayList<>();

            // Build subset based on binary representation
            for (int j = 0; j < n; j++) {
                if ((mask & (1 << j)) != 0) {
                    subset.add(allAssignments.get(j));
                }
            }

            // Step 3: Check if subset satisfies deadline constraints
            if (isValid(subset)) {
                // Step 4: Calculate total marks of this subset
                int total = calculateMarks(subset);
                
                // Step 5: Update best solution
                if (total > maxMarks) {
                    maxMarks = total;
                    bestSubset = new ArrayList<>(subset);
                }
            }    
        }
        
        // Step 6: Convert best subset into scheduled result
        List<ScheduledAssignment> selectedList = buildSchedule(bestSubset);
        
        // Step 7: Find unselected assignments
        List<Assignment> unselectedList = new ArrayList<>();
        for (Assignment a : allAssignments) {
            if (!bestSubset.contains(a)) {
                unselectedList.add(a);
            }
        }
        
        // End execution time
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        // Print internal timing
        System.out.println("[Brute Force Internal Time] " + duration + "ns");

        // Step 8: Return final result
        return new ScheduleResult(selectedList, unselectedList, maxMarks);
    }

    // Check deadline validity
    private static boolean isValid(List<Assignment> subset) {

        // Sort by deadline
        subset.sort(Comparator.comparingInt(Assignment::getDeadline));
        
        int time = 0;
        for (Assignment a : subset) {
            time++;

            // If current time exceeds deadline = invalid
            if (time > a.getDeadline()) {
                return false;
            }
        }
        return true;
    }

    // Calculate total marks
    private static int calculateMarks(List<Assignment> subset) {
        int total = 0;
        for (Assignment a : subset) {
            total += a.getMarks();
        }
        return total;
    }
    
    // Build schedule output 
    private static List<ScheduledAssignment> buildSchedule(List<Assignment> subset) {
        List<ScheduledAssignment> scheduled = new ArrayList<>();

        // Sort again by deadline to assign days properly
        subset.sort(Comparator.comparingInt(Assignment::getDeadline));
        
        int day = 1;
        for (Assignment a : subset) {
            scheduled.add(new ScheduledAssignment(a, day));
            day++;
        }
        return scheduled;
    }
}

// Reference:
// https://github.com/halokaiwei/JobSquencing/blob/main/BruteForceAlgorithms.java

// Dependency:
// Language Support for Java(TM) by Red Hat