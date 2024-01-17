import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LoginRequest, TokenResponse } from 'src/models/authentication/login';
import { RegistrationRequest } from 'src/models/authentication/registration';
import { UserBasicInfo } from 'src/models/user/user';
import { HttpRequestService } from 'src/utils/http-request.service';
import { getTokenExpiration } from 'src/utils/jwtTokenUtils';

import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private loggedUserSubject = new BehaviorSubject<UserBasicInfo | undefined>(
    undefined
  );

  constructor(
    private httpRequestService: HttpRequestService,
    private router: Router
  ) {
    if (this.loggedUserSubject.value === undefined) {
      this.getCurrentUser().subscribe({
        next: (user: UserBasicInfo) => {
          this.onNewUserReceived(user);
        },
      });
    }
  }

  login(loginRequest: LoginRequest): Observable<TokenResponse> {
    const url = environment.API_BASE_URL + '/auth/login';
    const body = JSON.stringify(loginRequest);

    return this.httpRequestService.post(url, body) as Observable<TokenResponse>;
  }

  logout(): void {
    sessionStorage.clear();
    localStorage.clear();
    this.onNewUserReceived(undefined);
    this.router.navigate(['/login']);
  }

  signup(signupRequest: RegistrationRequest): Observable<UserBasicInfo | null> {
    const url = environment.API_BASE_URL + '/auth/register';
    const body = JSON.stringify(signupRequest);

    return this.httpRequestService.post(
      url,
      body
    ) as Observable<UserBasicInfo | null>;
  }

  getLoggedUser(): Observable<UserBasicInfo | undefined> {
    if (this.loggedUserSubject.value === undefined) {
      this.getCurrentUser().subscribe({
        next: (user: UserBasicInfo) => {
          this.onNewUserReceived(user);
        },
      });
    }
    return this.loggedUserSubject.asObservable();
  }

  handleSuccessfulAuth(token: TokenResponse): void {
    sessionStorage.setItem(
      environment.TOKEN_EXPIRATION,
      token.expiresAt.toString()
    );
    sessionStorage.setItem(environment.TOKEN, token.accessToken);

    this.getCurrentUser().subscribe({
      next: (user: UserBasicInfo) => {
        this.onNewUserReceived(user);
        this.router.navigate(['/home']);
      },
      error: (e: HttpErrorResponse) => {
        console.log(e);
      },
    });
  }

  isAuthenticated(): boolean {
    const tokenExpiration = getTokenExpiration();
    if (tokenExpiration) {
      return Date.now() < tokenExpiration;
    }
    return false;
  }

  private onNewUserReceived(msg: UserBasicInfo | undefined) {
    this.loggedUserSubject.next(msg);
  }

  private getCurrentUser(): Observable<UserBasicInfo> {
    const url = environment.API_BASE_URL + '/auth/me';
    return this.httpRequestService.get(url) as Observable<UserBasicInfo>;
  }
}
