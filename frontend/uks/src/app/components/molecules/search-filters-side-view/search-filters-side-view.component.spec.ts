import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchFiltersSideViewComponent } from './search-filters-side-view.component';
import { HttpClientModule } from '@angular/common/http';
import { MatDialogModule } from '@angular/material/dialog';

describe('SearchFiltersSideViewComponent', () => {
  let component: SearchFiltersSideViewComponent;
  let fixture: ComponentFixture<SearchFiltersSideViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SearchFiltersSideViewComponent],
      imports: [MatDialogModule]
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
