import { UserBasicInfo, getEmptyUser } from '../user/user';

export interface RepoBasicInfoDTO {
  id: string;
  isPublic: boolean;
  name: string;
  owner: UserBasicInfo;
  forkCount: number;
  starCount: number;
  watchCount: number;
  defaultBranch: number;
  forkParent?: ForkParentDTO;
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

export interface RepoForkRequest {
  name: string;
  ownerId: string;
  isPublic: boolean;
  originalRepoId: string;
}

export interface ForkParentDTO {
  id: string;
  isPublic: boolean;
  name: string;
  owner: UserBasicInfo;
}

export const getEmptyRepo = (): RepoBasicInfoDTO => {
  return {
    id: '',
    isPublic: false,
    name: '',
    owner: getEmptyUser(),
    forkParent: getEmptyForkParent(),
    forkCount: 0,
    starCount: 0,
    watchCount: 0,
    defaultBranch: 0,
  }
}

export const getEmptyForkParent = (): ForkParentDTO => {
  return {
    id: '',
    isPublic: false,
    name: '',
    owner: getEmptyUser()
  }
}
