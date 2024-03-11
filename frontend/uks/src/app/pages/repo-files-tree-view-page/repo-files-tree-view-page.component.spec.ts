import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RepoFilesTreeViewPageComponent } from './repo-files-tree-view-page.component';

describe('RepoFilesTreeViewPageComponent', () => {
  let component: RepoFilesTreeViewPageComponent;
  let fixture: ComponentFixture<RepoFilesTreeViewPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RepoFilesTreeViewPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RepoFilesTreeViewPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
