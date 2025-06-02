import java.util.Scanner;

public class RegisterAction implements MenuAction{

    private static void handlePremiumCustomerRegistration(Scanner scanner, UserManager userManager, String username, String password) {
        System.out.print("Enter discount rate (e.g., 0.1 for 10%): ");
        double rate = 0.1;
        try {
            rate = Double.parseDouble(scanner.nextLine());
            userManager.registerUser(username, password, "premium_customer", rate);
        } catch (NumberFormatException e) {
            System.out.println("Invalid rate, using default 10% for premium customer registration.");
            userManager.registerUser(username, password, "premium_customer", 0.1);
        }
    }
	
    private static void handleRegistration(Scanner scanner, UserManager userManager) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Register as (customer/premium_customer/store_owner): ");
        String role = scanner.nextLine();

        if (role.equalsIgnoreCase("premium_customer")) {
            handlePremiumCustomerRegistration(scanner, userManager, username, password);
        } else {
            userManager.registerUser(username, password, role);
        }
    }
	
	@Override
	public User execute(Scanner scanner, UserManager userManager) {
		handleRegistration(scanner, userManager);
		return null;
	}
	
}
