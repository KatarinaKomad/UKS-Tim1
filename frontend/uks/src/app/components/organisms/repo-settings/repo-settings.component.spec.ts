import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';

import { RepoSettingsComponent } from './repo-settings.component';

describe('MyReposSideListComponent', () => {
  let component: RepoSettingsComponent;
  let fixture: ComponentFixture<RepoSettingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientModule],
      declarations: [RepoSettingsComponent]
    })
      .compileComponents();


    fixture = TestBed.createComponent(RepoSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
