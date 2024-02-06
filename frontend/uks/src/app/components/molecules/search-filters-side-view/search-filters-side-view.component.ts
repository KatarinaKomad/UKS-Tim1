import { Component, EventEmitter, Inject, Input, Output } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { SEARCH_TYPE, SearchRequest, createEmptySearchRequest, Keyword, inputRequired, OPERATIONS, isOnlyOneAllowed } from 'src/models/search/search';
import { SearchFilterInputComponent } from '../filter-overlays/search-filter-input/search-filter-input.component';
import { ISSUE_PR_KEYWORDS, REPO_KEYWORDS, USER_KEYWORDS, formatIssuePrKeyword, formatRepoKeyword, formatUserKeyword } from 'src/models/search/keywords';


@Component({
  selector: 'app-search-filters-side-view',
  templateUrl: './search-filters-side-view.component.html',
  styleUrl: './search-filters-side-view.component.scss'
})
export class SearchFiltersSideViewComponent {

  @Output() queryChangeEvent: EventEmitter<SearchRequest> = new EventEmitter<SearchRequest>();

  SEARCH_TYPE = SEARCH_TYPE;
  OPERATIONS = OPERATIONS;

  @Input() repoCount: number = 0;
  @Input() issueCount: number = 0;
  @Input() prCount: number = 0;
  @Input() userCount: number = 0;

  repoKeywords = Object.values(REPO_KEYWORDS);
  issuePrKeywords = Object.values(ISSUE_PR_KEYWORDS);
  userKeywords = Object.values(USER_KEYWORDS);
  formatRepoKeyword = formatRepoKeyword;
  formatIssuePrKeyword = formatIssuePrKeyword;
  formatUserKeyword = formatUserKeyword;

  request: SearchRequest = createEmptySearchRequest();

  dialogConfig = {
    width: '250px',
    height: '150px',
    position: {
      top: '50%',
      left: '40%'
    },
    data: {}
  }
  constructor(
    private dialog: MatDialog) {
  }

  changeSearchType(type: SEARCH_TYPE) {
    this.request.searchType = type;
    this.clearAllAdvanced();
    this.queryChangeEvent.emit(this.request)
  }


  advancedOption(keyword: Keyword) {
    if (this.isSelected(keyword)) {
      this.removeKeyword(keyword);
      return;
    }
    if (!inputRequired(keyword)) {
      this.addKeyword(keyword, keyword)
      return;
    }
    // input required
    let dialogRef = null;
    if (keyword.includes('created')) {
      dialogRef = this.openDateInput(keyword);
    } else if (keyword.includes('number')) {
      dialogRef = this.openNumberInput(keyword);
    } else {
      dialogRef = this.openStringInput(keyword);
    }
    if (dialogRef) {
      dialogRef.afterClosed().subscribe((result: string | null) => {
        if (result) {
          this.addKeyword(keyword, keyword + result);
        }
      });
    }

  }
  removeKeyword(keyword: string) {
    if (this.request.keywords.length < 2) {
      this.clearAllAdvanced();
      return;
    }
    const index = this.request.keywords.indexOf(keyword);

    if (index === 0) {
      // remove first keyword + operand
      this.request.keywords.splice(0, 2);
      this.request.query.splice(0, 2);
    }
    if (index > 0) {
      // remove that keyword and its operand
      this.request.keywords.splice(index - 1, 2);
      this.request.query.splice(index - 1, 2);
    }
    this.queryChangeEvent.emit(this.request);
  }

  removeLastAdvanced() {
    if (this.request.keywords.length >= 2) {
      const removeCount = this.isLastElemOperation() ? 1 : 2;
      this.request.keywords.splice(-removeCount); // Remove both 'AND' and keyword
      this.request.query.splice(-removeCount);
      this.queryChangeEvent.emit(this.request);
    }
    else {
      this.clearAllAdvanced();
    }
  }

  private addKeyword(keyword: Keyword, query: string) {
    if (!this.isLastElemOperation()) {
      (this.request.keywords as any).push(OPERATIONS.AND);
      this.request.query.push(OPERATIONS.AND);
    }
    (this.request.keywords as any).push(keyword);
    this.request.query.push(query);
    this.queryChangeEvent.emit(this.request);
  }

  clearAllAdvanced() {
    this.request.keywords = [];
    this.request.query = [];
    this.queryChangeEvent.emit(this.request)
  }

  private openDateInput(keyword: Keyword): MatDialogRef<SearchFilterInputComponent> {
    this.dialogConfig.data = { type: 'date', keyword }
    return this.dialog.open(SearchFilterInputComponent, this.dialogConfig);
  }

  private openNumberInput(keyword: Keyword): MatDialogRef<SearchFilterInputComponent> {
    this.dialogConfig.data = { type: 'number', keyword }
    return this.dialog.open(SearchFilterInputComponent, this.dialogConfig);
  }

  private openStringInput(keyword: Keyword): MatDialogRef<SearchFilterInputComponent> {
    this.dialogConfig.data = { type: 'string', keyword }
    return this.dialog.open(SearchFilterInputComponent, this.dialogConfig);
  }

  addOperation(operation: OPERATIONS) {
    (this.request.keywords as any).push(operation);
    this.request.query.push(operation);
    this.queryChangeEvent.emit(this.request);
  }

  isLastElemOperation() {
    const query = this.request.keywords as any[];
    if (query && query.length > 0) {
      return this.isOperation(query[query.length - 1]);
    }
    return true;
  }

  isOperation(keyword: Keyword) {
    return Object.values(OPERATIONS).includes(keyword as OPERATIONS)
  }

  isSelected(keyword: Keyword) {
    return isOnlyOneAllowed(keyword) && (this.request.keywords as any).includes(keyword)
  }
}


