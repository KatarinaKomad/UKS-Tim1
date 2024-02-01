package uns.ac.rs.uks.model;

public enum IssueEventType {

    LABEL("LABEL"),             // value -> name of label
    ASSIGNEE("ASSIGNEE"),       // value -> name of assignees
    MILESTONE("MILESTONE"),     // value -> name of milestone
    NAME("NAME"),               // value -> name of issue
    DESCRIPTION("DESCRIPTION"), // value -> changed description of issue
    STATE("STATE"),             // value -> open, close
    COMMENT("COMMENT"),         // value -> first 30char of comment
    PR_REF("PR_REF"),           // value -> open, closed, merged + target branch
    PR_REVIEW("PR_REVIEW"),     // value -> request, approve, reject
    COMMIT_REF("COMMIT_REF");  // value -> some kind of id?

    private final String name;

    private IssueEventType(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }
}
