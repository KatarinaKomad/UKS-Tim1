import { Component } from '@angular/core';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-forks-overview-page',
  templateUrl: './forks-overview-page.component.html',
  styleUrl: './forks-overview-page.component.scss'
})
export class ForksOverviewPageComponent {

  allRepos: RepoBasicInfoDTO[] = [];

  constructor(
    private repoService: RepoService
  ) {
    const repoId = localStorage.getItem('repoId') as string;

    this.repoService.getAllForked(repoId).subscribe({
      next: (list: RepoBasicInfoDTO[]) => {
        this.allRepos = list;
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }


}
