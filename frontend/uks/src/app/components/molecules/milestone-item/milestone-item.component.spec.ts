import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MilestoneItemComponent } from './milestone-item.component';
import { HttpClientModule } from '@angular/common/http';
import { MatDialogModule } from '@angular/material/dialog';
import { ToastrModule, ToastrService } from 'ngx-toastr';

describe('MilestoneItemComponent', () => {
  let component: MilestoneItemComponent;
  let fixture: ComponentFixture<MilestoneItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MilestoneItemComponent],
      imports: [MatDialogModule, HttpClientModule, ToastrModule.forRoot()],
      providers: [ToastrService],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MilestoneItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
