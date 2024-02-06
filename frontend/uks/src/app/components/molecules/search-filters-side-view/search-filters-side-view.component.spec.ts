import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchFiltersSideViewComponent } from './search-filters-side-view.component';

describe('SearchFiltersSideViewComponent', () => {
  let component: SearchFiltersSideViewComponent;
  let fixture: ComponentFixture<SearchFiltersSideViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchFiltersSideViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SearchFiltersSideViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
