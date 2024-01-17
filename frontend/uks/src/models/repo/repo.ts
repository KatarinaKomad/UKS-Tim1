import { UserBasicInfo } from "../user/user";

export interface RepoBasicInfoDTO {
    id: string;
    isPublic: boolean;
    name: string;
    owner: UserBasicInfo;
    forkCount: number;
    starCount: number;
    watchCount: number;

}