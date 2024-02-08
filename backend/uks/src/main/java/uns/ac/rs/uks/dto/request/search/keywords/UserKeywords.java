package uns.ac.rs.uks.dto.request.search.keywords;

public enum UserKeywords  implements Keyword {
    USERNAME("user:"), //name
    IN_NAME("in:name"),
    IN_EMAIL("in:email"),

    FULLNAME("fullname:"), // fName lName -> sensitive to space

    REPO_COUNT("number_repos"), // n, >n, >=n, <n, <=n, n..n
    CREATED("created:"); // DD-MM-YYYY, <DD-MM-YYYY,  <=DD-MM-YYYY, >DD-MM-YYYY, >=DD-MM-YYYY

    private final String name;

    private UserKeywords(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }
}
