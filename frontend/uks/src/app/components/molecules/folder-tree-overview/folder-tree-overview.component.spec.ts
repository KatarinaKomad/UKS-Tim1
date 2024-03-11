import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FolderTreeOverviewComponent } from './folder-tree-overview.component';

describe('FolderTreeOverviewComponent', () => {
  let component: FolderTreeOverviewComponent;
  let fixture: ComponentFixture<FolderTreeOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FolderTreeOverviewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FolderTreeOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
