
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
        return executeMenuChoice(choice);
    }

    private boolean executeMenuChoice(int choice) {
        switch (choice) {
            case 1:
                handleViewStoresAndGames();
                break;
            case 2:
                handleBuyGamePremium();
                break;
            case 3:
                handleTopUpBalance();
                break;
            case 4:
                handleViewMyGames();
                break;
            case 5:
                handlePerformAdminAction();
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

    private void handleViewStoresAndGames() {
        GameStore.viewAllStoresAndGames(storeService);
    }

    private void handleBuyGamePremium() {
        System.out.print("Enter store name: ");
        String storeName = scanner.nextLine();
        System.out.print("Enter game name: ");
        String gameName = scanner.nextLine();
        premiumCustomer.buyGame(storeName, gameName, storeService);
    }

    private void handleTopUpBalance() {
        System.out.print("Enter amount to top up: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            premiumCustomer.topUp(amount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
        }
    }

    private void handleViewMyGames() {
        premiumCustomer.viewMyGames();
    }

    private void handlePerformAdminAction() {
        premiumCustomer.performAdminAction(scanner, userManager, storeService);
    }
}
