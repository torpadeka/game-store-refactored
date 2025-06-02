import java.util.Scanner;

public interface MenuHandlerProvider {
	boolean handleMenu(Scanner scanner,UserManager userManager, StoreService storeService);
}
