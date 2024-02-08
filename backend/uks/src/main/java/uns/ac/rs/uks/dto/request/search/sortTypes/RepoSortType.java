package uns.ac.rs.uks.dto.request.search.sortTypes;

public enum RepoSortType implements SortType{
    ANY ("Any order"),
    NEWEST ("Newest"),         // date asc
    OLDEST ("Oldest"),         // date desc

    MOST_STARS("Most stars"),     // count asc
    LEAST_STARS ("Least stars"),    // count desc

    MOST_FORKS ("Most forks"),     // count asc
    LEAST_FORKS ("Least forks");   // count desc

    private final String name;

    private RepoSortType(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }
}
