import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { signUpForm } from 'src/models/forms/authenticationForm';
import { createFormGroupFromFormConfig } from 'src/models/forms/input';

@Component({
  selector: 'app-sign-up-page',
  templateUrl: './sign-up-page.component.html',
  styleUrls: [],
})
export class SignUpPageComponent implements OnInit {
  signUpForm = signUpForm;
  signUpFormValidity!: FormGroup;

  invalid = signUpForm.some((entity) => entity.control.invalid);

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.signUpFormValidity = this.fb.group(
      createFormGroupFromFormConfig(signUpForm)
    );
  }

  handeSignUpButtonClick(): void {
    alert('login');
  }
}
