import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { YesNoDialogComponent } from 'src/app/components/molecules/dialogs/yes-no-dialog/yes-no-dialog.component';
import {
  UserBasicInfo,
  UserDTO,
  UserUpdateRequest,
} from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { NavigationService } from 'src/services/navigation/navigation.service';
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

  isReadonly: boolean = true;
  userId: string = '';

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private toastr: ToastrService,
    private route: ActivatedRoute,
    public dialog: MatDialog,
    private navigationService: NavigationService,
  ) {

    this.request = {
      firstName: '',
      lastName: '',
      email: '',
      username: '',
    };
  }

  ngOnInit(): void {

    this.authService.getLoggedUser().subscribe({
      next: (response: UserBasicInfo | undefined) => {
        this.loggedUser = response;

        this.route.params.subscribe(params => {
          this.userId = params['userId'];

          const profileId = this.userId ? this.userId : this.loggedUser?.id as string
          this.setMyProfile(profileId);

          if (this.loggedUser?.id == this.userId || !this.userId) {
            this.isReadonly = false;
          }
        })
      }
    });
  }


  setMyProfile(userId: string) {
    this.userService.getProfileInfo(userId).subscribe({
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



  deleteAccountPrompt() {

    const dialogRef = this.dialog.open(YesNoDialogComponent, {
      height: '35%',
      width: '40%',
      data: { title: 'Delete account', prompt: 'Are you sure you want to delete your account?' },
    });
    dialogRef.afterClosed().subscribe((shouldDelete: boolean) => {
      if (shouldDelete) {
        this.deleteAccount();
      }
    });
  }

  private deleteAccount() {
    this.userService.deleteAccount(this.loggedUser?.id as string).subscribe({
      next: () => {
        this.toastr.success('Account successfully deleted');
        this.navigationService.navigateToLogin();
      },
      error: (e: HttpErrorResponse) => {
        console.log(e);
      },
    })
  }
}
