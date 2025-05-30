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
                currentUser = handleLoggedInState(currentUser, scanner, userManager, storeService);
            }
        }
    }

    private static User handleLoggedOutState(Scanner scanner, UserManager userManager) {
        displayLoggedOutMenu();
        int choice = getUserInputChoice(scanner);

        switch (choice) {
            case 1:
                handleRegistration(scanner, userManager);
                return null; // Stay logged out after registration
            case 2:
                return handleLogin(scanner, userManager);
            case 3:
                System.out.println("Exiting...");
                scanner.close();
                System.exit(0); // Terminate the application
            default:
                System.out.println("Invalid option!");
                return null;
        }
    }

    private static User handleLoggedInState(User currentUser, Scanner scanner, UserManager userManager, StoreService storeService) {
        System.out.println("\n--- Logged in as: " + currentUser.getUsername().getValue() + " (" + currentUser.getRole().getRoleName() + ") ---");
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

    private static int getUserInputChoice(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1; 
        }
    }

    private static void handleRegistration(Scanner scanner, UserManager userManager) {
        Username username = getUsernameInput(scanner);
        if (username == null) return;

        Password password = getPasswordInput(scanner);
        if (password == null) return;

        UserRole role = getUserRoleInput(scanner);
        if (role == null) return;

        registerUserWithRole(username, password, role, scanner, userManager);
    }

    private static Username getUsernameInput(Scanner scanner) {
        System.out.print("Enter username: ");
        String usernameStr = scanner.nextLine();
        try {
            return new Username(usernameStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    private static Password getPasswordInput(Scanner scanner) {
        System.out.print("Enter password: ");
        String passwordStr = scanner.nextLine();
        try {
            return new Password(passwordStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    private static UserRole getUserRoleInput(Scanner scanner) {
        System.out.print("Register as (customer/premium_customer/store_owner): ");
        String roleStr = scanner.nextLine();
        try {
            return UserRole.fromString(roleStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role: " + e.getMessage());
            return null;
        }
    }

    private static void registerUserWithRole(Username username, Password password, UserRole role, Scanner scanner, UserManager userManager) {
        if (role == UserRole.PREMIUM_CUSTOMER) {
             System.out.print("Enter discount rate (e.g., 0.1 for 10%): ");
             double rateValue;
             try {
                 rateValue = Double.parseDouble(scanner.nextLine());
                 DiscountRate rate = new DiscountRate(rateValue);
                 userManager.registerUser(username, password, role, rate);
             } catch (NumberFormatException e) {
                 System.out.println("Invalid rate, using default 10% for premium customer registration.");
                 userManager.registerUser(username, password, role, new DiscountRate(0.1)); 
             } catch (IllegalArgumentException e) {
                 System.out.println("Error with discount rate: " + e.getMessage() + ". Using default 10% for premium customer registration.");
                 userManager.registerUser(username, password, role, new DiscountRate(0.1)); 
             }
        } else {
            userManager.registerUser(username, password, role);
        }
    }

    private static User handleLogin(Scanner scanner, UserManager userManager) {
        Username loginUsername = getUsernameInput(scanner);
        if (loginUsername == null) return null;

        Password loginPassword = getPasswordInput(scanner);
        if (loginPassword == null) return null;

        return userManager.loginUser(loginUsername, loginPassword);
    }

    public static void viewAllStoresAndGames(StoreService storeService) {
        Map<StoreName, Map<GameName, Game>> allStores = storeService.getAllStores();
        if (allStores.isEmpty()) {
            System.out.println("No stores available yet.");
        } else {
            displayAllStoresAndGames(allStores, storeService);
        }
    }

    private static void displayAllStoresAndGames(Map<StoreName, Map<GameName, Game>> allStores, StoreService storeService) {
        for (Map.Entry<StoreName, Map<GameName, Game>> storeEntry : allStores.entrySet()) {
            System.out.println("\nStore: " + storeEntry.getKey().getValue() + " (Owner: " + storeService.getStoreOwner(storeEntry.getKey()).getValue() + ")");
            if (storeEntry.getValue().isEmpty()) {
                System.out.println("  - No games in this store yet.");
            } else {
                displayGamesInStore(storeEntry.getValue());
            }
        }
    }

    private static void displayGamesInStore(Map<GameName, Game> games) {
        for (Game game : games.values()) {
            System.out.println("  - " + game.getName().getValue() + " ($" + String.format("%.2f", game.getPrice().getValue()) + ") - " + game.getGenre().getValue());
        }
    }
}
