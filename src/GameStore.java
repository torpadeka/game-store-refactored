// GameStore.java
// This is the main class for the Game Store application.
// It contains the main method and manages the overall application state and logic.

// Import necessary Java utility classes.
import java.util.ArrayList; // Used for initializing lists, though not directly in this file for that purpose.
import java.util.HashMap;   // Used for creating HashMap instances for our data storage.
import java.util.List;    // Used for type declaration, e.g., List of games.
import java.util.Map;     // Used for type declaration, e.g., Map of users.
import java.util.Scanner; // Used for reading user input from the console.

public class GameStore {

    // This map stores all registered users. The key is the username (String),
    // and the value is the User object. It's public and static, meaning it's globally accessible.
    public static Map<String, User> users = new HashMap<>();

    // This map stores all the game stores. The key is the store name (String).
    // The value is another map, where the key is the game name (String) and the value is the Game object.
    // This is also public and static.
    public static Map<String, Map<String, Game>> stores = new HashMap<>();

    // This map keeps track of which store is owned by which user.
    // The key is the store name (String), and the value is the owner's username (String).
    // Public and static as well.
    public static Map<String, String> storeOwnership = new HashMap<>();

    // Constructor for the GameStore class.
    // Currently, it doesn't perform any specific initialization.
    public GameStore() {
        // The constructor is empty.
    }

    // This method retrieves the map of games for a given store name.
    // It takes the storeName (String) as input.
    // It returns a Map where keys are game names (String) and values are Game objects.
    public Map<String, Game> getGamesInStore(String storeName) {
        // Access the static 'stores' map and get the inner map for the specified storeName.
        return stores.get(storeName);
    }

    // This method checks if a store with the given name exists.
    // It takes the storeName (String) as input.
    // Returns true if the store exists, false otherwise.
    public boolean doesStoreExist(String storeName) {
        // Check if the static 'stores' map contains the given storeName as a key.
        return stores.containsKey(storeName);
    }

    // This method adds a new store to the system.
    // It takes the storeName (String) and the owner's username (String) as input.
    public void addStore(String storeName, String ownerUsername) {
        // Add a new entry to the static 'stores' map for the new store.
        // The value is a new empty HashMap for the games in this store.
        stores.put(storeName, new HashMap<>());
        // Add an entry to the static 'storeOwnership' map to link the store to its owner.
        storeOwnership.put(storeName, ownerUsername);
    }

    // This method adds a game to a specific store.
    // It takes the storeName (String) and the Game object as input.
    public void addGameToStore(String storeName, Game game) {
        // Check if the store exists in the static 'stores' map.
        if (stores.containsKey(storeName)) {
            // If the store exists, get its map of games and add the new game.
            // The game's name is used as the key in the inner map.
            stores.get(storeName).put(game.getName(), game);
        } else {
            // If the store doesn't exist, print an error message.
            System.out.println("Error: Store " + storeName + " not found when trying to add game.");
        }
    }

    // This static method renames an existing store.
    // It takes the oldName (String), newName (String), and the StoreOwner object as input.
    // Returns true if renaming was successful, false otherwise.
    public static boolean renameStore(String oldName, String newName, StoreOwner owner) {
        // Check if the old store name actually exists.
        if (!stores.containsKey(oldName)) {
            // If not, print an error and return false.
            System.out.println("Error: Old store '" + oldName + "' not found.");
            return false;
        }
        // Check if the new store name is already taken.
        if (stores.containsKey(newName)) {
            // If so, print an error and return false.
            System.out.println("Error: New store name '" + newName + "' already exists.");
            return false;
        }
        // If checks pass, proceed with renaming.
        // Remove the store entry with the old name and get its games.
        Map<String, Game> games = stores.remove(oldName);
        // Remove the ownership entry for the old name and get the owner's username.
        String ownerUsername = storeOwnership.remove(oldName);
        // Add a new entry to 'stores' with the new name and the games from the old store.
        stores.put(newName, games);
        // Add a new entry to 'storeOwnership' with the new name and the owner.
        storeOwnership.put(newName, ownerUsername);

        // The StoreOwner object also needs to update its internal list of store names.
        owner.updateStoreNameInList(oldName, newName);
        // Log this system event.
        logTransaction("SYSTEM", "RENAME_STORE", 0, oldName + " -> " + newName);
        // Return true indicating success.
        return true;
    }

