export enum SelectionOptions {
    HOME = '/home',
    ISSUES = '/issues',
    PULL_REQUESTS = '/pull-requests',
    REPOSITORIES = '/repositories',
    REPOSITORY = '/repository/',
    MY_PROFILE = '/profile',
    PROFILE = '/profile/',
    SSHKEY = '/sshkey',
    SEARCH = '/search'
}

export const titleMapper = (selection: SelectionOptions): string => {
    switch (selection) {
        case SelectionOptions.HOME: return 'Dashboard';
        case SelectionOptions.ISSUES: return 'Issues';
        case SelectionOptions.PULL_REQUESTS: return 'Pull Requests';
        case SelectionOptions.REPOSITORIES: return 'Repositories';
        case SelectionOptions.PROFILE: return 'Profile';
        case SelectionOptions.MY_PROFILE: return 'My profile';
        case SelectionOptions.SSHKEY: return 'SSH key';
        case SelectionOptions.SEARCH: return 'Search';
        default: return '';
    }
}

export enum TAB_VIEW {
    ISSUES = 'issues-view',
    PRS = 'pr-view',
    LABELS = 'labels-view',
    MILESTONES = 'milestones-view',
}

export interface YesNoPrompt {
    title: string;
    prompt: string;
}