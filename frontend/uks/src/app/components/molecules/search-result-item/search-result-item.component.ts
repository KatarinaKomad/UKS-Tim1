import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { IssueDTO } from 'src/models/issue/issue';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';
import { SEARCH_TYPE, SearchRequest, SearchResult } from 'src/models/search/search';
import { UserBasicInfo } from 'src/models/user/user';
import { NavigationService } from 'src/services/navigation/navigation.service';

@Component({
  selector: 'app-search-result-item',
  templateUrl: './search-result-item.component.html',
  styleUrl: './search-result-item.component.scss'
})
export class SearchResultItemComponent implements OnChanges {

  @Input() item?: SearchResult
  repoItem?: RepoBasicInfoDTO;
  userItem?: UserBasicInfo;
  issueItem?: IssueDTO;
  prItem?: any;
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
        this.userItem = undefined;
        this.issueItem = undefined;
        this.prItem = undefined;
      } else if (this.searchRequest?.searchType === SEARCH_TYPE.USER) {
        this.repoItem = undefined;
        this.userItem = changes['item'].currentValue as UserBasicInfo;
        this.issueItem = undefined;
        this.prItem = undefined;
      } else if (this.searchRequest?.searchType === SEARCH_TYPE.ISSUE) {
        this.repoItem = undefined;
        this.userItem = undefined;
        this.issueItem = changes['item'].currentValue as IssueDTO;
        this.prItem = undefined;
      }
      // else if (this.searchRequest?.searchType === SEARCH_TYPE.PR) {
      // this.repoItem = undefined;
      // this.userItem = undefined;
      // this.issueItem = undefined;
      // this.prItem = changes['item'].currentValue as PullRequestDTO;
      // }
    }
  }

  navigateToItem() {
    if (this.repoItem) {
      this.navigationService.navigateToRepo(this.repoItem);
    } else if (this.userItem) {
      this.navigationService.navigateToUser(this.userItem.id);
    } else if (this.issueItem) {
      this.navigationService.navigateToIssueOverview(this.issueItem);
    } else if (this.prItem) {
      // this.navigationService.navigateToPrOverview(this.prItem);
    }
  }
}
