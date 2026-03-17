package com.mycompany.app; // Package name

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Scanner; // Allows user input

/**
 * Console entry point for the Workout application.
 *
 * <p>Coordinates user interaction, input validation, and save/load behavior
 * through {@link WorkoutLog} and {@link FileStorage}.</p>
 */
public class WorkoutApp { // Main application class

    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MM/dd/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private static final String DATE_PROMPT_TEXT = "Enter date (MM/DD/YYYY): ";

    /**
     * Starts the interactive CLI loop.
     */
    public static void main(String[] args) { // Program starts here

        Scanner sc = new Scanner(System.in); // Create Scanner object

        String username = readNonEmptyText(sc, "Enter username: ");

        User user = new User(username); // Create User object

        System.out.println("Welcome, " + user.getUsername() + "!"); // Welcome message

        WorkoutLog log = new WorkoutLog();   // Create WorkoutLog to store workouts
        FileStorage storage = new FileStorage();

        int startupChoice = readIntInRange(
                sc,
                "Startup: 1) Create a new workout log  2) Load existing workout log\nChoose an option: ",
                1,
                2);

        if (startupChoice == 2) {
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
        } else {
            System.out.println("Starting a new empty workout log.");
        }

        boolean running = true; // Controls the menu loop

        while (running) { // Keep program running until user exits

            // Display menu options
            System.out.println("1. Add Workout");
            System.out.println("2. View Workouts");
            System.out.println("3. Edit Workout");
            System.out.println("4. Delete Workout");
            System.out.println("5. Search Workouts by Date");
            System.out.println("6. Exit");

            int choice = readIntInRange(sc, "Choose an option: ", 1, 6);

            switch (choice) { // Check which option user selected

                case 1: // Add a workout
                    addWorkout(sc, log);
                    break; // End case 1

                case 2: // View workouts

                    log.listAllWorkouts(); // Show all stored workouts
                    break; // End case 2

                case 3: // Edit workout
                    editWorkout(sc, log);
                    break;

                case 4: // Delete workout
                    deleteWorkout(sc, log);
                    break;

                case 5: // Search workouts by date
                    searchByDate(sc, log);
                    break;

                case 6: // Exit program

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
                    break;

                default: // If user enters invalid option

                    System.out.println("Invalid choice, please try again.");
            }
        }

        sc.close(); // Close Scanner before program ends
    }

    /**
     * Collects validated fields and adds a workout to the log.
     */
    private static void addWorkout(Scanner sc, WorkoutLog log) {

        String date = readValidatedDate(sc, DATE_PROMPT_TEXT);
        String exercise = readNonEmptyText(sc, "Enter exercise name: ");
        double weight = readPositiveDouble(sc, "Enter weight used: ");
        int reps = readPositiveInt(sc, "Enter reps: ");
        int sets = readPositiveInt(sc, "Enter number of sets: ");
        System.out.print("Any notes? ");
        String note = sc.nextLine();
        boolean completed = readYesNo(sc, "Mark as completed? (y/n): ");

        Workout workout = new Workout(date, exercise, weight, reps, sets, note, completed);
        log.addWorkout(workout);
        System.out.println("Workout added!");
    }

    /**
     * Edits a selected workout with full-field updates.
     */
    private static void editWorkout(Scanner sc, WorkoutLog log) {

        if (log.getTotalWorkouts() == 0) {
            System.out.println("No workouts available to edit.");
            return;
        }

        log.listAllWorkouts();
        int editIndex = readIntInRange(sc, "Enter workout index to edit: ", 0, log.getTotalWorkouts() - 1);

        String newDate = readValidatedDate(sc, "Enter new date (MM/DD/YYYY): ");
        String newExercise = readNonEmptyText(sc, "Enter new exercise name: ");
        double newWeight = readPositiveDouble(sc, "Enter new weight: ");
        int newReps = readPositiveInt(sc, "Enter new reps: ");
        int newSets = readPositiveInt(sc, "Enter new sets: ");
        System.out.print("Enter new notes: ");
        String newNote = sc.nextLine();
        boolean newCompleted = readYesNo(sc, "Mark as completed? (y/n): ");

        log.editWorkout(editIndex, newDate, newExercise, newWeight, newReps, newSets, newNote, newCompleted);
    }

    /**
     * Deletes a selected workout after user confirmation.
     */
    private static void deleteWorkout(Scanner sc, WorkoutLog log) {

        if (log.getTotalWorkouts() == 0) {
            System.out.println("No workouts available to delete.");
            return;
        }

        log.listAllWorkouts();
        int deleteIndex = readIntInRange(sc, "Enter workout index to delete: ", 0, log.getTotalWorkouts() - 1);
        if (readYesNo(sc, "Are you sure you want to delete this workout? (y/n): ")) {
            log.removeWorkout(deleteIndex);
        } else {
            System.out.println("Delete canceled.");
        }
    }

    /**
     * Finds workouts that match the requested date.
     */
    private static void searchByDate(Scanner sc, WorkoutLog log) {

        String searchDate = readValidatedDate(sc, "Enter date to search (MM/DD/YYYY): ");
        List<Workout> dateResults = log.searchByDate(searchDate);

        if (dateResults.isEmpty()) {
            System.out.println("No workouts found for that date.");
            return;
        }

        for (Workout workout : dateResults) {
            System.out.println(workout);
        }
    }

    /**
     * Reads an integer within [min, max] and keeps prompting until valid.
     */
    private static int readIntInRange(Scanner sc, String prompt, int min, int max) {

        while (true) {
            System.out.print(prompt);
            String raw = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(raw);
                if (value < min || value > max) {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    /**
     * Reads a positive integer and keeps prompting until valid.
     */
    private static int readPositiveInt(Scanner sc, String prompt) {

        while (true) {
            System.out.print(prompt);
            String raw = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(raw);
                if (value <= 0) {
                    System.out.println("Value must be a positive number.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    /**
     * Reads a positive double and keeps prompting until valid.
     */
    private static double readPositiveDouble(Scanner sc, String prompt) {

        while (true) {
            System.out.print(prompt);
            String raw = sc.nextLine().trim();
            try {
                double value = Double.parseDouble(raw);
                if (value <= 0) {
                    System.out.println("Value must be a positive number.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Reads non-empty text input.
     */
    private static String readNonEmptyText(Scanner sc, String prompt) {

        while (true) {
            System.out.print(prompt);
            String value = sc.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    /**
     * Reads and validates date input in strict MM/DD/YYYY format.
     */
    private static String readValidatedDate(Scanner sc, String prompt) {

        while (true) {
            System.out.print(prompt);
            String raw = sc.nextLine().trim();
            try {
                LocalDate parsed = LocalDate.parse(raw, INPUT_DATE_FORMAT);
                return parsed.format(INPUT_DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use MM/DD/YYYY.");
            }
        }
    }

    /**
     * Reads yes/no input and returns true for yes.
     */
    private static boolean readYesNo(Scanner sc, String prompt) {

        while (true) {
            System.out.print(prompt);
            String raw = sc.nextLine().trim().toLowerCase();
            if ("y".equals(raw) || "yes".equals(raw)) {
                return true;
            }
            if ("n".equals(raw) || "no".equals(raw)) {
                return false;
            }
            System.out.println("Please enter y or n.");
        }
    }
}