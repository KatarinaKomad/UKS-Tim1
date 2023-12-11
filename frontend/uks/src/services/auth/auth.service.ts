import { HttpErrorResponse } from '@angular/common/http';
import { Token } from '@angular/compiler';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LoginRequest, TokenResponse } from 'src/models/authentication/login';
import { LoggedUser } from 'src/models/user/user';
import { HttpRequestService } from 'src/utils/http-request.service';
import { getTokenExpiration } from 'src/utils/jwtTokenUtils';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loggedUserSubject = new BehaviorSubject<LoggedUser | undefined>(undefined);

  constructor(
    private httpRequestService: HttpRequestService,
    private router: Router
  ) {
    if (this.loggedUserSubject.value === undefined) {
      this.getCurrentUser().subscribe({
        next: (user: LoggedUser) => {
          this.onNewUserReceived(user);
        }
      });
    }
  }


  login(loginRequest: LoginRequest): Observable<TokenResponse> {
    const url = environment.API_BASE_URL + "/auth/login";
    const body = JSON.stringify(loginRequest);

    return this.httpRequestService.post(url, body) as Observable<TokenResponse>;
  }

  logout(): void {
    sessionStorage.clear();
    localStorage.clear();
    this.onNewUserReceived(undefined);
    this.router.navigate(['/login']);
  }

  getLoggedUser(): Observable<LoggedUser | undefined> {
    return this.loggedUserSubject.asObservable();
  }


  handleSuccessfulAuth(token: TokenResponse): void {
    sessionStorage.setItem(environment.TOKEN_EXPIRATION, token.expiresAt.toString());
    sessionStorage.setItem(environment.TOKEN, token.accessToken);

    this.getCurrentUser().subscribe({
      next: (user: LoggedUser) => {
        this.onNewUserReceived(user);
        this.router.navigate(['/home']);
      },
      error: (e: HttpErrorResponse) => {
        console.log(e);
      }
    })
  }

  isAuthenticated(): boolean {
    const tokenExpiration = getTokenExpiration();
    if (tokenExpiration) {
      return Date.now() < tokenExpiration;
    }
    return false;
  }

  private onNewUserReceived(msg: LoggedUser | undefined) {
    this.loggedUserSubject.next(msg);
  }

  private getCurrentUser(): Observable<LoggedUser> {
    const url = environment.API_BASE_URL + "/auth/me";
    return this.httpRequestService.get(url) as Observable<LoggedUser>;
  }

}
