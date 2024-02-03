import { UserBasicInfo } from '../user/user';

export interface RepoBasicInfoDTO {
  id: string;
  isPublic: boolean;
  name: string;
  owner: UserBasicInfo;
  forkCount: number;
  starCount: number;
  watchCount: number;
  defaultBranch: number;
}

export interface RepoUpdateRequest {
  name: string;
  isPublic: boolean;
  defaultBranch: number;
}

export interface RepoRequest {
  name: string;
  ownerId?: string;
  isPublic?: boolean;
}

export interface EditRepoRequest {
  userId: string;
  repoId: String;
  name?: string;
  ownerId?: string;
  isPublic?: boolean;
}
