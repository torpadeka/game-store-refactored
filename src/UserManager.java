
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, User> users;

    public UserManager() {
        this.users = new HashMap<>();
    }

    public boolean registerUser(String username, String password, String role) {
        if (role.equalsIgnoreCase("premium_customer")) {
            return registerUser(username, password, role, 0.1);
        }
        return registerUser(username, password, role, 0.0);
    }

    public boolean registerUser(String username, String password, String role, double discountRate) {
        if (this.users.containsKey(username)) {
            System.out.println("Username already exists!");
            return false;
        }

        User newUser;
        if (role.equalsIgnoreCase("customer")) {
            newUser = new Customer(username, password);
        } else if (role.equalsIgnoreCase("premium_customer")) {
            newUser = new PremiumCustomer(username, password, discountRate);
        } else if (role.equalsIgnoreCase("store_owner")) {
            newUser = new StoreOwner(username, password);
        } else {
            System.out.println("Invalid role for registration!");
            return false;
        }

        this.users.put(username, newUser);
        System.out.println(role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase().replace("_", " ") + " registration successful!");
        return true;
    }

    public User loginUser(String username, String password) {
        User user = this.users.get(username);

        if (user != null && user.checkPassword(password)) {
            System.out.println("Login successful! Welcome " + user.getUsername());
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
