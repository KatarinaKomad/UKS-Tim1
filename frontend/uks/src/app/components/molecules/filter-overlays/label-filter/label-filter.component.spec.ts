import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LabelFilterComponent } from './label-filter.component';
import { HttpClientModule } from '@angular/common/http';

describe('LabelFilterComponent', () => {
  let component: LabelFilterComponent;
  let fixture: ComponentFixture<LabelFilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LabelFilterComponent],
      imports: [HttpClientModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(LabelFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
