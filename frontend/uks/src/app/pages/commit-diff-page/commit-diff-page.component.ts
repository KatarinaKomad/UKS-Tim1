import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommitDiffRequest, CommitDiffResponseDTO } from 'src/models/commit/commit';
import { BranchService } from 'src/services/branch/branch.service';

@Component({
  selector: 'app-commit-diff-page',
  templateUrl: './commit-diff-page.component.html',
  styleUrl: './commit-diff-page.component.scss'
})
export class CommitDiffPageComponent {

  repoId: string = "";
  branchName: string = "master";
  commitHash: string = "";
  commitDiffs?: CommitDiffResponseDTO;

  constructor(
    private route: ActivatedRoute,
    private branchService: BranchService,
  ) {
    this.repoId = localStorage.getItem("repoId") as string
  }

  ngOnInit(): void {
    if (this.route.params) {
      this.route.params.subscribe((params) => {
        this.branchName = params['branchName'] ? params['branchName'] : "master";
        this.commitHash = params['commitHash'] ? params['commitHash'] : "";

        this.setFileDiffs();
      });
    }
  }

  private setFileDiffs() {
    const request: CommitDiffRequest = this.createCommitDiffRequest()
    this.branchService.getFileDiffs(request).subscribe({
      next: (res: CommitDiffResponseDTO) => {
        this.commitDiffs = res;
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }
  createCommitDiffRequest(): CommitDiffRequest {
    return {
      branchName: this.branchName,
      commit: this.commitHash,
      repoId: this.repoId
    }
  }
}

