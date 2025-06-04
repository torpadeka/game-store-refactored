public enum UserRole {
    CUSTOMER("customer"),
    PREMIUM_CUSTOMER("premium_customer"),
    STORE_OWNER("store_owner"),
    ADMIN("admin");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static UserRole fromString(String text) {
        for (UserRole r : UserRole.values()) {
            if (r.roleName.equalsIgnoreCase(text)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No role with text " + text + " found");
    }
}