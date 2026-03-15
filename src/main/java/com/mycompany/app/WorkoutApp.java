package com.mycompany.app; // Package name

import java.util.List;
import java.util.Scanner; // Allows user input

public class WorkoutApp { // Main application class

    public static void main(String[] args) { // Program starts here

        Scanner sc = new Scanner(System.in); // Create Scanner object

        System.out.print("Enter username: "); // Ask user for their name
        String username = sc.nextLine(); // Read username input

        User user = new User(username); // Create User object

        System.out.println("Welcome, " + user.getUsername() + "!"); // Welcome message

        WorkoutLog log = new WorkoutLog();   // Create WorkoutLog to store workouts
        FileStorage storage = new FileStorage();

        // Load existing workout history from file when app starts.
        try {
            List<Workout> loadedWorkouts = storage.loadWorkoutObjects();
            for (Workout workout : loadedWorkouts) {
                log.addWorkout(workout);
            }
            if (!loadedWorkouts.isEmpty()) {
                System.out.println("Loaded " + loadedWorkouts.size() + " workout(s) from storage.");
            }
        } catch (FileStorage.FileStorageException e) {
            System.out.println("Could not load saved workouts: " + e.getMessage());
        }

        boolean running = true; // Controls the menu loop

        while (running) { // Keep program running until user exits

            // Display menu options
            System.out.println("1. Add Workout");
            System.out.println("2. View Workouts");
            System.out.println("3. Delete Workout");
            System.out.println("4. Mark Workout Completed");
            System.out.println("5. Edit Workout");
            System.out.println("6. Search Workout by Exercise");
            System.out.println("7. Search Workout by Date");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            int choice; // Variable to store user menu choice

            try {
                choice = sc.nextInt(); // Try reading a number from the user
            } catch (Exception e) { // If user enters something that is not a number
                System.out.println("Invalid input. Please enter a number."); // Show error message
                sc.nextLine(); // Clear the invalid input from scanner
                continue; // Restart the menu loop
            }

            sc.nextLine(); // Clear leftover newline character

            switch (choice) { // Check which option user selected

                case 1: // Add a workout

                    System.out.print("Enter date (MM/DD/YR): ");
                    String date = sc.nextLine(); // Get date
                    // Validate date format to ensure it follows MM/DD/YY
                    if (!date.matches("\\d{2}/\\d{2}/\\d{2}")) { // Check if date matches pattern
                        System.out.println("Invalid date format. Please use MM/DD/YY."); // Show error message
                        break; // Return to menu without adding workout
                    }

                    System.out.print("Enter exercise name: ");
                    String exercise = sc.nextLine(); // Get exercise name

                    double weight; // Variable to store workout weight

                    try {
                        System.out.print("Enter weight used: "); // Ask user for weight
                        weight = sc.nextDouble(); // Try reading a decimal number
                    } catch (Exception e) { // If user types letters or invalid input
                        System.out.println("Invalid weight input."); // Show error message
                        sc.nextLine(); // Clear invalid input
                        break; // Exit this case and return to menu
                    }

                    int reps; // Variable to store number of reps

                    try {
                        System.out.print("Enter reps: "); // Ask user for reps
                        reps = sc.nextInt(); // Try reading an integer
                    } catch (Exception e) { // If input is not a number
                        System.out.println("Invalid reps input."); // Show error message
                        sc.nextLine(); // Clear invalid input
                        break; // Return to menu
                    }

                    int sets; // Variable to store number of sets

                    try {
                        System.out.print("Enter number of sets: "); // Ask user for sets
                        sets = sc.nextInt(); // Try reading integer
                        sc.nextLine(); // Clear leftover newline before reading text input
                    } catch (Exception e) { // If user enters invalid input
                        System.out.println("Invalid sets input."); // Show error message
                        sc.nextLine(); // Clear invalid input
                        break; // Return to menu
                    }

                    System.out.print("Any notes? ");
                    String note = sc.nextLine(); // Get notes

                    System.out.print("Mark as completed? (y/n): ");
                    boolean completed = sc.nextLine().equalsIgnoreCase("y"); // Convert to true/false

                    // Create Workout object
                    Workout workout = new Workout(date, exercise, weight, reps, sets, note, completed);

                    log.addWorkout(workout); // Add workout to the log

                    System.out.println("Workout added!"); // Confirmation message
                    break; // End case 1

                case 2: // View workouts

                    log.listAllWorkouts(); // Show all stored workouts
                    break; // End case 2

                case 3: // Delete workout

                    log.listAllWorkouts();

                    System.out.print("Enter workout index to delete: ");
                    int deleteIndex = sc.nextInt();
                    sc.nextLine();

                    log.removeWorkout(deleteIndex);

                    break;

                case 4: // Mark workout completed

                    log.listAllWorkouts();

                    System.out.print("Enter workout index to mark completed: ");
                    int completeIndex = sc.nextInt();
                    sc.nextLine();

                    log.markCompleted(completeIndex);

                    break;

                case 5: // Edit workout

                    log.listAllWorkouts(); // Show all workouts

                    System.out.print("Enter workout index to edit: ");
                    int editIndex = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter new exercise name: ");
                    String newExercise = sc.nextLine();

                    System.out.print("Enter new weight: ");
                    double newWeight = sc.nextDouble();

                    System.out.print("Enter new reps: ");
                    int newReps = sc.nextInt();

                    System.out.print("Enter new sets: ");
                    int newSets = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter new notes: ");
                    String newNote = sc.nextLine();

                    log.editWorkout(editIndex, newExercise, newWeight, newReps, newSets, newNote);

                    break;

                case 6: // Search workouts

                    System.out.print("Enter exercise name to search: ");
                    String keyword = sc.nextLine();

                    List<Workout> results = log.searchByExercise(keyword);

                    if (results.isEmpty()) { // Check if no workouts matched
                        System.out.println("No workouts found for that exercise."); // Inform user
                    } else {
                        for (Workout w : results) { // Print each matching workout
                            System.out.println(w);
                        }
                    }

                    break;

                case 7: // Search workouts by date

                    System.out.print("Enter date to search (MM/DD/YY): "); // Ask user for date
                    String searchDate = sc.nextLine(); // Read date input

                    List<Workout> dateResults = log.searchByDate(searchDate); // Call search method

                    if (dateResults.isEmpty()) { // Check if no workouts matched the date
                        System.out.println("No workouts found for that date."); // Inform the user
                    } else {
                        for (Workout w : dateResults) { // Print each matching workout
                            System.out.println(w);
                        }
                    }

                    break;

                case 8: // Exit program

                    // Save all workouts before exiting.
                    try {
                        boolean saved = storage.saveWorkoutObjects(log.getAllWorkouts());
                        if (saved) {
                            System.out.println("Workouts saved successfully.");
                        } else {
                            System.out.println("Workouts saved with some skipped invalid rows.");
                        }
                    } catch (FileStorage.FileStorageException e) {
                        System.out.println("Error saving workouts: " + e.getMessage());
                    }

                    running = false; // Stop the loop
                    System.out.println("Exiting program..."); // Exit message
                    break; // End case 3

                default: // If user enters invalid option

                    System.out.println("Invalid choice."); // Show error message
            }
        }

        sc.close(); // Close Scanner before program ends
    }
}