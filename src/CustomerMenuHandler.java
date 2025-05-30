import java.util.Scanner;

public class CustomerMenuHandler {
    private Customer customer;
    private Scanner scanner;
    private StoreService storeService;
    private UserManager userManager;

    public CustomerMenuHandler(Customer customer, Scanner scanner, UserManager userManager, StoreService storeService) {
        this.customer = customer;
        this.scanner = scanner;
        this.userManager = userManager;
        this.storeService = storeService;
    }

    public boolean processMenu() {
        displayMenuOptions();
        int choice = getUserChoice();

        switch (choice) {
            case 1:
                GameStore.viewAllStoresAndGames(storeService);
                break;
            case 2:
                handleBuyGame();
                break;
            case 3:
                handleTopUpBalance();
                break;
            case 4:
                customer.viewMyGames();
                break;
            case 5:
                customer.performAdminAction(scanner, userManager, storeService);
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

    private void displayMenuOptions() {
        System.out.println("Balance: $" + String.format("%.2f", customer.getBalance()));
        System.out.println("1. View Stores and Games");
        System.out.println("2. Buy Game");
        System.out.println("3. Top Up Balance");
        System.out.println("4. View My Games");
        System.out.println("5. Perform Admin Action (if applicable)");
        System.out.println("6. Logout");
        System.out.print("Choose an option: ");
    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1; 
        }
    }

    private void handleBuyGame() {
        System.out.print("Enter store name: ");
        String storeNameStr = scanner.nextLine();
        StoreName storeName;
        try {
            storeName = new StoreName(storeNameStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid store name: " + e.getMessage());
            return;
        }

        System.out.print("Enter game name: ");
        String gameNameStr = scanner.nextLine();
        GameName gameName;
        try {
            gameName = new GameName(gameNameStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid game name: " + e.getMessage());
            return;
        }
        customer.buyGame(storeName, gameName, storeService);
    }

    private void handleTopUpBalance() {
        System.out.print("Enter amount to top up: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
             customer.topUp(amount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
        }
    }
}
