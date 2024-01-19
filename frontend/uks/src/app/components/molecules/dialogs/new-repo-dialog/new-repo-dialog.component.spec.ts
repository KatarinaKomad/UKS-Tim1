import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewRepoDialogComponent } from './new-repo-dialog.component';
import { HttpClientModule } from '@angular/common/http';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';

describe('NewRepoDialogComponent', () => {
  let component: NewRepoDialogComponent;
  let fixture: ComponentFixture<NewRepoDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientModule, MatDialogModule],
      declarations: [NewRepoDialogComponent],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialogRef, useValue: {} }
      ]
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
