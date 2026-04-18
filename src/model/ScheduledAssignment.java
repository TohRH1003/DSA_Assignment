package model;

// Stores a selected assignment together with its assigned day in the final schedule.
public class ScheduledAssignment {
    // Reference to the original assignment/job information.
    private final Assignment assignment;
    // The actual day slot chosen for this assignment after scheduling.
    // Example: if a job has deadline 3 but is placed on day 2, then scheduledDay is 2.
    private final int scheduledDay;

    public ScheduledAssignment(Assignment assignment, int scheduledDay) {
        if (assignment == null) {
            throw new IllegalArgumentException("Assignment cannot be null.");
        }
        if (scheduledDay <= 0) {
            throw new IllegalArgumentException("Scheduled day must be greater than 0.");
        }

        this.assignment = assignment;
        this.scheduledDay = scheduledDay;
    }

    // Returns the selected assignment data.
    public Assignment getAssignment() {
        return assignment;
    }

    // Returns the day/slot where the assignment is placed in the final schedule.
    public int getScheduledDay() {
        return scheduledDay;
    }
}
