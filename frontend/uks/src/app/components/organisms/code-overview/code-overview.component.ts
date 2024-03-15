import { ChangeDetectorRef, Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FileDTO, FileRequest } from 'src/models/files/files';
import { RepoBasicInfoDTO, getEmptyRepo } from 'src/models/repo/repo';
import { FileService } from 'src/services/file/file.service';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-code-overview',
  templateUrl: './code-overview.component.html',
  styleUrl: './code-overview.component.scss'
})
export class CodeOverviewComponent {

  showFileContent: boolean = false;

  repo: RepoBasicInfoDTO = getEmptyRepo();
  repoId: string = "";
  repoName: string = "";

  branchName: string = "master";
  filePath: string = "";
  files: FileDTO[] = [];

  constructor(
    private repoService: RepoService,
    private navigationService: NavigationService,
    private fileService: FileService,
    private route: ActivatedRoute,
  ) {
    this.repoId = localStorage.getItem("repoId") as string;
    this.repoName = localStorage.getItem("repoName") as string;

    this.setRepo(this.repoId);
  }

  ngOnInit(): void {
    if (this.route.params) {
      this.route.params.subscribe((params) => {
        this.branchName = params['branchName'] ? params['branchName'] : "master";
        this.filePath = params['filePath'] ? params['filePath'] : "";

        this.setFiles();
      });
    }

    if (this.route.queryParams) {
      this.route.queryParams.subscribe((params) => {
        if (params['isFile']) {
          this.showFileContent = params['isFile'];
        } else
          this.showFileContent = false;
      });
    } else {
      this.showFileContent = false;
    }

  }

  private setFiles() {
    const request: FileRequest = this.createFileRequest()
    this.fileService.getFiles(request).subscribe({
      next: (res: FileDTO[]) => {
        console.log(res);
        this.files = res;
      }
    })
  }

  private setRepo(repoId: string) {
    this.repoService.getById(repoId).subscribe({
      next: (res: RepoBasicInfoDTO | null) => {
        if (res) {
          this.repo = res;
        }
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  private createFileRequest(): FileRequest {
    return {
      branchName: this.branchName,
      repoName: this.repoName,
      filePath: this.filePath
    };
  }

  changeBranch(selectedName: string) {
    this.navigationService.navigateToBranchCodeOverview(selectedName);
  }

  navigateToCommitHistory(file: FileDTO | null) {
    this.navigationService.navigateToCommitHistory(this.branchName, file ? file.path : this.filePath);
  }
}
