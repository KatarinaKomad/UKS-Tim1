import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForksOverviewPageComponent } from './forks-overview-page.component';
import { HttpClientModule } from '@angular/common/http';

describe('ForksOverviewPageComponent', () => {
  let component: ForksOverviewPageComponent;
  let fixture: ComponentFixture<ForksOverviewPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ForksOverviewPageComponent],
      imports: [HttpClientModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ForksOverviewPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
