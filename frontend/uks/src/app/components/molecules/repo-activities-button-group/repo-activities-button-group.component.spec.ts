import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepoActivitiesButtonGroupComponent } from './repo-activities-button-group.component';

describe('RepoActivitiesButtonGroupComponent', () => {
  let component: RepoActivitiesButtonGroupComponent;
  let fixture: ComponentFixture<RepoActivitiesButtonGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RepoActivitiesButtonGroupComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RepoActivitiesButtonGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
