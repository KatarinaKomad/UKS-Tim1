import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyReposSideListComponent } from './my-repos-side-list.component';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { HttpClientModule } from '@angular/common/http';

describe('MyReposSideListComponent', () => {
  let component: MyReposSideListComponent;
  let fixture: ComponentFixture<MyReposSideListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MatDialogModule, HttpClientModule],
      declarations: [MyReposSideListComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MyReposSideListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
