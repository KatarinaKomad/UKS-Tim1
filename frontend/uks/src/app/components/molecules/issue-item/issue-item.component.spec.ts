import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IssueItemComponent } from './issue-item.component';
import { HttpClientModule } from '@angular/common/http';
import { MatDialogModule } from '@angular/material/dialog';
import { ToastrModule, ToastrService } from 'ngx-toastr';

describe('IssueItemComponent', () => {
  let component: IssueItemComponent;
  let fixture: ComponentFixture<IssueItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [IssueItemComponent],
      imports: [MatDialogModule, HttpClientModule, ToastrModule.forRoot()],
      providers: [ToastrService],
    })
      .compileComponents();

    fixture = TestBed.createComponent(IssueItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
