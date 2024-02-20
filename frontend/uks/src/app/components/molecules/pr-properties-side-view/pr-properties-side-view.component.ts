import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { ISSUE_PROPERTIES, IssueProperties, ISSUE_EVENT_TYPE, IssueEventRequest, IssueDTO } from 'src/models/issue/issue';
import { LabelDTO } from 'src/models/label/label';
import { MilestoneDTO } from 'src/models/milestone/milestone';
import { PullRequestDTO, PullRequestProperties } from 'src/models/pull-request/pull-request';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { IssueService } from 'src/services/issue/issue.service';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { PullRequestService } from 'src/services/pull-request/pull-request.service';
import { Toastr } from 'src/utils/toastr.service';

@Component({
  selector: 'app-pr-properties-side-view',
  templateUrl: './pr-properties-side-view.component.html',
  styleUrl: './pr-properties-side-view.component.scss'
})
export class PrPropertiesSideViewComponent implements OnChanges {

  loggedUser?: UserBasicInfo;

  ISSUE_PROPERTIES = ISSUE_PROPERTIES;
  openedProperty: ISSUE_PROPERTIES | null = null;

  @Input() issuePropertiesInput: PullRequestProperties = {};
  issueProperties: PullRequestProperties = {};

  @Input() issueId?: string;
  @Output() changeEvent: EventEmitter<PullRequestProperties> = new EventEmitter<PullRequestProperties>();

  constructor(
    private prService: PullRequestService,
    private authService: AuthService,
    private navigationService: NavigationService,
    private toastr: Toastr
  ) {
    this.authService.getLoggedUser().subscribe({
      next: (logged?: UserBasicInfo) => {
        this.loggedUser = logged;
      }
    })

    this.updateIssueProperties(this.issuePropertiesInput);
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['issuePropertiesInput']?.currentValue) {
      this.updateIssueProperties(changes['issuePropertiesInput'].currentValue);
    }
  }

  deleteIssue() {
    this.prService.delete(String(this.issueId)).subscribe({
      next: () => {
        this.toastr.success(`PullRequest deleted!`, 'Success');
        this.navigationService.navigateToProjectPRs();
      },
      error: (res: any) => {
        this.toastr.success(res, 'Error');
        console.log(res);
      },
    })
  }

  closeAssigneeFilter(list: string[] | null) {
    this.openedProperty = null;
    if (list) {
      this.issueProperties.assignees = [...list]
      // if edit
      if (this.issueId) {
        let eventRequest = this.createIssueEventRequest(ISSUE_EVENT_TYPE.ASSIGNEE);
        // eventRequest.assigneeIds = list;
        // this.sendNewIssueEventRequest(eventRequest)
      }
      // if new 
      else {
        this.changeEvent.emit({ assignees: list })
      }
    }
  }

  closeLabelFilter(list: LabelDTO[] | null) {
    this.openedProperty = null;
    if (list) {
      this.issueProperties.labels = [...list]
      // if edit
      if (this.issueId) {
        let eventRequest = this.createIssueEventRequest(ISSUE_EVENT_TYPE.LABEL);
        eventRequest.labelIds = list.map(u => u.id);
        this.sendNewIssueEventRequest(eventRequest)
      }
      // if new 
      else {
        this.changeEvent.emit({ labels: list })
      }
    }
  }

  closeMilestoneFilter(milestone: string | null | undefined) {
    this.openedProperty = null;
    if (milestone !== null) {
      this.issueProperties.milestone = milestone;
      // if edit
      if (this.issueId) {
        let eventRequest = this.createIssueEventRequest(ISSUE_EVENT_TYPE.MILESTONE);
        // eventRequest.milestoneId = milestone
        // this.sendNewIssueEventRequest(eventRequest)
      }
      // if new 
      else {
        this.changeEvent.emit({ milestone })
      }
    }
  }


  private createIssueEventRequest(type: ISSUE_EVENT_TYPE): IssueEventRequest {
    return {
      issueId: this.issueId as string,
      authorId: this.loggedUser?.id as string,
      type
    }
  }

  private sendNewIssueEventRequest(eventRequest: IssueEventRequest) {
    this.prService.update(eventRequest).subscribe({
      next: (res: PullRequestDTO | null) => {
        console.log(res);
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  private updateIssueProperties(newProperties: PullRequestProperties) {
    if (newProperties.assignees) {
      this.issueProperties.assignees = [...newProperties.assignees];
    }
    if (newProperties.labels) {
      this.issueProperties.labels = [...newProperties.labels];
    }
    // if (newProperties.milestone) {
    //   this.issueProperties.milestone = { ...newProperties.milestone };
    // }
  }

  openFilter(property: ISSUE_PROPERTIES) {
    this.openedProperty = property;
  }

}
