import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsersOverviewPageComponent } from './users-overview-page.component';
import { HttpClientModule } from '@angular/common/http';

describe('UsersOverviewPageComponent', () => {
  let component: UsersOverviewPageComponent;
  let fixture: ComponentFixture<UsersOverviewPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UsersOverviewPageComponent],
      imports: [HttpClientModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(UsersOverviewPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
