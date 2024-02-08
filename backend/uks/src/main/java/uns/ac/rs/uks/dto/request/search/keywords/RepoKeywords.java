package uns.ac.rs.uks.dto.request.search.keywords;

public enum RepoKeywords  implements Keyword {
    REPO("repo:"), // name
    OWNER("owner:"), // name
    IN_NAME("in:name"),
    IN_DESCRIPTION("in:description"),

    FORK_TRUE("fork:true"),
    FORK_ONLY("fork:only"),

    NUMBER_FORKS("number_forks:"), // n, >n, >=n, <n, <=n, n..n
    NUMBER_WATCHERS("number_watchers:"), // n, >n, >=n, <n, <=n, n..n
    NUMBER_STARS("number_stars:"), // n, >n, >=n, <n, <=n, n..n

    IS_PUBLIC("is:public"),
    IS_PRIVATE("is:private"),

    CREATED("created:"); // DD-MM-YYYY, <DD-MM-YYYY,  <=DD-MM-YYYY, >DD-MM-YYYY, >=DD-MM-YYYY

    private final String name;

    private RepoKeywords(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }
}
