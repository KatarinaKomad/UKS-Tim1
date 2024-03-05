import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { AuthService } from 'src/services/auth/auth.service';
import { Toastr } from 'src/utils/toastr.service';

@Component({
  selector: 'app-forgot-password-page',
  templateUrl: './forgot-password-page.component.html',
  styleUrls: [],
})
export class ForgotPasswordPageComponent implements OnInit {
  email = new FormControl('', [Validators.required, Validators.email]);

  constructor(
    private authService: AuthService,
    private toastr: Toastr
  ) { }

  ngOnInit(): void { }

  handleSendEmailButton(): void {
    this.authService.resetPassword({ email: this.email.value as string }).subscribe({
      next: () => {
        this.toastr.success("New password sent", "Please check your email.")
      }, error: () => {
        this.toastr.error("Something went wrong", "Please try again later")
      }
    })
  }
}
