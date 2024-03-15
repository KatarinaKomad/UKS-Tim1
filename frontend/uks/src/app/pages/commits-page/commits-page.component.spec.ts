import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommitsPageComponent } from './commits-page.component';
import { HttpClientModule } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('CommitsPageComponent', () => {
  let component: CommitsPageComponent;
  let fixture: ComponentFixture<CommitsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CommitsPageComponent],
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

    fixture = TestBed.createComponent(CommitsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
