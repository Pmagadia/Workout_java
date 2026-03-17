package com.mycompany.app; // Package for the workout app

/**
 * Represents the active user running the workout tracker.
 */
public class User { // Represents a user of the workout log

    private String username; // Stores the user's name

    /**
     * Creates a user with the provided username.
     * @param username the username for the new user
     */
    public User(String username) {
        this.username = username; // Set username when user object is created
    }

    /**
     * Returns the username shown in welcome messages.
     * @return the username of the user
     */
    public String getUsername() {
        return username; // Return the stored username
    }
}