import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class StoreService {
    private Map<StoreName, Map<GameName, Game>> stores;
    private Map<StoreName, Username> storeOwnership;

    public StoreService() {
        this.stores = new HashMap<>();
        this.storeOwnership = new HashMap<>();
    }

    public Map<GameName, Game> getGamesInStore(StoreName storeName) {
        Map<GameName, Game> games = this.stores.get(storeName);
        return (games != null) ? games : Collections.emptyMap();
    }

    public Game getGameFromStore(StoreName storeName, GameName gameName) {
        Map<GameName, Game> gamesInStore = this.stores.get(storeName);
        if (gamesInStore != null) {
            return gamesInStore.get(gameName);
        }
        return null;
    }

    public boolean doesStoreExist(StoreName storeName) {
        return this.stores.containsKey(storeName);
    }

    public void addStore(StoreName storeName, Username ownerUsername) {
        this.stores.put(storeName, new HashMap<>());
        this.storeOwnership.put(storeName, ownerUsername);
        TransactionLogger.logTransaction(ownerUsername, "CREATE_STORE", new Price(0), storeName.getValue());
    }

    public void addGameToStore(StoreName storeName, Game game) {
        if (this.stores.containsKey(storeName)) {
            this.stores.get(storeName).put(game.getName(), game);
            TransactionLogger.logTransaction(this.storeOwnership.get(storeName), "ADD_GAME", game.getPrice(), game.getName().getValue() + " to " + storeName.getValue());
        } else {
            System.out.println("Error: Store " + storeName.getValue() + " not found when trying to add game.");
        }
    }

    public boolean renameStore(StoreName oldName, StoreName newName, StoreOwner owner) {
        if (!validateStoreRename(oldName, newName)) {
            return false;
        }
        performStoreRename(oldName, newName, owner);
        return true;
    }

    private boolean validateStoreRename(StoreName oldName, StoreName newName) {
        if (!this.stores.containsKey(oldName)) {
            System.out.println("Error: Old store '" + oldName.getValue() + "' not found.");
            return false;
        }
        if (this.stores.containsKey(newName)) {
            System.out.println("Error: New store name '" + newName.getValue() + "' already exists.");
            return false;
        }
        return true;
    }

    private void performStoreRename(StoreName oldName, StoreName newName, StoreOwner owner) {
        Map<GameName, Game> games = this.stores.remove(oldName);
        Username ownerUsername = this.storeOwnership.remove(oldName);
        this.stores.put(newName, games);
        this.storeOwnership.put(newName, ownerUsername);

        owner.updateStoreNameInList(oldName, newName);
        TransactionLogger.logTransaction(new Username("SYSTEM"), "RENAME_STORE", new Price(0), oldName.getValue() + " -> " + newName.getValue());
    }

    public void editGamePrice(StoreName storeName, GameName gameName, Price newPrice) {
        Game game = getGameForEdit(storeName, gameName);
        if (game != null) {
            game.setPrice(newPrice);
            TransactionLogger.logTransaction(this.storeOwnership.get(storeName), "EDIT_GAME_PRICE", newPrice, gameName.getValue() + " in " + storeName.getValue());
            System.out.println("Price updated successfully for " + gameName.getValue() + ".");
        } else {
            System.out.println("Store or game not found for price edit.");
        }
    }

     public void editGameGenre(StoreName storeName, GameName gameName, Genre newGenre) {
        Game game = getGameForEdit(storeName, gameName);
        if (game != null) {
            game.setGenre(newGenre);
            TransactionLogger.logTransaction(this.storeOwnership.get(storeName), "EDIT_GAME_GENRE", new Price(0), gameName.getValue() + " in " + storeName.getValue() + " to " + newGenre.getValue());
            System.out.println("Genre updated successfully for " + gameName.getValue() + ".");
        } else {
            System.out.println("Store or game not found for genre edit.");
        }
    }

    public void removeGame(StoreName storeName, GameName gameName) {
        Map<GameName, Game> gamesInStore = this.stores.get(storeName);
        if (gamesInStore != null && gamesInStore.containsKey(gameName)) {
            gamesInStore.remove(gameName);
            TransactionLogger.logTransaction(this.storeOwnership.get(storeName), "REMOVE_GAME", new Price(0), gameName.getValue() + " from " + storeName.getValue());
            System.out.println("Game removed successfully.");
        } else {
            System.out.println("Store or game not found for removal.");
        }
    }

    private Game getGameForEdit(StoreName storeName, GameName gameName) {
        Map<GameName, Game> gamesInStore = this.stores.get(storeName);
        if (gamesInStore != null && gamesInStore.containsKey(gameName)) {
            return gamesInStore.get(gameName);
        }
        return null;
    }

    public Map<StoreName, Map<GameName, Game>> getAllStores() {
        return this.stores;
    }

    public Username getStoreOwner(StoreName storeName) {
        return this.storeOwnership.get(storeName);
    }
}
