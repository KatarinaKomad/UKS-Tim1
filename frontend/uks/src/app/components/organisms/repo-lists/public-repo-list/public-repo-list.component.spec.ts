import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicRepoListComponent } from './public-repo-list.component';

describe('PublicRepoListComponent', () => {
  let component: PublicRepoListComponent;
  let fixture: ComponentFixture<PublicRepoListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PublicRepoListComponent]
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
