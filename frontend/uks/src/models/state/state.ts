
export enum STATE {
    OPEN = "OPEN",
    CLOSE = "CLOSE",
    MERGED = "MERGED"
}

export interface ChangeStateRequest {

    id: number;
    state: STATE;
}


export enum STATE_COLORS {
    OPEN = "#1f883d",
    CLOSE = "#8250df",
    MERGED = "#8250df"
}