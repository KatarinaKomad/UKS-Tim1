import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectMilestonesComponent } from './project-milestones.component';
import { MatDialogModule } from '@angular/material/dialog';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule, ToastrService } from 'ngx-toastr';

describe('ProjectMilestonesComponent', () => {
  let component: ProjectMilestonesComponent;
  let fixture: ComponentFixture<ProjectMilestonesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MatDialogModule, HttpClientModule, ToastrModule.forRoot()],
      providers: [ToastrService],
      declarations: [ProjectMilestonesComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ProjectMilestonesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
