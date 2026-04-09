import java.io.*;
import java.util.*;

public class EDFSchedule implements assignmentScheduler {
    private List<assignment> assignments = new ArrayList<>();

    public void setAssignments(List<assignment> assignments) {
        this.assignments = assignments;
    }

    public List<assignment> getAssignments() {
        return assignments;
    }

    public void readData(String filePath) {
        try (Scanner sc = new Scanner(new File(filePath))) {
            sc.nextLine();
            while (sc.hasNextLine()) {
                String[] details = sc.nextLine().split(",");
                String id = details[0];
                String title = details[1];
                int deadline = Integer.parseInt(details[2]);
                int marks = Integer.parseInt(details[3]);
                assignments.add(new concreteAssignment(id, title, deadline, marks));
            }
        } 
        catch (Exception e) {
            System.out.println("File error: " + e.getMessage());
        }
    }

    public List<assignment> selectAssignments() {
        // Sort assignments first by deadline in ascending order, 
        // then by marks (profit) in descending order (secondary sorting criteria)
        assignments.sort(Comparator
                .comparingInt(assignment::getDeadline)  // First sort by deadline (ascending)
                .thenComparingInt((assignment a) -> -a.getMarks())  // Then sort by marks (descending)
        );

        int n = assignments.size();
        List<assignment> result = new ArrayList<>();

        if (n == 0) {
            return result;
        }

        int[] D = new int[n + 1];
        int[] J = new int[n + 1];

        // Storing deadlines for easy comparison
        for (int i = 1; i <= n; i++) {
            D[i] = assignments.get(i - 1).getDeadline();
        }

        D[0] = 0;
        J[0] = 0;

        int k = 1;
        J[1] = 1;

        // Select jobs based on the earliest deadline and profit
        for (int i = 2; i <= n; i++) {
            int r = k;

            // Find the latest job whose deadline is earlier or equal
            while (r > 0 && D[J[r]] > D[i] && D[J[r]] != r) {
                r--;
            }

            // Find the higher mark, insert it into the result list
            if (D[J[r]] <= D[i] && D[i] > r) {
                for (int l = k; l >= r + 1; l--) {
                    J[l + 1] = J[l];
                }
                J[r + 1] = i;
                k++;
            }
        }

        // Add selected assignments to the result list
        for (int i = 1; i <= k; i++) {
            result.add(assignments.get(J[i] - 1));
        }

        // Identify unselected assignments
        List<assignment> unselectedAssignments = new ArrayList<>(assignments);
        unselectedAssignments.removeAll(result);

        // Display selected assignments
        System.out.println("Selected Assignments based on Earliest Deadline First:");
        int totalMarks = 0;
        for (assignment assignment : result) {
            System.out.println("Id: " + assignment.getId() + " | Title: " + assignment.getTitle() + " | Deadline: " + assignment.getDeadline() + " | Marks: " + assignment.getMarks());
            totalMarks += assignment.getMarks();
        }

        System.out.println("\nTotal Marks: " + totalMarks);

        // Display unselected assignments
        System.out.println("\nUnselected Assignments:");
        for (assignment assignment : unselectedAssignments) {
            System.out.println("Id: " + assignment.getId() + " | Title: " + assignment.getTitle() + " | Deadline: " + assignment.getDeadline() + " | Marks: " + assignment.getMarks());
        }

        return result;
    }


    
}

