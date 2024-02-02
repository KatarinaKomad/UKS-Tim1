import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssigneeFilterComponent } from './assignee-filter.component';
import { HttpClientModule } from '@angular/common/http';

describe('AssigneeFilterComponent', () => {
  let component: AssigneeFilterComponent;
  let fixture: ComponentFixture<AssigneeFilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AssigneeFilterComponent],
      imports: [HttpClientModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AssigneeFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
