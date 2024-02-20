import { Component, Input, SimpleChanges } from '@angular/core';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';
import { UserBasicInfo } from 'src/models/user/user';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-repo-about-side-view',
  templateUrl: './repo-about-side-view.component.html',
  styleUrl: './repo-about-side-view.component.scss'
})
export class RepoAboutSideViewComponent {

  @Input() repo?: RepoBasicInfoDTO;

  members: UserBasicInfo[] = [];

  constructor(
    private navigationService: NavigationService,
    private repoService: RepoService,
  ) {
    const repoId = localStorage.getItem("repoId") as string
    this.repoService.getRepoMembers(repoId).subscribe({
      next: (res: UserBasicInfo[]) => {
        this.members = res;
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['repo'].currentValue) {
      this.repo = changes['repo'].currentValue;
    }
  }

  onStarClick() {
    throw new Error('Method not implemented.');
  }
  onForkClick() {
    this.navigationService.navigateToForksOverview();
  }
  onWatchClick() {
    throw new Error('Method not implemented.');
  }
  showUserProfile(user: UserBasicInfo) {
    throw new Error('Method not implemented.');
  }
}
