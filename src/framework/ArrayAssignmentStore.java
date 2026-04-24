package framework;

import model.Assignment;

// Concrete assignment store based on ArrayList.
public class ArrayAssignmentStore extends AbstractAssignmentStore {
    @Override
    public void add(Assignment assignment) {
        if (assignment == null) {
            throw new IllegalArgumentException("Assignment cannot be null.");
        }

        assignments.add(assignment);
    }
}
