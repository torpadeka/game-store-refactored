
import java.util.Map;
import java.util.Scanner;

public class PremiumCustomer extends Customer {
    private double discountRate;

    public PremiumCustomer(String username, String password, double discountRate) {
        super(username, password);
        super.setRole("premium_customer");
        this.discountRate = discountRate;
        if (discountRate < 0 || discountRate >= 1) {
            System.out.println("Warning: Invalid discount rate " + discountRate + ". Setting to 0.1 (10%).");
            this.discountRate = 0.1;
        }
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        if (discountRate < 0 || discountRate >= 1) {
            System.out.println("Warning: Invalid discount rate " + discountRate + ". Not changed.");
            return;
        }
        this.discountRate = discountRate;
    }

    @Override
    public void buyGame(String storeName, String gameName, StoreService storeService) {
        Game gameToBuy = storeService.getGameFromStore(storeName, gameName);

        if (gameToBuy != null) {
            double originalPrice = gameToBuy.getPrice();
            double discountedPrice = originalPrice * (1 - this.discountRate);

            System.out.println("Premium Member Price for " + gameName + ": $" + String.format("%.2f", discountedPrice) +
                               " (Original: $" + String.format("%.2f", originalPrice) + ")");

            if (super.getBalance() >= discountedPrice) {
                if (adjustBalance(-discountedPrice)) {
                    super.getOwnedGames().add(gameName);
                    TransactionLogger.logTransaction(getUsername(), "PREMIUM_PURCHASE", discountedPrice, gameName + " from " + storeName);
                    System.out.println("Premium game '" + gameName + "' purchased successfully from '" + storeName + "'!");
                    System.out.println("It has been added to your library. New balance: $" + String.format("%.2f", getBalance()));
                } else {
                    System.out.println("Purchase failed during balance adjustment.");
                }
            } else {
                System.out.println("Insufficient balance for premium purchase!");
            }
        } else {
            System.out.println("Store or game not found!");
        }
    }

    @Override
    public void performAdminAction(Scanner scanner, UserManager userManager, StoreService storeService) {
        System.out.println("Premium Customers (" + getUsername() + ") cannot perform admin actions.");
    }
}
