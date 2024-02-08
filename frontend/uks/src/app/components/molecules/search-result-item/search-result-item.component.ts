import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';
import { SEARCH_TYPE, SearchRequest, SearchResult } from 'src/models/search/search';
import { NavigationService } from 'src/services/navigation/navigation.service';

@Component({
  selector: 'app-search-result-item',
  templateUrl: './search-result-item.component.html',
  styleUrl: './search-result-item.component.scss'
})
export class SearchResultItemComponent implements OnChanges {

  @Input() item?: SearchResult
  repoItem?: RepoBasicInfoDTO;
  searchRequest?: SearchRequest;

  constructor(
    private navigationService: NavigationService
  ) {
    const searchRequest = localStorage.getItem("searchRequest");
    if (searchRequest != null) {
      this.searchRequest = JSON.parse(searchRequest);
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['item'].currentValue) {
      if (this.searchRequest?.searchType === SEARCH_TYPE.REPO) {
        this.repoItem = changes['item'].currentValue as RepoBasicInfoDTO;
      }
    }
  }

  navigateToRepo() {
    if (this.repoItem) {
      this.navigationService.navigateToRepo(this.repoItem);
    }
  }
}
