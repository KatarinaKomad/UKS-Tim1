import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MilestoneFilterComponent } from './milestone-filter.component';
import { HttpClientModule } from '@angular/common/http';

describe('MilestoneFilterComponent', () => {
  let component: MilestoneFilterComponent;
  let fixture: ComponentFixture<MilestoneFilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MilestoneFilterComponent],
      imports: [HttpClientModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MilestoneFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
