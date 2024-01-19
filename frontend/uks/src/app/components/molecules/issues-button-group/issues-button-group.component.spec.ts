import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IssuesButtonGroupComponent } from './issues-button-group.component';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('IssuesButtonGroupComponent', () => {
  let component: IssuesButtonGroupComponent;
  let fixture: ComponentFixture<IssuesButtonGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [IssuesButtonGroupComponent],
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

    fixture = TestBed.createComponent(IssuesButtonGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
