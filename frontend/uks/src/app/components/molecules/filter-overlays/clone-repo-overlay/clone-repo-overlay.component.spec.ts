import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CloneRepoOverlayComponent } from './clone-repo-overlay.component';
import { HttpClientModule } from '@angular/common/http';

describe('CloneRepoOverlayComponent', () => {
  let component: CloneRepoOverlayComponent;
  let fixture: ComponentFixture<CloneRepoOverlayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CloneRepoOverlayComponent],
      imports: [HttpClientModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CloneRepoOverlayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
