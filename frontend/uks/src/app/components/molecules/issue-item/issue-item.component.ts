import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { RepoService } from 'src/services/repo/repo.service';
import { IssueDTO } from 'src/models/issue/issue';
import { Toastr } from 'src/utils/toastr.service';
import { STATE } from 'src/models/state/state';
import { IssueService } from 'src/services/issue/issue.service';
import { NavigationService } from 'src/services/navigation/navigation.service';

@Component({
  selector: 'app-issue-item',
  templateUrl: './issue-item.component.html',
  styleUrl: './issue-item.component.scss'
})
export class IssueItemComponent {

  STATE = STATE;

  repoId: string = '';

  completePercentage: number = 0;
  openIssues: [] = [];
  closedIssues: [] = [];
  canEdit: boolean = false;

  @Input() issue: IssueDTO | undefined;

  constructor(
    private navigationService: NavigationService,
    private repoService: RepoService,
    public dialog: MatDialog,
  ) { }


  ngOnInit(): void {
    this.repoId = localStorage.getItem("repoId") as string
  }

  ngAfterViewInit(): void {
    this.repoService.getCanEditRepoItems().subscribe({
      next: (canEdit: boolean) => {
        this.canEdit = canEdit;
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  viewIssue() {
    this.navigationService.navigateToIssueOverview(this.issue as IssueDTO);
  }

}
