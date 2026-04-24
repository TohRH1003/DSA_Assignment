package algorithm;

import framework.AssignmentStore;
import java.util.*;
import model.*;

/**
 * Earliest Deadline First scheduler.
 * Selects assignments by sorting first by deadline (ascending),
 * then by marks (descending), and uses an insertion-based feasibility check.
 */
public class EDFSchedule {

    public static ScheduleResult assignmentSequence(AssignmentStore assignments) {
        // Handle empty or null store
        if (assignments == null || assignments.size() == 0) {
            return new ScheduleResult(new ArrayList<>(), new ArrayList<>(), 0);
        }

        // 1. Extract all assignments into a mutable list
        List<Assignment> allAssignments = new ArrayList<>(assignments.toList());

        // 2. Sort by deadline ascending, then marks descending (original EDF order)
        allAssignments.sort(Comparator
                .comparingInt(Assignment::getDeadline)
                .thenComparingInt(a -> -a.getMarks()));

        int n = allAssignments.size();
        // 3. Run the original selection algorithm (using D and J arrays)
        int[] D = new int[n + 1];
        int[] J = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            D[i] = allAssignments.get(i - 1).getDeadline();
        }
        D[0] = 0;
        J[0] = 0;

        int k = 1;
        J[1] = 1;

        for (int i = 2; i <= n; i++) {
            int r = k;
            // Find the insertion point (original logic)
            while (r > 0 && D[J[r]] > D[i] && D[J[r]] != r) {
                r--;
            }
            if (D[J[r]] <= D[i] && D[i] > r) {
                // Shift jobs to the right to make room
                for (int l = k; l >= r + 1; l--) {
                    J[l + 1] = J[l];
                }
                J[r + 1] = i;
                k++;
            }
        }

        // 4. Collect selected assignments (based on indices in J[1..k])
        List<Assignment> selectedAssignments = new ArrayList<>();
        for (int i = 1; i <= k; i++) {
            selectedAssignments.add(allAssignments.get(J[i] - 1));
        }

       // 5. Schedule selected assignments with days using the loop approach
        List<ScheduledAssignment> scheduledList = new ArrayList<>();
        for (int i = 0; i < selectedAssignments.size(); i++) {
            scheduledList.add(new ScheduledAssignment(selectedAssignments.get(i), i + 1));
        }

        // 6. Identify unselected assignments
        List<Assignment> unselectedList = new ArrayList<>(allAssignments);
        unselectedList.removeAll(selectedAssignments);

        // 7. Compute total marks
        int totalMarks = scheduledList.stream()
                .mapToInt(sa -> sa.getAssignment().getMarks())
                .sum();
                
        return new ScheduleResult(scheduledList, unselectedList, totalMarks);
    }
}