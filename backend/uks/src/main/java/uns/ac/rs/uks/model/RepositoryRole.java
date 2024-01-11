package uns.ac.rs.uks.model;

public enum RepositoryRole {
    OWNER("OWNER"),
    COLLABORATOR("COLLABORATOR"),
    CONTRIBUTOR("CONTRIBUTOR");

    private final String name;

    private RepositoryRole(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }
}
