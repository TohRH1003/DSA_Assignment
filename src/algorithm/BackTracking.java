package algorithm;

import datastructures.AssignmentStore;
import java.util.*;
import model.*;

public class BackTracking {

    private static class Best {
        List<Assignment> selection = new ArrayList<>();
        int profit = 0;
    }

    public static ScheduleResult findOptimalSchedule(AssignmentStore store) {
        List<Assignment> allJobs = store.toList();
        Best best = new Best();
        backtrack(allJobs, 0, new ArrayList<>(), best);
        
        
        
        // Convert best selection to scheduled days 
        //(sorted by deadline)
        List<Assignment> bestSelection = best.selection;
        
        bestSelection.sort(Comparator.comparingInt(Assignment::getDeadline));
        
        List<ScheduledAssignment> scheduled = new ArrayList<>();
        
        for (int i = 0; i < bestSelection.size(); i++) {
            scheduled.add(new ScheduledAssignment(bestSelection.get(i), i + 1));
        }
        
        
        List<Assignment> unselected = new ArrayList<>(allJobs);
        unselected.removeAll(bestSelection);
        
        return new ScheduleResult(scheduled, unselected, best.profit);
    }

    
    
    private static void backtrack(List<Assignment> allJobs, int index,
                               
    		List<Assignment> current, Best best) {
        if (index == allJobs.size()) {
        	
            if (isFeasible(current)) {
            	
                int profit = current.stream().mapToInt(Assignment::getMarks).sum();
                
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
        current.remove(current.size() - 1);
    }
  //------------------------------------------------------

    
    private static boolean isFeasible(List<Assignment> jobs) {
    	
        List<Assignment> sorted = new ArrayList<>(jobs);
        
        sorted.sort(Comparator.comparingInt(Assignment::getDeadline));
        
        for (int i = 0; i < sorted.size(); i++) {
        	
            if (sorted.get(i).getDeadline() < i + 1) return false;
        }
        return true;
    }
}
