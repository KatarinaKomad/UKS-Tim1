import { UserBasicInfo } from "../user/user";

export interface BranchBasicInfoDTO {
  id: string;
  name: string;
}

export interface BranchDTO {
  id: number;
  name: string;
  createdAt: Date;
  updatedAt: Date;
  updatedBy: UserBasicInfo;
  repoId: string;
  // private List<Commit> commits;
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