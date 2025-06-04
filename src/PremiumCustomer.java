import java.util.Scanner;

public class PremiumCustomer extends Customer {
    private double discountRate;

    public PremiumCustomer(String username, String password, double discountRate) {
        super(username, password);
        super.setRole(UserRole.PREMIUM_CUSTOMER); 
        setDiscountRate(discountRate);
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        if (discountRate < 0 || discountRate >= 1) {
            System.out.println("Warning: Invalid discount rate " + discountRate + ". Setting to 0.1 (10%).");
            this.discountRate = 0.1;
        } else {
            this.discountRate = discountRate;
        }
    }

    @Override
    public void buyGame(String storeName, String gameName, StoreService storeService) {
        Game gameToBuy = storeService.getGameFromStore(storeName, gameName);

        if (gameToBuy != null) {
            double discountedPrice = calculateDiscountedPrice(gameToBuy);
            if (super.getBalance() >= discountedPrice) {
                finalizePremiumPurchase(gameName, storeName, discountedPrice);
            } else {
                System.out.println("Insufficient balance for premium purchase!");
            }
        } else {
            System.out.println("Store or game not found!");
        }
    }

    private double calculateDiscountedPrice(Game game) {
        double originalPrice = game.getPrice();
        double discountedPrice = originalPrice * (1 - this.discountRate);
        System.out.println("Premium Member Price for " + game.getName() + ": $" + String.format("%.2f", discountedPrice) +
                           " (Original: $" + String.format("%.2f", originalPrice) + ")");
        return discountedPrice;
    }

    private void finalizePremiumPurchase(String gameName, String storeName, double discountedPrice) {
        if (adjustBalance(-discountedPrice)) {
            super.getOwnedGames().add(gameName);
            TransactionLogger.logTransaction(getUsername(), "PREMIUM_PURCHASE", discountedPrice, gameName + " from " + storeName);
            System.out.println("Premium game '" + gameName + "' purchased successfully from '" + storeName + "'!");
            System.out.println("It has been added to your library. New balance: $" + String.format("%.2f", getBalance()));
        } else {
            System.out.println("Purchase failed during balance adjustment.");
        }
    }

    @Override
    public void performAdminAction(Scanner scanner, UserManager userManager, StoreService storeService) {
        System.out.println("Premium Customers (" + getUsername() + ") cannot perform admin actions.");
    }
    
    @Override
    public boolean handleMenu(Scanner scanner, UserManager userManager, StoreService storeService) {
    	return new PremiumCustomerMenuHandler(this, scanner, userManager, storeService).processMenu();
    }
}
