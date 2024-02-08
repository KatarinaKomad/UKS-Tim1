package uns.ac.rs.uks.dto.request.search.keywords;

public enum Operations implements Keyword {
    AND("AND"),
    OR("OR"),

    NOT("NOT"),
    NONE("NONE");

    private final String name;

    private Operations(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }
}
