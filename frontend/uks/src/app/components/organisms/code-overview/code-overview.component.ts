import { ChangeDetectorRef, Component } from '@angular/core';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-code-overview',
  templateUrl: './code-overview.component.html',
  styleUrl: './code-overview.component.scss'
})
export class CodeOverviewComponent {

  repo?: RepoBasicInfoDTO;

  constructor(
    private repoService: RepoService,
    private cdr: ChangeDetectorRef
  ) {
    const repoId = localStorage.getItem("repoId") as string;
    this.setRepo(repoId);
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

}
