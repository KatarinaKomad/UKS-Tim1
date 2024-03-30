import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemAddCommentsComponent } from './item-add-comments.component';

describe('ItemAddCommentsComponent', () => {
  let component: ItemAddCommentsComponent;
  let fixture: ComponentFixture<ItemAddCommentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ItemAddCommentsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ItemAddCommentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