    // This static method edits the price of a game in a specific store.
    // Takes storeName (String), gameName (String), and newPrice (double) as input.
    public static void editGamePrice(String storeName, String gameName, double newPrice) {
        // Check if the store exists and contains the specified game.
        if (stores.containsKey(storeName) && stores.get(storeName).containsKey(gameName)) {
            // If both exist, get the Game object.
            Game game = stores.get(storeName).get(gameName);
            // Set the new price for the game using its setter method.
            game.setPrice(newPrice);
            // Log this system event.
            logTransaction("SYSTEM", "EDIT_GAME_PRICE", newPrice, gameName + " in " + storeName);
            // Print a success message.
            System.out.println("Price updated successfully for " + gameName + ".");
        } else {
            // If store or game not found, print an error message.
            System.out.println("Store or game not found for price edit.");
        }
    }

    // This static method edits the genre of a game in a specific store.
    // Takes storeName (String), gameName (String), and newGenre (String) as input.
     public static void editGameGenre(String storeName, String gameName, String newGenre) {
        // Check if the store exists and if that store contains the specified game.
        if (stores.containsKey(storeName) && stores.get(storeName).containsKey(gameName)) {
            // If the store and game are found, retrieve the Game object.
            Game game = stores.get(storeName).get(gameName);
            // Call the setGenre method on the Game object to update its genre.
            game.setGenre(newGenre);
            // Log the transaction of editing a game's genre.
            logTransaction("SYSTEM", "EDIT_GAME_GENRE", 0, gameName + " in " + storeName + " to " + newGenre);
            // Print a confirmation message to the console.
            System.out.println("Genre updated successfully for " + gameName + ".");
        } else {
            // If the store or game is not found, print an error message to the console.
            System.out.println("Store or game not found for genre edit.");
        }
    }

    // This static method removes a game from a specific store.
    // Takes storeName (String) and gameName (String) as input.
    public static void removeGame(String storeName, String gameName) {
        // Check if the store exists and contains the specified game.
        if (stores.containsKey(storeName) && stores.get(storeName).containsKey(gameName)) {
            // If both exist, remove the game from the store's game map.
            stores.get(storeName).remove(gameName);
            // Log this system event.
            logTransaction("SYSTEM", "REMOVE_GAME", 0, gameName + " from " + storeName);
            // Print a success message.
            System.out.println("Game removed successfully.");
        } else {
            // If store or game not found, print an error message.
            System.out.println("Store or game not found for removal.");
        }
    }

    // This static method logs a transaction or system event.
    // It takes username (String), type of transaction (String), amount (double), and details (String).
    // Currently, it just prints to the console.
    public static void logTransaction(String username, String type, double amount, String details) {
        // This method formats and prints transaction details to the standard output.
        // In a real application, this would likely write to a persistent log file or a database.
        System.out.printf("[TRANSACTION LOG] User: %s, Type: %s, Amount/Value: %.2f, Details: %s%n",
                username, type, amount, details);
    }

