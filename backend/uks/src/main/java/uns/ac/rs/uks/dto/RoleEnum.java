package uns.ac.rs.uks.dto;


public enum RoleEnum {
    ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_OWNER("ROLE_OWNER"),
    ROLE_TENANT("ROLE_TENANT");

    private final String name;

    private RoleEnum(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }
}