
package algorithm;

public class BackTracking {
  
import datastructures.AssignmentStore;
import model.*;
import java.util.*;

public class BacktrackingSolver {

    public static ScheduleResult findOptimalSchedule(AssignmentStore store) {
        List<Assignment> allJobs = store.toList(); // unsorted, but we can keep as is
        List<Assignment> bestSelection = new ArrayList<>();
        int bestProfit = 0;

        // Start backtracking from index 0, empty selection
        List<Assignment> current = new ArrayList<>();
        bestSelection = backtrack(allJobs, 0, current, bestProfit, bestSelection);
        
        // Convert bestSelection into ScheduledAssignment list (with actual scheduled days)
        List<ScheduledAssignment> scheduled = new ArrayList<>();

      
        // Sort selected jobs by deadline (ascending) to assign day slots
        bestSelection.sort(Comparator.comparingInt(Assignment::getDeadline));
        for (int i = 0; i < bestSelection.size(); i++) {
            scheduled.add(new ScheduledAssignment(bestSelection.get(i), i + 1));
        }

      
        // Determine unselected jobs
        List<Assignment> unselected = new ArrayList<>(allJobs);
        unselected.removeAll(bestSelection);

      
        int totalMarks = bestProfit;
        return new ScheduleResult(scheduled, unselected, totalMarks);
    }

    private static List<Assignment> backtrack(List<Assignment> allJobs, int index,
                                              List<Assignment> current, int bestProfit,
                                              List<Assignment> bestSelection) {
        if (index == allJobs.size()) {
            // Check feasibility of current selection
            if (isFeasible(current)) {
                int profit = current.stream().mapToInt(Assignment::getMarks).sum();
                if (profit > bestProfit) {
                    bestProfit = profit;
                    return new ArrayList<>(current);
                }
            }
            return bestSelection;
        }


      //------------------------------------------------------------------------

        // Option 1: skip current job
        List<Assignment> result1 = backtrack(allJobs, index + 1, current, bestProfit, bestSelection);
        int profit1 = result1.stream().mapToInt(Assignment::getMarks).sum();
        if (profit1 > bestProfit) {
            bestProfit = profit1;
            bestSelection = result1;
        }

        // Option 2: include current job
        current.add(allJobs.get(index));
        List<Assignment> result2 = backtrack(allJobs, index + 1, current, bestProfit, bestSelection);
        int profit2 = result2.stream().mapToInt(Assignment::getMarks).sum();
        if (profit2 > bestProfit) {
            bestProfit = profit2;
            bestSelection = result2;
        }
        current.remove(current.size() - 1);

        return bestSelection;
    }

//--------------------------------------------
  
    // Feasibility check: can all jobs in the list be scheduled before their deadlines?
    // Each job takes 1 unit, no overlapping slots. Sort by deadline and assign earliest slots.

  
    private static boolean isFeasible(List<Assignment> jobs) {
        // Sort by deadline asending 
        List<Assignment> sorted = new ArrayList<>(jobs);
        sorted.sort(Comparator.comparingInt(Assignment::getDeadline));
        for (int i = 0; i < sorted.size(); i++) {
            // The i-th job (0-indexed) will occupy time slot (i+1)
            if (sorted.get(i).getDeadline() < i + 1) {
                return false;
            }
        }
        return true;
    }
}
