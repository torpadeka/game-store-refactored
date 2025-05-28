
import java.util.Scanner;

public class StoreOwnerMenuHandler {
    private StoreOwner storeOwner;
    private Scanner scanner;
    private UserManager userManager;
    private StoreService storeService;

    public StoreOwnerMenuHandler(StoreOwner storeOwner, Scanner scanner, UserManager userManager, StoreService storeService) {
        this.storeOwner = storeOwner;
        this.scanner = scanner;
        this.userManager = userManager;
        this.storeService = storeService;
    }

    public boolean processMenu() {
        System.out.println("1. Create Store");
        System.out.println("2. Add Game to Store");
        System.out.println("3. View My Stores");
        System.out.println("4. Edit Store");
        System.out.println("5. Perform Admin Action");
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
                System.out.print("Enter new store name: ");
                String newStoreName = scanner.nextLine();
                storeOwner.createStore(newStoreName, storeService);
                break;
            case 2:
                handleAddGameToStore();
                break;
            case 3:
                System.out.println("Your Stores:");
                if (storeOwner.myStores.isEmpty()) {
                    System.out.println("  - You don't own any stores yet.");
                } else {
                    for (String sName : storeOwner.myStores) {
                        System.out.println("  - " + sName);
                    }
                }
                break;
            case 4:
                handleEditStoreSubMenu();
                break;
            case 5:
                storeOwner.performAdminAction(scanner, userManager, storeService);
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

    private void handleAddGameToStore() {
        System.out.print("Enter store name to add game to: ");
        String storeToAddGame = scanner.nextLine();
        if (!storeOwner.myStores.contains(storeToAddGame)) {
            System.out.println("You don't own this store or it doesn't exist in your list.");
            return;
        }
        System.out.print("Enter game name: ");
        String newGameName = scanner.nextLine();
        System.out.print("Enter game price: ");
        double newGamePrice = 0;
        try {
            newGamePrice = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price. Game not added.");
            return;
        }
        System.out.print("Enter game genre: ");
        String newGameGenre = scanner.nextLine();
        storeOwner.addGameToStore(storeToAddGame, newGameName, newGamePrice, newGameGenre, storeService);
    }

    private void handleEditStoreSubMenu() {
        System.out.print("Enter the name of the store you want to edit: ");
        String storeToEdit = scanner.nextLine();
        if (storeOwner.myStores.contains(storeToEdit)) {
            System.out.println("\nEditing Store: " + storeToEdit);
            System.out.println("1. Change Store Name");
            System.out.println("2. Edit Game Price");
            System.out.println("3. Edit Game Genre");
            System.out.println("4. Remove Game");
            System.out.println("5. Back");
            System.out.print("Choose an edit option: ");
            int editChoice;
            try {
                editChoice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                return;
            }

            switch (editChoice) {
                case 1:
                    System.out.print("Enter the new store name: ");
                    String aNewStoreName = scanner.nextLine();
                    storeService.renameStore(storeToEdit, aNewStoreName, storeOwner);
                    break;
                case 2:
                    System.out.print("Enter the game name to edit price: ");
                    String gameToEditPrice = scanner.nextLine();
                    System.out.print("Enter the new price: ");
                    double aNewPrice;
                    try {
                        aNewPrice = Double.parseDouble(scanner.nextLine());
                        storeService.editGamePrice(storeToEdit, gameToEditPrice, aNewPrice);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid price.");
                    }
                    break;
                case 3:
                    System.out.print("Enter the game name to edit genre: ");
                    String gameToEditGenre = scanner.nextLine();
                    System.out.print("Enter the new genre: ");
                    String aNewGenre = scanner.nextLine();
                    storeService.editGameGenre(storeToEdit, gameToEditGenre, aNewGenre);
                    break;
                case 4:
                    System.out.print("Enter the game name to remove: ");
                    String gameToRemove = scanner.nextLine();
                    storeService.removeGame(storeToEdit, gameToRemove);
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Invalid edit option.");
            }
        } else {
            System.out.println("You do not own this store or it doesn't exist!");
        }
    }
}
