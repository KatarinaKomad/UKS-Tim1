package uns.ac.rs.uks.dto.request.search.sortTypes;

public enum UserSortType implements SortType{
    ANY ("Any order"),
    NEWEST ("Newest"),         // date asc
    OLDEST ("Oldest"),         // date desc

    MOST_REPOS("Most repositories"),     // count asc
    LEAST_REPOS ("Least repositories");    // count desc

    private final String name;

    private UserSortType(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }
}
