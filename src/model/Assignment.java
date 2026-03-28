package model;

// Stores the basic information for one assignment/job.
public class Assignment {
    private final String id;
    private final String title;
    private final int deadline;
    private final int marks;

    public Assignment(String id, String title, int deadline, int marks) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Assignment id cannot be blank.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Assignment title cannot be blank.");
        }
        if (deadline <= 0) {
            throw new IllegalArgumentException("Deadline must be greater than 0.");
        }
        if (marks < 0) {
            throw new IllegalArgumentException("Marks cannot be negative.");
        }

        this.id = id.trim();
        this.title = title.trim();
        this.deadline = deadline;
        this.marks = marks;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getDeadline() {
        return deadline;
    }

    public int getMarks() {
        return marks;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, deadline=%d, marks=%d)", id, title, deadline, marks);
    }
}
