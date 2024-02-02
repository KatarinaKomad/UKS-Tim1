import { Component } from '@angular/core';
import { IssueDTO, IssueProperties, IssueRequest } from 'src/models/issue/issue';
import { IssueService } from 'src/services/issue/issue.service';
import { NavigationService } from 'src/services/navigation/navigation.service';

@Component({
  selector: 'app-new-issue',
  templateUrl: './new-issue.component.html',
  styleUrl: './new-issue.component.scss'
})
export class NewIssueComponent {

  issueRequest?: IssueRequest;

  issueProperties: IssueProperties = {};

  constructor(
    private navigationService: NavigationService,
    private issueService: IssueService,
  ) { }

  ngOnInit(): void {
  }

  handleSubmitForm(issueRequest: IssueRequest | null) {
    // cancel
    if (issueRequest === null) {
      this.navigationService.navigateToProjectIssues();
      return;
    }
    // submit
    issueRequest.assigneeIds = this.issueProperties.assignees?.map(a => a.id);
    issueRequest.labelIds = this.issueProperties.labels?.map(l => l.id);
    issueRequest.milestoneId = this.issueProperties.milestone?.id;

    this.issueService.createNewIssue(issueRequest).subscribe({
      next: (res: IssueDTO | null) => {
        console.log(res);
        this.navigationService.navigateToProjectIssues();
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  handleIssuePropertiesChange(issueProperties: IssueProperties) {
    this.issueProperties = issueProperties;
  }
}
