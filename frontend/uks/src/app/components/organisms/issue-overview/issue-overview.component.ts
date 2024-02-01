import { Component } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { ActivatedRoute, NavigationExtras } from '@angular/router';
import { ISSUE_EVENT_TYPE, IssueDTO, IssueEventDTO, IssueEventRequest, IssueRequest } from 'src/models/issue/issue';
import { STATE, STATE_COLORS } from 'src/models/state/state';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { IssueService } from 'src/services/issue/issue.service';
import { NavigationService } from 'src/services/navigation/navigation.service';


@Component({
  selector: 'app-issue-overview',
  templateUrl: './issue-overview.component.html',
  styleUrl: './issue-overview.component.scss'
})
export class IssueOverviewComponent {


  repoId: string;
  issueId: string = '';
  loggedUser?: UserBasicInfo;

  STATE_COLORS = STATE_COLORS;
  STATE = STATE;
  ISSUE_EVENT_TYPE = ISSUE_EVENT_TYPE;

  isEdit: boolean = false;

  issue?: IssueDTO;
  events: IssueEventDTO[] = [];

  newIssueForm = this.formBuilder.group({
    name: new FormControl(""),
    description: new FormControl(""),
  })
  stateColor: string = STATE_COLORS.OPEN;

  constructor(
    private formBuilder: FormBuilder,
    private navigationService: NavigationService,
    private authService: AuthService,
    private issueService: IssueService,
    private route: ActivatedRoute
  ) {
    this.repoId = localStorage.getItem("repoId") as string

    this.authService.getLoggedUser().subscribe({
      next: (logged?: UserBasicInfo) => {
        this.loggedUser = logged;
      }
    })

    this.getIssueFromRoute();
    this.getIssueEvents();
  }

  ngOnInit(): void {
    this.newIssueForm.controls.name.setValue(this.issue?.name ? this.issue?.name : "")
    this.newIssueForm.controls.description.setValue(this.issue?.description ? this.issue?.description : "")
  }

  edit() {
    this.isEdit = true;
  }
  closeEdit(editedIssue: IssueDTO | null) {
    this.isEdit = false;
    if (this.issue && editedIssue) {
      this.issue.name = editedIssue.name;
      this.issue.description = editedIssue.description;
    }
  }

  changeStatus(state: STATE) {
    let eventRequest = this.createIssueEventRequest(ISSUE_EVENT_TYPE.STATE);
    eventRequest.state = state;
    this.issueService.updateIssue(eventRequest).subscribe({
      next: (res: IssueDTO | null) => {
        if (this.issue && res) {
          this.issue.state = state;
          this.setStateColor(state);
        }
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  showProjectIssues() {
    this.navigationService.navigateToProjectIssues();
  }


  private createIssueEventRequest(type: ISSUE_EVENT_TYPE): IssueEventRequest {
    return {
      issueId: this.issueId,
      authorId: this.loggedUser?.id as string,
      type
    }
  }

  private getIssueFromRoute() {
    if (this.route.params) {
      this.route.params.subscribe(params => {
        this.issueId = params['issueId'];
        this.issueService.getById(this.issueId).subscribe({
          next: (res: IssueDTO) => {
            this.issue = res;
            this.setStateColor(res.state);

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
    this.issueService.getIssueEventHistory(this.issueId).subscribe({
      next: (res: IssueEventDTO[]) => {
        this.events = res;
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }
}
