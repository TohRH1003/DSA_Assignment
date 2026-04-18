package datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Assignment;

// Provides shared behavior for assignment storage classes.
public abstract class AbstractAssignmentStore implements AssignmentStore {
    protected final List<Assignment> assignments;

    protected AbstractAssignmentStore() {
        this.assignments = new ArrayList<>();
    }

    @Override
    public void addAll(List<Assignment> assignments) {
        if (assignments == null) {
            throw new IllegalArgumentException("Assignments list cannot be null.");
        }

        for (Assignment assignment : assignments) {
            add(assignment);
        }
    }

    @Override
    public Assignment get(int index) {
        return assignments.get(index);
    }

    @Override
    public int size() {
        return assignments.size();
    }

    @Override
    public boolean isEmpty() {
        return assignments.isEmpty();
    }

    @Override
    public List<Assignment> toList() {
        return Collections.unmodifiableList(new ArrayList<>(assignments));
    }

    @Override
    public void sortByMark(boolean ascending) {
        if (ascending) {
            assignments.sort((Assignment a,Assignment b) -> a.getMarks() - b.getMarks());
        }

        else {
            assignments.sort((Assignment a,Assignment b) -> b.getMarks() - a.getMarks());
        }

    }
}
