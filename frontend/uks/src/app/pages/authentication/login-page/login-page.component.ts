import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { loginForm } from 'src/models/forms/authenticationForm';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: [],
})
export class LoginPageComponent implements OnInit {
  loginForm = loginForm;

  constructor() {}

  ngOnInit(): void {}

  handeLoginButtonClick(): void {
    alert('login');
  }
}
