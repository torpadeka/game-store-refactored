
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Customer extends User {
    private double balance;
    private List<String> ownedGames;

    public Customer(String username, String password) {
        super(username, password, "customer");
        this.balance = 0.0;
        this.ownedGames = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public List<String> getOwnedGames() {
        return ownedGames;
    }

    protected boolean adjustBalance(double amount) {
        if (this.balance + amount < 0) {
            if (amount < 0) { // Check only if spending
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
            TransactionLogger.logTransaction(getUsername(), "TOP_UP", amount, "N/A");
            System.out.println("Top up successful! Current balance: $" + String.format("%.2f", this.balance));
        } else {
            System.out.println("Top up failed unexpectedly.");
        }
    }

    public void buyGame(String storeName, String gameName, StoreService storeService) {
        Game gameToBuy = storeService.getGameFromStore(storeName, gameName);

        if (gameToBuy != null) {
            double price = gameToBuy.getPrice();
            if (this.balance >= price) {
                if (adjustBalance(-price)) {
                    this.ownedGames.add(gameName);
                    TransactionLogger.logTransaction(getUsername(), "PURCHASE", price, gameName + " from " + storeName);
                    System.out.println("Game '" + gameName + "' purchased successfully from '" + storeName + "'!");
                    System.out.println("It has been added to your library. New balance: $" + String.format("%.2f", this.balance));
                } else {
                    System.out.println("Purchase failed during balance adjustment.");
                }
            } else {
                System.out.println("Insufficient balance!");
            }
        } else {
            System.out.println("Store or game not found!");
        }
    }

    public void viewMyGames() {
        if (ownedGames.isEmpty()) {
            System.out.println("You do not own any games yet.");
        } else {
            System.out.println("\n--- Your Game Library ---");
            for (String gameName : ownedGames) {
                System.out.println("- " + gameName);
            }
            System.out.println("-------------------------");
        }
    }

    @Override
    public void performAdminAction(Scanner scanner, UserManager userManager, StoreService storeService) {
        System.out.println("Customers (" + getUsername() + ") cannot perform admin actions.");
    }
}