    // The main method - entry point of the application.
    public static void main(String[] args) {
        // Create a Scanner object to read input from the console.
        Scanner scanner = new Scanner(System.in);
        // Variable to store the currently logged-in user. Initially null (no one logged in).
        User currentUser = null;
        // Create an instance of GameStore. This is somewhat redundant as many fields/methods are static.
        GameStore appInstance = new GameStore();

        // Main application loop. This loop runs indefinitely until the user chooses to exit.
        while (true) {
            // Print the main welcome message for the application.
            System.out.println("\n--- Welcome to the Game Store App ---");
            // Check if there is a user currently logged in.
            if (currentUser == null) {
                // If no user is logged in, display options for registration, login, or exit.
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                // Variable to store the user's menu choice.
                int choice;
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
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine(); // Read username.
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine(); // Read password.
                        System.out.print("Register as (customer/premium_customer/store_owner): ");
                        String role = scanner.nextLine(); // Read desired role.

                        // Check if the username already exists in the 'users' map.
                        if (users.containsKey(username)) {
                            System.out.println("Username already exists!");
                        } else if (role.equalsIgnoreCase("customer")) {
                            // If role is customer, create a new Customer object and add to users map.
                            users.put(username, new Customer(username, password));
                            System.out.println("Customer registration successful!");
                        } else if (role.equalsIgnoreCase("premium_customer")) {
                            // If role is premium_customer, get discount rate and create object.
                            System.out.print("Enter discount rate (e.g., 0.1 for 10%): ");
                            double rate = 0.1; // Default discount rate.
                            try {
                                rate = Double.parseDouble(scanner.nextLine()); // Read discount rate.
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid rate, using default 10%.");
                            }
                            users.put(username, new PremiumCustomer(username, password, rate));
                            System.out.println("Premium Customer registration successful!");
                        }
                        else if (role.equalsIgnoreCase("store_owner")) {
                            // If role is store_owner, create a new StoreOwner object.
                            users.put(username, new StoreOwner(username, password));
                            System.out.println("Store Owner registration successful!");
                        } else {
                            // If the role is invalid.
                            System.out.println("Invalid role!");
                        }
                        break; // Exit switch statement.
                    case 2: // Handle user login.
                        System.out.print("Enter username: ");
                        String loginUsername = scanner.nextLine(); // Read username for login.
                        System.out.print("Enter password: ");
                        String loginPassword = scanner.nextLine(); // Read password for login.
                        // Retrieve the user object from the 'users' map.
                        User user = users.get(loginUsername);
                        // Check if user exists and password matches.
                        if (user != null && user.checkPassword(loginPassword)) {
                            currentUser = user; // Set the current user.
                            System.out.println("Login successful! Welcome " + currentUser.getUsername());
                        } else {
                            System.out.println("Invalid username or password!");
                        }
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

                // Check the type of the current user to display appropriate menu options.
                // This uses instanceof to determine the specific subclass of User.
                if (currentUser instanceof PremiumCustomer) {
                    // Cast currentUser to PremiumCustomer to access premium-specific methods/fields.
                    PremiumCustomer currentPremiumCustomer = (PremiumCustomer) currentUser;
                    // Display current balance and discount rate.
                    System.out.println("Balance: $" + String.format("%.2f", currentPremiumCustomer.getBalance()) +
                                       " (Discount: " + (currentPremiumCustomer.getDiscountRate()*100) + "%)");
                    // Display menu options for PremiumCustomer.
                    System.out.println("1. View Stores and Games");
                    System.out.println("2. Buy Game (Premium Discount)");
                    System.out.println("3. Top Up Balance");
                    System.out.println("4. View My Games");
                    System.out.println("5. Perform Admin Action (if applicable)");
                    System.out.println("6. Logout");
                    System.out.print("Choose an option: ");
                    int choice; // Variable for menu choice.
                     try { choice = Integer.parseInt(scanner.nextLine()); } // Read choice.
                     catch (NumberFormatException e) { System.out.println("Invalid input."); continue; } // Handle non-numeric input.

                    // Process PremiumCustomer's choice.
                    switch (choice) {
                        case 1: // View Stores and Games.
                             if (stores.isEmpty()) { // Check if any stores exist.
                                System.out.println("No stores available yet.");
                            } else {
                                // Iterate through all stores.
                                for (Map.Entry<String, Map<String, Game>> storeEntry : stores.entrySet()) {
                                    // Print store name and owner.
                                    System.out.println("\nStore: " + storeEntry.getKey() + " (Owner: " + storeOwnership.get(storeEntry.getKey()) + ")");
                                    if (storeEntry.getValue().isEmpty()) { // Check if store has games.
                                        System.out.println("  - No games in this store yet.");
                                    } else {
                                        // Iterate through games in the current store.
                                        for (Game game : storeEntry.getValue().values()) {
                                            // Print game details.
                                            System.out.println("  - " + game.getName() + " ($" + String.format("%.2f", game.getPrice()) + ") - " + game.getGenre());
                                        }
                                    }
                                }
                            }
                            break;
                        case 2: // Buy Game with premium discount.
                            System.out.print("Enter store name: ");
                            String storeName = scanner.nextLine(); // Read store name.
                            System.out.print("Enter game name: ");
                            String gameName = scanner.nextLine(); // Read game name.
                            // Call buyGame method of PremiumCustomer.
                            currentPremiumCustomer.buyGame(storeName, gameName, appInstance);
                            break;
                        case 3: // Top Up Balance.
                            System.out.print("Enter amount to top up: ");
                            double amount; // Variable for top-up amount.
                            try { amount = Double.parseDouble(scanner.nextLine()); } // Read amount.
                            catch (NumberFormatException e) { System.out.println("Invalid amount."); continue; } // Handle non-numeric.
                            currentPremiumCustomer.topUp(amount); // Call topUp method.
                            break;
                        case 4: // View My Games.
                            currentPremiumCustomer.viewMyGames(); // Call method to view owned games.
                            break;
                        case 5: // Perform Admin Action.
                            currentUser.performAdminAction(); // Call polymorphic admin action method.
                            break;
                        case 6: // Logout.
                            currentUser = null; // Set current user to null.
                            System.out.println("Logged out.");
                            break;
                        default: // Invalid option.
                            System.out.println("Invalid option!");
                    }
                } else if (currentUser instanceof Customer) { // This block is for regular Customer users.
                    // Cast currentUser to Customer.
                    Customer currentCustomer = (Customer) currentUser;
                    // Display current balance.
                    System.out.println("Balance: $" + String.format("%.2f", currentCustomer.getBalance()));
                    // Display menu options for Customer.
                    System.out.println("1. View Stores and Games");
                    System.out.println("2. Buy Game");
                    System.out.println("3. Top Up Balance");
                    System.out.println("4. View My Games");
                    System.out.println("5. Perform Admin Action (if applicable)");
                    System.out.println("6. Logout");
                    System.out.print("Choose an option: ");
                    int choice; // Variable for menu choice.
                    try { choice = Integer.parseInt(scanner.nextLine()); } // Read choice.
                    catch (NumberFormatException e) { System.out.println("Invalid input."); continue; } // Handle non-numeric.

                    // Process Customer's choice.
                    switch (choice) {
                        case 1: // View Stores and Games.
                            if (stores.isEmpty()) { // Check if any stores exist.
                                System.out.println("No stores available yet.");
                            } else {
                                // Iterate through all stores.
                                for (Map.Entry<String, Map<String, Game>> storeEntry : stores.entrySet()) {
                                    // Print store name and owner.
                                    System.out.println("\nStore: " + storeEntry.getKey() + " (Owner: " + storeOwnership.get(storeEntry.getKey()) + ")");
                                    if (storeEntry.getValue().isEmpty()) { // Check if store has games.
                                        System.out.println("  - No games in this store yet.");
                                    } else {
                                        // Iterate through games in the current store.
                                        for (Game game : storeEntry.getValue().values()) {
                                            // Print game details.
                                            System.out.println("  - " + game.getName() + " ($" + String.format("%.2f", game.getPrice()) + ") - " + game.getGenre());
                                        }
                                    }
                                }
                            }
                            break;
                        case 2: // Buy Game.
                            System.out.print("Enter store name: ");
                            String storeName = scanner.nextLine(); // Read store name.
                            System.out.print("Enter game name: ");
                            String gameName = scanner.nextLine(); // Read game name.
                            // Call buyGame method of Customer.
                            currentCustomer.buyGame(storeName, gameName, appInstance);
                            break;
                        case 3: // Top Up Balance.
                            System.out.print("Enter amount to top up: ");
                            double amount; // Variable for top-up amount.
                            try { amount = Double.parseDouble(scanner.nextLine()); } // Read amount.
                            catch (NumberFormatException e) { System.out.println("Invalid amount."); continue; } // Handle non-numeric.
                            currentCustomer.topUp(amount); // Call topUp method.
                            break;
                        case 4: // View My Games.
                            currentCustomer.viewMyGames(); // Call method to view owned games.
                            break;
                        case 5: // Perform Admin Action.
                            currentUser.performAdminAction(); // Call polymorphic admin action method.
                            break;
                        case 6: // Logout.
                            currentUser = null; // Set current user to null.
                            System.out.println("Logged out.");
                            break;
                        default: // Invalid option.
                            System.out.println("Invalid option!");
                    }
                } else if (currentUser instanceof StoreOwner) { // This block is for StoreOwner users.
                    // Cast currentUser to StoreOwner.
                    StoreOwner currentOwner = (StoreOwner) currentUser;
                    // Display menu options for StoreOwner.
                    System.out.println("1. Create Store");
                    System.out.println("2. Add Game to Store");
                    System.out.println("3. View My Stores");
                    System.out.println("4. Edit Store");
                    System.out.println("5. Perform Admin Action");
                    System.out.println("6. Logout");
                    System.out.print("Choose an option: ");
                    int choice; // Variable for menu choice.
                    try { choice = Integer.parseInt(scanner.nextLine()); } // Read choice.
                    catch (NumberFormatException e) { System.out.println("Invalid input."); continue; } // Handle non-numeric.

                    // Process StoreOwner's choice.
                    switch (choice) {
                        case 1: // Create Store.
                            System.out.print("Enter new store name: ");
                            String newStoreName = scanner.nextLine(); // Read new store name.
                            // Call createStore method of StoreOwner.
                            currentOwner.createStore(newStoreName, appInstance);
                            break;
                        case 2: // Add Game to Store.
                            System.out.print("Enter store name to add game to: ");
                            String storeToAddGame = scanner.nextLine(); // Read store name.
                            // Check if the owner actually owns this store (accessing public field myStores).
                            if (!currentOwner.myStores.contains(storeToAddGame)) {
                                System.out.println("You don't own this store or it doesn't exist in your list.");
                                break; // Break from switch if not owned.
                            }
                            System.out.print("Enter game name: ");
                            String newGameName = scanner.nextLine(); // Read game name.
                            System.out.print("Enter game price: ");
                            double newGamePrice; // Variable for game price.
                            try { newGamePrice = Double.parseDouble(scanner.nextLine()); } // Read price.
                            catch (NumberFormatException e) { System.out.println("Invalid price."); continue; } // Handle non-numeric.
                            System.out.print("Enter game genre: ");
                            String newGameGenre = scanner.nextLine(); // Read game genre.
                            // Call addGame method of StoreOwner.
                            currentOwner.addGame(storeToAddGame, newGameName, newGamePrice, newGameGenre, appInstance);
                            break;
                        case 3: // View My Stores.
                            System.out.println("Your Stores:");
                            // Check if the owner has any stores (accessing public field myStores).
                            if (currentOwner.myStores.isEmpty()) {
                                System.out.println("  - You don't own any stores yet.");
                            } else {
                                // Iterate through the owner's stores and print their names.
                                for (String sName : currentOwner.myStores) {
                                    System.out.println("  - " + sName);
                                }
                            }
                            break;
                        case 4: // Edit Store.
                            System.out.print("Enter the name of the store you want to edit: ");
                            String storeToEdit = scanner.nextLine(); // Read name of store to edit.
                            // Check if the owner owns this store (accessing public field myStores).
                            if (currentOwner.myStores.contains(storeToEdit)) {
                                System.out.println("\nEditing Store: " + storeToEdit);
                                // Display edit options for the store.
                                System.out.println("1. Change Store Name");
                                System.out.println("2. Edit Game Price");
                                System.out.println("3. Edit Game Genre");
                                System.out.println("4. Remove Game");
                                System.out.println("5. Back");
                                System.out.print("Choose an edit option: ");
                                int editChoice; // Variable for edit choice.
                                try { editChoice = Integer.parseInt(scanner.nextLine()); } // Read choice.
                                catch (NumberFormatException e) { System.out.println("Invalid input."); continue; } // Handle non-numeric.

                                // Process the edit choice.
                                switch (editChoice) {
                                    case 1: // Change Store Name.
                                        System.out.print("Enter the new store name: ");
                                        String aNewStoreName = scanner.nextLine(); // Read new store name.
                                        // Call static renameStore method from GameStore.
                                        GameStore.renameStore(storeToEdit, aNewStoreName, currentOwner);
                                        // The owner's list is updated internally by renameStore.
                                        break;
                                    case 2: // Edit Game Price.
                                        System.out.print("Enter the game name to edit price: ");
                                        String gameToEditPrice = scanner.nextLine(); // Read game name.
                                        System.out.print("Enter the new price: ");
                                        double aNewPrice; // Variable for new price.
                                        try { aNewPrice = Double.parseDouble(scanner.nextLine()); // Read price.
                                            // Call static editGamePrice method from GameStore.
                                            GameStore.editGamePrice(storeToEdit, gameToEditPrice, aNewPrice);
                                        } catch (NumberFormatException e) { System.out.println("Invalid price."); }
                                        break;
                                    case 3: // Edit Game Genre.
                                        System.out.print("Enter the game name to edit genre: ");
                                        String gameToEditGenre = scanner.nextLine(); // Read game name.
                                        System.out.print("Enter the new genre: ");
                                        String aNewGenre = scanner.nextLine(); // Read new genre.
                                        // Call static editGameGenre method from GameStore.
                                        GameStore.editGameGenre(storeToEdit, gameToEditGenre, aNewGenre);
                                        break;
                                    case 4: // Remove Game.
                                        System.out.print("Enter the game name to remove: ");
                                        String gameToRemove = scanner.nextLine(); // Read game name.
                                        // Call static removeGame method from GameStore.
                                        GameStore.removeGame(storeToEdit, gameToRemove);
                                        break;
                                    case 5: // Back to previous menu.
                                        break; // Do nothing, will loop back.
                                    default: // Invalid edit option.
                                        System.out.println("Invalid edit option.");
                                }
                            } else {
                                // If owner doesn't own the store.
                                System.out.println("You do not own this store or it doesn't exist!");
                            }
                            break; // Break from case 4 of StoreOwner menu.
                        case 5: // Perform Admin Action.
                            currentUser.performAdminAction(); // Call polymorphic admin action method.
                            break;
                        case 6: // Logout.
                            currentUser = null; // Set current user to null.
                            System.out.println("Logged out.");
                            break;
                        default: // Invalid option.
                            System.out.println("Invalid option!");
                    }
                }
            }
        } // End of main while loop.
    } // End of main method.
} // End of GameStore class.
