import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorFilterComponent } from './author-filter.component';
import { HttpClientModule } from '@angular/common/http';

describe('AuthorFilterComponent', () => {
  let component: AuthorFilterComponent;
  let fixture: ComponentFixture<AuthorFilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AuthorFilterComponent],
      imports: [HttpClientModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AuthorFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
