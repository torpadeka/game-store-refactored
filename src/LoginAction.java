import java.util.Scanner;

public class LoginAction implements MenuAction{
	
    private static User handleLogin(Scanner scanner, UserManager userManager) {
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();
        return userManager.loginUser(loginUsername, loginPassword);
    }
	@Override
	public User execute(Scanner scanner, UserManager userManager) {
		return handleLogin(scanner, userManager);
	}

}
