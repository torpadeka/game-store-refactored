
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

        switch (choice) {
            case 1:
                GameStore.viewAllStoresAndGames(storeService);
                break;
            case 2:
                System.out.print("Enter store name: ");
                String storeName = scanner.nextLine();
                System.out.print("Enter game name: ");
                String gameName = scanner.nextLine();
                customer.buyGame(storeName, gameName, storeService);
                break;
            case 3:
                System.out.print("Enter amount to top up: ");
                double amount;
                try {
                    amount = Double.parseDouble(scanner.nextLine());
                     customer.topUp(amount);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount.");
                }
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
}
