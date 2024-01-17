import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { RegistrationRequest } from 'src/models/authentication/registration';
import { getRegistrationRequest, signUpForm } from 'src/models/forms/authenticationForm';
import { createFormGroupFromFormConfig } from 'src/models/forms/input';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';

@Component({
  selector: 'app-sign-up-page',
  templateUrl: './sign-up-page.component.html',
  styleUrls: [],
})
export class SignUpPageComponent implements OnInit {
  signUpForm = signUpForm;
  signUpFormValidity!: FormGroup;

  invalid = signUpForm.some((entity) => entity.control.invalid);

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private toastr: ToastrService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.signUpFormValidity = this.fb.group(
      createFormGroupFromFormConfig(signUpForm)
    );
  }

  handeSignUpButtonClick(): void {
    const registrationRequest: RegistrationRequest = getRegistrationRequest(this.signUpForm);
    this.authService.signup(registrationRequest).subscribe({
      next: (response: UserBasicInfo | null) => {
        console.log(response);
        this.toastr.success("Registration success!", "Please login to continue.");
        this.router.navigate(['/login']);
      },
      error: (e: HttpErrorResponse) => {
        console.log(e);
        this.handleRegistrationError(e)
      }
    });
  }

  handleRegistrationError(error: any) {
    if (error.status === HttpStatusCode.Conflict) {
      this.toastr.error(error.error, "Please try again.");
    } else {
      this.toastr.error("Registration failed, please try again!");
    }
  }
}
