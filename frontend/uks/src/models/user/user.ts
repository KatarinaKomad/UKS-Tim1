export interface UserBasicInfo {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
    username: string;
    createdAt: Date;
}

export const getEmptyUser = (): UserBasicInfo => {
    return {
        id: '',
        email: '',
        firstName: '',
        lastName: '',
        username: '',
        createdAt: new Date()
    }
}

export interface UserDTO {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
    blockedByAdmin: boolean;
    name: string;
    username: string;
    createdAt: Date;
}

export interface UserUpdateRequest {
    firstName: string;
    lastName: string;
    email: string;
    username: string;
}

export enum REPO_INTEREST {
    STAR = 'Stargazers',
    WATCH = 'Watchers'
}