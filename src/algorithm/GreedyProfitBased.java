package algorithm;

import datastructures.*;
import java.util.ArrayList;
import java.util.List;
import model.*;

public class GreedyProfitBased {
    public static ScheduleResult assignmentSequence(AssignmentStore assignments) {
        // Check if store is empty
        if (assignments == null || assignments.size() == 0) {
            return new ScheduleResult(new ArrayList<>(), new ArrayList<>(), 0);
        }

        for (int i = 0; i < assignments.size(); i++) {
            System.out.println(assignments.get(i));
        }

        // Sort the assignment in descending order based on mark
        assignments.sort(false);

        // Call function to get the maximum deadline of the assignments set
        int max_deadline = maxDeadline(assignments);

        // Create a slot array to keep the allotted slot for assignment
        ScheduledAssignment[] scheduled = new ScheduledAssignment[max_deadline];

        // t represents the time slot/day where we try to schedule a job, starting from
        // the job's deadline and moving backwards to find an empty slot.
        // maxMark is totalMark
        int t;
        int maxMark = 0;

        // Track selected jobs to build result later
        List<ScheduledAssignment> selectedList = new ArrayList<>();

        for (int i = 0; i < assignments.size(); i++) {
            Assignment currentJob = assignments.get(i);
            t = currentJob.getDeadline() - 1;

            while (t >= 0 && scheduled[t] != null) {
                t--;
            }

            if (t >= 0 && scheduled[t] == null) {
                scheduled[t] = new ScheduledAssignment(currentJob, t + 1);
                maxMark += currentJob.getMarks();
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