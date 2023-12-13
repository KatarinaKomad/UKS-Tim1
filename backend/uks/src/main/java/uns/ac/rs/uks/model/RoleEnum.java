package uns.ac.rs.uks.model;

public enum RoleEnum {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    private final String name;

    private RoleEnum(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }

}
