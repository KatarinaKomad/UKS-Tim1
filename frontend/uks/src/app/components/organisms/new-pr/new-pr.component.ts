import { Component } from '@angular/core';
import { IssueRequest, IssueProperties, IssueDTO } from 'src/models/issue/issue';
import { PullRequestDTO, PullRequestProperties, PullRequestRequest } from 'src/models/pull-request/pull-request';
import { IssueService } from 'src/services/issue/issue.service';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { PullRequestService } from 'src/services/pull-request/pull-request.service';

@Component({
  selector: 'app-new-pr',
  templateUrl: './new-pr.component.html',
  styleUrl: './new-pr.component.scss'
})
export class NewPrComponent {

  prRequest?: PullRequestRequest;

  prProperties: PullRequestProperties = {};

  constructor(
    private navigationService: NavigationService,
    private prService: PullRequestService
  ) { }

  ngOnInit(): void {
  }

  handleSubmitForm(prReqest: PullRequestRequest | null) {
    // cancel
    if (prReqest === null) {
      this.navigationService.navigateToProjectPRs();
      return;
    }
    // submit
    prReqest.assigneeIds = (this.prProperties.assignees ?? []).map(a => a.id) || new Array;
    prReqest.labelIds = (this.prProperties.labels ?? []).map(l => l.id);
    prReqest.milestoneId = this.prProperties.milestone?.id;
    
    console.log(prReqest)

    this.prService.create(prReqest).subscribe({
      next: (res: PullRequestDTO | null) => {
        console.log(res);
        this.navigationService.navigateToProjectPRs();
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  handlePRPropertiesChange(prProperties: PullRequestProperties) {
    for (const key in prProperties) {
      if (prProperties.hasOwnProperty(key)) {
        this.prProperties[key] = prProperties[key]
      }
    }
  }

}
