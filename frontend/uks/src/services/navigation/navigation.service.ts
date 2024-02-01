import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { IssueDTO } from 'src/models/issue/issue';

@Injectable({
  providedIn: 'root'
})
export class NavigationService {

  repoName?: string;

  constructor(private router: Router) {
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
        `repository/${this.repoName}`,
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

}
