import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommitDiffPageComponent } from './commit-diff-page.component';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { HttpClientModule } from '@angular/common/http';

describe('CommitDiffPageComponent', () => {
  let component: CommitDiffPageComponent;
  let fixture: ComponentFixture<CommitDiffPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CommitDiffPageComponent],
      imports: [HttpClientModule],
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

    fixture = TestBed.createComponent(CommitDiffPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
