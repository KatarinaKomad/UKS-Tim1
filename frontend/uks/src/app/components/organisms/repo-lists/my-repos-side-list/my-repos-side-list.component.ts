import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-my-repos-side-list',
  templateUrl: './my-repos-side-list.component.html',
  styleUrl: './my-repos-side-list.component.scss'
})
export class MyReposSideListComponent {


  myRepos: RepoBasicInfoDTO[] = [];
  loggedUser!: UserBasicInfo | undefined;

  constructor(
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
    throw new Error('Method not implemented.');
  }


}
