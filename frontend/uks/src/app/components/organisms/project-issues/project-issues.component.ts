import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ISSUE_PROPERTIES, IssueDTO, IssueRequest } from 'src/models/issue/issue';
import { IssueService } from 'src/services/issue/issue.service';
import { STATE } from 'src/models/state/state';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { UserBasicInfo } from 'src/models/user/user';
import { MilestoneDTO } from 'src/models/milestone/milestone';
import { LabelDTO } from 'src/models/label/label';

@Component({
  selector: 'app-project-issues',
  templateUrl: './project-issues.component.html',
  styleUrls: ['./project-issues.component.scss']
})
export class ProjectIssuesComponent implements OnInit {


  allIssues: IssueDTO[] = [];
  shownIssues: IssueDTO[] = [];
  preFilterShown: IssueDTO[] = []

  shownOpen: boolean = true;

  repoId: string = "";
  repoName: string = "";

  ISSUE_PROPERTIES = ISSUE_PROPERTIES;
  openedProperty: ISSUE_PROPERTIES | null = null;

  selectedAssignees: UserBasicInfo[] = []
  assigneesFilters: string = "";

  selectedLabels: LabelDTO[] = []
  labelsFilters: string = "";

  selectedMilestone?: MilestoneDTO
  milestoneFilter: string = "";

  selectedAuthor?: UserBasicInfo
  authorFilter: string = "";

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
        this.shownIssues = [...this.filterIssuesByState(this.allIssues)];
      }
    })
  }


  addNewIssue() {
    this.navigationService.navigateToNewIssue();
  }


  private filterIssuesByState(issues: IssueDTO[]) {
    return issues.filter((elem: IssueDTO) =>
      this.shownOpen ? (elem.state === STATE.OPEN) : (elem.state === STATE.CLOSE));
  }

  changeShownIssues(shownOpen: boolean) {
    this.shownOpen = shownOpen;
    this.clearAllFilters();
  }

  openFilter(property: ISSUE_PROPERTIES) {
    this.openedProperty = property;
  }

  closeAssigneeFilter(list: UserBasicInfo[] | null) {
    this.openedProperty = null;
    if (list) {
      this.selectedAssignees = list;
      this.assigneesFilters = '';
      this.filterBySelectedItems();
      if (list.length > 0) {
        this.assigneesFilters += 'Assignees: ';
        this.assigneesFilters += list.map(filter => `${filter.firstName} ${filter.lastName}`).join(', ');
      }
    }
  }
  closeLabelFilter(list: LabelDTO[] | null) {
    this.openedProperty = null;
    if (list) {
      this.selectedLabels = list;
      this.labelsFilters = '';
      this.filterBySelectedItems();
      if (list.length > 0) {
        this.labelsFilters += 'Labels: ';
        this.labelsFilters += list.map(filter => filter.name).join(', ');
      }
    }
  }

  closeMilestoneFilter(milestone: MilestoneDTO | null) {
    this.openedProperty = null;
    if (milestone) {
      this.selectedMilestone = milestone
      this.milestoneFilter = `Milestone: ${milestone.name}`
    } else {
      this.selectedMilestone = undefined
      this.milestoneFilter = "";
    }
    this.filterBySelectedItems();
  }

  closeAuthorFilter(author: UserBasicInfo | null) {
    this.openedProperty = null;
    if (author) {
      this.selectedAuthor = author
      this.authorFilter = `Author: ${author.firstName} ${author.lastName}`
    } else {
      this.selectedAuthor = undefined
      this.authorFilter = "";
    }
    this.filterBySelectedItems();
  }

  private filterBySelectedItems() {
    let filtered = [...this.allIssues];

    if (this.selectedAssignees.length > 0) {
      filtered = filtered.filter(issue =>
        this.selectedAssignees.some(assignee =>
          issue.assignees.some(a => a.id === assignee.id)
        )
      );
    }
    if (this.selectedLabels.length > 0) {
      filtered = filtered.filter(issue =>
        this.selectedLabels.some(label =>
          issue.labels.some(l => l.id === label.id)
        )
      );
    }

    if (this.selectedMilestone) {
      let copy = [...filtered];
      for (let issue of copy) {
        if (issue.milestone.id !== this.selectedMilestone?.id) {
          filtered = filtered.filter(item => item.id !== issue.id);
        }
      }
    }
    if (this.selectedAuthor) {
      let copy = [...filtered];
      for (let issue of copy) {
        if (issue.author.id !== this.selectedAuthor?.id) {
          filtered = filtered.filter(item => item.id !== issue.id);
        }
      }
    }
    this.shownIssues = [...this.filterIssuesByState(filtered)]
  }


  clearAllFilters() {
    this.selectedAssignees = []
    this.selectedAuthor = undefined
    this.selectedLabels = []
    this.selectedMilestone = undefined;
    this.authorFilter = "";
    this.assigneesFilters = "";
    this.milestoneFilter = "";
    this.labelsFilters = "";

    this.shownIssues = [...this.filterIssuesByState(this.allIssues)];
  }
}
