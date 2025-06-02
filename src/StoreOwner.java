
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class StoreOwner extends User{
    public List<String> myStores;

    public StoreOwner(String username, String password) {
        super(username, password, "store_owner");
        this.myStores = new ArrayList<>();
    }

    public void createStore(String storeName, StoreService storeService) {
        if (storeService.doesStoreExist(storeName)) {
            System.out.println("Store name already exists!");
        } else {
            storeService.addStore(storeName, this.getUsername());
            this.myStores.add(storeName);
            System.out.println("Store '" + storeName + "' created successfully!");
        }
    }

    public void addGameToStore(String storeName, String gameName, double price, String genre, StoreService storeService) {
        if (this.myStores.contains(storeName)) {
            Game newGame = new Game(gameName, price, genre);
            storeService.addGameToStore(storeName, newGame);
            System.out.println("Game '" + gameName + "' ("+ genre +") added to '" + storeName + "' for $" + price);
        } else {
            System.out.println("You do not own this store or it doesn't exist!");
        }
    }

    public void updateStoreNameInList(String oldName, String newName) {
        if (myStores.contains(oldName)) {
            myStores.remove(oldName);
            myStores.add(newName);
        }
    }

    @Override
    public void performAdminAction(Scanner scanner, UserManager userManager, StoreService storeService) {
        System.out.println("\n--- Store Owner Admin Panel for " + getUsername() + " ---");
        System.out.println("Your Stores: " + myStores);

        if (myStores.isEmpty()) {
            System.out.println("You don't own any stores to perform admin actions on.");
            return;
        }

        String storeToInspect = promptForStoreToInspect(scanner);
        if (storeToInspect.equalsIgnoreCase("back")) {
            return;
        }

        if (isValidStoreForInspection(storeToInspect)) {
            displayGamesInStore(storeToInspect, storeService);
        } else {
            System.out.println("You do not own the store named '" + storeToInspect + "'.");
        }
    }

    private String promptForStoreToInspect(Scanner scanner) {
        System.out.print("Enter a store name from your list to view its games (or type 'back'): ");
        return scanner.nextLine();
    }

    private boolean isValidStoreForInspection(String storeName) {
        return myStores.contains(storeName);
    }

    private void displayGamesInStore(String storeName, StoreService storeService) {
        System.out.println("Games in your store '" + storeName + "':");
        Map<String, Game> games = storeService.getGamesInStore(storeName);
        if (games != null && !games.isEmpty()) {
            for (Game game : games.values()) {
                System.out.println(" - " + game.getName() + " ($" + game.getPrice() + ", " + game.getGenre() + ")");
            }
        } else {
            System.out.println("No games in this store.");
        }
    }

	@Override
	public boolean handleMenu(Scanner scanner, UserManager userManager, StoreService storeService) {
		return new StoreOwnerMenuHandler(this, scanner, userManager, storeService).processMenu();
	}
}
