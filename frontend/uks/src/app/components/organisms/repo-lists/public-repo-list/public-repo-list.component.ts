import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, Input, SimpleChanges } from '@angular/core';
import { FormControl } from '@angular/forms';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';
import { RepoService } from 'src/services/repo/repo.service';

@Component({
  selector: 'app-public-repo-list',
  templateUrl: './public-repo-list.component.html',
  styleUrl: './public-repo-list.component.scss'
})
export class PublicRepoListComponent {
  search = new FormControl('', []);

  allPublicRepos: RepoBasicInfoDTO[] = [];
  shownRepos: RepoBasicInfoDTO[] = [];

  constructor(
    private repoService: RepoService,
  ) {
    this.setPublicRepos();
  }

  setPublicRepos() {
    this.repoService.getAllPublic().subscribe({
      next: (response: RepoBasicInfoDTO[]) => {
        this.allPublicRepos = response;
        this.shownRepos = response;
      },
      error: (e: HttpErrorResponse) => {
        console.log(e);
      }
    });
  }

  onSearch() {
    const searchValue = (this.search.value as string).toLowerCase();
    let allCopy = this.allPublicRepos.slice();
    this.shownRepos = allCopy.filter(repo => this.checkRepoName(repo, searchValue) || this.checkOwnerName(repo, searchValue))
  }

  private checkRepoName(repo: any, searchValue: string) {
    return repo.name.toLowerCase().includes(searchValue);
  }

  private checkOwnerName(repo: any, searchValue: string) {
    const ownerName = (`${repo.owner.firstName} ${repo.owner.lastName}`);
    return ownerName.toLowerCase().includes(searchValue)
  }
}
