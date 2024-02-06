import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { SearchResult } from 'src/models/search/search';

@Component({
  selector: 'app-search-result-item',
  templateUrl: './search-result-item.component.html',
  styleUrl: './search-result-item.component.scss'
})
export class SearchResultItemComponent implements OnChanges {

  @Input() item?: SearchResult

  constructor() { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['item'].currentValue) {
      this.item = changes['item'].currentValue;
    }
  }
}
