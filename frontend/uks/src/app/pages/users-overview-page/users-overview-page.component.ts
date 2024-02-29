import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { REPO_INTEREST, UserBasicInfo } from 'src/models/user/user';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-users-overview-page',
  templateUrl: './users-overview-page.component.html',
  styleUrl: './users-overview-page.component.scss'
})
export class UsersOverviewPageComponent implements OnInit {

  title: string = '';
  interestType: REPO_INTEREST = REPO_INTEREST.STAR;

  search = new FormControl('', []);

  allUsers: UserBasicInfo[] = [];
  shownUsers: UserBasicInfo[] = [];

  repoId: string = '';

  constructor(
    private router: Router,
    private repoService: RepoService,
  ) { }

  ngOnInit(): void {

    this.repoId = localStorage.getItem('repoId') as string;

    if (this.router.url.includes('watch')) {
      this.interestType = REPO_INTEREST.WATCH;
      this.setWatchers();
    } else if (this.router.url.includes('star')) {
      this.interestType = REPO_INTEREST.STAR;
      this.setStargazers();
    }
  }

  private setWatchers() {
    this.repoService.getAllWatchers(this.repoId).subscribe({
      next: (res: UserBasicInfo[]) => {
        this.allUsers = res;
        this.shownUsers = res;
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  private setStargazers() {
    this.repoService.getAllStargazers(this.repoId).subscribe({
      next: (res: UserBasicInfo[]) => {
        this.allUsers = res;
        this.shownUsers = res;
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  onSearch() {
    const searchValue = (this.search.value as string).toLowerCase();
    let allCopy = this.allUsers.slice();
    this.shownUsers = allCopy.filter(user =>
      this.checkUsername(user, searchValue) ||
      this.checkName(user, searchValue) ||
      this.checkEmail(user, searchValue)
    )
  }

  private checkUsername(user: UserBasicInfo, searchValue: string) {
    return user.username.toLowerCase().includes(searchValue);
  }
  private checkEmail(user: UserBasicInfo, searchValue: string) {
    return user.email.toLowerCase().includes(searchValue);
  }
  private checkName(user: UserBasicInfo, searchValue: string) {
    const fullName = (`${user.firstName} ${user.lastName}`);
    return fullName.toLowerCase().includes(searchValue);
  }
}
