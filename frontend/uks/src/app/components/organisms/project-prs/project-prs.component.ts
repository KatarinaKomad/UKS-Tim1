import { Component, OnInit } from '@angular/core';
import { IssueDTO, ISSUE_PROPERTIES } from 'src/models/issue/issue';
import { LabelDTO } from 'src/models/label/label';
import { MilestoneDTO } from 'src/models/milestone/milestone';
import { PullRequestDTO, UserPullRequestDTO } from 'src/models/pull-request/pull-request';
import { STATE } from 'src/models/state/state';
import { UserBasicInfo } from 'src/models/user/user';
import { IssueService } from 'src/services/issue/issue.service';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { PullRequestService } from 'src/services/pull-request/pull-request.service';

@Component({
  selector: 'app-project-prs',
  templateUrl: './project-prs.component.html',
  styleUrl: './project-prs.component.scss'
})
export class ProjectPrsComponent implements OnInit {


  allPRs: PullRequestDTO[] = [];
  shownPRs: PullRequestDTO[] = [];
  preFilterShown: PullRequestDTO[] = []

  shownOpen: boolean = true;

  repoId: string = "";
  repoName: string = "";

  ISSUE_PROPERTIES = ISSUE_PROPERTIES;
  openedProperty: ISSUE_PROPERTIES | null = null;

  selectedAssignees: string[] = []
  assigneesFilters: string = "";

  selectedLabels: LabelDTO[] = []
  labelsFilters: string = "";

  selectedMilestone?: string
  milestoneFilter: string = "";

  selectedAuthor?: string
  authorFilter: string = "";

  constructor(
    private navigationService: NavigationService,
    private issueService: IssueService,
    private prService: PullRequestService
  ) {
    this.repoName = localStorage.getItem("repoName") as string;
  }

  ngOnInit(): void {
    this.repoId = localStorage.getItem("repoId") as string

    this.prService.getUserPullRequests('', this.repoId).subscribe({
      next: (res: UserPullRequestDTO) => {
        let uniqueIds = new Set();
        this.allPRs = res.createdPRs.concat(res.assignedPRs).filter(obj => {
          if (!uniqueIds.has(obj.id)) {
            uniqueIds.add(obj.id);
            return true;
          }
          return false;
        });
        this.shownPRs = [...this.filterPRsByState(this.allPRs)];
      }
    })
  }


  addNewIssue() {
    this.navigationService.navigateToNewPR();
  }


  private filterPRsByState(prs: PullRequestDTO[]) {
    return prs.filter((elem: PullRequestDTO) =>
      this.shownOpen ? (elem.state === STATE.OPEN) : (elem.state === STATE.CLOSE));
  }

  changeShownIssues(shownOpen: boolean) {
    this.shownOpen = shownOpen;
    this.clearAllFilters();
  }

  openFilter(property: ISSUE_PROPERTIES) {
    this.openedProperty = property;
  }

  closeAssigneeFilter(list: string[] | null) {
    this.openedProperty = null;
    if (list) {
      this.selectedAssignees = list;
      this.assigneesFilters = '';
      this.filterBySelectedItems();
      if (list.length > 0) {
        this.assigneesFilters += 'Assignees: ';
        this.assigneesFilters += list.map(filter => `${filter}`).join(', ');
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

  closeMilestoneFilter(milestone: string | null) {
    this.openedProperty = null;
    if (milestone) {
      this.selectedMilestone = milestone
      this.milestoneFilter = `Milestone: ${milestone}`
    } else {
      this.selectedMilestone = undefined
      this.milestoneFilter = "";
    }
    this.filterBySelectedItems();
  }

  closeAuthorFilter(author: string | null) {
    this.openedProperty = null;
    if (author) {
      this.selectedAuthor = author
      this.authorFilter = `Author: ${author}`
    } else {
      this.selectedAuthor = undefined
      this.authorFilter = "";
    }
    this.filterBySelectedItems();
  }

  private filterBySelectedItems() {
    let filtered = [...this.allPRs];

    if (this.selectedAssignees.length > 0) {
      filtered = filtered.filter(issue =>
        this.selectedAssignees.some(assignee =>
          issue.assignees.some(a => a === assignee)
        )
      );
    }
    if (this.selectedLabels.length > 0) {
      filtered = filtered.filter(issue =>
        this.selectedLabels.some(label =>
          issue.labels.some(l => l.name === label.name)
        )
      );
    }

    if (this.selectedMilestone) {
      let copy = [...filtered];
      for (let issue of copy) {
        if (issue.milestone !== this.selectedMilestone) {
          filtered = filtered.filter(item => item.id !== issue.id);
        }
      }
    }
    if (this.selectedAuthor) {
      let copy = [...filtered];
      for (let issue of copy) {
        if (issue.author !== this.selectedAuthor) {
          filtered = filtered.filter(item => item.id !== issue.id);
        }
      }
    }
    this.shownPRs = [...this.filterPRsByState(filtered)]
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

    this.shownPRs = [...this.filterPRsByState(this.allPRs)];
  }
}
