import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicRepoListComponent } from './public-repo-list.component';
import { HttpClientModule } from '@angular/common/http';

describe('PublicRepoListComponent', () => {
  let component: PublicRepoListComponent;
  let fixture: ComponentFixture<PublicRepoListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientModule],
      declarations: [PublicRepoListComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(PublicRepoListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
