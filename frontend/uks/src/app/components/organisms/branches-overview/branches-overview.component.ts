import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Clipboard } from '@angular/cdk/clipboard';
import { Component, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource, MatTableDataSourcePaginator } from '@angular/material/table';
import { BranchBasicInfoDTO, BranchDTO, OriginTargetBranchRequest } from 'src/models/branch/branch';
import { BranchService } from 'src/services/branch/branch.service';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { RepoService } from 'src/services/repo/repo.service';
import { Toastr } from 'src/utils/toastr.service';
import { NewBranchDialogComponent } from '../../molecules/dialogs/new-branch-dialog/new-branch-dialog.component';

@Component({
  selector: 'app-branches-overview',
  templateUrl: './branches-overview.component.html',
  styleUrl: './branches-overview.component.scss'
})
export class BranchesOverviewComponent {

  displayedColumns: string[] = ['name', 'updatedAt', 'pull request'];

  dataSource: MatTableDataSource<BranchDTO, MatTableDataSourcePaginator> = new MatTableDataSource();

  repoId: string = "";
  repoBranches: BranchDTO[] = [];
  canEdit: boolean = false;
  copySuccess: boolean[] = [];


  @ViewChild(MatSort) sort: MatSort = new MatSort;

  constructor(
    public dialog: MatDialog,
    private branchService: BranchService,
    private navigationService: NavigationService,
    private repoService: RepoService,
    private _liveAnnouncer: LiveAnnouncer,
    private toastr: Toastr,
    private clipboard: Clipboard) {

  }
  ngOnInit(): void {
    this.repoId = localStorage.getItem("repoId") as string

    this.branchService.getRepoBranches(this.repoId).subscribe({
      next: (res: Array<BranchDTO>) => {
        this.repoBranches = res;
        this.dataSource = new MatTableDataSource(res);
        this.dataSource.sort = this.sort;
      }
    })
  }

  ngAfterViewInit(): void {
    this.canEdit = this.repoService.getCanEditRepoItems()
    if (this.canEdit) {
      this.displayedColumns = ['name', 'updatedAt', 'pull request', 'actions'];
    }
  }

  addNewBranch() {
    const dialogRef = this.getNewBranchDialogRef(null);
    dialogRef.afterClosed().subscribe((result: OriginTargetBranchRequest | null) => {
      if (result) {
        this.branchService.createNewBranch(result).subscribe({
          next: (newBranch: BranchDTO | null) => {
            if (newBranch) {
              this.dataSource.data.push(newBranch as BranchDTO);
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

  editBranch(branch: BranchDTO): void {
    const dialogRef = this.getNewBranchDialogRef(branch);
    dialogRef.afterClosed().subscribe((result: OriginTargetBranchRequest | null) => {
      if (result) {
        this.branchService.renameBranch(result).subscribe({
          next: (edited: BranchDTO | null) => {
            if (edited) {
              const index = this.dataSource.data.findIndex((lbl: BranchDTO) => lbl.id === edited.id);
              if (index !== -1) {
                this.dataSource.data[index] = edited;
                this.dataSource.data = [...this.dataSource.data];
                this.toastr.success(`Branch renamed!`, 'Success');
              } else {
                console.error(`Object with id ${edited.id} not found.`);
              }
            }
          },
          error: (res: any) => {
            this.toastr.error(res?.message, 'Error');
            console.log(res);
          },
        });
      }
    });
  }

  deleteBranch(branch: BranchDTO): void {
    this.removeFromTable(branch.id)

    this.branchService.deleteBranch({ repoId: this.repoId, branchName: branch.name }).subscribe({
      next: (res: any) => {
        this.toastr.success(`Branch ${branch.name} deleted!`, 'Success');
      },
      error: (res: any) => {
        this.toastr.success(res, 'Error');
        console.log(res);
      },
    })
  }

  removeFromTable(branchId: number): void {
    this.dataSource.data = this.dataSource.data.filter((elem: { id: number; }) => elem.id !== branchId);
    this.dataSource.data = [...this.dataSource.data];
  }

  navigateToBranchCode() {
    // this.navigationService.navigateToBranchCode();
  }
  navigateToPr(pr: any) {
    // this.navigationService.navigateToPr(pr.id);
  }

  copyName(branchName: string, i: number) {
    this.clipboard.copy(branchName);
    this.copySuccess[i] = true;
    setTimeout(() => {
      this.copySuccess[i] = false;
    }, 2000);
  }

  private getNewBranchDialogRef(branch: BranchDTO | null) {
    return this.dialog.open(NewBranchDialogComponent, {
      height: '60%',
      width: '65%',
      data: { repoId: this.repoId, repoBranches: this.repoBranches, branchToEdit: branch },
    });
  }


}

