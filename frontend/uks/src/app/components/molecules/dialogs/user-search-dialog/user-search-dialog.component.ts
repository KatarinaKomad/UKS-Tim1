import { Component, Inject } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Page } from 'src/models/page/page';
import { USER_KEYWORDS } from 'src/models/search/keywords';
import { OPERATIONS, SEARCH_TYPE, SearchRequest, SearchResult, createEmptySearchRequest } from 'src/models/search/search';
import { USER_SORT_TYPE } from 'src/models/search/sortType';
import { RepoMemberDTO } from 'src/models/user/member';
import { UserBasicInfo } from 'src/models/user/user';
import { PaginationService } from 'src/services/pagination/pagination.service';
import { SearchService } from 'src/services/search/search.service';

@Component({
  selector: 'app-user-search-dialog',
  templateUrl: './user-search-dialog.component.html',
  styleUrl: './user-search-dialog.component.scss'
})
export class UserSearchDialogComponent {

  page: Page<UserBasicInfo> = new Page();
  inputControl: FormControl<string> = new FormControl();
  searchRequest: SearchRequest = createEmptySearchRequest();
  selectedUser?: UserBasicInfo;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<UserSearchDialogComponent>,
    private paginationService: PaginationService,
    private searchService: SearchService,
  ) {
    this.searchRequest.searchType = SEARCH_TYPE.USER;
    this.searchRequest.sortType = USER_SORT_TYPE.ANY;
    this.searchRequest.keywords = [USER_KEYWORDS.IN_NAME, OPERATIONS.OR, USER_KEYWORDS.IN_EMAIL]
    this.searchRequest.query = [USER_KEYWORDS.IN_NAME, OPERATIONS.OR, USER_KEYWORDS.IN_EMAIL]
  }

  onSubmitClick() {
    this.dialogRef.close(this.selectedUser?.id);
  }
  onCancelClick() {
    this.dialogRef.close(null);
  }


  onCheckboxChange(event: any, selected: UserBasicInfo): void {
    this.selectedUser = selected;
  }

  public getNextPage(): void {
    this.page.pageable = this.paginationService.getNextPage(this.page);
    this.search();
  }

  public getPreviousPage(): void {
    this.page.pageable = this.paginationService.getPreviousPage(this.page);
    this.search();
  }

  public getPageInNewSize(pageSize: any): void {
    this.page.pageable = this.paginationService.getPageInNewSize(this.page, pageSize);
    this.search();
  }

  search() {
    this.searchRequest.inputValue = this.inputControl.value;
    this.searchRequest.page = this.page.pageable.pageNumber;
    this.searchRequest.size = this.page.pageable.pageSize;

    this.sendSearchRequest()
  }

  private sendSearchRequest() {
    this.searchService.search(this.searchRequest).subscribe({
      next: (page: Page<SearchResult>) => {
        if (page != null) {
          this.page = page as Page<UserBasicInfo>;
          this.page.pageable.pagePerShow = page.number + 1;
          // remove already existing members
          this.page.content = this.page.content.filter(u => {
            return !this.data.members.some((member: RepoMemberDTO) => member.id === u.id)
          })
        }
      }, error: (e: any) => {
        console.log(e);
      }
    })
  }
}
