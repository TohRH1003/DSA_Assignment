package framework;

import java.util.List;
import model.Assignment;

// Defines the common operations for storing assignments.
public interface AssignmentStore {
    void add(Assignment assignment);

    void addAll(List<Assignment> assignments);

    Assignment get(int index);

    int size();

    boolean isEmpty();

    List<Assignment> toList();

    void sortByMark(boolean ascending);
}
