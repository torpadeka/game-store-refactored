// TransactionLogger.java
// This class is responsible for logging transactions and system events.
// In a real application, this would likely interact with a file system or database.
public class TransactionLogger {

    // This static method logs a transaction or system event.
    // It takes username (String), type of transaction (String), amount (double), and details (String).
    // Currently, it just prints to the console.
    public static void logTransaction(String username, String type, double amount, String details) {
        // This method formats and prints transaction details to the standard output.
        // The use of System.out.printf allows for formatted string output.
        // The format string specifies how each argument should be presented.
        // %s is for strings, %.2f is for a double formatted to two decimal places.
        // %n is a platform-independent newline character.
        System.out.printf("[TRANSACTION LOG] User: %s, Type: %s, Amount/Value: %.2f, Details: %s%n",
                username, type, amount, details);
    }
}
