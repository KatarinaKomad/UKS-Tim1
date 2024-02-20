import { Pageable } from './pageable';

export class Page<T> {
  content: Array<T>;
  pageable: Pageable;
  last: boolean;
  totalPages: number;
  totalElements: number;
  first: boolean;
  numberOfElements: number;
  size: number;
  number: number;

  public constructor() {
    this.pageable = new Pageable();
    this.content = [];
    this.last = false;
    this.totalPages = 0;
    this.totalElements = 0;
    this.first = true;
    this.numberOfElements = 0;
    this.size = 0;
    this.number = 0;
  }

}
