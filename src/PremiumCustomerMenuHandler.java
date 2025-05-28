
import java.util.Scanner;

public class PremiumCustomerMenuHandler {
    private PremiumCustomer premiumCustomer;
    private Scanner scanner;
    private StoreService storeService;
    private UserManager userManager;

    public PremiumCustomerMenuHandler(PremiumCustomer premiumCustomer, Scanner scanner, UserManager userManager, StoreService storeService) {
        this.premiumCustomer = premiumCustomer;
        this.scanner = scanner;
        this.userManager = userManager;
        this.storeService = storeService;
    }

    public boolean processMenu() {
        System.out.println("Balance: $" + String.format("%.2f", premiumCustomer.getBalance()) +
                           " (Discount: " + (premiumCustomer.getDiscountRate() * 100) + "%)");
        System.out.println("1. View Stores and Games");
        System.out.println("2. Buy Game (Premium Discount)");
        System.out.println("3. Top Up Balance");
        System.out.println("4. View My Games");
        System.out.println("5. Perform Admin Action (if applicable)");
        System.out.println("6. Logout");
        System.out.print("Choose an option: ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return false;
        }

        switch (choice) {
            case 1:
                GameStore.viewAllStoresAndGames(storeService);
                break;
            case 2:
                System.out.print("Enter store name: ");
                String storeName = scanner.nextLine();
                System.out.print("Enter game name: ");
                String gameName = scanner.nextLine();
                premiumCustomer.buyGame(storeName, gameName, storeService);
                break;
            case 3:
                System.out.print("Enter amount to top up: ");
                double amount;
                try {
                    amount = Double.parseDouble(scanner.nextLine());
                    premiumCustomer.topUp(amount);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount.");
                }
                break;
            case 4:
                premiumCustomer.viewMyGames();
                break;
            case 5:
                premiumCustomer.performAdminAction(scanner, userManager, storeService);
                break;
            case 6:
                System.out.println("Logged out.");
                return true;
            default:
                System.out.println("Invalid option!");
                break;
        }
        return false;
    }
}
