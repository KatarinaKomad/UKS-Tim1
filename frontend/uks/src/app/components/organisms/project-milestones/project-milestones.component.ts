import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Sort } from '@angular/material/sort';
import { MilestoneDTO, MilestoneRequest } from 'src/models/milestone/milestone';
import { MilestoneService } from 'src/services/milestone/milestone.service';
import { Toastr } from 'src/utils/toastr.service';
import { NewMilestoneDialogComponent } from '../../molecules/dialogs/new-milestone-dialog/new-milestone-dialog.component';
import { STATE } from 'src/models/state/state';

@Component({
  selector: 'app-project-milestones',
  templateUrl: './project-milestones.component.html',
  styleUrl: './project-milestones.component.scss'
})
export class ProjectMilestonesComponent implements OnInit {


  allMilestones: MilestoneDTO[] = [];
  shownMilestones: MilestoneDTO[] = [];

  shownOpen: boolean = true;

  repoId: string = "";

  constructor(
    public dialog: MatDialog,
    private milestoneService: MilestoneService
  ) { }

  ngOnInit(): void {
    this.repoId = localStorage.getItem("repoId") as string

    this.milestoneService.getAllRepoMilestones(this.repoId).subscribe({
      next: (res: Array<MilestoneDTO>) => {
        this.allMilestones = res;
        this.filterShownMilestones();
      }
    })
  }


  addNewMilestone() {
    const dialogRef = this.dialog.open(NewMilestoneDialogComponent, {
      height: '60%',
      width: '65%',
      data: { repoId: this.repoId },
    });
    dialogRef.afterClosed().subscribe((result: MilestoneRequest) => {
      if (result) {
        this.milestoneService.createNewMilestone(result).subscribe({
          next: (newMilestone: MilestoneDTO | null) => {
            if (newMilestone) {
              this.allMilestones.push(newMilestone as MilestoneDTO);
              this.filterShownMilestones();
            }
          }
        });
      }
    });
  }

  updateMilestone(editedMilestone: MilestoneDTO | null): void {
    if (editedMilestone) {
      const index = this.allMilestones.findIndex((lbl: MilestoneDTO) => lbl.id === editedMilestone.id);
      if (index !== -1) {
        this.allMilestones[index] = editedMilestone;
        this.filterShownMilestones();
      } else {
        console.error(`Object with id ${editedMilestone.id} not found.`);
      }
    }

  }

  deleteMilestone(milestone: MilestoneDTO | null): void {
    if (milestone) {
      this.allMilestones = this.allMilestones.filter((elem: { id: number; }) => elem.id !== milestone.id);
      this.filterShownMilestones();
    }

  }


  private filterShownMilestones() {
    this.shownMilestones = this.allMilestones.filter((elem: MilestoneDTO) =>
      this.shownOpen ? (elem.state === STATE.OPEN) : (elem.state === STATE.CLOSE));

    this.shownMilestones = [...this.shownMilestones];
  }

  changeShownMilestones(shownOpen: boolean) {
    this.shownOpen = shownOpen;
    this.filterShownMilestones();

  }


}
