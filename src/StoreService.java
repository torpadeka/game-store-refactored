// StoreService.java
// This class is responsible for managing store and game-related operations.
// It handles creation, modification, and retrieval of store and game data.
import java.util.HashMap;
import java.util.Map;

public class StoreService {
    // This map stores all the game stores. The key is the store name (String).
    // The value is another map, where the key is the game name (String) and the value is the Game object.
    private Map<String, Map<String, Game>> stores;
    // This map keeps track of which store is owned by which user.
    // The key is the store name (String), and the value is the owner's username (String).
    private Map<String, String> storeOwnership;

    // Constructor for the StoreService class.
    // Initializes the internal maps for stores and store ownership.
    public StoreService() {
        // Create a new HashMap to store the stores and their games.
        this.stores = new HashMap<>();
        // Create a new HashMap to store the ownership details of stores.
        this.storeOwnership = new HashMap<>();
    }

    // This method retrieves the map of games for a given store name.
    // It takes the storeName (String) as input.
    // It returns a Map where keys are game names (String) and values are Game objects.
    public Map<String, Game> getGamesInStore(String storeName) {
        // Access the 'stores' map and get the inner map for the specified storeName.
        return this.stores.get(storeName);
    }

    // This method retrieves a specific Game object from a specific store.
    // Takes storeName (String) and gameName (String) as input.
    // Returns the Game object if found, null otherwise.
    public Game getGameFromStore(String storeName, String gameName) {
        // Get the map of games for the given store.
        Map<String, Game> gamesInStore = this.stores.get(storeName);
        // If the store exists and contains games, try to get the specific game.
        if (gamesInStore != null) {
            return gamesInStore.get(gameName);
        }
        // Return null if the store or game is not found.
        return null;
    }


    // This method checks if a store with the given name exists.
    // It takes the storeName (String) as input.
    // Returns true if the store exists, false otherwise.
    public boolean doesStoreExist(String storeName) {
        // Check if the 'stores' map contains the given storeName as a key.
        return this.stores.containsKey(storeName);
    }

    // This method adds a new store to the system.
    // It takes the storeName (String) and the owner's username (String) as input.
    public void addStore(String storeName, String ownerUsername) {
        // Add a new entry to the 'stores' map for the new store.
        // The value is a new empty HashMap for the games in this store.
        this.stores.put(storeName, new HashMap<>());
        // Add an entry to the 'storeOwnership' map to link the store to its owner.
        this.storeOwnership.put(storeName, ownerUsername);
        // Log this action.
        TransactionLogger.logTransaction(ownerUsername, "CREATE_STORE", 0, storeName);
    }

    // This method adds a game to a specific store.
    // It takes the storeName (String) and the Game object as input.
    public void addGameToStore(String storeName, Game game) {
        // Check if the store exists in the 'stores' map.
        if (this.stores.containsKey(storeName)) {
            // If the store exists, get its map of games and add the new game.
            // The game's name is used as the key in the inner map.
            this.stores.get(storeName).put(game.getName(), game);
            // Log this action.
            TransactionLogger.logTransaction(this.storeOwnership.get(storeName), "ADD_GAME", game.getPrice(), game.getName() + " to " + storeName);
        } else {
            // If the store doesn't exist, print an error message.
            System.out.println("Error: Store " + storeName + " not found when trying to add game.");
        }
    }

