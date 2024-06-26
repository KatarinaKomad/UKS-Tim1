export class Login {
  constructor(public email: string, public password: string) { }
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface TokenResponse {
  accessToken: string;
  expiresAt: number;
}

export interface Token {
  sub: string;
}


export interface PasswordUpdateRequest {
  email: string;
  currentPassword: string;
  password: string;
  passwordConfirmation: string;
}

export interface PasswordResetRequest {
  email: string;
}