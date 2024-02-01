import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IssuePropertiesSideViewComponent } from './issue-properties-side-view.component';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule, ToastrService } from 'ngx-toastr';

describe('IssuePropertiesSideViewComponent', () => {
  let component: IssuePropertiesSideViewComponent;
  let fixture: ComponentFixture<IssuePropertiesSideViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [IssuePropertiesSideViewComponent],
      imports: [HttpClientModule, ToastrModule.forRoot()],
      providers: [ToastrService],
    })
      .compileComponents();

    fixture = TestBed.createComponent(IssuePropertiesSideViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
