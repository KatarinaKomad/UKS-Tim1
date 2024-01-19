import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewLabelDialogComponent } from './new-label-dialog.component';

describe('NewLabelDialogComponent', () => {
  let component: NewLabelDialogComponent;
  let fixture: ComponentFixture<NewLabelDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewLabelDialogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NewLabelDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
