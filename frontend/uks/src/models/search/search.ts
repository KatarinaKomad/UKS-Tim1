import { ISSUE_PR_KEYWORDS, REPO_KEYWORDS, USER_KEYWORDS, formatIssuePrKeyword, formatRepoKeyword, formatUserKeyword } from "./keywords";
import { REPO_SORT_TYPE, SORT_TYPE } from "./sortType";

export enum SEARCH_TYPE {
    REPO = 'repo',
    ISSUE = 'issue',
    PR = 'pr',
    USER = 'user'
}
export enum OPERATIONS {
    AND = 'AND',
    OR = 'OR',
    NOT = 'NOT'
}


export type Keyword = REPO_KEYWORDS | ISSUE_PR_KEYWORDS | USER_KEYWORDS | string | OPERATIONS;
export type KeywordTypes = 'repo' | 'issue' | 'pr' | 'user';

export enum SignEnum {
    EQUAL = '=',
    GREATER = '>',
    LESS = '<',
    GREATER_EQUAL = '>=',
    LESS_EQUAL = '<=',
}

export const inputRequired = (keyword: Keyword): boolean => {
    return formatKeyword(keyword).includes('...');
}
export const isOnlyOneAllowed = (keyword: Keyword): boolean => {
    return keyword.startsWith('in:') || keyword.startsWith('is:') || keyword.startsWith('no:') || keyword.startsWith('fork:');
}

export const formatKeyword = (keyword: Keyword): string => {
    if (Object.values(REPO_KEYWORDS).includes(keyword as REPO_KEYWORDS)) {
        return formatRepoKeyword(keyword as REPO_KEYWORDS);
    }
    if (Object.values(ISSUE_PR_KEYWORDS).includes(keyword as ISSUE_PR_KEYWORDS)) {
        return formatIssuePrKeyword(keyword as ISSUE_PR_KEYWORDS);
    }
    if (Object.values(USER_KEYWORDS).includes(keyword as USER_KEYWORDS)) {
        return formatUserKeyword(keyword as USER_KEYWORDS);
    }
    return '';
}


// export interface SearchRequest {
//     repoKeywords: (REPO_KEYWORDS | OPERATIONS)[];
//     repoQuery: string[];
//     issueKeywords: (ISSUE_PR_KEYWORDS | OPERATIONS)[];
//     issueQuery: string[];
//     prKeywords: (ISSUE_PR_KEYWORDS | OPERATIONS)[];
//     prQuery: string[];
//     userKeywords: (USER_KEYWORDS | OPERATIONS)[];
//     userQuery: string[];
//     searchType: SEARCH_TYPE;
//     inputValue?: string;
//     sortType: SORT_TYPE
// }

export interface SearchRequest {
    keywords: Keyword[];
    query: string[];
    searchType: SEARCH_TYPE;
    inputValue: string;
    sortType: SORT_TYPE
}

export const createEmptySearchRequest = (): SearchRequest => {
    return {
        keywords: [],
        query: [],
        searchType: SEARCH_TYPE.REPO,
        sortType: REPO_SORT_TYPE.ANY,
        inputValue: ''
    }
}
// export const createEmptySearchRequest = (): SearchRequest => {
//     return {
//         repoKeywords: [],
//         repoQuery: [],
//         issueKeywords: [],
//         issueQuery: [],
//         prKeywords: [],
//         prQuery: [],
//         userKeywords: [],
//         userQuery: [],
//         searchType: SEARCH_TYPE.REPO,
//         sortType: REPO_SORT_TYPE.ANY
//     }
// }

export interface SearchResult {

}