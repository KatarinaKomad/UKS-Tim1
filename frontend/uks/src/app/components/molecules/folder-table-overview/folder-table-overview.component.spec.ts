import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FolderTableOverviewComponent } from './folder-table-overview.component';

describe('FolderTableOverviewComponent', () => {
  let component: FolderTableOverviewComponent;
  let fixture: ComponentFixture<FolderTableOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FolderTableOverviewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FolderTableOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
