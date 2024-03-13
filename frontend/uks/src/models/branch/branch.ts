import { UserBasicInfo } from "../user/user";

export interface BranchBasicInfoDTO {
  id: string;
  name: string;
}

export interface BranchDTO {
  id: number;
  name: string;
  updatedAt: Date;
  updatedBy: string;
}

export interface OriginTargetBranchRequest {
  repoId: string;
  originName: string;
  targetName: string;
}

export interface TargetBranchRequest {
  repoId: string;
  branchName: string;
}

export interface CommitsResponseDto {
  hash: string;
  message: string;
  gitUser: string;
  timeAgo: string;
}