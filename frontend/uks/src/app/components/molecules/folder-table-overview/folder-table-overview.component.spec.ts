import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FolderTableOverviewComponent } from './folder-table-overview.component';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { HttpClientModule } from '@angular/common/http';

describe('FolderTableOverviewComponent', () => {
  let component: FolderTableOverviewComponent;
  let fixture: ComponentFixture<FolderTableOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FolderTableOverviewComponent],
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

    fixture = TestBed.createComponent(FolderTableOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
