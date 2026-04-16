package algorithm;

import datastructures.*;
import java.util.ArrayList;
import java.util.List;
import model.*;

public class GreedyProfitBased {
    public static ScheduleResult assignmentSequence(AssignmentStore assignments) {
        // Check if assignment is empty
        if (assignments == null || assignments.size() == 0) {
            return new ScheduleResult(new ArrayList<>(), new ArrayList<>(), 0);
        }

        // Sort the assignment in descending order based on mark
        assignments.sortByMark(false);

        // Call function to get the maximum deadline of the assignments list
        int max_deadline = maxDeadline(assignments);

        // Create a slot array to keep the allotted slot for assignment
        ScheduledAssignment[] scheduled = new ScheduledAssignment[max_deadline];

        // t represents the time slot/day where we try to schedule a job, starting from
        // the assignment deadline and moving backwards to find an empty slot.
        // maxMark is totalMark
        int t;
        int maxMark = 0;

        // Track selected assignment to build result later
        List<ScheduledAssignment> selectedList = new ArrayList<>();

        // Loop through each assignment that has been sorted
        for (int i = 0; i < assignments.size(); i++) {
            Assignment currentAssignment = assignments.get(i);

            // Start from the last available time slot before the assignment's deadline
            // Convert deadline from 1 indexed to 0 indexed in array position
            t = currentAssignment.getDeadline() - 1; 

            // Find the latest empty time slot on or before the deadline
            // Move backwards through time slots until we find an empty slot
            while (t >= 0 && scheduled[t] != null) {
                t--; //If this slot is taken, try earlier slot
            }

            // If we found an available slot (t >= 0) that is empty
            if (t >= 0 && scheduled[t] == null) {
                // Schedule the assignment at this slot, convert back t to the actual day instead of index position
                scheduled[t] = new ScheduledAssignment(currentAssignment, t + 1);
                maxMark += currentAssignment.getMarks(); //Add the selected assignment mark to the maxMark
                selectedList.add(scheduled[t]); // Add to selected list
            }
        }

        // Get all assignments and find unselected ones
        List<Assignment> allAssignments = assignments.toList();
        List<Assignment> unselectedList = new ArrayList<>();

        // Build unselected list (jobs not in scheduled array)
        for (Assignment assignment : allAssignments) {
            boolean isSelected = false;
            for (ScheduledAssignment sa : selectedList) {
                if (sa.getAssignment().getId().equals(assignment.getId())) {
                    isSelected = true;
                    break;
                }
            }
            if (!isSelected) {
                unselectedList.add(assignment);
            }
        }

        // Sort selected by scheduled day
        selectedList.sort((a, b) -> a.getScheduledDay() - b.getScheduledDay());

        return new ScheduleResult(selectedList, unselectedList, maxMark);
    }

    // Method to find the maximum deadline
    private static int maxDeadline(AssignmentStore assignments) {
        // Add empty check
        if (assignments.size() == 0) {
            return 0;
        }

        int max_deadline = assignments.get(0).getDeadline();

        for (int i = 1; i < assignments.size(); i++) {
            if (max_deadline < assignments.get(i).getDeadline()) {
                max_deadline = assignments.get(i).getDeadline();
            }
        }

        return max_deadline;
    }
}

// Reference :
// https://github.com/Kumar-laxmi/Algorithms/blob/main/C%2B%2B/Greedy-Algorithm/JobSequencing.cpp