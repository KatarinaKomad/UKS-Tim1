import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { RepoService } from 'src/services/repo/repo.service';
import {
  MatDialog,
  MAT_DIALOG_DATA,
  MatDialogRef,
  MatDialogTitle,
  MatDialogContent,
  MatDialogActions,
  MatDialogClose,
} from '@angular/material/dialog';
import { NewRepoDialogComponent } from 'src/app/components/molecules/new-repo-dialog/new-repo-dialog.component';

@Component({
  selector: 'app-my-repos-side-list',
  templateUrl: './my-repos-side-list.component.html',
  styleUrl: './my-repos-side-list.component.scss'
})
export class MyReposSideListComponent {


  myRepos: RepoBasicInfoDTO[] = [];
  loggedUser!: UserBasicInfo | undefined;

  constructor(
    public dialog: MatDialog,
    private router: Router,
    private repoService: RepoService,
    private authService: AuthService
  ) {
    this.setLoggedUser();
  }

  setLoggedUser() {
    return this.authService.getLoggedUser().subscribe({
      next: (response: UserBasicInfo | undefined) => {
        if (response) {
          this.loggedUser = response;
          this.setMyRepos();
        }
      },
      error: (e: HttpErrorResponse) => {
        console.log(e);
      }
    });
  }

  setMyRepos() {
    this.repoService.getMyRepos(this.loggedUser?.id as string).subscribe({
      next: (response: RepoBasicInfoDTO[]) => {
        this.myRepos = response;
      },
      error: (e: HttpErrorResponse) => {
        console.log(e);
      }
    });
  }

  navigateToRepo(repository: RepoBasicInfoDTO) {
    const link = `/repository/${repository?.name}`
    this.router.navigate([link], { state: { repository } })
  }

  addNewRepository() {
    const user = this.loggedUser;
    const dialogRef = this.dialog.open(NewRepoDialogComponent, {
      maxWidth: '100vw',
      maxHeight: '100vh',
      height: '50%',
      width: '50%',
      data: { user },

    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.repoService.createNewRepo(result).subscribe({
          next: (newRepo: RepoBasicInfoDTO | null) => {
            if (newRepo) {
              this.myRepos.push(newRepo as RepoBasicInfoDTO);
            }
          }
        });
      }
    });
  }


}
