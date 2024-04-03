import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BranchFilterOverlayComponent } from './branch-filter-overlay.component';
import { HttpClientModule } from '@angular/common/http';

describe('BranchFilterOverlayComponent', () => {
  let component: BranchFilterOverlayComponent;
  let fixture: ComponentFixture<BranchFilterOverlayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BranchFilterOverlayComponent],
      imports: [HttpClientModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BranchFilterOverlayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
