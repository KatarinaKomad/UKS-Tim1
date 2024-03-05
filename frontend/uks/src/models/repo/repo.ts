import { SearchResult } from '../search/search';
import { UserBasicInfo, getEmptyUser } from '../user/user';

export interface RepoBasicInfoDTO extends SearchResult {
  id: string;
  isPublic: boolean;
  name: string;
  description: string;
  owner: UserBasicInfo;
  forkCount: number;
  starCount: number;
  watchCount: number;
  defaultBranch: number;
  forkParent?: ForkParentDTO;
  createdAt?: Date;
}

export interface RepoUpdateRequest {
  name: string;
  description: string;
  isPublic: boolean;
  defaultBranch: number;
}

export interface RepoRequest {
  name: string;
  description: string;
  ownerId?: string;
  isPublic?: boolean;
}

export interface EditRepoRequest {
  userId: string;
  repoId: String;
  name?: string;
  description?: string;
  ownerId?: string;
  isPublic?: boolean;
}

export interface RepoForkRequest {
  name: string;
  description: string;
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
    description: '',
    owner: getEmptyUser(),
    forkParent: getEmptyForkParent(),
    forkCount: 0,
    starCount: 0,
    watchCount: 0,
    defaultBranch: 0,
    createdAt: new Date()
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


export interface RepoUserRequest {
  userId: string;
  repoId: string;
}
export interface WatchStarResponseDTO {
  watching: boolean;
  stargazing: boolean;
}