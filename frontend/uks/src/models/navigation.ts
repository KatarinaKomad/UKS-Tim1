export enum SelectionOptions {
    HOME = '/home',
    ISSUES = '/issues',
    PULL_REQUESTS = '/pull-requests',
    REPOSITORIES = '/repositories',
    REPOSITORY = '/repository/',
    SEARCH = '/search',
}

export const titleMapper = (selection: SelectionOptions): string => {
    switch (selection) {
        case SelectionOptions.HOME: return 'Dashboard';
        case SelectionOptions.ISSUES: return 'Issues';
        case SelectionOptions.PULL_REQUESTS: return 'Pull Requests';
        case SelectionOptions.REPOSITORIES: return 'Repositories';
        case SelectionOptions.SEARCH: return 'Search';
        default: return '';
    }
}
