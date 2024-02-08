export class Pageable {

  // tslint:disable-next-line: member-ordering
  static readonly DEFAULT_PAGE_SIZE = 10;
  static readonly FIRST_PAGE_NUMBER = 0;
  pageSize: number;
  pageNumber: number;
  offset: number;
  unpaged: boolean;
  paged: boolean;
  pagePerShow: number;

  public constructor() {
    this.pageSize = Pageable.DEFAULT_PAGE_SIZE;
    this.pageNumber = Pageable.FIRST_PAGE_NUMBER;
    this.offset = 0;
    this.unpaged = false;
    this.paged = false;
    this.pagePerShow = 0;
  }

}
