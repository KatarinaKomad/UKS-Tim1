import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { PasswordUpdateRequest } from 'src/models/authentication/login';
import { getPasswordUpdateRequest, updatePasswordFrom } from 'src/models/forms/authenticationForm';
import { FormInput, createFormGroupFromFormConfig } from 'src/models/forms/input';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';

@Component({
  selector: 'app-password-update-dialog',
  templateUrl: './password-update-dialog.component.html',
  styleUrl: './password-update-dialog.component.scss'
})
export class PasswordUpdateDialogComponent {

  updatePasswordFrom: FormInput[] = [];
  updatePasswordFromValidity!: FormGroup;

  invalid = updatePasswordFrom.some((entity) => entity.control.invalid);

  loggedUser?: UserBasicInfo;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private toastr: ToastrService,
    public dialogRef: MatDialogRef<PasswordUpdateDialogComponent>,
  ) {
    this.authService.getLoggedUser().subscribe({
      next: (res?: UserBasicInfo) => {
        this.loggedUser = res;
      }
    })
  }

  ngOnInit(): void {
    this.updatePasswordFrom = [...updatePasswordFrom];
    this.updatePasswordFromValidity = this.fb.group(createFormGroupFromFormConfig(updatePasswordFrom));
  }

  updatePassword(): void {
    const registrationRequest: PasswordUpdateRequest = getPasswordUpdateRequest(this.updatePasswordFrom, this.loggedUser?.email);
    this.authService.updatePassword(registrationRequest).subscribe({
      next: () => {
        this.toastr.success("Password successfully updated!", "Please login to continue.");
        this.dialogRef.close(true);
      },
      error: (e: HttpErrorResponse) => {
        console.log(e);
        this.toastr.error("Password not valid");
        this.dialogRef.close(false);
      }
    });
  }

  close() {
    this.dialogRef.close(false);
  }

}
