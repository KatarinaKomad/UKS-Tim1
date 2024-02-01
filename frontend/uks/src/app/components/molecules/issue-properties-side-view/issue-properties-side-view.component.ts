import { Component, EventEmitter, Input, Output } from '@angular/core';
import { IssueDTO } from 'src/models/issue/issue';
import { IssueService } from 'src/services/issue/issue.service';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { Toastr } from 'src/utils/toastr.service';

@Component({
  selector: 'app-issue-properties-side-view',
  templateUrl: './issue-properties-side-view.component.html',
  styleUrl: './issue-properties-side-view.component.scss'
})
export class IssuePropertiesSideViewComponent {

  @Input() issue: IssueDTO | undefined;
  // @Output() editEvent: EventEmitter<IssueDTO | null> = new EventEmitter<IssueDTO | null>();

  constructor(
    private issueService: IssueService,
    private navigationService: NavigationService,
    private toastr: Toastr
  ) {

  }

  deleteIssue() {
    this.issueService.deleteIssue(String(this.issue?.id)).subscribe({
      next: () => {
        this.toastr.success(`Issue deleted!`, 'Success');
        this.navigationService.navigateToProjectIssues();
      },
      error: (res: any) => {
        this.toastr.success(res, 'Error');
        console.log(res);
      },
    })
  }

  showMilestones() {
    throw new Error('Method not implemented.');
  }
  showLabels() {
    throw new Error('Method not implemented.');
  }
  showAssignees() {
    throw new Error('Method not implemented.');
  }

}
