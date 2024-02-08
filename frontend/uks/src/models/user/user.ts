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