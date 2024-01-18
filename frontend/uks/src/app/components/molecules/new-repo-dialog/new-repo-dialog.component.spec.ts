import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewRepoDialogComponent } from './new-repo-dialog.component';

describe('NewRepoDialogComponent', () => {
  let component: NewRepoDialogComponent;
  let fixture: ComponentFixture<NewRepoDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewRepoDialogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NewRepoDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
