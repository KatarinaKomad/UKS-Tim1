import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {
  ISSUE_EVENT_TYPE,
  IssueDTO,
  IssueEventDTO,
  IssueEventRequest,
  IssueProperties,
  IssueRequest,
} from 'src/models/issue/issue';
import { STATE, STATE_COLORS } from 'src/models/state/state';
import { UserBasicInfo } from 'src/models/user/user';
import { AuthService } from 'src/services/auth/auth.service';
import { IssueService } from 'src/services/issue/issue.service';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { ItemCommentsViewComponent } from '../../molecules/item-comments-view/item-comments-view.component';
import { Comment } from 'src/models/comment/comment';

@Component({
  selector: 'app-issue-overview',
  templateUrl: './issue-overview.component.html',
  styleUrl: './issue-overview.component.scss',
})
export class IssueOverviewComponent implements AfterViewInit {
  @ViewChild(ItemCommentsViewComponent)
  itemCommentsViewComponent!: ItemCommentsViewComponent;

  repoId: string;
  issueId: string = '';
  loggedUser?: UserBasicInfo;

  STATE_COLORS = STATE_COLORS;
  STATE = STATE;
  ISSUE_EVENT_TYPE = ISSUE_EVENT_TYPE;

  isEdit: boolean = false;

  issue?: IssueDTO;
  issueProperties: IssueProperties = {};
  events: IssueEventDTO[] = [];

  stateColor: string = STATE_COLORS.OPEN;

  showComments: boolean = true;
  commentToEdit?: Comment;

  constructor(
    private navigationService: NavigationService,
    private authService: AuthService,
    private issueService: IssueService,
    private route: ActivatedRoute
  ) {
    this.repoId = localStorage.getItem('repoId') as string;

    this.authService.getLoggedUser().subscribe({
      next: (logged?: UserBasicInfo) => {
        this.loggedUser = logged;
      },
    });
  }

  ngOnInit(): void {
    this.getIssueFromRoute();
    this.getIssueEvents();
  }

  ngAfterViewInit() {
    this.handleCommentAdded();
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
      },
      error: (e: any) => {
        console.log(e);
      },
    });
  }

  handleIssuePropertiesChange(issueProperties: IssueProperties) {
    this.issueProperties = { ...issueProperties };
  }

  showProjectIssues() {
    this.navigationService.navigateToProjectIssues();
  }

  private createIssueEventRequest(type: ISSUE_EVENT_TYPE): IssueEventRequest {
    return {
      issueId: this.issueId,
      authorId: this.loggedUser?.id as string,
      type,
    };
  }

  private getIssueFromRoute() {
    if (this.route.params) {
      this.route.params.subscribe((params) => {
        this.issueId = params['issueId'];
        this.issueService.getById(this.issueId).subscribe({
          next: (res: IssueDTO) => {
            this.issue = res;
            this.setStateColor(res.state);

            this.issueProperties.assignees = this.issue?.assignees;
            this.issueProperties.labels = this.issue?.labels;
            this.issueProperties.milestone = this.issue?.milestone;

            this.issueProperties = { ...this.issueProperties };
          },
          error: (e: any) => {
            console.log(e);
          },
        });
      });
    }
  }
  private setStateColor(state: STATE) {
    switch (state) {
      case STATE.OPEN:
        this.stateColor = STATE_COLORS.OPEN.toString();
        break;
      case STATE.CLOSE:
        this.stateColor = STATE_COLORS.CLOSE.toString();
        break;
      case STATE.MERGED:
        this.stateColor = STATE_COLORS.MERGED.toString();
        break;
      default:
        this.stateColor = STATE_COLORS.OPEN;
    }
  }

  private getIssueEvents() {
    this.issueService.getIssueEventHistory(this.issueId).subscribe({
      next: (res: IssueEventDTO[]) => {
        this.events = res;
      },
      error: (e: any) => {
        console.log(e);
      },
    });
  }

  onNoComments(hasNoComments: boolean) {
    this.showComments = !hasNoComments;
  }

  handleCommentAdded() {
    this.itemCommentsViewComponent.getComments(this.issueId);
  }

  handleCommentDeleted() {
    this.itemCommentsViewComponent.getComments(this.issueId);
  }

  onEditComment(comment: Comment) {
    this.commentToEdit = comment;
  }
}
