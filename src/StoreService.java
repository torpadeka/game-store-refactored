
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class StoreService {
    private Map<String, Map<String, Game>> stores;
    private Map<String, String> storeOwnership;

    public StoreService() {
        this.stores = new HashMap<>();
        this.storeOwnership = new HashMap<>();
    }

    public Map<String, Game> getGamesInStore(String storeName) {
        Map<String, Game> games = this.stores.get(storeName);
        return (games != null) ? games : Collections.emptyMap();
    }

    public Game getGameFromStore(String storeName, String gameName) {
        Map<String, Game> gamesInStore = this.stores.get(storeName);
        if (gamesInStore != null) {
            return gamesInStore.get(gameName);
        }
        return null;
    }

    public boolean doesStoreExist(String storeName) {
        return this.stores.containsKey(storeName);
    }

    public void addStore(String storeName, String ownerUsername) {
        this.stores.put(storeName, new HashMap<>());
        this.storeOwnership.put(storeName, ownerUsername);
        TransactionLogger.logTransaction(ownerUsername, "CREATE_STORE", 0, storeName);
    }

    public void addGameToStore(String storeName, Game game) {
        if (this.stores.containsKey(storeName)) {
            this.stores.get(storeName).put(game.getName(), game);
            TransactionLogger.logTransaction(this.storeOwnership.get(storeName), "ADD_GAME", game.getPrice(), game.getName() + " to " + storeName);
        } else {
            System.out.println("Error: Store " + storeName + " not found when trying to add game.");
        }
    }

    public boolean renameStore(String oldName, String newName, StoreOwner owner) {
        if (!this.stores.containsKey(oldName)) {
            System.out.println("Error: Old store '" + oldName + "' not found.");
            return false;
        }
        if (this.stores.containsKey(newName)) {
            System.out.println("Error: New store name '" + newName + "' already exists.");
            return false;
        }
        Map<String, Game> games = this.stores.remove(oldName);
        String ownerUsername = this.storeOwnership.remove(oldName);
        this.stores.put(newName, games);
        this.storeOwnership.put(newName, ownerUsername);

        owner.updateStoreNameInList(oldName, newName);
        TransactionLogger.logTransaction("SYSTEM", "RENAME_STORE", 0, oldName + " -> " + newName);
        return true;
    }

    public void editGamePrice(String storeName, String gameName, double newPrice) {
        Map<String, Game> gamesInStore = this.stores.get(storeName);
        if (gamesInStore != null && gamesInStore.containsKey(gameName)) {
            Game game = gamesInStore.get(gameName);
            game.setPrice(newPrice);
            TransactionLogger.logTransaction(this.storeOwnership.get(storeName), "EDIT_GAME_PRICE", newPrice, gameName + " in " + storeName);
            System.out.println("Price updated successfully for " + gameName + ".");
        } else {
            System.out.println("Store or game not found for price edit.");
        }
    }

     public void editGameGenre(String storeName, String gameName, String newGenre) {
        Map<String, Game> gamesInStore = this.stores.get(storeName);
        if (gamesInStore != null && gamesInStore.containsKey(gameName)) {
            Game game = gamesInStore.get(gameName);
            game.setGenre(newGenre);
            TransactionLogger.logTransaction(this.storeOwnership.get(storeName), "EDIT_GAME_GENRE", 0, gameName + " in " + storeName + " to " + newGenre);
            System.out.println("Genre updated successfully for " + gameName + ".");
        } else {
            System.out.println("Store or game not found for genre edit.");
        }
    }

    public void removeGame(String storeName, String gameName) {
        Map<String, Game> gamesInStore = this.stores.get(storeName);
        if (gamesInStore != null && gamesInStore.containsKey(gameName)) {
            gamesInStore.remove(gameName);
            TransactionLogger.logTransaction(this.storeOwnership.get(storeName), "REMOVE_GAME", 0, gameName + " from " + storeName);
            System.out.println("Game removed successfully.");
        } else {
            System.out.println("Store or game not found for removal.");
        }
    }

    public Map<String, Map<String, Game>> getAllStores() {
        return this.stores;
    }

    public String getStoreOwner(String storeName) {
        return this.storeOwnership.get(storeName);
    }
}
