import { BranchBasicInfoDTO } from "../branch/branch";
import { ISSUE_EVENT_TYPE } from "../issue/issue";
import { LabelDTO } from "../label/label";
import { MilestoneDTO } from "../milestone/milestone";
import { RepoBasicInfoDTO } from "../repo/repo";
import { STATE } from "../state/state";
import { UserBasicInfo } from "../user/user";

export interface PullRequestDTO {
    id: string;
    name: string;
    author: UserBasicInfo;
    milestone: MilestoneDTO;
    createdAt: Date;
    counter: number;
    labels: LabelDTO[];
    assignees: UserBasicInfo[];
    state: STATE;
    description: string;
    origin: BranchBasicInfoDTO;
    target: BranchBasicInfoDTO;
    repo: RepoBasicInfoDTO;
}

export interface UserPullRequestDTO {
    createdPRs: PullRequestDTO[];
    assignedPRs: PullRequestDTO[];
}

export interface PullRequestProperties {
    assignees?: UserBasicInfo[];
    labels?: LabelDTO[];
    milestone?: MilestoneDTO;
    [key: string]: any;
}

export interface PullRequestEventDTO {
    id: number;
    author: UserBasicInfo;
    value: string;
    type: ISSUE_EVENT_TYPE;
    createdAt: Date;
}

export interface PullRequestBasicInfoDTO {
    id: string;
    counter: number;
    name: string;
    description: string;
    state: STATE;
}

export interface PullRequestRequest extends PullRequestItem {
    repoId: string;
}

export interface PullRequestEventRequest extends PullRequestItem {
    prId: string;
    type: ISSUE_EVENT_TYPE;
}

interface PullRequestItem {
    authorId: string; // can't be updated
    name?: string;
    description?: string;
    state?: STATE;
    assigneeIds?: string[];
    milestoneId?: number;
    labelIds?: number[];
    originId?: number;
    targetId?: number;
}