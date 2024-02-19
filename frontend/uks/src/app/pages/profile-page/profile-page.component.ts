import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import {
  UserBasicInfo,
  UserDTO,
  UserUpdateRequest,
} from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { UserService } from 'src/services/user/user.service';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.scss',
})
export class ProfilePageComponent implements OnInit {
  profileInfo!: UserDTO | null;
  loggedUser!: UserBasicInfo | undefined;
  request!: UserUpdateRequest;

  userFirstNameControl = new FormControl('');
  userLastNameControl = new FormControl('');
  userEmailControl = new FormControl('', [Validators.required]);
  userUsernameControl = new FormControl('', [Validators.required]);

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private toastr: ToastrService
  ) {
    this.setLoggedUser();
    this.request = {
      firstName: '',
      lastName: '',
      email: '',
      username: '',
    };
  }

  setLoggedUser() {
    return this.authService.getLoggedUser().subscribe({
      next: (response: UserBasicInfo | undefined) => {
        if (response) {
          this.loggedUser = response;
          this.setMyProfile();
        }
      },
      error: (e: HttpErrorResponse) => {
        console.log(e);
      },
    });
  }

  setMyProfile() {
    this.userService.getProfileInfo(this.loggedUser?.id as string).subscribe({
      next: (response: UserDTO) => {
        this.profileInfo = response;
        this.setControls();
      },
      error: (e: HttpErrorResponse) => {
        console.log(e);
      },
    });
  }

  setControls() {
    this.userFirstNameControl.setValue(this.profileInfo?.firstName || '');
    this.userLastNameControl.setValue(this.profileInfo?.lastName || '');
    this.userEmailControl.setValue(this.profileInfo?.email || '');
    this.userUsernameControl.setValue(this.profileInfo?.username || '');
  }

  ngOnInit(): void {}

  handleSave(): void {
    this.request.firstName = this.userFirstNameControl.value!;
    this.request.lastName = this.userLastNameControl.value!;
    this.request.email = this.userEmailControl.value!;
    this.request.username = this.userUsernameControl.value!;
    console.log(this.request);

    this.userService
      .updateMyProfile(
        this.loggedUser?.id as string,
        this.request as UserUpdateRequest
      )
      .subscribe({
        next: (response: UserDTO | null) => {
          this.profileInfo = response;
          console.log(response);
          this.toastr.success('Profile info updated successfully.');
        },
        error: (e: HttpErrorResponse) => {
          console.log(e);
        },
      });
  }
}
