import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormBuilder } from '@angular/forms';
import { ISSUE_EVENT_TYPE, IssueDTO, IssueRequest, IssueEventDTO, IssueEventRequest } from 'src/models/issue/issue';
import { PullRequestDTO, PullRequestEventDTO } from 'src/models/pull-request/pull-request';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { IssueService } from 'src/services/issue/issue.service';

@Component({
  selector: 'app-new-pr-form',
  templateUrl: './new-pr-form.component.html',
  styleUrl: './new-pr-form.component.scss'
})
export class NewPrFormComponent {

  repoId: string;
  loggedUser?: UserBasicInfo;

  ISSUE_EVENT_TYPE = ISSUE_EVENT_TYPE;

  @Input() pr?: PullRequestDTO;
  @Output() updateIssueEvent: EventEmitter<PullRequestDTO | null> = new EventEmitter<PullRequestDTO | null>();
  @Output() newIssueEvent: EventEmitter<IssueRequest | null> = new EventEmitter<IssueRequest | null>();
  events: PullRequestEventDTO[] = [];

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
    this.newIssueForm.controls.name.setValue(this.pr?.name ? this.pr?.name : "")
    this.newIssueForm.controls.description.setValue(this.pr?.description ? this.pr?.description : "")
  }

  onCancelClick(): void {
    this.pr ? this.emitUpdateIssueEvent(null) : this.emitNewIssueEvent(null);
  }

  onSubmitClick(): void {
    this.pr ? this.updateIssue() : this.emitNewIssueEvent(this.createIssueRequest());
  }

  private updateIssue() {
    let eventRequest;
    const newName = this.newIssueForm.controls.name.value as string;
    const newDescription = this.newIssueForm.controls.description.value as string;
    if (newName !== this.pr?.name) {
      eventRequest = this.createIssueEventRequest(ISSUE_EVENT_TYPE.NAME);
      eventRequest.name = newName
      this.sendNewIssueEventRequest(eventRequest)
      if (this.pr) {
        this.pr.name = newName
      }
    }
    if (newDescription !== this.pr?.description) {
      eventRequest = this.createIssueEventRequest(ISSUE_EVENT_TYPE.DESCRIPTION);
      eventRequest.description = newDescription
      this.sendNewIssueEventRequest(eventRequest)
      if (this.pr) {
        this.pr.description = newDescription
      }
    }
    if (this.pr) {
      this.emitUpdateIssueEvent(this.pr);
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

  private emitUpdateIssueEvent(changedIssue: PullRequestDTO | null) {
    this.updateIssueEvent.emit(changedIssue);
  }

  private emitNewIssueEvent(issueRequest: IssueRequest | null) {
    this.newIssueEvent.emit(issueRequest);
  }


  private createIssueEventRequest(type: ISSUE_EVENT_TYPE): IssueEventRequest {
    return {
      issueId: this.pr?.id as string,
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
