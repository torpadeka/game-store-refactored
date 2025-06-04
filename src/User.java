import java.util.Scanner;

public abstract class User implements MenuHandlerProvider {
    private String username;
    private String password;
    private UserRole role; // Changed from String to UserRole

    public User(String username, String password, String roleString) { // Keep this constructor for backward compatibility or initial loading if needed
        this.username = username;
        this.password = password;
        this.role = UserRole.fromString(roleString); // Convert string to enum
    }
    
    // New constructor to directly accept UserRole enum
    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() { 
        return role;
    }

    public void setRole(String roleString) { 
        this.role = UserRole.fromString(roleString); 
        System.out.println("User role changed to " + roleString + " post-creation.");
    }
    
    public void setRole(UserRole role) { 
        this.role = role;
        System.out.println("User role changed to " + role.getRoleName() + " post-creation.");
    }

    public boolean checkPassword(String passwordAttempt) {
        return this.password.equals(passwordAttempt);
    }

    public void performAdminAction(java.util.Scanner scanner, UserManager userManager, StoreService storeService) {
        System.out.println("User " + username + " does not have specific admin actions.");
    }

    public abstract boolean handleMenu(Scanner scanner, UserManager userManager, StoreService storeService);
}