export enum REPO_KEYWORDS {
    REPO = "repo:",         // name
    OWNER = "owner:",       // name

    IN_NAME = "in:name",
    IN_DESCRIPTION = "in:description",

    FORK_TRUE = "fork:true",
    FORK_ONLY = "fork:only",

    NUMBER_FORKS = "number_forks:",       // n, >n, >=n, <n, <=n, n..n
    NUMBER_WATCHERS = "number_watchers:", // n, >n, >=n, <n, <=n, n..n
    NUMBER_STARS = "number_stars:",       // n, >n, >=n, <n, <=n, n..n

    IS_PUBLIC = "is:public",
    IS_PRIVATE = "is:private",

    CREATED = "created:"    // DD-MM-YYYY, <DD-MM-YYYY,  <=DD-MM-YYYY, >DD-MM-YYYY, >=DD-MM-YYYY
}

const repoKeywordNames = {
    [REPO_KEYWORDS.REPO]: 'Repository name...',
    [REPO_KEYWORDS.OWNER]: 'Owner name/username...',
    [REPO_KEYWORDS.IN_NAME]: 'In name',
    [REPO_KEYWORDS.IN_DESCRIPTION]: 'In description',
    [REPO_KEYWORDS.FORK_TRUE]: 'Include forks',
    [REPO_KEYWORDS.FORK_ONLY]: 'Only forks',
    [REPO_KEYWORDS.NUMBER_FORKS]: 'Number of forks...',
    [REPO_KEYWORDS.NUMBER_WATCHERS]: 'Number of watchers...',
    [REPO_KEYWORDS.NUMBER_STARS]: 'Number of stars...',
    [REPO_KEYWORDS.IS_PUBLIC]: 'Public repositories',
    [REPO_KEYWORDS.IS_PRIVATE]: 'Private repositories',
    [REPO_KEYWORDS.CREATED]: 'Created at...',
};

export const formatRepoKeyword = (keyword: REPO_KEYWORDS): string => {
    return repoKeywordNames[keyword] || '';
}


export enum ISSUE_PR_KEYWORDS {
    IN_NAME = "in:name",
    IN_DESCRIPTION = "in:description",

    IS_OPEN = "is:open",
    IS_CLOSED = "is:closed",

    AUTHOR = "author:", // username
    ASSIGNEE = "assignee:", // username

    LABEL = "label:",    // name
    MILESTONE = "milestone:", //name
    NO_LABEL = "no:label",
    NO_MILESTONE = "no:milestone",

    CREATED = "created:" // DD-MM-YYYY, <DD-MM-YYYY,  <=DD-MM-YYYY, >DD-MM-YYYY, >=DD-MM-YYYY
}

const issuePrKeywordNames = {
    [ISSUE_PR_KEYWORDS.IN_NAME]: 'In name',
    [ISSUE_PR_KEYWORDS.IN_DESCRIPTION]: 'In description',
    [ISSUE_PR_KEYWORDS.IS_OPEN]: 'Only open',
    [ISSUE_PR_KEYWORDS.IS_CLOSED]: 'Only closed',
    [ISSUE_PR_KEYWORDS.AUTHOR]: 'Author username...',
    [ISSUE_PR_KEYWORDS.ASSIGNEE]: 'Assignee username...',
    [ISSUE_PR_KEYWORDS.LABEL]: 'Label name...',
    [ISSUE_PR_KEYWORDS.MILESTONE]: 'Milestone name...',
    [ISSUE_PR_KEYWORDS.NO_LABEL]: 'No label',
    [ISSUE_PR_KEYWORDS.NO_MILESTONE]: 'No milestone',
    [ISSUE_PR_KEYWORDS.CREATED]: 'Created at...',
};

export const formatIssuePrKeyword = (keyword: ISSUE_PR_KEYWORDS): string => {
    return issuePrKeywordNames[keyword] || '';
}


export enum USER_KEYWORDS {
    USERNAME = "user:", //name
    IN_NAME = "in:name",
    IN_EMAIL = "in:email",

    REPO_COUNT = "number_repos", // n, >n, >=n, <n, <=n, n..n
    CREATED = "created:"
}

const userKeywordNames = {
    [USER_KEYWORDS.USERNAME]: 'Username...',
    [USER_KEYWORDS.IN_NAME]: 'In name',
    [USER_KEYWORDS.IN_EMAIL]: 'In email',
    [USER_KEYWORDS.REPO_COUNT]: 'Number of repositories...',
    [USER_KEYWORDS.CREATED]: 'Created at...',
};

export const formatUserKeyword = (keyword: USER_KEYWORDS): string => {
    return userKeywordNames[keyword] || '';
}
