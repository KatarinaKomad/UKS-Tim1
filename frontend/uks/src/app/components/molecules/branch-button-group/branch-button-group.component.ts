import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BranchDTO } from 'src/models/branch/branch';
import { FileDTO } from 'src/models/files/files';
import { BranchService } from 'src/services/branch/branch.service';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-branch-button-group',
  templateUrl: './branch-button-group.component.html',
  styleUrl: './branch-button-group.component.scss'
})
export class BranchButtonGroupComponent implements OnInit {

  @Input() showCodeOptions: boolean = false;
  @Output() changeBranchEvent: EventEmitter<string> = new EventEmitter<string>();

  selectedBranchName?: string;
  branchCount: number = 0;

  repoId: string;


  isCodeOptionOpen: boolean = false;
  isBranchOptionOpen: boolean = false;

  constructor(
    private navigationService: NavigationService,
    private repoService: RepoService,
    private branchService: BranchService,
    private route: ActivatedRoute,
  ) {
    this.repoId = localStorage.getItem("repoId") as string;

    this.setBranchCount();
  }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.selectedBranchName = params['branchName'] ? params['branchName'] : this.setDefaultBranch();;

    });
  }

  private setDefaultBranch() {
    this.repoService.getDefaultBranch(this.repoId).subscribe({
      next: (res: BranchDTO) => {
        this.selectedBranchName = res.name;
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

  changeBranch(selectedName: string | null | undefined) {
    this.isBranchOptionOpen = !this.isBranchOptionOpen;
    if (selectedName) {
      this.selectedBranchName = selectedName;
      this.changeBranchEvent.emit(selectedName);
    }
  }

  navigateToBranches() {
    this.navigationService.navigateToBranchesPage();
  }

  openCodeOptions(isOpen: boolean) {
    this.isCodeOptionOpen = isOpen;
  }

}
