import { TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';

import { BranchService } from './branch.service';

describe('BranchService', () => {
  let service: BranchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [BranchService],
    });
    service = TestBed.inject(BranchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
