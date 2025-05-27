// StoreOwner.java
// This class represents a Store Owner user, inheriting from User.
// Store Owners can create and manage their stores and the games within them.
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner; // For performAdminAction

public class StoreOwner extends User {
    // This list stores the names of the stores owned by this StoreOwner.
    // It is public, which is generally not good practice for encapsulation.
    public List<String> myStores;

    // Constructor for the StoreOwner class.
    // Initializes a new StoreOwner with a username and password,
    // and sets the role to "store_owner".
    public StoreOwner(String username, String password) {
        // Call the constructor of the superclass (User).
        super(username, password, "store_owner");
        // Initialize the list of owned stores as a new empty ArrayList.
        this.myStores = new ArrayList<>();
    }

    // Method for a StoreOwner to create a new store.
    // Takes the desired store name and a StoreService instance as parameters.
    public void createStore(String storeName, StoreService storeService) {
        // Check if a store with the given name already exists using the StoreService.
        if (storeService.doesStoreExist(storeName)) {
            // If the store name exists, print an error message.
            System.out.println("Store name already exists!");
        } else {
            // If the store name is unique, add the new store via the StoreService.
            storeService.addStore(storeName, this.getUsername());
            // Add the name of the newly created store to this owner's list of stores.
            this.myStores.add(storeName);
            // Print a success message.
            System.out.println("Store '" + storeName + "' created successfully!");
        }
    }

    // Method for a StoreOwner to add a new game to one of their stores.
    // Takes store name, game name, price, genre, and StoreService instance as parameters.
    public void addGameToStore(String storeName, String gameName, double price, String genre, StoreService storeService) {
        // Check if this StoreOwner actually owns the specified store.
        if (this.myStores.contains(storeName)) {
            // If they own the store, create a new Game object.
            Game newGame = new Game(gameName, price, genre);
            // Add the new game to the store via the StoreService.
            storeService.addGameToStore(storeName, newGame);
            // Print a success message.
            System.out.println("Game '" + gameName + "' ("+ genre +") added to '" + storeName + "' for $" + price);
        } else {
            // If they don't own the store, or the store doesn't exist in their list, print an error message.
            System.out.println("You do not own this store or it doesn't exist!");
        }
    }

    // Method to update a store's name in this StoreOwner's internal list of owned stores.
    // This is typically called when a store is renamed in the main system.
    // Takes the old store name and the new store name as parameters.
    public void updateStoreNameInList(String oldName, String newName) {
        // Check if the owner's list contains the old store name.
        if (myStores.contains(oldName)) {
            // If it does, remove the old name from the list.
            myStores.remove(oldName);
            // Add the new name to the list.
            myStores.add(newName);
        }
    }

    // Overridden method from the User class.
    // Provides specific administrative actions for StoreOwners.
    // Now takes Scanner and service instances as parameters.
    @Override
    public void performAdminAction(Scanner scanner, UserManager userManager, StoreService storeService) {
        // Print a header for the admin panel.
        System.out.println("\n--- Store Owner Admin Panel for " + getUsername() + " ---");
        // Display the list of stores owned by this user.
        System.out.println("Your Stores: " + myStores);

        // Check if the store owner has any stores.
        if (!myStores.isEmpty()) {
            // Prompt the store owner to select a store for inspection or action.
            System.out.print("Enter a store name from your list to view its games (or type 'back'): ");
            String storeToInspect = scanner.nextLine();

            // If the owner types 'back', then return to the previous menu.
            if (storeToInspect.equalsIgnoreCase("back")) {
                return;
            }

            // Check if the entered store name is one of the owner's stores.
            if (myStores.contains(storeToInspect)) {
                // Print a sub-header for the games in the selected store.
                System.out.println("Games in your store '" + storeToInspect + "':");
                // Get the games from the store using the StoreService.
                Map<String, Game> games = storeService.getGamesInStore(storeToInspect);
                // Check if the store and its game map exist and are not empty.
                if (games != null && !games.isEmpty()) {
                    // Iterate through the games in the store and print their details.
                    for (Game game : games.values()) {
                        System.out.println(" - " + game.getName() + " ($" + game.getPrice() + ", " + game.getGenre() + ")");
                    }
                } else {
                    // Print a message if there are no games in this store.
                    System.out.println("No games in this store.");
                }
            } else {
                // Print a message if the entered store name is not owned by this user.
                System.out.println("You do not own the store named '" + storeToInspect + "'.");
            }
        } else {
            // Print a message if the store owner doesn't own any stores.
            System.out.println("You don't own any stores to perform admin actions on.");
        }
    }
}
