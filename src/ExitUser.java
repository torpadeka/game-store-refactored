import java.util.Scanner;

public class ExitUser implements MenuAction{

	@Override
	public User execute(Scanner scanner, UserManager userManager) {
        System.out.println("Exiting...");
        scanner.close();
        System.exit(0); 
		return null;
	}
	
}
