// StoreOwnerMenuHandler.java
// This class handles menu interactions for StoreOwner users.
import java.util.Scanner;

public class StoreOwnerMenuHandler {
    // The StoreOwner object.
    private StoreOwner storeOwner;
    // Scanner for user input.
    private Scanner scanner;
    // UserManager for user-related operations if needed by admin actions.
    private UserManager userManager;
    // StoreService for store and game operations.
    private StoreService storeService;

    // Constructor for StoreOwnerMenuHandler.
    public StoreOwnerMenuHandler(StoreOwner storeOwner, Scanner scanner, UserManager userManager, StoreService storeService) {
        this.storeOwner = storeOwner;
        this.scanner = scanner;
        this.userManager = userManager;
        this.storeService = storeService;
    }

    // Displays the store owner menu and processes choices.
    // Returns true if logout is chosen, false otherwise.
    public boolean processMenu() {
        // Display menu options for StoreOwner.
        System.out.println("1. Create Store");
        System.out.println("2. Add Game to Store");
        System.out.println("3. View My Stores");
        System.out.println("4. Edit Store");
        System.out.println("5. Perform Admin Action");
        System.out.println("6. Logout");
        System.out.print("Choose an option: ");
        int choice; // Variable for menu choice.
        try {
            // Read choice.
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            // Handle non-numeric.
            System.out.println("Invalid input. Please enter a number.");
            return false; // Not logging out.
        }

        // Process StoreOwner's choice.
        switch (choice) {
            case 1: // Create Store.
                System.out.print("Enter new store name: ");
                String newStoreName = scanner.nextLine(); // Read new store name.
                // Call createStore method of StoreOwner, passing the StoreService.
                storeOwner.createStore(newStoreName, storeService);
                break;
            case 2: // Add Game to Store.
                handleAddGameToStore(); // Delegate to a private helper method.
                break;
            case 3: // View My Stores.
                System.out.println("Your Stores:");
                // Check if the owner has any stores (accessing public field myStores).
                if (storeOwner.myStores.isEmpty()) {
                    System.out.println("  - You don't own any stores yet.");
                } else {
                    // Iterate through the owner's stores and print their names.
                    for (String sName : storeOwner.myStores) {
                        System.out.println("  - " + sName);
                    }
                }
                break;
            case 4: // Edit Store.
                handleEditStoreSubMenu(); // Delegate to sub-menu handler.
                break;
            case 5: // Perform Admin Action.
                // Call performAdminAction on the store owner object.
                storeOwner.performAdminAction(scanner, userManager, storeService);
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

    // Private helper method to handle adding a game to a store.
    private void handleAddGameToStore() {
        System.out.print("Enter store name to add game to: ");
        String storeToAddGame = scanner.nextLine(); // Read store name.
        // Check if the owner actually owns this store (accessing public field myStores).
        if (!storeOwner.myStores.contains(storeToAddGame)) {
            System.out.println("You don't own this store or it doesn't exist in your list.");
            return; // Return if not owned.
        }
        System.out.print("Enter game name: ");
        String newGameName = scanner.nextLine(); // Read game name.
        System.out.print("Enter game price: ");
        double newGamePrice = 0; // Variable for game price, initialized.
        try {
            newGamePrice = Double.parseDouble(scanner.nextLine()); // Read price.
        } catch (NumberFormatException e) {
            System.out.println("Invalid price. Game not added."); // Handle non-numeric.
            return;
        }
        System.out.print("Enter game genre: ");
        String newGameGenre = scanner.nextLine(); // Read game genre.
        // Call addGame method of StoreOwner, passing the StoreService.
        storeOwner.addGameToStore(storeToAddGame, newGameName, newGamePrice, newGameGenre, storeService);
    }

    // Private helper method to handle the "Edit Store" sub-menu for StoreOwners.
    private void handleEditStoreSubMenu() {
        System.out.print("Enter the name of the store you want to edit: ");
        String storeToEdit = scanner.nextLine(); // Read name of store to edit.
        // Check if the owner owns this store (accessing public field myStores).
        if (storeOwner.myStores.contains(storeToEdit)) {
            System.out.println("\nEditing Store: " + storeToEdit);
            // Display edit options for the store.
            System.out.println("1. Change Store Name");
            System.out.println("2. Edit Game Price");
            System.out.println("3. Edit Game Genre");
            System.out.println("4. Remove Game");
            System.out.println("5. Back");
            System.out.print("Choose an edit option: ");
            int editChoice; // Variable for edit choice.
            try {
                editChoice = Integer.parseInt(scanner.nextLine()); // Read choice.
            } catch (NumberFormatException e) {
                System.out.println("Invalid input."); // Handle non-numeric.
                return;
            }

            // Process the edit choice.
            switch (editChoice) {
                case 1: // Change Store Name.
                    System.out.print("Enter the new store name: ");
                    String aNewStoreName = scanner.nextLine(); // Read new store name.
                    // Call renameStore method from StoreService.
                    storeService.renameStore(storeToEdit, aNewStoreName, storeOwner);
                    break;
                case 2: // Edit Game Price.
                    System.out.print("Enter the game name to edit price: ");
                    String gameToEditPrice = scanner.nextLine(); // Read game name.
                    System.out.print("Enter the new price: ");
                    double aNewPrice; // Variable for new price.
                    try {
                        aNewPrice = Double.parseDouble(scanner.nextLine()); // Read price.
                        // Call editGamePrice method from StoreService.
                        storeService.editGamePrice(storeToEdit, gameToEditPrice, aNewPrice);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid price.");
                    }
                    break;
                case 3: // Edit Game Genre.
                    System.out.print("Enter the game name to edit genre: ");
                    String gameToEditGenre = scanner.nextLine(); // Read game name.
                    System.out.print("Enter the new genre: ");
                    String aNewGenre = scanner.nextLine(); // Read new genre.
                    // Call editGameGenre method from StoreService.
                    storeService.editGameGenre(storeToEdit, gameToEditGenre, aNewGenre);
                    break;
                case 4: // Remove Game.
                    System.out.print("Enter the game name to remove: ");
                    String gameToRemove = scanner.nextLine(); // Read game name.
                    // Call removeGame method from StoreService.
                    storeService.removeGame(storeToEdit, gameToRemove);
                    break;
                case 5: // Back to previous menu.
                    break; // Do nothing, will return from this method.
                default: // Invalid edit option.
                    System.out.println("Invalid edit option.");
            }
        } else {
            // If owner doesn't own the store.
            System.out.println("You do not own this store or it doesn't exist!");
        }
    }
}
