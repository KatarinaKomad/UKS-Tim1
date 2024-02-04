export interface UserBasicInfo {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
}

export const getEmptyUser = (): UserBasicInfo => {
    return {
        id: '',
        email: '',
        firstName: '',
        lastName: ''
    }
}