import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectPrsComponent } from './project-prs.component';

describe('ProjectPrsComponent', () => {
  let component: ProjectPrsComponent;
  let fixture: ComponentFixture<ProjectPrsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProjectPrsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProjectPrsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
