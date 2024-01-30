
export enum STATE {
    OPEN = "OPEN",
    CLOSE = "CLOSE",
    MERGED = "MERGED"
}

export interface ChangeStateRequest {

    id: number;
    state: STATE;
}
