import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForkOptionsComponent } from './fork-options.component';
import { HttpClientModule } from '@angular/common/http';

describe('ForkOptionsComponent', () => {
  let component: ForkOptionsComponent;
  let fixture: ComponentFixture<ForkOptionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ForkOptionsComponent],
      imports: [HttpClientModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ForkOptionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
