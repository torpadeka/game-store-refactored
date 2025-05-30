import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Customer extends User {
    private double balance;
    private List<GameName> ownedGames;

    public Customer(Username username, Password password) {
        super(username, password, UserRole.CUSTOMER);
        this.balance = 0.0;
        this.ownedGames = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public List<GameName> getOwnedGames() {
        return ownedGames;
    }

    protected boolean adjustBalance(double amount) {
        if (this.balance + amount < 0) {
            if (amount < 0) { 
                 System.out.println("Adjustment failed: insufficient funds for spending " + (-amount));
                 return false;
            }
        }
        this.balance += amount;
        return true;
    }

    public void topUp(double amount) {
        if (amount <= 0) {
            System.out.println("Top-up amount must be positive.");
            return;
        }
        if (adjustBalance(amount)) {
            TransactionLogger.logTransaction(getUsername(), "TOP_UP", new Price(amount), "N/A");
            System.out.println("Top up successful! Current balance: $" + String.format("%.2f", this.balance));
        } else {
            System.out.println("Top up failed unexpectedly.");
        }
    }

    public void buyGame(StoreName storeName, GameName gameName, StoreService storeService) {
        Game gameToBuy = storeService.getGameFromStore(storeName, gameName);

        if (gameToBuy == null) {
            System.out.println("Store or game not found!");
            return;
        }

        Price price = gameToBuy.getPrice();
        if (canAffordGame(price)) {
            processPurchase(gameName, storeName, price);
        } else {
            System.out.println("Insufficient balance!");
        }
    }

    private boolean canAffordGame(Price price) {
        return this.balance >= price.getValue();
    }

    private void processPurchase(GameName gameName, StoreName storeName, Price price) {
        if (adjustBalance(-price.getValue())) {
            this.ownedGames.add(gameName);
            TransactionLogger.logTransaction(getUsername(), "PURCHASE", price, gameName.getValue() + " from " + storeName.getValue());
            System.out.println("Game '" + gameName.getValue() + "' purchased successfully from '" + storeName.getValue() + "'!");
            System.out.println("It has been added to your library. New balance: $" + String.format("%.2f", this.balance));
        } else {
            System.out.println("Purchase failed during balance adjustment.");
        }
    }

    public void viewMyGames() {
        if (ownedGames.isEmpty()) {
            System.out.println("You do not own any games yet.");
        } else {
            System.out.println("\n--- Your Game Library ---");
            for (GameName gameName : ownedGames) {
                System.out.println("- " + gameName.getValue());
            }
            System.out.println("-------------------------");
        }
    }

    @Override
    public void performAdminAction(Scanner scanner, UserManager userManager, StoreService storeService) {
        System.out.println("Customers (" + getUsername().getValue() + ") cannot perform admin actions.");
    }
}
