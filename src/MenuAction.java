import java.util.Scanner;

public interface MenuAction {
	User execute(Scanner scanner,UserManager userManager);
}
