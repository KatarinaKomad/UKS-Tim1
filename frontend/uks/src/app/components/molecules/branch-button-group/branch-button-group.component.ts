import { Component } from '@angular/core';
import { BranchBasicInfoDTO, BranchDTO } from 'src/models/branch/branch';
import { BranchService } from 'src/services/branch/branch.service';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-branch-button-group',
  templateUrl: './branch-button-group.component.html',
  styleUrl: './branch-button-group.component.scss'
})
export class BranchButtonGroupComponent {

  selectedBranch?: BranchDTO;
  branchCount: number = 0;

  repoId: string;

  isCodeOptionOpen: boolean = false;
  isBranchOptionOpen: boolean = false;

  constructor(
    private navigationService: NavigationService,
    private repoService: RepoService,
    private branchService: BranchService,
  ) {
    this.repoId = localStorage.getItem("repoId") as string;
    this.setDefaultBranch();
    this.setBranchCount();
  }
  private setDefaultBranch() {
    this.repoService.getDefaultBranch(this.repoId).subscribe({
      next: (res: BranchDTO) => {
        this.selectedBranch = res;
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  private setBranchCount() {
    this.branchService.getBranchCount(this.repoId).subscribe({
      next: (res: number) => {
        this.branchCount = res;
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  changeBranch(selected: BranchDTO | null | undefined) {
    this.isBranchOptionOpen = !this.isBranchOptionOpen;
    if (selected) {
      this.selectedBranch = selected;
    }
  }

  navigateToBranches() {
    this.navigationService.navigateToBranchesPage();
  }
  openFileOptions() {
    throw new Error('Method not implemented.');
  }
  openCodeOptions(isOpen: boolean) {
    this.isCodeOptionOpen = isOpen;
  }

}
