import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<Username, User> users;

    public UserManager() {
        this.users = new HashMap<>();
    }

    public boolean registerUser(Username username, Password password, UserRole role) {
        if (role == UserRole.PREMIUM_CUSTOMER) {
            return registerUser(username, password, role, new DiscountRate(0.1));
        }
        return registerUser(username, password, role, new DiscountRate(0.0));
    }

    public boolean registerUser(Username username, Password password, UserRole role, DiscountRate discountRate) {
        if (this.users.containsKey(username)) {
            System.out.println("Username already exists!");
            return false;
        }

        User newUser = createUserBasedOnRole(username, password, role, discountRate);
        if (newUser == null) {
            System.out.println("Invalid role for registration!");
            return false;
        }

        this.users.put(username, newUser);
        System.out.println(role.getRoleName().substring(0, 1).toUpperCase() + role.getRoleName().substring(1).toLowerCase().replace("_", " ") + " registration successful!");
        return true;
    }

    private User createUserBasedOnRole(Username username, Password password, UserRole role, DiscountRate discountRate) {
        if (role == UserRole.CUSTOMER) {
            return new Customer(username, password);
        } else if (role == UserRole.PREMIUM_CUSTOMER) {
            return new PremiumCustomer(username, password, discountRate);
        } else if (role == UserRole.STORE_OWNER) {
            return new StoreOwner(username, password);
        }
        return null; 
    }

    public User loginUser(Username username, Password password) {
        User user = this.users.get(username);

        if (user != null && user.checkPassword(password)) {
            System.out.println("Login successful! Welcome " + user.getUsername().getValue());
            return user;
        } else {
            System.out.println("Invalid username or password!");
            return null;
        }
    }

    public User getUserByUsername(Username username) {
        return this.users.get(username);
    }
}
