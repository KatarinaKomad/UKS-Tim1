import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrPropertiesSideViewComponent } from './pr-properties-side-view.component';

describe('PrPropertiesSideViewComponent', () => {
  let component: PrPropertiesSideViewComponent;
  let fixture: ComponentFixture<PrPropertiesSideViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PrPropertiesSideViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PrPropertiesSideViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
