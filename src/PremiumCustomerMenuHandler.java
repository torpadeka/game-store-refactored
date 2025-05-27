// PremiumCustomerMenuHandler.java
// This class is responsible for handling menu interactions for a Premium Customer.
import java.util.Scanner;

public class PremiumCustomerMenuHandler {
    // The PremiumCustomer object.
    private PremiumCustomer premiumCustomer;
    // Scanner for user input.
    private Scanner scanner;
    // StoreService for store/game operations.
    private StoreService storeService;
    // UserManager instance, passed for consistency in performAdminAction.
    private UserManager userManager;

    // Constructor for PremiumCustomerMenuHandler.
    public PremiumCustomerMenuHandler(PremiumCustomer premiumCustomer, Scanner scanner, UserManager userManager, StoreService storeService) {
        this.premiumCustomer = premiumCustomer;
        this.scanner = scanner;
        this.userManager = userManager;
        this.storeService = storeService;
    }

    // Displays the premium customer menu and processes choices.
    // Returns true if logout is chosen, false otherwise.
    public boolean processMenu() {
        // Display current balance and discount rate.
        System.out.println("Balance: $" + String.format("%.2f", premiumCustomer.getBalance()) +
                           " (Discount: " + (premiumCustomer.getDiscountRate() * 100) + "%)");
        // Display menu options for PremiumCustomer.
        System.out.println("1. View Stores and Games");
        System.out.println("2. Buy Game (Premium Discount)");
        System.out.println("3. Top Up Balance");
        System.out.println("4. View My Games");
        System.out.println("5. Perform Admin Action (if applicable)");
        System.out.println("6. Logout");
        System.out.print("Choose an option: ");
        int choice; // Variable for menu choice.
        try {
            // Read choice.
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            // Handle non-numeric input.
            System.out.println("Invalid input. Please enter a number.");
            return false; // Not logging out.
        }

        // Process PremiumCustomer's choice.
        switch (choice) {
            case 1: // View Stores and Games.
                // Delegate to a common display method.
                GameStore.viewAllStoresAndGames(storeService);
                break;
            case 2: // Buy Game with premium discount.
                System.out.print("Enter store name: ");
                String storeName = scanner.nextLine(); // Read store name.
                System.out.print("Enter game name: ");
                String gameName = scanner.nextLine(); // Read game name.
                // Call buyGame method of PremiumCustomer.
                premiumCustomer.buyGame(storeName, gameName, storeService);
                break;
            case 3: // Top Up Balance.
                System.out.print("Enter amount to top up: ");
                double amount; // Variable for top-up amount.
                try {
                    amount = Double.parseDouble(scanner.nextLine()); // Read amount.
                    premiumCustomer.topUp(amount); // Call topUp method.
                } catch (NumberFormatException e) {
                    // Handle non-numeric.
                    System.out.println("Invalid amount.");
                }
                break;
            case 4: // View My Games.
                premiumCustomer.viewMyGames(); // Call method to view owned games.
                break;
            case 5: // Perform Admin Action.
                // Call performAdminAction on the premium customer object.
                premiumCustomer.performAdminAction(scanner, userManager, storeService);
                break;
            case 6: // Logout.
                System.out.println("Logged out.");
                return true; // Indicate logout.
            default: // Invalid option.
                System.out.println("Invalid option!");
                break;
        }
        // If not logging out, return false.
        return false;
    }
}
