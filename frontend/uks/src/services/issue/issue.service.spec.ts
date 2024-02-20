import { TestBed } from '@angular/core/testing';

import { IssueService } from './issue.service';
import { HttpClientModule } from '@angular/common/http';
import { MilestoneService } from '../milestone/milestone.service';

describe('IssueService', () => {
  let service: IssueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [MilestoneService]
    });
    service = TestBed.inject(IssueService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
