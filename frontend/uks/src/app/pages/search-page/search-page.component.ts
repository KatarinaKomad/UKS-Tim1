import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { SEARCH_TYPE, SearchRequest, SearchResult, createEmptySearchRequest } from 'src/models/search/search';
import { ISSUE_PR_SORT_TYPE, REPO_SORT_TYPE, SORT_TYPE, USER_SORT_TYPE } from 'src/models/search/sortType';
import { SearchService } from 'src/services/search/search.service';

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrl: './search-page.component.scss'
})
export class SearchPageComponent {

  repoSortTypes = Object.values(REPO_SORT_TYPE);
  issuePrSortTypes = Object.values(ISSUE_PR_SORT_TYPE);
  userSortTypes = Object.values(USER_SORT_TYPE);

  sortTypes: SORT_TYPE[] = [];

  SEARCH_TYPE = SEARCH_TYPE;
  searchResults: SearchResult[] = [];

  repoCount: number = 0;
  issueCount: number = 0;
  prCount: number = 0;
  userCount: number = 0;

  searchRequest: SearchRequest = createEmptySearchRequest();
  inputControl: FormControl<string> = new FormControl();
  sortTypeControl: FormControl<SORT_TYPE> = new FormControl();

  constructor(
    private searchService: SearchService,
  ) {
    this.sortTypes = this.repoSortTypes;
    this.sortTypeControl.setValue(REPO_SORT_TYPE.ANY)
  }

  search() {
    const searchRequest = { ...this.searchRequest, inputValue: this.inputControl.value };
    console.log(searchRequest)
    // this.sendSearchRequest(searchRequest);
    // this.setRepoCount(searchRequest);
    // this.setIssueCount(searchRequest);
    // this.setUserCount(searchRequest);
    // this.setPrCount(searchRequest);
  }

  sendSearchRequest(searchRequest: SearchRequest) {
    this.searchService.search(searchRequest).subscribe({
      next: (res: SearchResult[]) => {
        this.searchResults = res;
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  setRepoCount(searchRequest: SearchRequest) {
    this.searchService.getRepoCount(searchRequest).subscribe({
      next: (res: number) => {
        this.repoCount = res
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }
  setIssueCount(searchRequest: SearchRequest) {
    this.searchService.getIssueCount(searchRequest).subscribe({
      next: (res: number) => {
        this.issueCount = res
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }
  setUserCount(searchRequest: SearchRequest) {
    this.searchService.getUserCount(searchRequest).subscribe({
      next: (res: number) => {
        this.userCount = res
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }
  setPrCount(searchRequest: SearchRequest) {
    this.searchService.getPrCount(searchRequest).subscribe({
      next: (res: number) => {
        this.prCount = res
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }

  queryChangeEvent(searchRequest: SearchRequest) {
    if (this.searchRequest.searchType !== searchRequest.searchType) {
      switch (searchRequest.searchType) {
        case SEARCH_TYPE.REPO: this.sortTypes = this.repoSortTypes; break;
        case SEARCH_TYPE.USER: this.sortTypes = this.userSortTypes; break;
        case SEARCH_TYPE.ISSUE: this.sortTypes = this.issuePrSortTypes; break;
        case SEARCH_TYPE.PR: this.sortTypes = this.issuePrSortTypes; break;
        default: this.sortTypes = this.repoSortTypes; break;
      }
    }
    this.searchRequest = { ...searchRequest };
  }

  onSortTypeChange(event: Event) {
    this.searchRequest.sortType = (event.target as HTMLInputElement).value as SORT_TYPE;
  }
}

