import java.util.Map;
import java.util.Scanner;

public class PremiumCustomer extends Customer {
    private DiscountRate discountRate;

    public PremiumCustomer(Username username, Password password, DiscountRate discountRate) {
        super(username, password);
        super.setRole(UserRole.PREMIUM_CUSTOMER);
        this.discountRate = discountRate;
    }

    public DiscountRate getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(DiscountRate discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public void buyGame(StoreName storeName, GameName gameName, StoreService storeService) {
        Game gameToBuy = storeService.getGameFromStore(storeName, gameName);

        if (gameToBuy == null) {
            System.out.println("Store or game not found!");
            return;
        }

        Price originalPrice = gameToBuy.getPrice();
        Price discountedPrice = calculateDiscountedPrice(originalPrice);

        displayPriceInformation(gameName, originalPrice, discountedPrice);

        if (canAffordGame(discountedPrice)) {
            processPurchase(gameName, storeName, discountedPrice);
        } else {
            System.out.println("Insufficient balance for premium purchase!");
        }
    }

    private Price calculateDiscountedPrice(Price originalPrice) {
        double discountedPriceValue = originalPrice.getValue() * (1 - this.discountRate.getValue());
        return new Price(discountedPriceValue);
    }

    private void displayPriceInformation(GameName gameName, Price originalPrice, Price discountedPrice) {
        System.out.println("Premium Member Price for " + gameName.getValue() + ": $" + String.format("%.2f", discountedPrice.getValue()) +
                           " (Original: $" + String.format("%.2f", originalPrice.getValue()) + ")");
    }

    private boolean canAffordGame(Price price) {
        return super.getBalance() >= price.getValue();
    }

    private void processPurchase(GameName gameName, StoreName storeName, Price price) {
        if (adjustBalance(-price.getValue())) {
            super.getOwnedGames().add(gameName);
            TransactionLogger.logTransaction(getUsername(), "PREMIUM_PURCHASE", price, gameName.getValue() + " from " + storeName.getValue());
            System.out.println("Premium game '" + gameName.getValue() + "' purchased successfully from '" + storeName.getValue() + "'!");
            System.out.println("It has been added to your library. New balance: $" + String.format("%.2f", getBalance()));
        } else {
            System.out.println("Purchase failed during balance adjustment.");
        }
    }

    @Override
    public void performAdminAction(Scanner scanner, UserManager userManager, StoreService storeService) {
        System.out.println("Premium Customers (" + getUsername().getValue() + ") cannot perform admin actions.");
    }
}
