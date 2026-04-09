import java.util.List;

public interface assignmentScheduler {
    void setAssignments(List<assignment> assignments);
    List<assignment> getAssignments();
    void readData(String filePath);   // Reads data from file
    List<assignment> selectAssignments();    // Selects assignments based on the EDF algorithm
}