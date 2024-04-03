export interface CommitsResponseDto {
    hash: string;
    message: string;
    gitUser: string;
    timeAgo: string;
}

export interface CommitDiffRequest {
    repoId: string;
    branchName: string;
    commit: string;
}

export interface CommitDiffResponseDTO {
    stats: string;
    fileChanges: FileChangeResponseDTO[];
}

export interface FileChangeResponseDTO {
    fileName: string;
    changes: string;
    stats: string;
}