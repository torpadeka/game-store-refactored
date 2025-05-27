// PremiumCustomer.java
// This class represents a Premium Customer, inheriting from Customer.
// Premium Customers get a discount on game purchases.
import java.util.Map; // Not directly used, but often associated with store/game data.
import java.util.Scanner; // For performAdminAction signature consistency

public class PremiumCustomer extends Customer {
    // This variable stores the discount rate for the premium customer (e.g., 0.1 for 10%).
    private double discountRate;

    // Constructor for the PremiumCustomer class.
    // Takes username, password, and discount rate as parameters.
    public PremiumCustomer(String username, String password, double discountRate) {
        // Call the superclass (Customer) constructor.
        super(username, password);
        // Explicitly set the role for this premium customer.
        // This overrides the role set by the Customer constructor.
        super.setRole("premium_customer");
        // Set the discount rate for this premium customer.
        this.discountRate = discountRate;
        // Validate the provided discount rate.
        if (discountRate < 0 || discountRate >= 1) {
            // If the rate is invalid, print a warning and set a default rate.
            System.out.println("Warning: Invalid discount rate " + discountRate + ". Setting to 0.1 (10%).");
            this.discountRate = 0.1; // Default discount rate is 10%.
        }
    }

    // Getter method for the discount rate.
    // Returns the discount rate applicable to this premium customer.
    public double getDiscountRate() {
        // Return the value of the 'discountRate' instance variable.
        return discountRate;
    }

    // Setter method for the discount rate.
    // Allows changing the discount rate after the object is created.
    public void setDiscountRate(double discountRate) {
        // Validate the new discount rate.
        if (discountRate < 0 || discountRate >= 1) {
            // If the rate is invalid, print a warning and do not change the current rate.
            System.out.println("Warning: Invalid discount rate " + discountRate + ". Not changed.");
            // Exit the method.
            return;
        }
        // Update the discount rate if the new rate is valid.
        this.discountRate = discountRate;
    }

    // Overridden method for buying a game, specific to Premium Customers.
    // Applies a discount to the game price.
    // Takes storeName, gameName, and StoreService instance.
    @Override
    public void buyGame(String storeName, String gameName, StoreService storeService) {
        // Get the Game object from the store using the StoreService.
        Game gameToBuy = storeService.getGameFromStore(storeName, gameName);

        // Check if the game exists.
        if (gameToBuy != null) {
            // Get the original price of the game.
            double originalPrice = gameToBuy.getPrice();
            // Calculate the discounted price using the customer's discount rate.
            double discountedPrice = originalPrice * (1 - this.discountRate);

            // Print information about the premium pricing.
            System.out.println("Premium Member Price for " + gameName + ": $" + String.format("%.2f", discountedPrice) +
                               " (Original: $" + String.format("%.2f", originalPrice) + ")");

            // Check if the customer has enough balance for the discounted price.
            // Uses getBalance() inherited from Customer.
            if (super.getBalance() >= discountedPrice) {
                // Attempt to adjust the balance by subtracting the discounted price.
                // Uses adjustBalance(double) inherited and made protected in Customer.
                if (adjustBalance(-discountedPrice)) {
                    // Add the game to the customer's owned games list.
                    // Uses getOwnedGames() inherited from Customer to get the list.
                    super.getOwnedGames().add(gameName);
                    // Log the premium purchase transaction.
                    TransactionLogger.logTransaction(getUsername(), "PREMIUM_PURCHASE", discountedPrice, gameName + " from " + storeName);
                    // Print a success message.
                    System.out.println("Premium game '" + gameName + "' purchased successfully from '" + storeName + "'!");
                    // Inform the customer about their new balance.
                    System.out.println("It has been added to your library. New balance: $" + String.format("%.2f", getBalance()));
                } else {
                    System.out.println("Purchase failed during balance adjustment.");
                }
            } else {
                // Print a message if the balance is insufficient for the premium price.
                System.out.println("Insufficient balance for premium purchase!");
            }
        } else {
            // Print a message if the store or game is not found.
            System.out.println("Store or game not found!");
        }
    }
     // Override performAdminAction for consistency, though it does the same as Customer.
    @Override
    public void performAdminAction(Scanner scanner, UserManager userManager, StoreService storeService) {
        System.out.println("Premium Customers (" + getUsername() + ") cannot perform admin actions.");
    }
}
