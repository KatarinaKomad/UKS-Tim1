import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { OriginTargetBranchRequest } from 'src/models/branch/branch';
import { ISSUE_EVENT_TYPE, IssueEventDTO } from 'src/models/issue/issue';
import { PullRequestDTO, PullRequestEventDTO, PullRequestEventRequest, PullRequestProperties } from 'src/models/pull-request/pull-request';
import { STATE_COLORS, STATE } from 'src/models/state/state';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { BranchService } from 'src/services/branch/branch.service';
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
    private branchService: BranchService,
    private route: ActivatedRoute,
    private prService: PullRequestService,
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
    this.getPRFromRoute();
    this.getPREvents();
  }

  edit() {
    this.isEdit = true;
  }
  closeEdit(editedPR: PullRequestDTO | null) {
    this.isEdit = false;
    if (this.pr && editedPR) {
      this.pr.name = editedPR.name;
      this.pr.description = editedPR.description;
    }
  }

  mergeBranches(state: STATE) {
    let mr = this.createMergeRequest();
    this.branchService.mergeBranches(mr).subscribe({
      next: () => {
        this.changeStatus(state);
      }, error: () => {
        this.toastr.error('Cannot merge these branches')
      }
    })
  }

  changeStatus(state: STATE) {
    let eventRequest = this.createPREventRequest(ISSUE_EVENT_TYPE.STATE);
    eventRequest.state = state;
    this.prService.update(eventRequest).subscribe({
      next: (res: PullRequestDTO | null) => {
        if (this.pr && res) {
          this.pr.state = state;
          this.setStateColor(state);
          this.toastr.success('Brances are merged successfully')
        }
      }, error: (e: any) => {
        this.toastr.error('Branches are merged but status is not changed')
      }
    })
  }

  createMergeRequest(): OriginTargetBranchRequest {
    return {
      repoId: this.repoId,
      originName: this.pr!.origin.name,
      targetName: this.pr!.target.name
    }
  }

  handlePRPropertiesChange(prProperties: PullRequestProperties) {
    this.prProperties = { ...prProperties };
  }

  showProjectPRs() {
    this.navigationService.navigateToProjectPRs();
  }


  private createPREventRequest(type: ISSUE_EVENT_TYPE): PullRequestEventRequest {
    return {
      prId: this.prId,
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

  private getPREvents() {
    this.prService.getPREventHistory(this.prId).subscribe({
      next: (res: PullRequestEventDTO[]) => {
        this.events = res;
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

}
