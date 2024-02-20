package uns.ac.rs.uks.dto.request.search.sortTypes;

public enum IssuePrSortType implements SortType{
    ANY ("Any order"),
    NEWEST ("Newest"),         // date asc
    OLDEST ("Oldest"),         // date desc

    MOST_COMMENTS("Most comments"),     // count asc
    LEAST_COMMENTS ("Least comments");    // count desc

    private final String name;

    private IssuePrSortType(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }
}
