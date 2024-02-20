import { Injectable } from '@angular/core';
import { Page } from 'src/models/page/page';
import { Pageable } from 'src/models/page/pageable';

@Injectable({
  providedIn: 'root'
})
export class PaginationService {

  constructor() { }

  public getNextPage(page: Page<any>): Pageable {
    if (!page.last) {
      page.pageable.pageNumber = page.pageable.pageNumber + 1;
    }
    return page.pageable;
  }

  public getPreviousPage(page: Page<any>): Pageable {
    if (!page.first) {
      page.pageable.pageNumber = page.pageable.pageNumber - 1;
    }
    return page.pageable;
  }

  public getFirstPage(page: Page<any>): Pageable {
    page.pageable.pageNumber = 0;
    return page.pageable;
  }

  public getLastPage(page: Page<any>): Pageable {
    page.pageable.pageNumber = page.totalPages - 1;
    return page.pageable;
  }

  public getPageInNewSize(page: Page<any>, pageSize: number): Pageable {
    page.pageable.pageSize = pageSize;
    page.pageable.pageNumber = Pageable.FIRST_PAGE_NUMBER;

    return page.pageable;
  }
}
