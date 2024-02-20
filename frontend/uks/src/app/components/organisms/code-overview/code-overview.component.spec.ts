import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CodeOverviewComponent } from './code-overview.component';
import { HttpClientModule } from '@angular/common/http';

describe('CodeOverviewComponent', () => {
  let component: CodeOverviewComponent;
  let fixture: ComponentFixture<CodeOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CodeOverviewComponent],
      imports: [HttpClientModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CodeOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
