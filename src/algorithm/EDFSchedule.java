package algorithm;

import datastructures.AssignmentStore;
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

        // 5. Assign scheduled days to the selected assignments
        List<ScheduledAssignment> scheduledList = assignScheduledDays(selectedAssignments);

        // 6. Build the list of unselected assignments
        List<Assignment> unselectedList = new ArrayList<>();
        for (Assignment a : allAssignments) {
            boolean isSelected = false;
            for (ScheduledAssignment sa : scheduledList) {
                if (sa.getAssignment().getId().equals(a.getId())) {
                    isSelected = true;
                    break;
                }
            }
            if (!isSelected) {
                unselectedList.add(a);
            }
        }

        // 7. Compute total marks
        int totalMarks = scheduledList.stream()
                .mapToInt(sa -> sa.getAssignment().getMarks())
                .sum();

        // 8. Return the structured result (selected sorted by scheduled day)
        scheduledList.sort(Comparator.comparingInt(ScheduledAssignment::getScheduledDay));
        return new ScheduleResult(scheduledList, unselectedList, totalMarks);
    }

    /**
     * Assigns a feasible day (>0) to each selected assignment using the
     * "latest free slot not exceeding the deadline" strategy.
     *
     * @param selected the list of selected assignments (order does not matter)
     * @return a list of ScheduledAssignment objects with concrete days
     */
    private static List<ScheduledAssignment> assignScheduledDays(List<Assignment> selected) {
        if (selected.isEmpty()) {
            return Collections.emptyList();
        }

        // Find the maximum deadline among all assignments (or selected)
        int maxDeadline = selected.stream()
                .mapToInt(Assignment::getDeadline)
                .max()
                .orElse(0);

        boolean[] used = new boolean[maxDeadline + 1]; // indices 1..maxDeadline
        List<ScheduledAssignment> result = new ArrayList<>();

        // Process in order of increasing deadline for deterministic assignment
        selected.sort(Comparator.comparingInt(Assignment::getDeadline));

        for (Assignment a : selected) {
            int deadline = a.getDeadline();
            int slot = -1;
            // Find the largest free slot ≤ deadline
            for (int day = deadline; day >= 1; day--) {
                if (!used[day]) {
                    slot = day;
                    break;
                }
            }
            // Fallback (should never happen if the selection algorithm is correct)
            if (slot == -1) {
                for (int day = 1; day <= maxDeadline; day++) {
                    if (!used[day]) {
                        slot = day;
                        break;
                    }
                }
            }
            used[slot] = true;
            result.add(new ScheduledAssignment(a, slot));
        }
        return result;
    }
}