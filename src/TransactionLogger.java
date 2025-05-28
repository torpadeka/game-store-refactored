
public class TransactionLogger {

    public static void logTransaction(String username, String type, double amount, String details) {
        System.out.printf("[TRANSACTION LOG] User: %s, Type: %s, Amount/Value: %.2f, Details: %s%n",
                username, type, amount, details);
    }
}