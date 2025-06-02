import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GameStore {
	
	private static final Map<Integer, MenuAction> loggedOutActions = new HashMap<>();
    static {
    	loggedOutActions.put(1, new RegisterAction());
    	loggedOutActions.put(2, new LoginAction());
    	loggedOutActions.put(3, new ExitUser());
    }
	
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        User currentUser = null;

        UserManager userManager = new UserManager();
        StoreService storeService = new StoreService();

        while (true) {
            System.out.println("\n--- Welcome to the Game Store App ---");
            if (currentUser == null) {
                currentUser = handleLoggedOutState(scanner, userManager);
            } else {
                currentUser = handleLoggedInState(scanner, userManager, storeService, currentUser);
            }
        }
    }

	private static User handleLoggedInState(Scanner scanner, UserManager userManager, StoreService storeService, User currentUser) {
        System.out.println("\n--- Logged in as: " + currentUser.getUsername() + " (" + currentUser.getRole() + ") ---");
        boolean loggedOut = currentUser.handleMenu(scanner, userManager, storeService);
        return loggedOut ? null : currentUser;
    }
	
    private static User handleLoggedOutState(Scanner scanner, UserManager userManager) {
        displayLoggedOutMenu();
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return null; 
        }
        
        MenuAction action = loggedOutActions.getOrDefault(choice, new InvalidAction());
		return action.execute(scanner, userManager);
    }

    private static void displayLoggedOutMenu() {
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
    }

    public static void viewAllStoresAndGames(StoreService storeService) {
        Map<String, Map<String, Game>> allStores = storeService.getAllStores();
        if (allStores.isEmpty()) {
            System.out.println("No stores available yet.");
        } else {
            for (Map.Entry<String, Map<String, Game>> storeEntry : allStores.entrySet()) {
                System.out.println("\nStore: " + storeEntry.getKey() + " (Owner: " + storeService.getStoreOwner(storeEntry.getKey()) + ")");
                if (storeEntry.getValue().isEmpty()) {
                    System.out.println("  - No games in this store yet.");
                } else {
                    for (Game game : storeEntry.getValue().values()) {
                        System.out.println("  - " + game.getName() + " ($" + String.format("%.2f", game.getPrice()) + ") - " + game.getGenre());
                    }
                }
            }
        }
    }
}
