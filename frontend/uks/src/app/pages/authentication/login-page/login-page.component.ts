import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginRequest, TokenResponse } from 'src/models/authentication/login';
import { loginForm, getLoginRequest } from 'src/models/forms/authenticationForm';
import { AuthService } from 'src/services/auth/auth.service';
import { Toastr } from 'src/utils/toastr.service';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: [],
})
export class LoginPageComponent implements OnInit {
  loginForm = loginForm;

  constructor(
    private toastr: Toastr,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void { }

  handeLoginButtonClick(): void {
    const loginRequest: LoginRequest = getLoginRequest(this.loginForm);

    this.authService.login(loginRequest).subscribe({
      next: (response: TokenResponse) => {
        this.authService.handleSuccessfulAuth(response);
      },
      error: (e: HttpErrorResponse) => {
        this.handleLoginError(e)
      }
    });
  }


  handleLoginError(e: any): void {
    if (e.status === HttpStatusCode.Unauthorized) {       // Invalid credentials
      this.toastr.error('Make sure you have an activated account.', e.error);
    } else if (e.status === HttpStatusCode.NotFound) {    // User not found 
      this.toastr.error('Make sure you have an activated account. ', e.error);
    } else {
      this.toastr.error('Oops something went wrong ', e.error);
      console.log(e.error)
    }
  }

  continueAsAGuest() {
    this.router.navigate(['/home'])
  }
}
