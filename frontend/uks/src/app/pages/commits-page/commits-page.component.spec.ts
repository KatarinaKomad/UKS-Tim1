import { ComponentFixture, TestBed } from '@angular/core/testing';

<<<<<<<< HEAD:frontend/uks/src/app/pages/commits-page/commits-page.component.spec.ts
import { CommitsPageComponent } from './commits-page.component';
========
import { NewIssueComponent } from './new-issue.component';
>>>>>>>> main:frontend/uks/src/app/components/organisms/new-issue/new-issue.component.spec.ts
import { HttpClientModule } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

<<<<<<<< HEAD:frontend/uks/src/app/pages/commits-page/commits-page.component.spec.ts
describe('CommitsPageComponent', () => {
  let component: CommitsPageComponent;
  let fixture: ComponentFixture<CommitsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CommitsPageComponent],
========
describe('NewIssueComponent', () => {
  let component: NewIssueComponent;
  let fixture: ComponentFixture<NewIssueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NewIssueComponent],
>>>>>>>> main:frontend/uks/src/app/components/organisms/new-issue/new-issue.component.spec.ts
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

<<<<<<<< HEAD:frontend/uks/src/app/pages/commits-page/commits-page.component.spec.ts
    fixture = TestBed.createComponent(CommitsPageComponent);
========
    fixture = TestBed.createComponent(NewIssueComponent);
>>>>>>>> main:frontend/uks/src/app/components/organisms/new-issue/new-issue.component.spec.ts
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
