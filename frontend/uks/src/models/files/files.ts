import { CommitsResponseDto } from "../branch/branch";

export interface FileRequest {
    repoName: string;
    branchName: string;
    filePath: string;
}

export interface FileDTO {
    name: string;
    path: string;
    content?: string | undefined;
    isFolder: boolean;
    commitHistory?: CommitsResponseDto[];
    parentPath?: string | undefined;
}