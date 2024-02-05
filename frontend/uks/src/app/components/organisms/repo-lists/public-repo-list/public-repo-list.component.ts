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

  @Input() repos: RepoBasicInfoDTO[] = [];
  @Input() title: string = '';

  search = new FormControl('', []);

  allRepos: RepoBasicInfoDTO[] = [];
  shownRepos: RepoBasicInfoDTO[] = [];

  constructor(
  ) { }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['repos'].currentValue) {
      this.allRepos = [...changes['repos'].currentValue];
      this.shownRepos = [...changes['repos'].currentValue];
    }
  }

  onSearch() {
    const searchValue = (this.search.value as string).toLowerCase();
    let allCopy = this.allRepos.slice();
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
