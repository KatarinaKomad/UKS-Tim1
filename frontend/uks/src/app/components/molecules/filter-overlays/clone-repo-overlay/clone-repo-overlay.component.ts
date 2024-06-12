import { Clipboard } from '@angular/cdk/clipboard';
import { Component, EventEmitter, Output } from '@angular/core';
import { BranchDTO } from 'src/models/branch/branch';
import { RepoBasicInfoDTO, getEmptyRepo } from 'src/models/repo/repo';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-clone-repo-overlay',
  templateUrl: './clone-repo-overlay.component.html',
  styleUrl: './clone-repo-overlay.component.scss'
})
export class CloneRepoOverlayComponent {

  repo: RepoBasicInfoDTO = getEmptyRepo();
  @Output() closeEvent: EventEmitter<BranchDTO | null> = new EventEmitter<BranchDTO | null>();

  repoId: string = '';
  copySuccess: boolean = false;

  constructor(
    private repoService: RepoService,
    private clipboard: Clipboard
  ) {
    this.repoId = localStorage.getItem("repoId") as string;
    this.repoService.getById(this.repoId).subscribe({
      next: (res: RepoBasicInfoDTO | null) => {
        if (res) {
          this.repo = res;
        }
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  closeForm() {
    this.closeEvent.emit(null);
  }


  copyName() {
    this.clipboard.copy(this.repo.cloneUri as string);
    this.copySuccess = true;
    setTimeout(() => {
      this.copySuccess = false;
    }, 2000);
  }

}
