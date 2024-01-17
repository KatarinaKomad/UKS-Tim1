import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';

import { RepoItemComponent } from './repo-item.component';

describe('RepoItemComponent', () => {
  let component: RepoItemComponent;
  let fixture: ComponentFixture<RepoItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RepoItemComponent],
      imports: [HttpClientModule],
    }).compileComponents();

    fixture = TestBed.createComponent(RepoItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
