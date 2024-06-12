import { Component, Input, SimpleChanges } from '@angular/core';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';
import { RepoMemberDTO } from 'src/models/user/member';
import { MemberService } from 'src/services/member/member.service';
import { NavigationService } from 'src/services/navigation/navigation.service';

@Component({
  selector: 'app-repo-about-side-view',
  templateUrl: './repo-about-side-view.component.html',
  styleUrl: './repo-about-side-view.component.scss'
})
export class RepoAboutSideViewComponent {

  @Input() repo?: RepoBasicInfoDTO;

  members: RepoMemberDTO[] = [];

  constructor(
    private navigationService: NavigationService,
    private memberService: MemberService,
  ) {
    const repoId = localStorage.getItem("repoId") as string
    this.memberService.getRepoMembers(repoId).subscribe({
      next: (res: RepoMemberDTO[]) => {
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
    this.navigationService.navigateToStarsOverview();
  }
  onForkClick() {
    this.navigationService.navigateToForksOverview();
  }
  onWatchClick() {
    this.navigationService.navigateToWatchersOverview();
  }
  showUserProfile(user: RepoMemberDTO) {
    this.navigationService.navigateToUser(user.id);
  }
}
