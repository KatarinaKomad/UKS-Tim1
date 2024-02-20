import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IssueOverviewComponent } from './issue-overview.component';
import { HttpClientModule } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('IssueOverviewComponent', () => {
  let component: IssueOverviewComponent;
  let fixture: ComponentFixture<IssueOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [IssueOverviewComponent],
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
            url: of([{ path: 'example' }]), // Mock the url observable
          },
        },
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(IssueOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
