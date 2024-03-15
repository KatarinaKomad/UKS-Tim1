import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommitsResponseDto } from 'src/models/branch/branch';
import { FileDTO } from 'src/models/files/files';
import { BranchService } from 'src/services/branch/branch.service';
import { FileService } from 'src/services/file/file.service';
import { NavigationService } from 'src/services/navigation/navigation.service';

@Component({
  selector: 'app-commits-page',
  templateUrl: './commits-page.component.html',
  styleUrl: './commits-page.component.scss'
})
export class CommitsPageComponent {

  commitHistory?: CommitsResponseDto[] = [];
  // file: FileDTO = { isFolder: false, name: '', path: '' };
  repoName: string;
  branchName: string = "master";
  filePath: string = "";

  constructor(
    private navigationService: NavigationService,
    private fileService: FileService,
    private route: ActivatedRoute,
  ) {
    this.repoName = localStorage.getItem("repoName") as string
  }

  ngOnInit(): void {
    if (this.route.params) {
      this.route.params.subscribe((params) => {
        this.branchName = params['branchName'] ? params['branchName'] : "master";
        this.filePath = params['filePath'] ? params['filePath'] : "";

        this.fileService.getFileCommits({ branchName: this.branchName, filePath: this.filePath, repoName: this.repoName }).subscribe({
          next: (res: CommitsResponseDto[]) => {
            this.commitHistory = res;
          }
        })
      });
    }
  }

  changeBranch(branchName: string) {
    this.navigationService.navigateToCommitHistory(branchName, this.filePath);
  }
}
