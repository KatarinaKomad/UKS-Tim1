export interface UserBasicInfo {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
    username: string;
}

export const getEmptyUser = (): UserBasicInfo => {
    return {
        id: '',
        email: '',
        firstName: '',
        lastName: '',
        username: ''
    }
}