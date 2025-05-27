// UserManager.java
// This class is responsible for managing user-related operations
// such as registration, login, and storing user data.
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    // This map stores all registered users. The key is the username (String),
    // and the value is the User object. This is encapsulated within the UserManager.
    private Map<String, User> users;

    // Constructor for the UserManager class.
    // Initializes the internal map of users.
    public UserManager() {
        // Create a new HashMap to store user data.
        this.users = new HashMap<>();
    }

    // Overloaded method to register a new user (customer or store_owner).
    // Takes username, password, and role as input.
    // Returns true if registration is successful, false if username already exists or role is invalid.
    public boolean registerUser(String username, String password, String role) {
        // This is a convenience overload, assuming a default for roles that might need more info (like premium_customer).
        // For premium_customer, it will call the more specific method with a default rate.
        if (role.equalsIgnoreCase("premium_customer")) {
            // Call the specific registration method for premium customers with a default rate.
            return registerUser(username, password, role, 0.1); // Default 10% discount
        }
        // Delegate to the main registration logic, which will handle non-premium roles or use defaults.
        return registerUser(username, password, role, 0.0); // Pass 0.0 as rate, won't be used for non-premium
    }


    // Method to register a new user, potentially with a discount rate for premium customers.
    // Takes username, password, role, and discountRate as input.
    // The discountRate is only used if the role is "premium_customer".
    // Returns true if registration is successful, false if username already exists or role is invalid.
    public boolean registerUser(String username, String password, String role, double discountRate) {
        // Check if the username already exists in the 'users' map.
        if (this.users.containsKey(username)) {
            // If username exists, print a message and return false.
            System.out.println("Username already exists!");
            return false;
        }

        // Create a new User object based on the role.
        User newUser;
        if (role.equalsIgnoreCase("customer")) {
            // Create a new Customer object.
            newUser = new Customer(username, password);
        } else if (role.equalsIgnoreCase("premium_customer")) {
            // Create a new PremiumCustomer object using the provided discount rate.
            newUser = new PremiumCustomer(username, password, discountRate);
            System.out.println("Premium Customer registered with " + (discountRate * 100) + "% discount.");
        } else if (role.equalsIgnoreCase("store_owner")) {
            // Create a new StoreOwner object.
            newUser = new StoreOwner(username, password);
        } else {
            // If the role is not recognized, print a message and return false.
            System.out.println("Invalid role for registration!");
            return false;
        }

        // Add the newly created user to the 'users' map.
        this.users.put(username, newUser);
        // Print a success message, capitalizing the first letter of the role.
        System.out.println(role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase().replace("_", " ") + " registration successful!");
        // Return true indicating successful registration.
        return true;
    }

    // Method to log in a user.
    // Takes username and password as input.
    // Returns the User object if login is successful, null otherwise.
    public User loginUser(String username, String password) {
        // Retrieve the user object from the 'users' map based on the provided username.
        User user = this.users.get(username);

        // Check if the user exists (user is not null) and if the provided password matches.
        if (user != null && user.checkPassword(password)) {
            // If login is successful, print a welcome message.
            System.out.println("Login successful! Welcome " + user.getUsername());
            // Return the User object.
            return user;
        } else {
            // If user does not exist or password does not match, print an error message.
            System.out.println("Invalid username or password!");
            // Return null indicating login failure.
            return null;
        }
    }

    // Method to get a user by their username.
    // This might be used by other services if needed, though direct access should be limited.
    public User getUserByUsername(String username) {
        // Return the user object from the map, or null if not found.
        return this.users.get(username);
    }
}
