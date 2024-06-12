import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MilestoneDTO, MilestoneRequest } from 'src/models/milestone/milestone';
import { MilestoneService } from 'src/services/milestone/milestone.service';
import { NewMilestoneDialogComponent } from '../dialogs/new-milestone-dialog/new-milestone-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { Toastr } from 'src/utils/toastr.service';
import { ChangeStateRequest, STATE } from 'src/models/state/state';
import { RepoService } from 'src/services/repo/repo.service';
import { IssueBasicInfoDTO, IssueDTO } from 'src/models/issue/issue';
import { NavigationService } from 'src/services/navigation/navigation.service';

@Component({
  selector: 'app-milestone-item',
  templateUrl: './milestone-item.component.html',
  styleUrl: './milestone-item.component.scss'
})
export class MilestoneItemComponent implements OnInit {

  STATE = STATE;

  repoId: string = '';

  completePercentage: number = 0;
  openIssues: IssueBasicInfoDTO[] = [];
  closedIssues: IssueBasicInfoDTO[] = [];
  canEdit: boolean = false;

  @Input() milestone: MilestoneDTO | undefined;
  @Output() editEvent: EventEmitter<MilestoneDTO | null> = new EventEmitter<MilestoneDTO | null>();
  @Output() deleteEvent: EventEmitter<MilestoneDTO | null> = new EventEmitter<MilestoneDTO | null>();



  constructor(
    private milestoneService: MilestoneService,
    private repoService: RepoService,
    private navigationService: NavigationService,
    public dialog: MatDialog,
    private toastr: Toastr,
  ) { }


  ngOnInit(): void {
    this.repoId = localStorage.getItem("repoId") as string

    if (this.milestone) {
      this.openIssues = this.milestone.issues.filter(i => i.state === STATE.OPEN)
      this.closedIssues = this.milestone.issues.filter(i => i.state === STATE.CLOSE)
      this.setCompletedPercentage();

    }
  }
  private setCompletedPercentage() {
    if (this.milestone && this.milestone.issues.length > 0) {
      const value = (this.closedIssues.length / this.milestone?.issues.length) * 100;
      this.completePercentage = Number(value.toFixed(2));
    }
  }


  ngAfterViewInit(): void {
    this.canEdit = this.repoService.getCanEditRepoItems()
  }


  editMilestone() {
    const dialogRef = this.dialog.open(NewMilestoneDialogComponent, {
      height: '60%',
      width: '65%',
      data: { repoId: this.repoId, milestone: this.milestone },
    });
    dialogRef.afterClosed().subscribe((result: MilestoneRequest) => {
      if (result) {
        this.milestoneService.updateMilestone(result).subscribe({
          next: (editedMilestone: MilestoneDTO | null) => {
            this.editEvent.emit(editedMilestone);
            this.toastr.success(`Milestone edited!`, 'Success');
          },
          error: (res: any) => {
            console.log(res);
          },
        });
      }
    });
  }
  deleteMilestone() {
    this.milestoneService.deleteMilestone(String(this.milestone?.id)).subscribe({
      next: () => {
        this.deleteEvent.emit(this.milestone);
        this.toastr.success(`Milestone deleted!`, 'Success');
      },
      error: (res: any) => {
        this.toastr.success(res, 'Error');
        console.log(res);
      },
    })
  }

  changeState(state: STATE) {
    const changeStateRequest: ChangeStateRequest = {
      id: this.milestone?.id as number,
      state
    };
    if (this.milestone) {
      this.milestone.state = state;
    }

    this.milestoneService.changeStateMilestone(changeStateRequest).subscribe({
      next: () => {
        this.editEvent.emit(this.milestone);
        this.toastr.success(`Milestone closed!`, 'Success');
      },
      error: (res: any) => {
        this.toastr.success(res, 'Error');
        console.log(res);
      },
    })
  }

  navigateToIssues() {
    this.navigationService.navigateToProjectIssues();
  }

}
