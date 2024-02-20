import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewPrFormComponent } from './new-pr-form.component';

describe('NewPrFormComponent', () => {
  let component: NewPrFormComponent;
  let fixture: ComponentFixture<NewPrFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewPrFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NewPrFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
