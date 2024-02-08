import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Page } from 'src/models/page/page';

@Component({
  selector: 'app-custom-pagination',
  templateUrl: './custom-pagination.component.html',
  styleUrl: './custom-pagination.component.scss'
})
export class CustomPaginationComponent {

  @Input() page: Page<any> = new Page();
  @Output() firstPageEvent = new EventEmitter();
  @Output() lastPageEvent = new EventEmitter();
  @Output() nextPageEvent = new EventEmitter();
  @Output() previousPageEvent = new EventEmitter();
  @Output() pageSizeEvent: EventEmitter<number> = new EventEmitter<number>();

  pageSizeStr = 10;

  constructor() { }

  ngOnInit(): void {
    this.pageSizeStr = this.page.pageable.pageSize;
  }

  firstPage(): void {
    this.firstPageEvent.emit(null);
  }
  lastPage(): void {
    this.lastPageEvent.emit(null);
  }

  nextPage(): void {
    this.nextPageEvent.emit(null);
  }

  previousPage(): void {
    this.previousPageEvent.emit(null);
  }

  updatePageSize(): void {
    const pageSize: number = +this.pageSizeStr;
    this.pageSizeEvent.emit(pageSize);
    console.log(this.page);
  }
}
