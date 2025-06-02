import java.util.Map;
import java.util.Scanner;

public class GameStore {

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

    private static User handleLoggedOutState(Scanner scanner, UserManager userManager) {
        displayLoggedOutMenu();
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return null; 
        }

        switch (choice) {
            case 1:
                handleRegistration(scanner, userManager);
                return null; 
            case 2:
                return handleLogin(scanner, userManager);
            case 3:
                System.out.println("Exiting...");
                scanner.close();
                System.exit(0); 
            default:
                System.out.println("Invalid option!");
                return null; 
        }
    }

    private static User handleLoggedInState(Scanner scanner, UserManager userManager, StoreService storeService, User currentUser) {
        System.out.println("\n--- Logged in as: " + currentUser.getUsername() + " (" + currentUser.getRole() + ") ---");
        boolean loggedOut = false;

        if (currentUser instanceof PremiumCustomer) {
            PremiumCustomerMenuHandler menuHandler = new PremiumCustomerMenuHandler((PremiumCustomer) currentUser, scanner, userManager, storeService);
            loggedOut = menuHandler.processMenu();
        } else if (currentUser instanceof Customer) {
            CustomerMenuHandler menuHandler = new CustomerMenuHandler((Customer) currentUser, scanner, userManager, storeService);
            loggedOut = menuHandler.processMenu();
        } else if (currentUser instanceof StoreOwner) {
            StoreOwnerMenuHandler menuHandler = new StoreOwnerMenuHandler((StoreOwner) currentUser, scanner, userManager, storeService);
            loggedOut = menuHandler.processMenu();
        }

        return loggedOut ? null : currentUser;
    }

    private static void displayLoggedOutMenu() {
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
    }

    private static void handleRegistration(Scanner scanner, UserManager userManager) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Register as (customer/premium_customer/store_owner): ");
        String role = scanner.nextLine();

        if (role.equalsIgnoreCase("premium_customer")) {
            handlePremiumCustomerRegistration(scanner, userManager, username, password);
        } else {
            userManager.registerUser(username, password, role);
        }
    }

    private static void handlePremiumCustomerRegistration(Scanner scanner, UserManager userManager, String username, String password) {
        System.out.print("Enter discount rate (e.g., 0.1 for 10%): ");
        double rate = 0.1;
        try {
            rate = Double.parseDouble(scanner.nextLine());
            userManager.registerUser(username, password, "premium_customer", rate);
        } catch (NumberFormatException e) {
            System.out.println("Invalid rate, using default 10% for premium customer registration.");
            userManager.registerUser(username, password, "premium_customer", 0.1); // Fallback
        }
    }

    private static User handleLogin(Scanner scanner, UserManager userManager) {
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();
        return userManager.loginUser(loginUsername, loginPassword);
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
