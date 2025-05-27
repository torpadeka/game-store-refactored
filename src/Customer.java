// Customer.java
// This class represents a Customer user, inheriting from the User class.
// It includes customer-specific properties like balance and owned games.
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Customer extends User {
    // This variable stores the customer's account balance as a double.
    private double balance;
    // This list stores the names of games owned by the customer. Each element is a String.
    private List<String> ownedGames;

    // Constructor for the Customer class.
    // It initializes a new Customer object with a username and password,
    // and sets the role to "customer".
    public Customer(String username, String password) {
        // Call the constructor of the superclass (User) to initialize common fields.
        super(username, password, "customer");
        // Initialize the customer's balance to 0.0.
        this.balance = 0.0;
        // Initialize the list of owned games as a new empty ArrayList.
        this.ownedGames = new ArrayList<>();
    }

    // Getter method for the customer's balance.
    // Returns the current balance.
    public double getBalance() {
        // Return the value of the 'balance' instance variable.
        return balance;
    }

    // Getter method for the list of owned games.
    // Returns the list of game names.
    public List<String> getOwnedGames() {
        // Return the 'ownedGames' list.
        return ownedGames;
    }

    // Protected method to adjust the customer's balance.
    // It can be used by this class or its subclasses (like PremiumCustomer).
    // Takes a double 'amount' which can be positive (for adding) or negative (for spending).
    // Returns true if the adjustment was successful, false otherwise (e.g., insufficient funds for spending).
    protected boolean adjustBalance(double amount) {
        // Check if the adjustment would result in a negative balance (only relevant when spending).
        if (this.balance + amount < 0) {
            // This condition applies if 'amount' is negative (spending).
            if (amount < 0) {
                // Print an error message if trying to spend more than available.
                 System.out.println("Adjustment failed: insufficient funds for spending " + (-amount));
                 // Return false indicating failure.
                 return false;
            }
        }
        // Add the amount to the balance (this works for both adding and spending).
        this.balance += amount;
        // Return true indicating success.
        return true;
    }

    // Method for a customer to top up their account balance.
    // Takes a double 'amount' to be added.
    public void topUp(double amount) {
        // Check if the top-up amount is positive.
        if (amount <= 0) {
            // Print an error message if the amount is not positive.
            System.out.println("Top-up amount must be positive.");
            // Exit the method.
            return;
        }
        // Attempt to adjust the balance by adding the amount.
        if (adjustBalance(amount)) {
            // Log the transaction if the balance adjustment was successful.
            GameStore.logTransaction(getUsername(), "TOP_UP", amount, "N/A");
            // Print a success message with the new balance.
            System.out.println("Top up successful! Current balance: $" + String.format("%.2f", this.balance));
        } else {
            // This block should ideally not be reached for a positive top-up amount.
            System.out.println("Top up failed unexpectedly.");
        }
    }

    // Method for a customer to buy a game from a store.
    // Takes the store name, game name, and a GameStore instance as parameters.
    public void buyGame(String storeName, String gameName, GameStore gameStore) {
        // Get the map of games available in the specified store.
        Map<String, Game> storeGames = gameStore.getGamesInStore(storeName);

        // Check if the store and the game exist.
        if (storeGames != null && storeGames.containsKey(gameName)) {
            // Get the Game object for the game to be bought.
            Game gameToBuy = storeGames.get(gameName);
            // Get the price of the game.
            double price = gameToBuy.getPrice();

            // Check if the customer has enough balance to buy the game.
            if (this.balance >= price) {
                // Attempt to adjust the balance by subtracting the game's price.
                if (adjustBalance(-price)) {
                    // Add the game name to the customer's list of owned games.
                    this.ownedGames.add(gameName);
                    // Log the purchase transaction.
                    GameStore.logTransaction(getUsername(), "PURCHASE", price, gameName + " from " + storeName);
                    // Print a success message.
                    System.out.println("Game '" + gameName + "' purchased successfully from '" + storeName + "'!");
                    // Inform the customer that the game has been added to their library.
                    System.out.println("It has been added to your library. New balance: $" + String.format("%.2f", this.balance));
                } else {
                     // This might happen if adjustBalance has more complex rules, though unlikely here with the current check.
                    System.out.println("Purchase failed during balance adjustment.");
                }
            } else {
                // Print a message if the balance is insufficient.
                System.out.println("Insufficient balance!");
            }
        } else {
            // Print a message if the store or game is not found.
            System.out.println("Store or game not found!");
        }
    }

    // Method for the customer to view their owned games.
    public void viewMyGames() {
        // Check if the list of owned games is empty.
        if (ownedGames.isEmpty()) {
            // Print a message if the customer owns no games.
            System.out.println("You do not own any games yet.");
        } else {
            // Print a header for the game library.
            System.out.println("\n--- Your Game Library ---");
            // Iterate through the list of owned game names and print each one.
            for (String gameName : ownedGames) {
                System.out.println("- " + gameName);
            }
            // Print a footer for the game library.
            System.out.println("-------------------------");
        }
    }

    // Overridden method from the User class.
    // Customers do not have admin actions.
    @Override
    public void performAdminAction() {
        // Print a message indicating that customers cannot perform admin actions.
        System.out.println("Customers (" + getUsername() + ") cannot perform admin actions.");
    }
}
