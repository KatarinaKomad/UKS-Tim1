import { LabelDTO } from "../label/label";
import { MilestoneDTO } from "../milestone/milestone";
import { RepoBasicInfoDTO } from "../repo/repo";
import { STATE } from "../state/state";
import { UserBasicInfo } from "../user/user";

export interface IssueRequest extends IssueItem {
    repoId: string;
}
export interface IssueEventRequest extends IssueItem {
    issueId: string;
    type: ISSUE_EVENT_TYPE;
}

interface IssueItem {
    authorId: string; // can't be updated
    name?: string;
    description?: string;
    state?: STATE;
    assigneeIds?: string[];
    milestoneId?: number;
    labelIds?: number[];
}


export interface IssueDTO {
    id: string;
    counter: number;
    name: string;
    description: string;
    state: STATE;
    author: UserBasicInfo;
    createdAt: Date;
    assignees: UserBasicInfo[];
    milestone: MilestoneDTO;
    labels: LabelDTO[];
    participants: UserBasicInfo[];
    repo: RepoBasicInfoDTO;
}

export interface IssueBasicInfoDTO {
    id: string;
    counter: number;
    name: string;
    description: string;
    state: STATE;
}

export interface IssueEventDTO {
    id: number;
    author: UserBasicInfo;
    value: string;
    type: ISSUE_EVENT_TYPE;
    createdAt: Date;
}

export enum ISSUE_EVENT_TYPE {
    LABEL = "LABEL",
    ASSIGNEE = "ASSIGNEE",
    MILESTONE = "MILESTONE",
    NAME = "NAME",
    DESCRIPTION = "DESCRIPTION",
    STATE = "STATE",
    COMMENT = "COMMENT",
    PR_REF = "PR_REF",
    PR_REVIEW = "PR_REVIEW",
    COMMIT_REF = "COMMIT_REF"
}

export enum ISSUE_PROPERTIES {
    LABEL = "LABEL",
    ASSIGNEE = "ASSIGNEE",
    MILESTONE = "MILESTONE",
    AUTHOR = "AUTHOR"
}

export interface IssueProperties {
    assignees?: UserBasicInfo[];
    labels?: LabelDTO[];
    milestone?: MilestoneDTO;
}

export interface UserIssuesDTO {
    createdIssues: IssueDTO[];
    assignedIssues: IssueDTO[];
}
