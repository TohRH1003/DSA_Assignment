import java.util.ArrayList;
import java.util.List;
import java.util.Random;


// Generates random assignment data for testing.
public class RandomAssignmentGenerator {
    private final Random random = new Random();

    public List<assignment> generate(
        int count,
        int minDeadline,
        int maxDeadline,
        int minMarks,
        int maxMarks
    ) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be greater than 0.");
        }
        if (minDeadline > maxDeadline) {
            throw new IllegalArgumentException("Minimum deadline cannot be greater than maximum deadline.");
        }
        if (minMarks > maxMarks) {
            throw new IllegalArgumentException("Minimum marks cannot be greater than maximum marks.");
        }

        List<assignment> assignments = new ArrayList<>();
        for (int index = 1; index <= count; index++) {
            int deadline = nextIntInRange(minDeadline, maxDeadline);
            int marks = nextIntInRange(minMarks, maxMarks);
            assignments.add(new concreteAssignment("A" + index, "Assignment " + index, deadline, marks));
        }
        return assignments;
    }

    private int nextIntInRange(int minValue, int maxValue) {
        return random.nextInt(maxValue - minValue + 1) + minValue;
    }
}
