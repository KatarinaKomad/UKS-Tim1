import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { IssueDTO, IssueRequest } from 'src/models/issue/issue';
import { IssueService } from 'src/services/issue/issue.service';
import { STATE } from 'src/models/state/state';
import { NavigationService } from 'src/services/navigation/navigation.service';

@Component({
  selector: 'app-project-issues',
  templateUrl: './project-issues.component.html',
  styleUrls: ['./project-issues.component.scss']
})
export class ProjectIssuesComponent implements OnInit {


  isFilterFormVisible = false;

  allIssues: IssueDTO[] = [];
  shownIssues: IssueDTO[] = [];

  shownOpen: boolean = true;

  repoId: string = "";
  repoName: string = "";

  constructor(
    private navigationService: NavigationService,
    private issueService: IssueService
  ) {
    this.repoName = localStorage.getItem("repoName") as string;
  }

  ngOnInit(): void {
    this.repoId = localStorage.getItem("repoId") as string

    this.issueService.getAllRepoIssues(this.repoId).subscribe({
      next: (res: Array<IssueDTO>) => {
        this.allIssues = res;
        this.filterShownIssues();
      }
    })
  }


  addNewIssue() {
    this.navigationService.navigateToNewIssue();
  }


  private filterShownIssues() {
    this.shownIssues = this.allIssues.filter((elem: IssueDTO) =>
      this.shownOpen ? (elem.state === STATE.OPEN) : (elem.state === STATE.CLOSE));

    this.shownIssues = [...this.shownIssues];
  }

  changeShownIssues(shownOpen: boolean) {
    this.shownOpen = shownOpen;
    this.filterShownIssues();

  }


  showFilterForm(show: boolean) {
    this.isFilterFormVisible = show;
  }
  closeFilter($event: Event) {
    this.isFilterFormVisible = false;
  }

}
