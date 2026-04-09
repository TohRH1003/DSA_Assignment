public abstract class assignment {
    private String id;
    private String title;
    private int deadline;
    private int marks;

    // Constructor
    public assignment(String id, String title, int deadline, int marks) {
        this.id = id;
        this.title = title;
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
}
