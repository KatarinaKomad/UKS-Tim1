import { ISSUE_EVENT_TYPE } from "../issue/issue";
import { LabelDTO } from "../label/label";
import { UserBasicInfo } from "../user/user";

export interface PullRequestDTO {
    id: string;
    name: string;
    author: string;
    milestone: string;
    createdAt: Date;
    counter: number;
    labels: LabelDTO[];
    assignees: string[];
    state: string;
    description: string;
    origin: string;
    target: string;
    repo: string;
}

export interface UserPullRequestDTO {
    createdPRs: PullRequestDTO[];
    assignedPRs: PullRequestDTO[];
}

export interface PullRequestProperties {
    assignees?: string[];
    labels?: LabelDTO[];
    milestone?: string;
}

export interface PullRequestEventDTO {
    id: number;
    author: UserBasicInfo;
    value: string;
    type: ISSUE_EVENT_TYPE;
    createdAt: Date;
}