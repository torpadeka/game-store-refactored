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
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return false;
        }

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

    private void handleCreateStore() {
        System.out.print("Enter new store name: ");
        String newStoreNameStr = scanner.nextLine();
        StoreName newStoreName = new StoreName(newStoreNameStr);
        storeOwner.createStore(newStoreName, storeService);
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
        System.out.print("Enter store name to add game to: ");
        String storeToAddGameStr = scanner.nextLine();
        StoreName storeToAddGame = new StoreName(storeToAddGameStr);

        if (!storeOwner.myStores.contains(storeToAddGame)) {
            System.out.println("You don't own this store or it doesn't exist in your list.");
            return;
        }

        System.out.print("Enter game name: ");
        String newGameNameStr = scanner.nextLine();
        GameName newGameName = new GameName(newGameNameStr);

        System.out.print("Enter game price: ");
        double priceValue = 0;
        try {
            priceValue = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) { 
            System.out.println("Invalid price. Game not added.");
            return;
        }
        Price newGamePrice = new Price(priceValue);


        System.out.print("Enter game genre: ");
        String newGameGenreStr = scanner.nextLine();
        Genre newGameGenre = new Genre(newGameGenreStr);

        storeOwner.addGameToStore(storeToAddGame, newGameName, newGamePrice, newGameGenre, storeService);
    }

    private void handleEditStoreSubMenu() {
        System.out.print("Enter the name of the store you want to edit: ");
        String storeToEditStr = scanner.nextLine();
        StoreName storeToEdit = new StoreName(storeToEditStr);

        if (!storeOwner.myStores.contains(storeToEdit)) {
            System.out.println("You do not own this store or it doesn't exist!");
            return;
        }

        displayEditStoreOptions(storeToEdit);
        int editChoice;
        try {
            editChoice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) { 
            System.out.println("Invalid input.");
            return;
        }

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
        StoreName aNewStoreName = new StoreName(aNewStoreNameStr);
        storeService.renameStore(oldStoreName, aNewStoreName, storeOwner);
    }

    private void handleEditGamePrice(StoreName storeToEdit) {
        System.out.print("Enter the game name to edit price: ");
        String gameToEditPriceStr = scanner.nextLine();
        GameName gameToEditPrice = new GameName(gameToEditPriceStr);

        System.out.print("Enter the new price: ");
        double aNewPriceValue;
        try {
            aNewPriceValue = Double.parseDouble(scanner.nextLine());
            Price aNewPrice = new Price(aNewPriceValue);
            storeService.editGamePrice(storeToEdit, gameToEditPrice, aNewPrice);
        } catch (NumberFormatException e) { 
            System.out.println("Invalid price.");
        }
    }

    private void handleEditGameGenre(StoreName storeToEdit) {
        System.out.print("Enter the game name to edit genre: ");
        String gameToEditGenreStr = scanner.nextLine();
        GameName gameToEditGenre = new GameName(gameToEditGenreStr);

        System.out.print("Enter the new genre: ");
        String aNewGenreStr = scanner.nextLine();
        Genre aNewGenre = new Genre(aNewGenreStr);
        storeService.editGameGenre(storeToEdit, gameToEditGenre, aNewGenre);
    }

    private void handleRemoveGame(StoreName storeToEdit) {
        System.out.print("Enter the game name to remove: ");
        String gameToRemoveStr = scanner.nextLine();
        GameName gameToRemove = new GameName(gameToRemoveStr);
        storeService.removeGame(storeToEdit, gameToRemove);
    }
}
