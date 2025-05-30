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
        displayMenuOptions();
        int choice = getUserChoice();

        switch (choice) {
            case 1:
                handleCreateStore();
                break;
            case 2:
                handleAddGameToStore();
                break;
            case 3:
                displayMyStores();
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

    private void displayMenuOptions() {
        System.out.println("1. Create Store");
        System.out.println("2. Add Game to Store");
        System.out.println("3. View My Stores");
        System.out.println("4. Edit Store");
        System.out.println("5. Perform Admin Action");
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

    private void handleCreateStore() {
        System.out.print("Enter new store name: ");
        String newStoreNameStr = scanner.nextLine();
        try {
            StoreName newStoreName = new StoreName(newStoreNameStr);
            storeOwner.createStore(newStoreName, storeService);
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating store: " + e.getMessage());
        }
    }

    private void displayMyStores() {
        System.out.println("Your Stores:");
        if (storeOwner.myStores.isEmpty()) {
            System.out.println("  - You don't own any stores yet.");
        } else {
            for (StoreName sName : storeOwner.myStores) {
                System.out.println("  - " + sName.getValue());
            }
        }
    }

    private void handleAddGameToStore() {
        StoreName storeToAddGame = promptForStoreName("Enter store name to add game to: ");
        if (storeToAddGame == null || !storeOwner.myStores.contains(storeToAddGame)) {
            System.out.println("You don't own this store or it doesn't exist in your list.");
            return;
        }

        GameName newGameName = promptForGameName("Enter game name: ");
        if (newGameName == null) return;

        Price newGamePrice = promptForPrice("Enter game price: ");
        if (newGamePrice == null) return;

        Genre newGameGenre = promptForGenre("Enter game genre: ");
        if (newGameGenre == null) return;

        storeOwner.addGameToStore(storeToAddGame, newGameName, newGamePrice, newGameGenre, storeService);
    }

    private void handleEditStoreSubMenu() {
        StoreName storeToEdit = promptForStoreName("Enter the name of the store you want to edit: ");
        if (storeToEdit == null || !storeOwner.myStores.contains(storeToEdit)) {
            System.out.println("You do not own this store or it doesn't exist!");
            return;
        }

        displayEditStoreOptions(storeToEdit);
        int editChoice = getUserChoice();
        processEditStoreChoice(editChoice, storeToEdit);
    }

    private void displayEditStoreOptions(StoreName storeToEdit) {
        System.out.println("\nEditing Store: " + storeToEdit.getValue());
        System.out.println("1. Change Store Name");
        System.out.println("2. Edit Game Price");
        System.out.println("3. Edit Game Genre");
        System.out.println("4. Remove Game");
        System.out.println("5. Back");
        System.out.print("Choose an edit option: ");
    }

    private void processEditStoreChoice(int editChoice, StoreName storeToEdit) {
        switch (editChoice) {
            case 1:
                handleChangeStoreName(storeToEdit);
                break;
            case 2:
                handleEditGamePrice(storeToEdit);
                break;
            case 3:
                handleEditGameGenre(storeToEdit);
                break;
            case 4:
                handleRemoveGame(storeToEdit);
                break;
            case 5:
                break;
            default:
                System.out.println("Invalid edit option.");
        }
    }

    private void handleChangeStoreName(StoreName oldStoreName) {
        System.out.print("Enter the new store name: ");
        String aNewStoreNameStr = scanner.nextLine();
        try {
            StoreName aNewStoreName = new StoreName(aNewStoreNameStr);
            storeService.renameStore(oldStoreName, aNewStoreName, storeOwner);
        } catch (IllegalArgumentException e) {
            System.out.println("Error renaming store: " + e.getMessage());
        }
    }

    private void handleEditGamePrice(StoreName storeToEdit) {
        GameName gameToEditPrice = promptForGameName("Enter the game name to edit price: ");
        if (gameToEditPrice == null) return;

        Price aNewPrice = promptForPrice("Enter the new price: ");
        if (aNewPrice == null) return;

        storeService.editGamePrice(storeToEdit, gameToEditPrice, aNewPrice);
    }

    private void handleEditGameGenre(StoreName storeToEdit) {
        GameName gameToEditGenre = promptForGameName("Enter the game name to edit genre: ");
        if (gameToEditGenre == null) return;

        Genre aNewGenre = promptForGenre("Enter the new genre: ");
        if (aNewGenre == null) return;

        storeService.editGameGenre(storeToEdit, gameToEditGenre, aNewGenre);
    }

    private void handleRemoveGame(StoreName storeToEdit) {
        GameName gameToRemove = promptForGameName("Enter the game name to remove: ");
        if (gameToRemove == null) return;

        storeService.removeGame(storeToEdit, gameToRemove);
    }

  
    private StoreName promptForStoreName(String prompt) {
        System.out.print(prompt);
        String nameStr = scanner.nextLine();
        try {
            return new StoreName(nameStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid store name: " + e.getMessage());
            return null;
        }
    }

    private GameName promptForGameName(String prompt) {
        System.out.print(prompt);
        String nameStr = scanner.nextLine();
        try {
            return new GameName(nameStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid game name: " + e.getMessage());
            return null;
        }
    }

    private Price promptForPrice(String prompt) {
        System.out.print(prompt);
        try {
            double priceValue = Double.parseDouble(scanner.nextLine());
            return new Price(priceValue);
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format. Please enter a number.");
            return null;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid price: " + e.getMessage());
            return null;
        }
    }

    private Genre promptForGenre(String prompt) {
        System.out.print(prompt);
        String genreStr = scanner.nextLine();
        try {
            return new Genre(genreStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid genre: " + e.getMessage());
            return null;
        }
    }
}
