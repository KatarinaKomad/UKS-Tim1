import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IssuesButtonGroupComponent } from './issues-button-group.component';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { HttpClientModule } from '@angular/common/http';
import { NavigationService } from 'src/services/navigation/navigation.service';

describe('IssuesButtonGroupComponent', () => {
  let component: IssuesButtonGroupComponent;
  let fixture: ComponentFixture<IssuesButtonGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [IssuesButtonGroupComponent],
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

    fixture = TestBed.createComponent(IssuesButtonGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
