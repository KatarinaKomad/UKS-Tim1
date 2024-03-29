import { IssueBasicInfoDTO, IssueDTO } from "../issue/issue";

export interface LabelRequest {
    id?: number;
    name: string;
    color: string;
    description?: string;
    repoId: string;
}


export interface LabelDTO {
    id: number;
    name: string;
    color: string;
    description: string;
    repoId: string;
    issues: IssueBasicInfoDTO[];
}