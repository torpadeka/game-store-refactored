import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class StoreOwner extends User {
    public List<StoreName> myStores;

    public StoreOwner(Username username, Password password) {
        super(username, password, UserRole.STORE_OWNER);
        this.myStores = new ArrayList<>();
    }

    public void createStore(StoreName storeName, StoreService storeService) {
        if (storeService.doesStoreExist(storeName)) {
            System.out.println("Store name already exists!");
        } else {
            storeService.addStore(storeName, this.getUsername());
            this.myStores.add(storeName);
            System.out.println("Store '" + storeName.getValue() + "' created successfully!");
        }
    }

    public void addGameToStore(StoreName storeName, GameName gameName, Price price, Genre genre, StoreService storeService) {
        if (this.myStores.contains(storeName)) {
            Game newGame = new Game(gameName, price, genre);
            storeService.addGameToStore(storeName, newGame);
            System.out.println("Game '" + gameName.getValue() + "' ("+ genre.getValue() +") added to '" + storeName.getValue() + "' for $" + price.getValue());
        } else {
            System.out.println("You do not own this store or it doesn't exist!");
        }
    }

    public void updateStoreNameInList(StoreName oldName, StoreName newName) {
        if (myStores.contains(oldName)) {
            myStores.remove(oldName);
            myStores.add(newName);
        }
    }

    @Override
    public void performAdminAction(Scanner scanner, UserManager userManager, StoreService storeService) {
        displayAdminPanelHeader();

        if (myStores.isEmpty()) {
            System.out.println("You don't own any stores to perform admin actions on.");
            return;
        }

        StoreName storeToInspect = promptForStoreToInspect(scanner);
        if (storeToInspect == null) {
            return; 
        }

        displayGamesInStore(storeToInspect, storeService);
    }

    private void displayAdminPanelHeader() {
        System.out.println("\n--- Store Owner Admin Panel for " + getUsername().getValue() + " ---");
        System.out.println("Your Stores: " + myStores);
    }

    private StoreName promptForStoreToInspect(Scanner scanner) {
        System.out.print("Enter a store name from your list to view its games (or type 'back'): ");
        String storeToInspectStr = scanner.nextLine();

        if (storeToInspectStr.equalsIgnoreCase("back")) {
            return null;
        }

        try {
            StoreName storeToInspect = new StoreName(storeToInspectStr);
            if (myStores.contains(storeToInspect)) {
                return storeToInspect;
            } else {
                System.out.println("You do not own the store named '" + storeToInspect.getValue() + "'.");
                return null;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid store name: " + e.getMessage());
            return null;
        }
    }

    private void displayGamesInStore(StoreName storeToInspect, StoreService storeService) {
        System.out.println("Games in your store '" + storeToInspect.getValue() + "':");
        Map<GameName, Game> games = storeService.getGamesInStore(storeToInspect);
        if (games != null && !games.isEmpty()) {
            for (Game game : games.values()) {
                System.out.println(" - " + game.getName().getValue() + " ($" + game.getPrice().getValue() + ", " + game.getGenre().getValue() + ")");
            }
        } else {
            System.out.println("No games in this store.");
        }
    }
}
