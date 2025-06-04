import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, User> users;

    public UserManager() {
        this.users = new HashMap<>();
    }

    public boolean registerUser(String username, String password, String roleString) {
        try {
            UserRole role = UserRole.fromString(roleString);
            if (role == UserRole.PREMIUM_CUSTOMER) {
                return registerUser(username, password, role, 0.1);
            }
            return registerUser(username, password, role, 0.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role for registration: " + roleString);
            return false;
        }
    }

    public boolean registerUser(String username, String password, UserRole role) {
        return registerUser(username, password, role, 0.0); // Default discountRate for non-premium roles
    }

    public boolean registerUser(String username, String password, UserRole role, double discountRate) {
        if (this.users.containsKey(username)) {
            System.out.println("Username already exists!");
            return false;
        }

        User newUser;
        switch (role) {
            case CUSTOMER:
                newUser = new Customer(username, password);
                break;
            case PREMIUM_CUSTOMER:
                newUser = new PremiumCustomer(username, password, discountRate);
                break;
            case STORE_OWNER:
                newUser = new StoreOwner(username, password);
                break;
            case ADMIN: 
                System.out.println("Admin role not yet fully implemented for registration.");
                return false;
            default:
                System.out.println("Invalid role for registration!");
                return false;
        }

        this.users.put(username, newUser);
        System.out.println(role.getRoleName().substring(0, 1).toUpperCase() + role.getRoleName().substring(1).toLowerCase().replace("_", " ") + " registration successful!");
        return true;
    }

    public User loginUser(String username, String password) {
        User user = this.users.get(username);

        if (user != null && user.checkPassword(password)) {
            System.out.println("Login successful! Welcome " + user.getUsername() + " (" + user.getRole().getRoleName() + ")");
            return user;
        } else {
            System.out.println("Invalid username or password!");
            return null;
        }
    }

    public User getUserByUsername(String username) {
        return this.users.get(username);
    }
}