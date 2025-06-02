import java.util.Scanner;

public class InvalidAction implements MenuAction{
	
    @Override
    public User execute(Scanner scanner, UserManager userManager) {
        System.out.println("Invalid option!");
        return null;
    }


}
