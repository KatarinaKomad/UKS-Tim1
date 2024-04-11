import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { ISSUE_PROPERTIES, IssueProperties, ISSUE_EVENT_TYPE, IssueEventRequest, IssueDTO } from 'src/models/issue/issue';
import { LabelDTO } from 'src/models/label/label';
import { MilestoneDTO } from 'src/models/milestone/milestone';
import { PullRequestDTO, PullRequestEventRequest, PullRequestProperties } from 'src/models/pull-request/pull-request';
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

  PR_PROPERTIES = ISSUE_PROPERTIES;
  openedProperty: ISSUE_PROPERTIES | null = null;

  @Input() prPropertiesInput: PullRequestProperties = {};
  prProperties: PullRequestProperties = {};

  @Input() prId?: string;
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

    this.updatePrProperties(this.prPropertiesInput);
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['prPropertiesInput']?.currentValue) {
      this.updatePrProperties(changes['prPropertiesInput'].currentValue);
    }
  }

  deletePr() {
    this.prService.delete(String(this.prId)).subscribe({
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

  closeAssigneeFilter(list: UserBasicInfo[] | null) {
    this.openedProperty = null;
    if (list) {
      this.prProperties.assignees = [...list]
      // if edit
      if (this.prId) {
        let eventRequest = this.createPrEventRequest(ISSUE_EVENT_TYPE.ASSIGNEE);
        eventRequest.assigneeIds = list.map(u => u.id);
        this.sendNewPrEventRequest(eventRequest)
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
      this.prProperties.labels = [...list]
      // if edit
      if (this.prId) {
        let eventRequest = this.createPrEventRequest(ISSUE_EVENT_TYPE.LABEL);
        eventRequest.labelIds = list.map(u => u.id);
        this.sendNewPrEventRequest(eventRequest)
      }
      // if new 
      else {
        this.changeEvent.emit({ labels: list })
      }
    }
  }

  closeMilestoneFilter(milestone: MilestoneDTO | null | undefined) {
    this.openedProperty = null;
    if (milestone !== null) {
      this.prProperties.milestone = milestone;
      // if edit
      if (this.prId) {
        let eventRequest = this.createPrEventRequest(ISSUE_EVENT_TYPE.MILESTONE);
        eventRequest.milestoneId = milestone?.id
        this.sendNewPrEventRequest(eventRequest)
      }
      // if new 
      else {
        this.changeEvent.emit({ milestone })
      }
    }
  }


  private createPrEventRequest(type: ISSUE_EVENT_TYPE): PullRequestEventRequest {
    return {
      prId: this.prId as string,
      authorId: this.loggedUser?.id as string,
      type
    }
  }

  private sendNewPrEventRequest(eventRequest: PullRequestEventRequest) {
    this.prService.update(eventRequest).subscribe({
      next: (res: PullRequestDTO | null) => {
        console.log(res);
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  private updatePrProperties(newProperties: PullRequestProperties) {
    if (newProperties.assignees) {
      this.prProperties.assignees = [...newProperties.assignees];
    }
    if (newProperties.labels) {
      this.prProperties.labels = [...newProperties.labels];
    }
    if (newProperties.milestone) {
      this.prProperties.milestone = { ...newProperties.milestone };
    }
  }

  openFilter(property: ISSUE_PROPERTIES) {
    this.openedProperty = property;
  }

}
