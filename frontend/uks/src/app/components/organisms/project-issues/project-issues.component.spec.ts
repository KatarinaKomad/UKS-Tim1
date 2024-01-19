import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectIssuesComponent } from './project-issues.component';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('ProjectIssuesComponent', () => {
  let component: ProjectIssuesComponent;
  let fixture: ComponentFixture<ProjectIssuesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProjectIssuesComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: () => 'exampleParamValue',
              },
            },
            queryParams: of({}),
          },
        },
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(ProjectIssuesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
