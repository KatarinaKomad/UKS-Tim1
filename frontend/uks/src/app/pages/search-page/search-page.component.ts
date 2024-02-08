import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Page } from 'src/models/page/page';
import { SEARCH_TYPE, SearchRequest, SearchResult, createEmptySearchRequest } from 'src/models/search/search';
import { ISSUE_PR_SORT_TYPE, REPO_SORT_TYPE, SORT_TYPE, USER_SORT_TYPE } from 'src/models/search/sortType';
import { PaginationService } from 'src/services/pagination/pagination.service';
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

  searchRequest: SearchRequest
  inputControl: FormControl<string> = new FormControl();
  sortTypeControl: FormControl<SORT_TYPE> = new FormControl();

  page: Page<SearchResult> = new Page();

  constructor(
    private searchService: SearchService,
    private paginationService: PaginationService,
  ) {
    this.sortTypes = this.repoSortTypes;
    this.sortTypeControl.setValue(REPO_SORT_TYPE.ANY)

    const searchRequest = localStorage.getItem("searchRequest");
    if (searchRequest != null) {
      this.searchRequest = JSON.parse(searchRequest);
      this.search();
    } else {
      this.searchRequest = createEmptySearchRequest();
    }

  }

  search() {
    const searchRequest = this.createSearchRequest();
    this.sendSearchRequest(searchRequest);
    // this.setRepoCount(searchRequest);
    // this.setIssueCount(searchRequest);
    // this.setUserCount(searchRequest);
    // this.setPrCount(searchRequest);
  }

  private createSearchRequest() {
    return {
      ...this.searchRequest,
      inputValue: this.inputControl.value,
      page: this.page.pageable.pageNumber,
      size: this.page.pageable.pageSize
    };
  }

  sendSearchRequest(searchRequest: SearchRequest) {
    this.searchService.search(searchRequest).subscribe({
      next: (page: Page<SearchResult>) => {
        this.page = page;
        this.page.pageable.pagePerShow = page.number + 1;
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
    if (searchRequest.keywords.length === 0) {
      this.page = new Page();
    }
    this.searchRequest = { ...searchRequest };
    localStorage.setItem("searchRequest", JSON.stringify(this.searchRequest))
  }

  onSortTypeChange(event: Event) {
    this.searchRequest.sortType = (event.target as HTMLInputElement).value as SORT_TYPE;
  }

  public getNextPage(): void {
    this.page.pageable = this.paginationService.getNextPage(this.page);
    this.sendSearchRequest(this.createSearchRequest());
  }

  public getPreviousPage(): void {
    this.page.pageable = this.paginationService.getPreviousPage(this.page);
    this.sendSearchRequest(this.createSearchRequest());
  }

  public getPageInNewSize(pageSize: any): void {
    this.page.pageable = this.paginationService.getPageInNewSize(this.page, pageSize);
    this.sendSearchRequest(this.createSearchRequest());
  }

}

