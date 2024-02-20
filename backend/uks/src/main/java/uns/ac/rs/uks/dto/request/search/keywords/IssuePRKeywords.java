package uns.ac.rs.uks.dto.request.search.keywords;

public enum IssuePRKeywords implements Keyword {

    IN_NAME("in:name"),
    IN_DESCRIPTION("in:description"),

    IS_OPEN("is:open"),
    IS_CLOSED("is:closed"),

    AUTHOR("author:"), // username
    ASSIGNEE("assignee:"), // username

    LABEL("label:"),    // name
    MILESTONE("milestone:"), //name
    NO_LABEL("no:label"),
    NO_MILESTONE("no:milestone"),


    CREATED("created:"); // DD-MM-YYYY, <DD-MM-YYYY,  <=DD-MM-YYYY, >DD-MM-YYYY, >=DD-MM-YYYY

    private final String name;

    private IssuePRKeywords(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }
}
