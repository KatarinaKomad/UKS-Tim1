import { CommitsResponseDto } from "../branch/branch";

export interface FileRequest {
    repoId: string;
    branchName: string;
    filePath: string;
}

export interface FileDTO {
    name: string;
    path: string;
    content: string;
    isFolder: boolean;
    commitHistory: CommitsResponseDto[];
}