    // This method renames an existing store.
    // It takes the oldName (String), newName (String), and the StoreOwner object as input.
    // Returns true if renaming was successful, false otherwise.
    public boolean renameStore(String oldName, String newName, StoreOwner owner) {
        // Check if the old store name actually exists.
        if (!this.stores.containsKey(oldName)) {
            // If not, print an error and return false.
            System.out.println("Error: Old store '" + oldName + "' not found.");
            return false;
        }
        // Check if the new store name is already taken.
        if (this.stores.containsKey(newName)) {
            // If so, print an error and return false.
            System.out.println("Error: New store name '" + newName + "' already exists.");
            return false;
        }
        // If checks pass, proceed with renaming.
        // Remove the store entry with the old name and get its games.
        Map<String, Game> games = this.stores.remove(oldName);
        // Remove the ownership entry for the old name and get the owner's username.
        String ownerUsername = this.storeOwnership.remove(oldName);
        // Add a new entry to 'stores' with the new name and the games from the old store.
        this.stores.put(newName, games);
        // Add a new entry to 'storeOwnership' with the new name and the owner.
        this.storeOwnership.put(newName, ownerUsername);

        // The StoreOwner object also needs to update its internal list of store names.
        // This maintains consistency between the StoreService and the StoreOwner's view.
        owner.updateStoreNameInList(oldName, newName);
        // Log this system event.
        TransactionLogger.logTransaction("SYSTEM", "RENAME_STORE", 0, oldName + " -> " + newName);
        // Return true indicating success.
        return true;
    }

    // This method edits the price of a game in a specific store.
    // Takes storeName (String), gameName (String), and newPrice (double) as input.
    public void editGamePrice(String storeName, String gameName, double newPrice) {
        // Check if the store exists and contains the specified game.
        if (this.stores.containsKey(storeName) && this.stores.get(storeName).containsKey(gameName)) {
            // If both exist, get the Game object.
            Game game = this.stores.get(storeName).get(gameName);
            // Set the new price for the game using its setter method.
            game.setPrice(newPrice);
            // Log this system event.
            TransactionLogger.logTransaction(this.storeOwnership.get(storeName), "EDIT_GAME_PRICE", newPrice, gameName + " in " + storeName);
            // Print a success message.
            System.out.println("Price updated successfully for " + gameName + ".");
        } else {
            // If store or game not found, print an error message.
            System.out.println("Store or game not found for price edit.");
        }
    }

    // This method edits the genre of a game in a specific store.
    // Takes storeName (String), gameName (String), and newGenre (String) as input.
     public void editGameGenre(String storeName, String gameName, String newGenre) {
        // Check if the store exists and if that store contains the specified game.
        if (this.stores.containsKey(storeName) && this.stores.get(storeName).containsKey(gameName)) {
            // If the store and game are found, retrieve the Game object.
            Game game = this.stores.get(storeName).get(gameName);
            // Call the setGenre method on the Game object to update its genre.
            game.setGenre(newGenre);
            // Log the transaction of editing a game's genre.
            TransactionLogger.logTransaction(this.storeOwnership.get(storeName), "EDIT_GAME_GENRE", 0, gameName + " in " + storeName + " to " + newGenre);
            // Print a confirmation message to the console.
            System.out.println("Genre updated successfully for " + gameName + ".");
        } else {
            // If the store or game is not found, print an error message to the console.
            System.out.println("Store or game not found for genre edit.");
        }
    }

    // This method removes a game from a specific store.
    // Takes storeName (String) and gameName (String) as input.
    public void removeGame(String storeName, String gameName) {
        // Check if the store exists and contains the specified game.
        if (this.stores.containsKey(storeName) && this.stores.get(storeName).containsKey(gameName)) {
            // If both exist, remove the game from the store's game map.
            this.stores.get(storeName).remove(gameName);
            // Log this system event.
            TransactionLogger.logTransaction(this.storeOwnership.get(storeName), "REMOVE_GAME", 0, gameName + " from " + storeName);
            // Print a success message.
            System.out.println("Game removed successfully.");
        } else {
            // If store or game not found, print an error message.
            System.out.println("Store or game not found for removal.");
        }
    }

    // Method to get all stores and their games, for display purposes.
    // Returns a copy or unmodifiable view if we want to protect the original map,
    // but for simplicity here, it returns the direct map.
    public Map<String, Map<String, Game>> getAllStores() {
        // Return the internal 'stores' map.
        return this.stores;
    }

    // Method to get the owner of a specific store.
    // Takes storeName (String) as input.
    // Returns the username of the owner (String).
    public String getStoreOwner(String storeName) {
        // Return the owner's username from the 'storeOwnership' map.
        return this.storeOwnership.get(storeName);
    }
}
