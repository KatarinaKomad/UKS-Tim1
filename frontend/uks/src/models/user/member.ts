import { REPO_ROLE } from "./user";

export enum MEMBER_INVITE_STATUS {
    PENDING = "PENDING",
    ACCEPTED = "ACCEPTED",
    REJECTED = "REJECTED"
}


export interface RepoMemberDTO {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
    username: string;
    repositoryRole: REPO_ROLE;
    repoName: string;
    repoId: string;
    createdAt: Date;
    inviteStatus: MEMBER_INVITE_STATUS;
}

export interface ChangeMemberRoleRequest {
    repositoryRole: REPO_ROLE;
    userId: string;
    repoId: string;

}
