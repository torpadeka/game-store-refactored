import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Customer extends User {
    private double balance;
    private List<String> ownedGames;

    public Customer(String username, String password) {
        super(username, password, UserRole.CUSTOMER); 
        this.balance = 0.0;
        this.ownedGames = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public List<String> getOwnedGames() {
        return ownedGames;
    }

	public boolean adjustBalance(double amount) {
        if (this.balance + amount < 0) {
            System.out.println("Cannot complete transaction: Insufficient balance.");
            return false;
        }
        this.balance += amount;
        return true;
    }

    public void topUp(double amount) {
        if (amount > 0) {
            adjustBalance(amount);
            TransactionLogger.logTransaction(getUsername(), "TOP_UP", amount, "Balance topped up");
            System.out.println("Balance topped up successfully. New balance: $" + String.format("%.2f", balance));
        } else {
            System.out.println("Top up amount must be positive.");
        }
    }
    
    public void buyGame(String storeName, String gameName, StoreService storeService) {
        Game gameToBuy = storeService.getGameFromStore(storeName, gameName);
        	
        if (!isGameAvailable(gameToBuy)) {
            return;
        }
        
        double price = gameToBuy.getPrice();
        if (canAffordGame(price)) {
            processGamePurchase(gameName, storeName, price);
        }
    }

    private boolean isGameAvailable(Game game) {
        if (game == null) {
            System.out.println("Store or game not found!");
            return false;
        }
        return true;
    }

    private boolean canAffordGame(double price) {
        if (this.balance < price) {
            System.out.println("Insufficient balance!");
            return false;
        }
        return true;
    }

    protected void processGamePurchase(String gameName, String storeName, double price) {
        if (adjustBalance(-price)) {
            this.ownedGames.add(gameName);
            TransactionLogger.logTransaction(getUsername(), "PURCHASE", price, gameName + " from " + storeName);
            System.out.println("Game '" + gameName + "' purchased successfully from '" + storeName + "'!");
            System.out.println("It has been added to your library. New balance: $" + String.format("%.2f", balance));
        } else {
            System.out.println("Purchase failed during balance adjustment.");
        }
    }

    public void viewMyGames() {
        System.out.println("\n--- Your Game Library ---");
        if (ownedGames.isEmpty()) {
            System.out.println("You don't own any games yet.");
        } else {
            for (String game : ownedGames) {
                System.out.println("- " + game);
            }
        }
    }

    @Override
    public void performAdminAction(Scanner scanner, UserManager userManager, StoreService storeService) {
        System.out.println("Customers (" + getUsername() + ") cannot perform admin actions.");
    }

	@Override
	public boolean handleMenu(Scanner scanner, UserManager userManager, StoreService storeService) {
		return new CustomerMenuHandler(this, scanner, userManager, storeService).processMenu();
	}


}
