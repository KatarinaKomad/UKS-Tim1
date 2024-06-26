import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { IssueDTO } from 'src/models/issue/issue';
import { PullRequestDTO } from 'src/models/pull-request/pull-request';
import { EditRepoRequest, RepoBasicInfoDTO } from 'src/models/repo/repo';
import { UserBasicInfo } from 'src/models/user/user';
import { RepoService } from '../repo/repo.service';
import { AuthService } from '../auth/auth.service';
import { TAB_VIEW } from 'src/models/navigation';
import { FileDTO } from 'src/models/files/files';

@Injectable({
  providedIn: 'root'
})
export class NavigationService {

  constructor(
    private router: Router,
    private repoService: RepoService,
    private authService: AuthService,
  ) { }

  // issues navigation

  navigateToNewIssue() {
    const repoName = localStorage.getItem("repoName") as string;
    this.router.navigate(
      [
        `repository/${repoName}/issues`,
        { outlets: { 'issues-tab': ['new'], 'pr-tab': null } }
      ],
      { queryParams: { tab: 1 } },
    );
  }

  navigateToIssueOverview(issue: IssueDTO) {
    this.canEditCheck(issue.repo.id, (canEdit: boolean) => {

      this.setRepoToLocalStorage(issue.repo, canEdit)
      this.router.navigate(
        [
          `repository/${issue.repo.name}/issues`,
          { outlets: { 'issues-tab': [issue.id], 'pr-tab': null } }
        ],
        { queryParams: { tab: 1 } },
      );
    })
  }



  navigateToProjectViewFromIssueView(viewName: TAB_VIEW) {
    const repoName = localStorage.getItem("repoName") as string;
    this.router.navigate(
      [
        `repository/${repoName}/issues`,
        { outlets: { 'issues-tab': [viewName], 'pr-tab': null } }
      ],
      { queryParams: { tab: 1 } }
    );
  }

  navigateToProjectIssues() {
    const repoName = localStorage.getItem("repoName") as string;
    this.router.navigate(
      [
        `repository/${repoName}/issues`,
        { outlets: { 'issues-tab': ['issues-view'], 'pr-tab': null } }
      ],
      { queryParams: { tab: 1 } }
    );
  }

  // pr navigation

  navigateToNewPR() {
    const repoName = localStorage.getItem("repoName") as string;
    this.router.navigate(
      [
        `repository/${repoName}/pull-requests`,
        { outlets: { 'pr-tab': ['new'], 'issues-tab': null } }
      ],
      { queryParams: { tab: 2 } },
    );
  }
  navigateToPROverview(pr: PullRequestDTO) {
    this.router.navigate(
      [
        `repository/${pr.repo}/pull-requests`,
        { outlets: { 'pr-tab': [pr.id], 'issues-tab': null } }
      ],
      { queryParams: { tab: 2 } },
    );
  }
  navigateToProjectViewFromPRView(viewName: TAB_VIEW) {
    const repoName = localStorage.getItem("repoName") as string;
    this.router.navigate(
      [
        `repository/${repoName}/pull-requests`,
        { outlets: { 'pr-tab': [viewName], 'issues-tab': null } }
      ],
      { queryParams: { tab: 2 } }
    );
  }
  navigateToProjectPRs() {
    const repoName = localStorage.getItem("repoName") as string;
    this.router.navigate(
      [
        `repository/${repoName}/pull-requests`,
        { outlets: { 'pr-tab': ['pr-view'], 'issues-tab': null } }
      ],
      { queryParams: { tab: 2 } }
    );
  }

  navigateToTab(index: number) {
    const repoName = localStorage.getItem("repoName") as string;
    this.router.navigate(
      [`repository/${repoName}`],
      { queryParams: { tab: index } }
    );
  }

  navigateToRepo(repo: RepoBasicInfoDTO) {
    const oldRepoName = localStorage.getItem("repoName") as string;
    this.canEditCheck(repo.id, (canEdit: boolean) => {

      this.setRepoToLocalStorage(repo, canEdit)

      this.router.navigate([`/repository/${repo?.name}`], { state: { repo } }).then(() => {
        if (oldRepoName === repo.name && oldRepoName) {
          window.location.reload();
        }
      });

    })
  }

  navigateToNewFork() {
    this.router.navigate(['/repository/fork']);
  }
  navigateToForksOverview() {
    this.router.navigate(['/repository/forks-overview']);
  }
  navigateToStarsOverview() {
    this.router.navigate(['/repository/stars-overview']);
  }
  navigateToWatchersOverview() {
    this.router.navigate(['/repository/watchers-overview']);
  }
  navigateToLogin() {
    this.router.navigate(['/login']);
  }
  navigateToHome() {
    this.router.navigate(['/home']);
  }

  navigateToUser(userId: string) {
    this.router.navigate([`/profile/${userId}`])
  }

  navigateToBranchesPage() {
    this.router.navigate([`repository/branches`]);
  }

  navigateToBranchCodeOverview(branchName: string) {
    this.router.navigate([`repository/branch/${branchName}`])
  }
  navigateToFile(branchName: string, file: FileDTO) {
    if (file.isFolder) {
      this.router.navigate([`repository/branch/${branchName}/${file.path}`])
    }
    else {
      this.router.navigate([`repository/branch/${branchName}/${file.path}`], { queryParams: { isFile: true } })
    }
  }
  navigateToCommitHistory(branchName: string, filePath: string) {
    this.router.navigate([`repository/commits/${branchName}/${filePath}`])
  }

  navigateToCommitDifferences(branchName: string, commitHash: string) {
    this.router.navigate([`repository/commitDiff/${branchName}/${commitHash}`])
  }

  private canEditCheck(repoId: string, functionRef: (a: boolean) => void) {
    this.authService.getLoggedUser().subscribe({
      next: (user: UserBasicInfo | undefined) => {

        const repoRequest = this.createEditRepoRequest(user, repoId);

        if (repoRequest) {
          this.repoService.canEditRepoItems(repoRequest).subscribe({
            next: (canEdit: boolean) => {
              functionRef(canEdit)
            },
            error: (e: any) => { console.log(e); },
          })
        } else {
          functionRef(false)
        }
      }, error: (e: any) => { console.log(e); },
    });
  }



  private setRepoToLocalStorage(repo: RepoBasicInfoDTO, canEdit: boolean) {
    localStorage.setItem("repoId", repo.id);
    localStorage.setItem("repoName", repo.name);
    localStorage.setItem("canEditRepoItems", canEdit.toString());
  }


  private createEditRepoRequest(user: UserBasicInfo | undefined, repoId: any): EditRepoRequest | null {
    if (!repoId || !user?.id) return null;
    return { repoId, userId: user.id };
  }

}
