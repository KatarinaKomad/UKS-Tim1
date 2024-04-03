import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CodeOverviewComponent } from './code-overview.component';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('CodeOverviewComponent', () => {
  let component: CodeOverviewComponent;
  let fixture: ComponentFixture<CodeOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CodeOverviewComponent],
      imports: [HttpClientModule, ToastrModule.forRoot(), MatDialogModule],
      providers: [ToastrService,
        { provide: MatDialogRef, useValue: {} },
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

    fixture = TestBed.createComponent(CodeOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
