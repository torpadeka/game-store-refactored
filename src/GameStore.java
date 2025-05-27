// GameStore.java
// This is the main class for the Game Store application.
// It contains the main method and orchestrates the application flow,
// delegating menu handling to specific handler classes.

// Import necessary Java utility classes.
import java.util.Map;     // Used for type declaration, e.g., Map of users.
import java.util.Scanner; // Used for reading user input from the console.

public class GameStore {

    // The main method - entry point of the application.
    public static void main(String[] args) {
        // Create a Scanner object to read input from the console.
        Scanner scanner = new Scanner(System.in);
        // Variable to store the currently logged-in user. Initially null (no one logged in).
        User currentUser = null;

        // Instantiate the service classes that manage different parts of the application.
        UserManager userManager = new UserManager();
        StoreService storeService = new StoreService();
        // TransactionLogger contains only static methods, so no instance is needed.

        // Main application loop. This loop runs indefinitely until the user chooses to exit.
        while (true) {
            // Print the main welcome message for the application.
            System.out.println("\n--- Welcome to the Game Store App ---");
            // Check if there is a user currently logged in.
            if (currentUser == null) {
                // If no user is logged in, display options for registration, login, or exit.
                displayLoggedOutMenu(); // Call a helper method to display the menu.
                int choice; // Variable to store the user's menu choice.
                try {
                    // Read the user's choice from the console and parse it as an integer.
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    // If the input is not a valid number, print an error and restart the loop.
                    System.out.println("Invalid input. Please enter a number.");
                    continue; // Continue to the next iteration of the while loop.
                }

                // Process the user's choice using a switch statement.
                switch (choice) {
                    case 1: // Handle user registration.
                        handleRegistration(scanner, userManager); // Delegate to a helper method.
                        break; // Exit switch statement.
                    case 2: // Handle user login.
                        // Attempt to log in the user and update currentUser.
                        currentUser = handleLogin(scanner, userManager);
                        break; // Exit switch statement.
                    case 3: // Handle application exit.
                        System.out.println("Exiting...");
                        scanner.close(); // Close the scanner resource.
                        return; // Terminate the main method, thus exiting the application.
                    default: // Handle invalid menu options.
                        System.out.println("Invalid option!");
                }
            } else { // This block executes if a user is currently logged in.
                // Display a welcome message for the logged-in user, showing their username and role.
                System.out.println("\n--- Logged in as: " + currentUser.getUsername() + " (" + currentUser.getRole() + ") ---");

                // Variable to track if the user chose to logout from a menu handler.
                boolean loggedOut = false;

                // Delegate to a specific menu handler based on the type of the current user.
                if (currentUser instanceof PremiumCustomer) {
                    // Create and use PremiumCustomerMenuHandler.
                    PremiumCustomerMenuHandler menuHandler = new PremiumCustomerMenuHandler((PremiumCustomer) currentUser, scanner, userManager, storeService);
                    loggedOut = menuHandler.processMenu();
                } else if (currentUser instanceof Customer) {
                    // Create and use CustomerMenuHandler.
                    CustomerMenuHandler menuHandler = new CustomerMenuHandler((Customer) currentUser, scanner, userManager, storeService);
                    loggedOut = menuHandler.processMenu();
                } else if (currentUser instanceof StoreOwner) {
                    // Create and use StoreOwnerMenuHandler.
                    StoreOwnerMenuHandler menuHandler = new StoreOwnerMenuHandler((StoreOwner) currentUser, scanner, userManager, storeService);
                    loggedOut = menuHandler.processMenu();
                }

                // If the menu handler indicated a logout, set currentUser to null.
                if (loggedOut) {
                    currentUser = null;
                }
            }
        } // End of main while loop.
    } // End of main method.

    // Helper method to display the menu for logged-out users.
    private static void displayLoggedOutMenu() {
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
    }

    // Helper method to handle user registration.
    private static void handleRegistration(Scanner scanner, UserManager userManager) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine(); // Read username.
        System.out.print("Enter password: ");
        String password = scanner.nextLine(); // Read password.
        System.out.print("Register as (customer/premium_customer/store_owner): ");
        String role = scanner.nextLine(); // Read desired role.

        // Attempt to register the user via the UserManager.
        if (role.equalsIgnoreCase("premium_customer")) {
             System.out.print("Enter discount rate (e.g., 0.1 for 10%): ");
             double rate = 0.1; // Default discount rate.
             try {
                 rate = Double.parseDouble(scanner.nextLine()); // Read discount rate.
                 userManager.registerUser(username, password, role, rate);
             } catch (NumberFormatException e) {
                 System.out.println("Invalid rate, using default 10% for premium customer registration.");
                 userManager.registerUser(username, password, role, 0.1); // Fallback to default
             }
        } else {
            userManager.registerUser(username, password, role);
        }
    }

    // Helper method to handle user login.
    // Returns the User object if login is successful, null otherwise.
    private static User handleLogin(Scanner scanner, UserManager userManager) {
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine(); // Read username for login.
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine(); // Read password for login.
        // Attempt to log in the user via the UserManager.
        return userManager.loginUser(loginUsername, loginPassword);
    }

    // This static helper method is used by menu handlers to display all stores and games.
    // It remains in GameStore as a utility accessible by different menu handlers.
    public static void viewAllStoresAndGames(StoreService storeService) {
        // Get all stores from the StoreService.
        Map<String, Map<String, Game>> allStores = storeService.getAllStores();
        // Check if any stores exist.
        if (allStores.isEmpty()) {
            System.out.println("No stores available yet.");
        } else {
            // Iterate through all stores.
            for (Map.Entry<String, Map<String, Game>> storeEntry : allStores.entrySet()) {
                // Print store name and owner (retrieved from StoreService).
                System.out.println("\nStore: " + storeEntry.getKey() + " (Owner: " + storeService.getStoreOwner(storeEntry.getKey()) + ")");
                // Check if the current store has any games.
                if (storeEntry.getValue().isEmpty()) {
                    System.out.println("  - No games in this store yet.");
                } else {
                    // Iterate through games in the current store.
                    for (Game game : storeEntry.getValue().values()) {
                        // Print game details: name, price, and genre.
                        System.out.println("  - " + game.getName() + " ($" + String.format("%.2f", game.getPrice()) + ") - " + game.getGenre());
                    }
                }
            }
        }
    }

}
