import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ISSUE_EVENT_TYPE, IssueDTO, IssueProperties, IssueEventDTO, IssueEventRequest } from 'src/models/issue/issue';
import { PullRequestDTO, PullRequestEventDTO, PullRequestProperties } from 'src/models/pull-request/pull-request';
import { STATE_COLORS, STATE } from 'src/models/state/state';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { IssueService } from 'src/services/issue/issue.service';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { PullRequestService } from 'src/services/pull-request/pull-request.service';

@Component({
  selector: 'app-pr-overview',
  templateUrl: './pr-overview.component.html',
  styleUrl: './pr-overview.component.scss'
})
export class PrOverviewComponent {

  repoId: string;
  prId: string = '';
  loggedUser?: UserBasicInfo;

  STATE_COLORS = STATE_COLORS;
  STATE = STATE;
  PR_EVENT_TYPE = ISSUE_EVENT_TYPE;

  isEdit: boolean = false;

  pr?: PullRequestDTO;
  prProperties: PullRequestProperties = {};
  events: IssueEventDTO[] = [];

  stateColor: string = STATE_COLORS.OPEN;

  constructor(
    private navigationService: NavigationService,
    private authService: AuthService,
    private issueService: IssueService,
    private route: ActivatedRoute,
    private prService: PullRequestService
  ) {
    this.repoId = localStorage.getItem("repoId") as string

    this.authService.getLoggedUser().subscribe({
      next: (logged?: UserBasicInfo) => {
        this.loggedUser = logged;
      }
    })
  }

  ngOnInit(): void {
    this.getPRFromRoute();
    this.getIssueEvents();
  }

  edit() {
    this.isEdit = true;
  }
  closeEdit(editedIssue: IssueDTO | null) {
    this.isEdit = false;
    if (this.pr && editedIssue) {
      this.pr.name = editedIssue.name;
      this.pr.description = editedIssue.description;
    }
  }

  changeStatus(state: STATE) {
    let eventRequest = this.createIssueEventRequest(ISSUE_EVENT_TYPE.STATE);
    eventRequest.state = state;
    this.prService.update(eventRequest).subscribe({
      next: (res: PullRequestDTO | null) => {
        if (this.pr && res) {
          this.pr.state = state;
          this.setStateColor(state);
        }
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  handleIssuePropertiesChange(prProperties: PullRequestProperties) {
    this.prProperties = { ...prProperties };
  }

  showProjectPRs() {
    this.navigationService.navigateToProjectPRs();
  }


  private createIssueEventRequest(type: ISSUE_EVENT_TYPE): IssueEventRequest {
    return {
      issueId: this.prId,
      authorId: this.loggedUser?.id as string,
      type
    }
  }

  private getPRFromRoute() {
    if (this.route.params) {
      this.route.params.subscribe(params => {
        this.prId = params['prId'];
        this.prService.getById(this.prId).subscribe({
          next: (res: PullRequestDTO) => {
            this.pr = res;
            const stateColor = STATE[res.state as keyof typeof STATE];
            this.setStateColor(stateColor);

            this.prProperties.assignees = this.pr?.assignees
            this.prProperties.labels = this.pr?.labels
            this.prProperties.milestone = this.pr?.milestone

            this.prProperties = { ...this.prProperties }
          }, error: (e: any) => {
            console.log(e);
          }
        })
      });
    }

  }
  private setStateColor(state: STATE) {
    switch (state) {
      case STATE.OPEN: this.stateColor = STATE_COLORS.OPEN.toString(); break;
      case STATE.CLOSE: this.stateColor = STATE_COLORS.CLOSE.toString(); break;
      case STATE.MERGED: this.stateColor = STATE_COLORS.MERGED.toString(); break;
      default: this.stateColor = STATE_COLORS.OPEN;
    }
  }

  private getIssueEvents() {
    this.prService.getPREventHistory(this.prId).subscribe({
      next: (res: PullRequestEventDTO[]) => {
        this.events = res;
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

}
