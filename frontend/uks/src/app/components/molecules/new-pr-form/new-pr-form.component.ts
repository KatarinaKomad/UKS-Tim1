import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormBuilder } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { BranchDTO } from 'src/models/branch/branch';
import { ISSUE_EVENT_TYPE, IssueDTO, IssueRequest, IssueEventDTO, IssueEventRequest } from 'src/models/issue/issue';
import { PullRequestDTO, PullRequestEventDTO, PullRequestEventRequest, PullRequestRequest } from 'src/models/pull-request/pull-request';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { BranchService } from 'src/services/branch/branch.service';
import { IssueService } from 'src/services/issue/issue.service';
import { PullRequestService } from 'src/services/pull-request/pull-request.service';

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
  @Output() updatePREvent: EventEmitter<PullRequestDTO | null> = new EventEmitter<PullRequestDTO | null>();
  @Output() newPREvent: EventEmitter<PullRequestRequest | null> = new EventEmitter<PullRequestRequest | null>();
  events: PullRequestEventDTO[] = [];

  newPRForm = this.formBuilder.group({
    name: new FormControl(""),
    description: new FormControl(""),
    origin: new FormControl(""),
    target: new FormControl("")
  })

  targetBranches?: BranchDTO[];
  originBranches?: BranchDTO[];

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private prService: PullRequestService,
    private branchService: BranchService,
    private toastr: ToastrService
  ) {
    this.repoId = localStorage.getItem("repoId") as string

    this.authService.getLoggedUser().subscribe({
      next: (logged?: UserBasicInfo) => {
        this.loggedUser = logged;
      }
    })

  }

  ngOnInit(): void {
    this.newPRForm.controls.name.setValue(this.pr?.name ? this.pr?.name : "")
    this.newPRForm.controls.description.setValue(this.pr?.description ? this.pr?.description : "")
    this.newPRForm.controls.origin.setValue(this.pr?.origin?.name ? this.pr?.origin?.name : "")
    this.newPRForm.controls.target.setValue(this.pr?.target?.name ? this.pr?.target?.name : "")

    if (!this.pr) {
      this.branchService.getRepoBranches(this.repoId).subscribe({
        next: (res: BranchDTO[]) => {
          this.originBranches = res;
          this.targetBranches = res;
        }, 
        error: (error) => {
          console.log(error)
        }
      })
    }
  }

  onCancelClick(): void {
    this.pr ? this.emitUpdatePREvent(null) : this.emitNewPREvent(null);
  }

  onSubmitClick(): void {
    if (!this.pr) {
      if (this.newPRForm.controls.origin.value === this.newPRForm.controls.target.value) {
        this.toastr.error("Origin and target branch cannot be the same!");
        return;
      }
      this.emitNewPREvent(this.createPRRequest());
    }
    else {
      this.updatePR();
    }
  }

  private updatePR() {
    let eventRequest;
    const newName = this.newPRForm.controls.name.value as string;
    const newDescription = this.newPRForm.controls.description.value as string;
    if (newName !== this.pr?.name) {
      eventRequest = this.createPREventRequest(ISSUE_EVENT_TYPE.NAME);
      eventRequest.name = newName
      this.sendNewPREventRequest(eventRequest)
      if (this.pr) {
        this.pr.name = newName
      }
    }
    if (newDescription !== this.pr?.description) {
      eventRequest = this.createPREventRequest(ISSUE_EVENT_TYPE.DESCRIPTION);
      eventRequest.description = newDescription
      this.sendNewPREventRequest(eventRequest)
      if (this.pr) {
        this.pr.description = newDescription
      }
    }
    if (this.pr) {
      this.emitUpdatePREvent(this.pr);
    }
  }

  private sendNewPREventRequest(eventRequest: PullRequestEventRequest) {
    this.prService.update(eventRequest).subscribe({
      next: (res: PullRequestDTO | null) => {
        console.log(res);
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  private emitUpdatePREvent(changedPR: PullRequestDTO | null) {
    this.updatePREvent.emit(changedPR);
  }

  private emitNewPREvent(prRequest: PullRequestRequest | null) {
    this.newPREvent.emit(prRequest);
  }


  private createPREventRequest(type: ISSUE_EVENT_TYPE): PullRequestEventRequest {
    return {
      prId: this.pr?.id as string,
      authorId: this.loggedUser?.id as string,
      type
    }
  }

  private createPRRequest(): PullRequestRequest {
    return {
      name: this.newPRForm.controls.name.value as string,
      description: this.newPRForm.controls.description.value as string,
      repoId: this.repoId,
      authorId: this.loggedUser?.id as string,
      originId: this.originBranches?.filter((br) => br.name == this.newPRForm.controls.origin.value).at(0)?.id,
      targetId: this.targetBranches?.filter((br) => br.name == this.newPRForm.controls.target.value).at(0)?.id
    }
  }

}
