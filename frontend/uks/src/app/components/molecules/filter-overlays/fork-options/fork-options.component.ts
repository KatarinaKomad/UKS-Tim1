import { Component, EventEmitter, Input, Output } from '@angular/core';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';
import { NavigationService } from 'src/services/navigation/navigation.service';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-fork-options',
  templateUrl: './fork-options.component.html',
  styleUrl: './fork-options.component.scss'
})
export class ForkOptionsComponent {

  @Output() closeEvent: EventEmitter<void> = new EventEmitter<void>();

  forkedRepos: RepoBasicInfoDTO[] = [];

  constructor(
    private repoService: RepoService,
    private navigationService: NavigationService
  ) {

    const repoId = localStorage.getItem('repoId') as string;

    this.repoService.getAllForked(repoId).subscribe({
      next: (list: RepoBasicInfoDTO[]) => {
        this.forkedRepos = list;
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  closeForm() {
    this.closeEvent.emit();
  }

  navigate(repo: RepoBasicInfoDTO | null) {
    console.log(repo)
    repo !== null ?
      this.navigationService.navigateToRepo(repo) :
      this.navigationService.navigateToNewFork();
  }

}
