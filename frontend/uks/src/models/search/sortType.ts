
// enum SORT_TYPEs {
//     // svi
//     ANY = 'Any order',
//     NEWEST = 'Newest',         // date asc
//     OLDEST = 'Oldest',         // date desc

//     // repo
//     MOST_STARS = 'Most stars',     // count asc
//     LEAST_STARS = 'Least stars',    // count desc 

//     MOST_FORKS = 'Most forks',     // count asc
//     LEAST_FORKS = 'Least forks',    // count desc 

//     // issue
//     MOST_COMMENTS = 'Most comments',     // count asc
//     LEAST_COMMENTS = 'Least comments',    // count desc 

//     // users
//     MOST_REPOS = 'Most repositories',     // count asc
//     LEAST_REPOS = 'Least repositories',    // count desc 
// }

export enum REPO_SORT_TYPE {
    ANY = 'Any order',
    NEWEST = 'Newest',         // date asc
    OLDEST = 'Oldest',         // date desc

    MOST_STARS = 'Most stars',     // count asc
    LEAST_STARS = 'Least stars',    // count desc 

    MOST_FORKS = 'Most forks',     // count asc
    LEAST_FORKS = 'Least forks',    // count desc 
}

export enum ISSUE_PR_SORT_TYPE {
    ANY = 'Any order',
    NEWEST = 'Newest',         // date asc
    OLDEST = 'Oldest',         // date desc

    MOST_COMMENTS = 'Most comments',     // count asc
    LEAST_COMMENTS = 'Least comments',    // count desc 
}

export enum USER_SORT_TYPE {
    ANY = 'Any order',
    NEWEST = 'Newest',         // date asc
    OLDEST = 'Oldest',         // date desc

    MOST_REPOS = 'Most repositories',     // count asc
    LEAST_REPOS = 'Least repositories',    // count desc 
}

export type SORT_TYPE = USER_SORT_TYPE | ISSUE_PR_SORT_TYPE | REPO_SORT_TYPE;