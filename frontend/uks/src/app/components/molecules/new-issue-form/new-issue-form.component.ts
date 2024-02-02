import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ISSUE_EVENT_TYPE, IssueDTO, IssueEventDTO, IssueEventRequest, IssueRequest } from 'src/models/issue/issue';
import { STATE, STATE_COLORS } from 'src/models/state/state';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { IssueService } from 'src/services/issue/issue.service';
import { NavigationService } from 'src/services/navigation/navigation.service';

@Component({
  selector: 'app-new-issue-form',
  templateUrl: './new-issue-form.component.html',
  styleUrl: './new-issue-form.component.scss'
})
export class NewIssueFormComponent {


  repoId: string;
  loggedUser?: UserBasicInfo;

  ISSUE_EVENT_TYPE = ISSUE_EVENT_TYPE;

  @Input() issue?: IssueDTO;
  @Output() updateIssueEvent: EventEmitter<IssueDTO | null> = new EventEmitter<IssueDTO | null>();
  @Output() newIssueEvent: EventEmitter<IssueRequest | null> = new EventEmitter<IssueRequest | null>();
  events: IssueEventDTO[] = [];

  newIssueForm = this.formBuilder.group({
    name: new FormControl(""),
    description: new FormControl(""),
  })

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private issueService: IssueService
  ) {
    this.repoId = localStorage.getItem("repoId") as string

    this.authService.getLoggedUser().subscribe({
      next: (logged?: UserBasicInfo) => {
        this.loggedUser = logged;
      }
    })

  }

  ngOnInit(): void {
    this.newIssueForm.controls.name.setValue(this.issue?.name ? this.issue?.name : "")
    this.newIssueForm.controls.description.setValue(this.issue?.description ? this.issue?.description : "")
  }

  onCancelClick(): void {
    this.issue ? this.emitUpdateIssueEvent(null) : this.emitNewIssueEvent(null);
  }

  onSubmitClick(): void {
    this.issue ? this.updateIssue() : this.emitNewIssueEvent(this.createIssueRequest());
  }

  private updateIssue() {
    let eventRequest;
    const newName = this.newIssueForm.controls.name.value as string;
    const newDescription = this.newIssueForm.controls.description.value as string;
    if (newName !== this.issue?.name) {
      eventRequest = this.createIssueEventRequest(ISSUE_EVENT_TYPE.NAME);
      eventRequest.name = newName
      this.sendNewIssueEventRequest(eventRequest)
      if (this.issue) {
        this.issue.name = newName
      }
    }
    if (newDescription !== this.issue?.description) {
      eventRequest = this.createIssueEventRequest(ISSUE_EVENT_TYPE.DESCRIPTION);
      eventRequest.description = newDescription
      this.sendNewIssueEventRequest(eventRequest)
      if (this.issue) {
        this.issue.description = newDescription
      }
    }
    if (this.issue) {
      this.emitUpdateIssueEvent(this.issue);
    }
  }

  private sendNewIssueEventRequest(eventRequest: IssueEventRequest) {
    this.issueService.updateIssue(eventRequest).subscribe({
      next: (res: IssueDTO | null) => {
        console.log(res);
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  private emitUpdateIssueEvent(changedIssue: IssueDTO | null) {
    this.updateIssueEvent.emit(changedIssue);
  }

  private emitNewIssueEvent(issueRequest: IssueRequest | null) {
    this.newIssueEvent.emit(issueRequest);
  }


  private createIssueEventRequest(type: ISSUE_EVENT_TYPE): IssueEventRequest {
    return {
      issueId: this.issue?.id as string,
      authorId: this.loggedUser?.id as string,
      type
    }
  }

  private createIssueRequest(): IssueRequest {
    return {
      name: this.newIssueForm.controls.name.value as string,
      description: this.newIssueForm.controls.description.value as string,
      repoId: this.repoId,
      authorId: this.loggedUser?.id as string,
    }
  }

}
