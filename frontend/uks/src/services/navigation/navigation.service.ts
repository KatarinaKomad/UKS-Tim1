import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { IssueDTO } from 'src/models/issue/issue';
import { PullRequestDTO } from 'src/models/pull-request/pull-request';
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

  navigateToNewPR() {
    this.router.navigate(
      [
        `repository/${this.repoName}`,
        { outlets: { 'project-view': [], 'pull-request-view': ['pull-request', 'new'] } }
      ],
      { queryParams: { tab: 2 } },
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

  navigateToPROverview(pr: PullRequestDTO) {
    this.router.navigate(
      [
        `repository/${pr.repo}`,
        { outlets: { 'project-view': [], 'pull-request-view': ['pull-request', pr.id] } }
      ],
      { queryParams: { tab: 2 } },
    );
  }

  navigateToProjectViewFromIssueView(viewName: string) {
    this.router.navigate(
      [
        `repository/${this.repoName}`,
        { outlets: { 'project-view': [viewName], 'issue-view': [] } }
      ],
      { queryParams: { tab: 1 } }
    );
  }

  navigateToProjectViewFromPRView(viewName: string) {
    this.router.navigate(
      [
        `repository/${this.repoName}`,
        { outlets: { 'project-view': [viewName], 'pull-request-view': [] } }
      ],
      { queryParams: { tab: 2 } }
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

  navigateToProjectPRs() {
    this.router.navigate(
      [
        `repository/${this.repoName}`,
        { outlets: { 'project-view': ['pull-requests'], 'pull-request-view': [] } }
      ],
      { queryParams: { tab: 2 } }
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
