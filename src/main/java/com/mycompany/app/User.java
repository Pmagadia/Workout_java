package com.mycompany.app; // Package for the workout app

public class User { // Represents a user of the workout log

    private String username; // Stores the user's name

    // Constructor to create a user
    public User(String username) {
        this.username = username; // Set username when user object is created
    }

    // Getter method to retrieve username
    public String getUsername() {
        return username; // Return the stored username
    }
}