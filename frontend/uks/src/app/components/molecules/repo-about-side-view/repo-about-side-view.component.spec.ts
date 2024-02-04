import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepoAboutSideViewComponent } from './repo-about-side-view.component';
import { HttpClientModule } from '@angular/common/http';

describe('RepoAboutSideViewComponent', () => {
  let component: RepoAboutSideViewComponent;
  let fixture: ComponentFixture<RepoAboutSideViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RepoAboutSideViewComponent],
      imports: [HttpClientModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RepoAboutSideViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
