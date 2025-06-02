
import java.util.Scanner;

public class CustomerMenuHandler {
    private Customer customer;
    private Scanner scanner;
    private UserManager userManager;
    private StoreService storeService;

    public CustomerMenuHandler(Customer customer, Scanner scanner, UserManager userManager, StoreService storeService) {
        this.customer = customer;
        this.scanner = scanner;
        this.userManager = userManager;
        this.storeService = storeService;
    }

    public boolean processMenu() {
        System.out.println("Balance: $" + String.format("%.2f", customer.getBalance()));
        System.out.println("1. View Stores and Games");
        System.out.println("2. Buy Game");
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
                handleBuyGame();
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

    private void handleBuyGame() {
        System.out.print("Enter store name: ");
        String storeName = scanner.nextLine();
        System.out.print("Enter game name: ");
        String gameName = scanner.nextLine();
        customer.buyGame(storeName, gameName, storeService);
    }

    private void handleTopUpBalance() {
        System.out.print("Enter amount to top up: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            customer.topUp(amount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
        }
    }

    private void handleViewMyGames() {
        customer.viewMyGames();
    }

    private void handlePerformAdminAction() {
        customer.performAdminAction(scanner, userManager, storeService);
    }
}
