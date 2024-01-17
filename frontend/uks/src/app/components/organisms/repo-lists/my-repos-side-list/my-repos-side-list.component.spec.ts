import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';

import { MyReposSideListComponent } from './my-repos-side-list.component';

describe('MyReposSideListComponent', () => {
  let component: MyReposSideListComponent;
  let fixture: ComponentFixture<MyReposSideListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MyReposSideListComponent],
      imports: [HttpClientModule],
    }).compileComponents();

    fixture = TestBed.createComponent(MyReposSideListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
