
public class User {
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
        System.out.println("User role changed to " + role + " post-creation.");
    }

    public boolean checkPassword(String passwordAttempt) {
        return this.password.equals(passwordAttempt);
    }

    public void performAdminAction(java.util.Scanner scanner, UserManager userManager, StoreService storeService) {
        System.out.println("User " + username + " does not have specific admin actions.");
    }
}
