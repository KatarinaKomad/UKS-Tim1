import { IssueBasicInfoDTO } from "../issue/issue";
import { STATE } from "../state/state";

export interface MilestoneRequest {
    id?: number;
    name: string;
    description?: string;
    repoId: string;
    dueDate: Date;
}


export interface MilestoneDTO {
    id: number;
    name: string;
    description: string;
    repoId: string;
    dueDate: any;
    state: STATE;
    issues: IssueBasicInfoDTO[];
}
