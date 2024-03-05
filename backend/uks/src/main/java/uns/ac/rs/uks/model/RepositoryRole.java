package uns.ac.rs.uks.model;

import java.io.Serializable;

public enum RepositoryRole implements Serializable {
    OWNER("OWNER"),
    COLLABORATOR("COLLABORATOR"),
    CONTRIBUTOR("CONTRIBUTOR"),
    VIEWER("VIEWER");

    private final String name;

    private RepositoryRole(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }
}
