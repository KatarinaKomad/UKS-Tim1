import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BranchButtonGroupComponent } from './branch-button-group.component';
import { HttpClientModule } from '@angular/common/http';

describe('BranchButtonGroupComponent', () => {
  let component: BranchButtonGroupComponent;
  let fixture: ComponentFixture<BranchButtonGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BranchButtonGroupComponent],
      imports: [HttpClientModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BranchButtonGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
