import { TestBed } from '@angular/core/testing';

import { MilestoneService } from './milestone.service';
import { HttpClientModule } from '@angular/common/http';

describe('MilestoneService', () => {
  let service: MilestoneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [MilestoneService],
    });
    service = TestBed.inject(MilestoneService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
