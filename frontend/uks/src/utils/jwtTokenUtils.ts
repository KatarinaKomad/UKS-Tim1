import { jwtDecode } from "jwt-decode";
import { environment } from "src/environments/environment";
import { Token } from "src/models/authentication/login";


export function getUsernameFromToken() {
    const userToken = getToken();
    if (userToken) {
        const token = jwtDecode(userToken) as Token;
        return token.sub;
    }
    return null;
}

export function getTokenExpiration(): number | null {
    const timestampString = sessionStorage.getItem(environment.TOKEN_EXPIRATION);
    if (timestampString)
        return Number(timestampString)
    return null;
}

export function getToken(): string | null {
    const token = sessionStorage.getItem(environment.TOKEN);
    return token ? token : null;
}

