// User.java
// This is the base class for all types of users in the system.
// It contains common properties like username, password, and role.

import java.util.Scanner;

public class User {
    // Stores the username of the user. This is a String.
    private String username;
    // Stores the password of the user. This is also a String.
    private String password;
    // Stores the role of the user (e.g., "customer", "store_owner"). This is a String.
    private String role;

    // Constructor for the User class.
    // Initializes a new User object with the given username, password, and role.
    public User(String username, String password, String role) {
        // Set the username for this user instance.
        this.username = username;
        // Set the password for this user instance.
        this.password = password;
        // Set the role for this user instance.
        this.role = role;
    }

    // Getter method for the username.
    // Returns the username of this user.
    public String getUsername() {
        // Return the current value of the username field.
        return username;
    }

    // Setter method for the username.
    // Allows updating the username of this user.
    public void setUsername(String username) {
        // Assign the new username value to the username field.
        this.username = username;
    }

    // Getter method for the password.
    // Returns the password of this user.
    public String getPassword() {
        // Return the current value of the password field.
        return password;
    }

    // Setter method for the password.
    // Allows updating the password of this user.
    public void setPassword(String password) {
        // Assign the new password value to the password field.
        this.password = password;
    }

    // Getter method for the role.
    // Returns the role of this user.
    public String getRole() {
        // Return the current value of the role field.
        return role;
    }

    // Setter method for the role.
    // Allows updating the role of this user after the object has been created.
    public void setRole(String role) {
        // Assign the new role value to the role field.
        this.role = role;
        // Print a message to the console indicating that the role was changed.
        System.out.println("User role changed to " + role + " post-creation.");
    }

    // Method to check if the provided password attempt matches the user's actual password.
    // Takes a String 'passwordAttempt' as input.
    // Returns true if the passwords match, false otherwise.
    public boolean checkPassword(String passwordAttempt) {
        // Compare the stored password with the password attempt using the equals method for Strings.
        return this.password.equals(passwordAttempt);
    }

    // This method is intended for actions specific to administrative users.
    // In this base User class, it provides a default behavior.
    // It now takes necessary service instances as parameters, though not used in this base implementation.
    public void performAdminAction(Scanner scanner, UserManager userManager, StoreService storeService) {
        // Print a message indicating that generic users don't have specific admin actions.
        System.out.println("User " + username + " does not have specific admin actions.");
    }
}
