import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BranchesOverviewComponent } from './branches-overview.component';
import { MatDialogModule } from '@angular/material/dialog';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule, ToastrService } from 'ngx-toastr';

describe('BranchesOverviewComponent', () => {
  let component: BranchesOverviewComponent;
  let fixture: ComponentFixture<BranchesOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BranchesOverviewComponent],
      imports: [MatDialogModule, HttpClientModule, ToastrModule.forRoot()],
      providers: [ToastrService],
    })
      .compileComponents();

    fixture = TestBed.createComponent(BranchesOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
