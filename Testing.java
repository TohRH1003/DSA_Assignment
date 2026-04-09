import java.util.List;
import java.util.Scanner;

public class Testing {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize necessary classes
        RandomAssignmentGenerator generator = new RandomAssignmentGenerator();
        assignmentScheduler scheduler = new EDFSchedule(); // Using the EDF scheduler
        
        List<assignment> assignments = null;

        // Prompt the user to choose CSV file or generate random assignments
        System.out.println("===== Assignment Scheduler =====");
        System.out.println("1. Read assignments from CSV file");
        System.out.println("2. Generate random assignments");
        System.out.print("Choose an option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume the newline character
        
        if (choice == 1) {
            scheduler.readData("assignments.csv");
        } else if (choice == 2) {
            // Generate random assignments
            System.out.print("Enter number of assignments: ");
            int count = scanner.nextInt();

            System.out.print("Enter minimum deadline: ");
            int minDeadline = scanner.nextInt();

            System.out.print("Enter maximum deadline: ");
            int maxDeadline = scanner.nextInt();

            System.out.print("Enter minimum marks: ");
            int minMarks = scanner.nextInt();

            System.out.print("Enter maximum marks: ");
            int maxMarks = scanner.nextInt();

            assignments = generator.generate(count, minDeadline, maxDeadline, minMarks, maxMarks);

            // Display all assignments
        System.out.println("\nAll Assignments:");
        for (assignment assignment : assignments) {
            System.out.println(assignment.getId() + " | " + assignment.getTitle() + " | Deadline: "
                    + assignment.getDeadline() + " | Marks: " + assignment.getMarks() + "\n");
        }

        // Pass assignments to scheduler and select assignments
        scheduler.setAssignments(assignments);
        } else {
            System.out.println("Invalid choice.");
            scanner.close();
            return;
        }

        // Run scheduling for both options
        List<assignment> selected = scheduler.selectAssignments();
        if (selected.isEmpty()) {
            System.out.println("No assignments selected.");
        } 
        scanner.close();  
    }
}