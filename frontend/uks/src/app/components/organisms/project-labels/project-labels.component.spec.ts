import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectLabelsComponent } from './project-labels.component';
import { MatDialogModule } from '@angular/material/dialog';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule, ToastrService } from 'ngx-toastr';

describe('ProjectLabelsComponent', () => {
  let component: ProjectLabelsComponent;
  let fixture: ComponentFixture<ProjectLabelsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProjectLabelsComponent],
      imports: [MatDialogModule, HttpClientModule, ToastrModule.forRoot()],
      providers: [ToastrService],
    })
      .compileComponents();

    fixture = TestBed.createComponent(ProjectLabelsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
