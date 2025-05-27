// CustomerMenuHandler.java
// This class is responsible for handling the menu interactions for a regular Customer user.
import java.util.Scanner;

public class CustomerMenuHandler {
    // The Customer object for whom this menu is being handled.
    private Customer customer;
    // Scanner object for reading user input.
    private Scanner scanner;
    // StoreService instance for accessing store and game data.
    private StoreService storeService;
    // UserManager instance, passed for consistency in performAdminAction, though not used by Customer.
    private UserManager userManager;


    // Constructor for CustomerMenuHandler.
    // Initializes the handler with the current customer, scanner, and necessary services.
    public CustomerMenuHandler(Customer customer, Scanner scanner, UserManager userManager, StoreService storeService) {
        this.customer = customer;
        this.scanner = scanner;
        this.userManager = userManager; // Stored for performAdminAction
        this.storeService = storeService;
    }

    // This method displays the customer menu and processes user choices.
    // Returns true if the user chose to logout, false otherwise.
    public boolean processMenu() {
        // Display current balance.
        System.out.println("Balance: $" + String.format("%.2f", customer.getBalance()));
        // Display menu options for Customer.
        System.out.println("1. View Stores and Games");
        System.out.println("2. Buy Game");
        System.out.println("3. Top Up Balance");
        System.out.println("4. View My Games");
        System.out.println("5. Perform Admin Action (if applicable)");
        System.out.println("6. Logout");
        System.out.print("Choose an option: ");
        int choice; // Variable for menu choice.
        try {
            // Read the user's choice from the console and parse it as an integer.
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            // If the input is not a valid number, print an error and indicate no logout.
            System.out.println("Invalid input. Please enter a number.");
            return false; // Not logging out, stay in menu.
        }

        // Process Customer's choice.
        switch (choice) {
            case 1: // View Stores and Games.
                // Delegate to a common method (now in GameStore or a new UIManager/DisplayManager).
                // For now, let's assume GameStore has a static helper for this.
                GameStore.viewAllStoresAndGames(storeService);
                break;
            case 2: // Buy Game.
                System.out.print("Enter store name: ");
                String storeName = scanner.nextLine(); // Read store name.
                System.out.print("Enter game name: ");
                String gameName = scanner.nextLine(); // Read game name.
                // Call buyGame method of Customer.
                customer.buyGame(storeName, gameName, storeService);
                break;
            case 3: // Top Up Balance.
                System.out.print("Enter amount to top up: ");
                double amount; // Variable for top-up amount.
                try {
                    amount = Double.parseDouble(scanner.nextLine()); // Read amount.
                     customer.topUp(amount); // Call topUp method.
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount."); // Handle non-numeric.
                }
                break;
            case 4: // View My Games.
                customer.viewMyGames(); // Call method to view owned games.
                break;
            case 5: // Perform Admin Action.
                // Call performAdminAction on the customer object.
                customer.performAdminAction(scanner, userManager, storeService);
                break;
            case 6: // Logout.
                System.out.println("Logged out.");
                return true; // Indicate that logout was chosen.
            default: // Invalid option.
                System.out.println("Invalid option!");
                break;
        }
        // If not logging out, return false.
        return false;
    }
}
