package com.mycompany.app; // Package for the workout app

import java.util.ArrayList;
import java.util.List;

/**
 * Domain model for one workout entry.
 *
 * <p>This class stores workout details used by both the CLI layer and the
 * persistence layer. It also includes CSV conversion helpers so FileStorage
 * can safely serialize/deserialize workout rows.</p>
 */
public class Workout { // Represents one workout entry

    private String date; // Workout date
    private String exercise; // Exercise name
    private double weight; // Weight used
    private int reps; // Number of reps
    private int sets; // Number of sets
    private String note; // Extra notes
    private boolean completed; // Whether workout is done

    /**
     * Constructs a new Workout with the given details.
     *
     * @param date     the date of the workout
     * @param exercise the name of the exercise
     * @param weight   the weight used
     * @param reps     the number of reps
     * @param sets     the number of sets
     * @param note     any additional notes
     * @param completed whether the workout is completed
     */
    public Workout(String date, String exercise, double weight, int reps, int sets, String note, boolean completed) {
        this.date = date; // Set date
        this.exercise = exercise; // Set exercise
        this.weight = weight; // Set weight
        this.reps = reps; // Set reps
        this.sets = sets; // Set sets
        this.note = note; // Set note
        this.completed = completed; // Set completed status
    }

    /**
     * Returns the date of the workout.
     */
    public String getDate() { // Get date
        return date;
    }

    /**
     * Updates the date of the workout.
     *
     * @param date the new date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns the name of the exercise.
     */
    public String getExercise() { // Get exercise
        return exercise;
    }

    /**
     * Returns the weight used.
     */
    public double getWeight() { // Get weight
        return weight;
    }

    /**
     * Returns the number of reps.
     */
    public int getReps() { // Get reps
        return reps;
    }

    /**
     * Returns the number of sets.
     */
    public int getSets() { // Get sets
        return sets;
    }

    /**
     * Returns any additional notes.
     */
    public String getNote() { // Get note
        return note;
    }

    /**
     * Returns whether the workout is completed.
     */
    public boolean isCompleted() { // Check if completed
        return completed;
    }

    /**
     * Updates the completion status of the workout.
     *
     * @param completed the new completion status
     */
    public void setCompleted(boolean completed) { // Update completed status
        this.completed = completed;
    }

    /**
     * Updates the exercise name.
     *
     * @param exercise the new exercise name
     */
    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    /**
     * Updates the weight used.
     *
     * @param weight the new weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Updates the number of reps.
     *
     * @param reps the new number of reps
     */
    public void setReps(int reps) {
        this.reps = reps;
    }

    /**
     * Updates the number of sets.
     *
     * @param sets the new number of sets
     */
    public void setSets(int sets) {
        this.sets = sets;
    }

    /**
     * Updates any additional notes.
     *
     * @param note the new notes
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Returns a readable summary for CLI output.
     */
    public String toString() { // Print workout nicely
        return "Date: " + date +
                ", Exercise: " + exercise +
                ", Weight: " + weight +
                ", Reps: " + reps +
                ", Sets: " + sets +
                ", Note: " + note +
                ", Completed: " + completed;
    }

    /**
     * Converts this workout into a CSV-safe line for persistence.
     */
    public String toCsvLine() {
        List<String> fields = new ArrayList<>();
        fields.add(date);
        fields.add(exercise);
        fields.add(String.valueOf(weight));
        fields.add(String.valueOf(reps));
        fields.add(String.valueOf(sets));
        fields.add(note);
        fields.add(String.valueOf(completed));
        return toCsv(fields);
    }

    /**
     * Builds a Workout from one CSV line loaded from storage.
     *
     * @param line the CSV line
     * @return a new Workout object
     * @throws IllegalArgumentException if the line does not contain required fields
     */
    public static Workout fromCsvLine(String line) {
        List<String> fields = parseCsv(line);
        if (fields.size() < 7) {
            throw new IllegalArgumentException("Invalid workout CSV line: " + line);
        }

        String date = fields.get(0).trim();
        String exercise = fields.get(1).trim();
        double weight = Double.parseDouble(fields.get(2).trim());
        int reps = Integer.parseInt(fields.get(3).trim());
        int sets = Integer.parseInt(fields.get(4).trim());
        String note = fields.get(5).trim();
        boolean completed = Boolean.parseBoolean(fields.get(6).trim());

        return new Workout(date, exercise, weight, reps, sets, note, completed);
    }

    /**
     * Joins fields into one CSV row with escaping.
     *
     * @param fields the fields to join
     * @return the joined CSV row
     */
    private static String toCsv(List<String> fields) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            if (i > 0) {
                out.append(',');
            }
            out.append(escapeCsv(fields.get(i)));
        }
        return out.toString();
    }

    /**
     * Escapes one CSV field and quotes it when needed.
     *
     * @param value the field to escape
     * @return the escaped field
     */
    private static String escapeCsv(String value) {
        String safe = value == null ? "" : value.replace("\r", " ").replace("\n", " ").trim();
        boolean needsQuotes = safe.contains(",") || safe.contains("\"");
        safe = safe.replace("\"", "\"\"");
        return needsQuotes ? "\"" + safe + "\"" : safe;
    }

    /**
     * Parses one CSV row while handling quoted commas/quotes.
     *
     * @param line the CSV row to parse
     * @return the parsed fields
     */
    private static List<String> parseCsv(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        fields.add(current.toString().trim());
        return fields;
    }
}