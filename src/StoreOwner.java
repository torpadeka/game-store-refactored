// StoreOwner.java
// This class represents a Store Owner user, inheriting from User.
// Store Owners can create and manage their stores and the games within them.
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner; // Not used in this version, but kept from previous.

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
    // Takes the desired store name and a GameStore instance as parameters.
    public void createStore(String storeName, GameStore gameStore) {
        // Check if a store with the given name already exists by accessing GameStore's static public map.
        if (GameStore.stores.containsKey(storeName)) {
            // If the store name exists, print an error message.
            System.out.println("Store name already exists!");
        } else {
            // If the store name is unique, add the new store via the GameStore instance.
            gameStore.addStore(storeName, this.getUsername());
            // Add the name of the newly created store to this owner's list of stores.
            this.myStores.add(storeName);
            // Print a success message.
            System.out.println("Store '" + storeName + "' created successfully!");
        }
    }

    // Method for a StoreOwner to add a new game to one of their stores.
    // Takes store name, game name, price, genre, and GameStore instance as parameters.
    public void addGame(String storeName, String gameName, double price, String genre, GameStore gameStore) {
        // Check if this StoreOwner actually owns the specified store.
        if (this.myStores.contains(storeName)) {
            // If they own the store, create a new Game object.
            Game newGame = new Game(gameName, price, genre);
            // Add the new game to the store via the GameStore instance.
            gameStore.addGameToStore(storeName, newGame);
            // Print a success message.
            System.out.println("Game '" + gameName + "' ("+ genre +") added to '" + storeName + "' for $" + price);
        } else {
            // If they don't own the store, or the store doesn't exist in their list, print an error message.
            System.out.println("You do not own this store or it doesn't exist!");
        }
    }

    // Method to update a store's name in this StoreOwner's internal list of owned stores.
    // This is typically called when a store is renamed in the main GameStore system.
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
    @Override
    public void performAdminAction() {
        // Print a header for the admin panel.
        System.out.println("\n--- Store Owner Admin Panel for " + getUsername() + " ---");
        // Display the list of stores owned by this user.
        System.out.println("Your Stores: " + myStores);
        // Example action: List all games in the first store owned by this user.
        if (!myStores.isEmpty()) {
            // Get the name of the first store in the list (for demonstration).
            String storeToInspect = myStores.get(0);
            // Print a sub-header for the games in the selected store.
            System.out.println("Games in your store '" + storeToInspect + "':");
            // Access the games directly from GameStore's public static 'stores' map.
            Map<String, Game> games = GameStore.stores.get(storeToInspect);
            // Check if the store and its game map exist and are not empty.
            if (games != null && !games.isEmpty()) {
                // Iterate through the games in the store and print their details.
                for (Game game : games.values()) {
                    System.out.println(" - " + game.getName() + " ($" + game.getPrice() + ", " + game.getGenre() + ")");
                }
            } else {
                // Print a message if there are no games or if data inconsistency is suspected.
                System.out.println("No games in this store or store not found in main map (data inconsistency?).");
            }
        } else {
            // Print a message if the store owner doesn't own any stores.
            System.out.println("You don't own any stores to perform admin actions on.");
        }
    }
}
