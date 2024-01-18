import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomePageComponent } from './home-page.component';
import { PublicRepoListComponent } from 'src/app/components/organisms/repo-lists/public-repo-list/public-repo-list.component';
import { MyReposSideListComponent } from 'src/app/components/organisms/repo-lists/my-repos-side-list/my-repos-side-list.component';
import { RepoItemComponent } from 'src/app/components/molecules/repo-item/repo-item.component';

describe('HomePageComponent', () => {
  let component: HomePageComponent;
  let fixture: ComponentFixture<HomePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HomePageComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(HomePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
