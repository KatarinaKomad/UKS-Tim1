import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommitsResponseDto } from 'src/models/branch/branch';
import { FileDTO } from 'src/models/files/files';
import { BranchService } from 'src/services/branch/branch.service';
import { FileService } from 'src/services/file/file.service';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { Clipboard } from '@angular/cdk/clipboard';

@Component({
  selector: 'app-commits-page',
  templateUrl: './commits-page.component.html',
  styleUrl: './commits-page.component.scss'
})
export class CommitsPageComponent {

  commitHistory?: CommitsResponseDto[] = [];
  repoName: string;
  branchName: string = "master";
  filePath: string = "";
  copySuccess: boolean[] = [];

  constructor(
    private navigationService: NavigationService,
    private fileService: FileService,
    private route: ActivatedRoute,
    private clipboard: Clipboard,
  ) {
    this.repoName = localStorage.getItem("repoName") as string
  }

  ngOnInit(): void {
    if (this.route.params) {
      this.route.params.subscribe((params) => {
        this.branchName = params['branchName'] ? params['branchName'] : "master";
        this.filePath = params['filePath'] ? params['filePath'] : "";

        this.setCommitHistory();
      });
    }
  }
  private setCommitHistory() {
    const fileRequest = { branchName: this.branchName, filePath: this.filePath, repoName: this.repoName };
    this.fileService.getFileCommits(fileRequest).subscribe({
      next: (res: CommitsResponseDto[]) => {
        this.commitHistory = res;
      }, error: (e: any) => {
        console.log(e)
      }
    })
  }
  changeBranch(branchName: string) {
    this.navigationService.navigateToCommitHistory(branchName, this.filePath);
  }

  copyName(hash: string, i: number) {
    this.clipboard.copy(hash);
    this.copySuccess[i] = true;
    setTimeout(() => {
      this.copySuccess[i] = false;
    }, 2000);
  }

  navigateToCommitOverview(commit: CommitsResponseDto) {
    console.log(commit)
    throw new Error('Method not implemented.');
  }
}
