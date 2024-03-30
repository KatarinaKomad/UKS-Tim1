import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemCommentsViewComponent } from './item-comments-view.component';

describe('ItemCommentsViewComponent', () => {
  let component: ItemCommentsViewComponent;
  let fixture: ComponentFixture<ItemCommentsViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ItemCommentsViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ItemCommentsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
