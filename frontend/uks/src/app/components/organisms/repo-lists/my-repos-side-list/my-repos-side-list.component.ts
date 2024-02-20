import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { RepoService } from 'src/services/repo/repo.service';
import { MatDialog } from '@angular/material/dialog';
import { NewRepoDialogComponent } from 'src/app/components/molecules/dialogs/new-repo-dialog/new-repo-dialog.component';
import { NavigationService } from 'src/services/navigation/navigation.service';

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
    private navigationService: NavigationService,
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
    this.navigationService.navigateToRepo(repository);
  }

  addNewRepository() {
    const user = this.loggedUser;
    const dialogRef = this.dialog.open(NewRepoDialogComponent, {
      maxWidth: '100vw',
      maxHeight: '100vh',
      height: '70%',
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
