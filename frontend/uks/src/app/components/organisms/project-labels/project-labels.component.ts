import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NewLabelDialogComponent } from '../../molecules/dialogs/new-label-dialog/new-label-dialog.component';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { LabelService } from 'src/services/label/label.service';
import { LabelDTO, LabelRequest } from 'src/models/label/label';
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Toastr } from 'src/utils/toastr.service';
import { AuthService } from 'src/services/auth/auth.service';
import { RepoService } from 'src/services/repo/repo.service';
import { UserBasicInfo } from 'src/models/user/user';
import { EditRepoRequest } from 'src/models/repo/repo';


@Component({
  selector: 'app-project-labels',
  templateUrl: './project-labels.component.html',
  styleUrl: './project-labels.component.scss'
})
export class ProjectLabelsComponent implements OnInit, AfterViewInit {

  displayedColumns: string[] = ['name', 'description', 'issues'];

  dataSource: any;

  repoId: string = "";
  canEdit: boolean = false;

  @ViewChild(MatSort)
  sort: MatSort = new MatSort;

  constructor(
    public dialog: MatDialog,
    private labelService: LabelService,
    private authService: AuthService,
    private repoService: RepoService,
    private _liveAnnouncer: LiveAnnouncer,
    private toastr: Toastr) {

  }
  ngOnInit(): void {
    this.repoId = localStorage.getItem("repoId") as string

    this.labelService.getAllRepoLabels(this.repoId).subscribe({
      next: (res: Array<LabelDTO>) => {
        this.dataSource = new MatTableDataSource(res);
        this.dataSource.sort = this.sort;
      }
    })
  }

  ngAfterViewInit(): void {
    this.setAddButtonVisible();
  }

  addNewLabel() {
    const dialogRef = this.dialog.open(NewLabelDialogComponent, {
      height: '60%',
      width: '65%',
      data: { repoId: this.repoId },
    });
    dialogRef.afterClosed().subscribe((result: LabelRequest) => {
      if (result) {
        this.labelService.createNewLabel(result).subscribe({
          next: (newLabel: LabelDTO | null) => {
            if (newLabel) {
              this.dataSource.data.push(newLabel as LabelDTO);
              this.dataSource.data = [...this.dataSource.data];
            }
          }
        });
      }
    });
  }

  announceSortChange(sortState: Sort) {
    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  }

  editLabel(label: LabelDTO): void {
    const dialogRef = this.dialog.open(NewLabelDialogComponent, {
      height: '60%',
      width: '65%',
      data: { repoId: this.repoId, label },
    });
    dialogRef.afterClosed().subscribe((result: LabelRequest) => {
      if (result) {
        this.labelService.editLabel(result).subscribe({
          next: (editedLabel: LabelDTO | null) => {
            if (editedLabel) {
              const index = this.dataSource.data.findIndex((lbl: LabelDTO) => lbl.id === editedLabel.id);
              if (index !== -1) {
                this.dataSource.data[index] = editedLabel;
                this.dataSource.data = [...this.dataSource.data];
                this.toastr.success(`Label edited!`, 'Success');
              } else {
                console.error(`Object with id ${editedLabel.id} not found.`);
              }
            }
          },
          error: (res: any) => {
            this.toastr.success(res, 'Error');
            console.log(res);
          },
        });
      }
    });
  }

  deleteLabel(label: LabelDTO): void {
    this.removeFromTable(label.id)

    this.labelService.deleteLabel(String(label.id)).subscribe({
      next: (res: any) => {
        this.toastr.success(`Label deleted!`, 'Success');
      },
      error: (res: any) => {
        this.toastr.success(res, 'Error');
        console.log(res);
      },
    })
  }

  removeFromTable(labelId: number): void {
    this.dataSource.data = this.dataSource.data.filter((elem: { id: number; }) => elem.id !== labelId);
    this.dataSource.data = [...this.dataSource.data];
  }


  private setAddButtonVisible() {
    this.authService.getLoggedUser().subscribe({
      next: (user: UserBasicInfo | undefined) => {
        const repoRequest = this.getCanEditRepoRequest(user);
        if (!repoRequest) return;
        this.repoService.canEditRepoItems(repoRequest).subscribe({
          next: (canEdit: boolean) => {
            this.canEdit = canEdit;
            if (canEdit) {
              this.displayedColumns = ['name', 'description', 'issues', 'actions'];
            }
          }, error: (e: any) => {
            console.log(e);
          }
        })
      }, error: (e: any) => {
        console.log(e);
      },
    });
  }

  getCanEditRepoRequest(user: UserBasicInfo | undefined): EditRepoRequest | null {
    const repoId = localStorage.getItem("repoId");
    if (!repoId || !user?.id) return null;
    return { repoId, userId: user.id }
  }

}
