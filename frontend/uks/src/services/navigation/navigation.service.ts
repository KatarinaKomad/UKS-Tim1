import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { IssueDTO } from 'src/models/issue/issue';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';

@Injectable({
  providedIn: 'root'
})
export class NavigationService {

  repoName?: string;

  constructor(
    private router: Router,
  ) {
    this.repoName = localStorage.getItem("repoName") as string;
  }

  navigateToNewIssue() {
    this.router.navigate(
      [
        `repository/${this.repoName}`,
        { outlets: { 'project-view': [], 'issue-view': ['issue', 'new'] } }
      ],
      { queryParams: { tab: 1 } },
    );
  }

  navigateToIssueOverview(issue: IssueDTO) {
    this.router.navigate(
      [
        `repository/${issue.repo.name}`,
        { outlets: { 'project-view': [], 'issue-view': ['issue', issue.id] } }
      ],
      { queryParams: { tab: 1 } },
    );
  }

  navigateToProjectView(viewName: string) {
    this.router.navigate(
      [
        `repository/${this.repoName}`,
        { outlets: { 'project-view': [viewName], 'issue-view': [] } }
      ],
      { queryParams: { tab: 1 } }
    );
  }

  navigateToProjectIssues() {
    this.router.navigate(
      [
        `repository/${this.repoName}`,
        { outlets: { 'project-view': ['issues'], 'issue-view': [] } }
      ],
      { queryParams: { tab: 1 } }
    );
  }

  navigateToTab(index: number) {
    this.router.navigate(
      [`repository/${this.repoName}`],
      { queryParams: { tab: index } }
    );
  }

  navigateToRepo(repo: RepoBasicInfoDTO) {
    const oldRepoName = localStorage.getItem("repoName") as string;

    this.setRepoToLocalStorage(repo);
    this.router.navigate(
      [`/repository/${repo?.name}`],
      { state: { repo } }
    ).then(() => {
      if (oldRepoName === repo.name && oldRepoName) {
        window.location.reload();
      }
    });

  }

  navigateToNewFork() {
    this.router.navigate(['/repository/fork']);
  }
  navigateToForksOverview() {
    this.router.navigate(['/repository/forks-overview']);
  }

  private setRepoToLocalStorage(repo: RepoBasicInfoDTO) {
    localStorage.setItem("repoId", repo.id);
    localStorage.setItem("repoName", repo.name);
  }
}
