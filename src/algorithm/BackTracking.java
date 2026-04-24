package algorithm;

import framework.AssignmentStore;
import java.util.*;
import model.*;

public class BackTracking {


    // this is a helper class to hold the best solutions found so far 
    private static class Best {

        //the selected assignments
        List<Assignment> selection = new ArrayList<>(); 
        
        int profit = 0; //the total marks of the selection 
    }

    public static ScheduleResult findOptimalSchedule(AssignmentStore store) {
        //all assignments as a list 
        List<Assignment> allJobs = store.toList(); 

        // initialized empty, the best solution 
        Best best = new Best();


        // backtracking starts here from index 0 with empty current selection 
        backtrack(allJobs, 0, new ArrayList<>(), best);
        
        
        
        // after the backtraccking, best will contain the optimal subset (solution)
        List<Assignment> bestSelection = best.selection;

        //sort by deadline 
        bestSelection.sort(Comparator.comparingInt(Assignment::getDeadline));


        // with this loop, we create scheduled assignments and assign
        // numbers for each days starting from 1
        List<ScheduledAssignment> scheduled = new ArrayList<>();
        
        for (int i = 0; i < bestSelection.size(); i++) {
            scheduled.add(new ScheduledAssignment(bestSelection.get(i), i + 1));
        }
        
        //find out the assignments that were not selected 
        List<Assignment> unselected = new ArrayList<>(allJobs);
        unselected.removeAll(bestSelection);


        // return the complete result 
        return new ScheduleResult(scheduled, unselected, best.profit);
    }

    

    
    private static void backtrack(List<Assignment> allJobs, int index,
                               
    		List<Assignment> current, Best best) {

        
        if (index == allJobs.size()) {
        	// BC: check if current seletion is feasible 
            if (isFeasible(current)) {
                
            	// calculate all marks of this selection 
                int profit = current.stream().mapToInt(Assignment::getMarks).sum();

                //update best, if current selection has more profit
                if (profit > best.profit) {
                    best.profit = profit;
                    best.selection = new ArrayList<>(current);
                }
            }
            return;
        }
        
    //------------------------------------------------------
        // Option 1: skip current job
        backtrack(allJobs, index + 1, current, best);
        
        
        // Option 2: include current job
        current.add(allJobs.get(index));
        backtrack(allJobs, index + 1, current, best);

        // BACKTRACK: remove the last added element 
        current.remove(current.size() - 1);
    }
  //------------------------------------------------------

    
    private static boolean isFeasible(List<Assignment> jobs) {
    	
        List<Assignment> sorted = new ArrayList<>(jobs);
        // sort by deadline 
        sorted.sort(Comparator.comparingInt(Assignment::getDeadline));

        //for each job in sorted order, check if its dealine is at least of its position 
        for (int i = 0; i < sorted.size(); i++) {

            
            if (sorted.get(i).getDeadline() < i + 1) return false; //deadline violation 
        }
        return true; // all deadlines are satisfied 
    }
}
