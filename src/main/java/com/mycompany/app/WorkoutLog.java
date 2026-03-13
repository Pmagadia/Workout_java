package com.mycompany.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// WorkoutLog manages a list of workout and provides methods
// to add, remove, mark complete, list, and search workouts
public class WorkoutLog {

    private List<Workout> workoutList; // List to store all workouts

    // Constructor: called when creating a new WorkoutLog 
    // Initializes the list so we can start adding workouts
    public WorkoutLog() {
        workoutList = new ArrayList<>();
    }


    // Add a new workout to the log | Rejects null entries
    public void addWorkout(Workout workout) {
        if (workout == null) {
            System.out.println("Cannot add a null workout. Skipping");
            return; // Stop method is workout is null
        }
        // Try-catch block that add the workout to the list
        // Catching any unexpected errors
        try {
            workoutList.add(workout);
        } catch (Exception e) {
            System.out.println("Unexpected error adding workout: " + e.getMessage());
        }
    }


    // Remove a workout by its position (index) in the list
    public void removeWorkout(int workoutIndex) {
       if (workoutList.isEmpty()) {
           System.out.println("No workouts to remove.");
           return;
       }
       // Check if the index is valid
       if (workoutIndex >= 0 && workoutIndex < workoutList.size()) {
           try {
               workoutList.remove(workoutIndex); // Remove the workout
               System.out.println("Workout removed.");               
           } catch (Exception e) {
               System.out.println("Error removing workout: " + e.getMessage());
           }
       // Index is out of range
       } else {
           System.out.println("Invalid index (" + workoutIndex + "). Must be between 0 and " +
           (workoutList.size() - 1) + ". No workout removed.");
       }
    }


    // Get a workout by its position (index) | Return null if list is empty or index is invalid
    public Workout getWorkout(int workoutIndex) {
        if (workoutList.isEmpty()) {
            System.out.println("No workout in the log.");
            return null;
        }
        if (workoutIndex >= 0 && workoutIndex < workoutList.size()) {
            try {
            return workoutList.get(workoutIndex); // Return the workout
            } catch (Exception e) {
                System.out.println("Error retrieving workout " + e.getMessage());
                return null;
            }
        } else {
            System.out.println("Invalid index (" + workoutIndex + "). Must be between 0 and " +
            (workoutList.size() - 1 ) + ".");
            return null;
        }
    }    


    // List all workouts
    public void listAllWorkouts() {
        if (workoutList.isEmpty()) {
            System.out.println("No workouts in the log.");
            return;
        } 
        try {
            for (int workoutIndex = 0; workoutIndex < workoutList.size(); workoutIndex++) {
                Workout workout = workoutList.get(workoutIndex);
                // Check if workout is null
                if (workout == null) {
                    System.out.println("[" + workoutIndex + "] (missing/corrupted workout entry)");
                } else {
                    System.out.println("[" + workoutIndex + "] " + workout);
                }
            }
        } catch (Exception e) {
            System.out.println("Error listing workouts: " + e.getMessage());
        }
    }


    // Mark a workout as completed
    public void markCompleted(int workoutIndex) {
        if (workoutList.isEmpty()) {
            System.out.println("No workouts to mark. ");
            return;
        }
        if (workoutIndex >= 0 && workoutIndex < workoutList.size()) {
            try {
                Workout workout = workoutList.get(workoutIndex);
                if (workout == null) {
                    System.out.println("Workout at workoutIndex " + workoutIndex + " is invalid. Cannot mark completed.");
                    return;
                }
                if (workout.isCompleted()) {
                    System.out.println("Workout is already marked as completed.");
                } else {
                    workout.setCompleted(true); // Marked completed
                    System.out.println("Workout marked as completed");
                }
            } catch (Exception e) {
                System.out.println("Error marking workout as completed: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid workoutIndex (" + workoutIndex + "). Cannot mark completed.");
        }
    }


    // Return the total number of workouts in the log
    public int getTotalWorkouts() {
        return workoutList.size();
    }

    // Return an unmodifiable snapshop of all workouts for the persistence layer
    // Using Collections.unmodifiableList prevents accidental outside modification
    public List<Workout> getAllWorkouts() {
        return Collections.unmodifiableList(new ArrayList<>(workoutList));
    }


    // Clear all workouts from the log (useful for resets or testing)
    public void clearAll() {
        try {
            workoutList.clear();
            System.out.println("All workouts cleared.");
        } catch (Exception e) {
            System.out.println("Error clearing workouts: " + e.getMessage());
        }
    }


    // Search workouts by exercise name (case-insensitive partial match)
    public List<Workout> searchByExercise(String keyword) {
        List<Workout> results = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return results;
        }
        try {
            String lowerKeyword = keyword.trim().toLowerCase();
            for (Workout workout: workoutList) {
                if (workout != null && workout.getExercise() != null
                        && workout.getExercise().toLowerCase().contains(lowerKeyword)) {
                    results.add(workout); // Add matching workout to results    
                }
            }
            if (results.isEmpty()) {
                System.out.println("No workouts found matching: " + keyword);
            }
        } catch (Exception e) {
            System.out.println("Error during search: " + e.getMessage());
        }
        return results; // Return list of found workouts
    }
}