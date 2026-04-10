package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Stores the output of a scheduling algorithm.
public class ScheduleResult {
    // Assignments that were chosen, together with their scheduled day.
    private final List<ScheduledAssignment> selectedJobs;
    // Assignments that could not be included in the final schedule.
    private final List<Assignment> unselectedJobs;
    // Total marks/profit collected from all selected assignments.
    private final int totalMarks;

    public ScheduleResult(
        List<ScheduledAssignment> selectedJobs,
        List<Assignment> unselectedJobs,
        int totalMarks
    ) {
        this.selectedJobs = Collections.unmodifiableList(new ArrayList<>(selectedJobs));
        this.unselectedJobs = Collections.unmodifiableList(new ArrayList<>(unselectedJobs));
        this.totalMarks = totalMarks;
    }

    // Returns the assignments that were successfully scheduled.
    public List<ScheduledAssignment> getSelectedJobs() {
        return selectedJobs;
    }

    // Returns the assignments that were left out.
    public List<Assignment> getUnselectedJobs() {
        return unselectedJobs;
    }

    // Returns the total marks/profit of the selected assignments.
    public int getTotalMarks() {
        return totalMarks;
    }
}
