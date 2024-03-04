import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { YesNoDialogComponent } from 'src/app/components/molecules/dialogs/yes-no-dialog/yes-no-dialog.component';
import { getEmptyRepo } from 'src/models/repo/repo';
import { RepoMemberDTO } from 'src/models/user/member';
import { MemberService } from 'src/services/member/member.service';
import { NavigationService } from 'src/services/navigation/navigation.service';

@Component({
  selector: 'app-repo-invitation-page',
  templateUrl: './repo-invitation-page.component.html',
  styleUrl: './repo-invitation-page.component.scss'
})
export class RepoInvitationPageComponent {

  link: string = '';
  repoName: string = '';

  constructor(
    private route: ActivatedRoute,
    public dialog: MatDialog,
    private memberService: MemberService,
    private navigationService: NavigationService,
  ) {

    this.setRouteParams();
    this.openYesNo()
  }

  private setRouteParams() {
    this.route.params.subscribe(params => {
      this.link = params['link'];
    })
    this.route.queryParams.subscribe((params) => {
      if (params['repoName']) {
        this.repoName = params['repoName'];
      }
    });
  }
  private openYesNo() {
    const dialogRef = this.dialog.open(YesNoDialogComponent, {
      height: '35%',
      width: '40%',
      data: { title: 'Join repository', prompt: 'Confirm joining and contributing to repository: ' + this.repoName },
    });
    dialogRef.afterClosed().subscribe((answer: boolean) => {
      if (answer) {
        this.confirmInvitation();
      }
    });
  }
  private confirmInvitation() {
    this.memberService.confirmInvitation(this.link).subscribe({
      next: (res: RepoMemberDTO) => {

        const repo = getEmptyRepo()
        repo.id = res.repoId;
        repo.name = this.repoName;
        this.navigationService.navigateToRepo(repo);
      }, error: (e: any) => {
        console.log(e);
      }
    });
  }


}
