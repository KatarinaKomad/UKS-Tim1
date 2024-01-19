import { TestBed } from '@angular/core/testing';

import { LabelService } from './label.service';
import { HttpClientModule } from '@angular/common/http';

describe('LabelService', () => {
  let service: LabelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [LabelService],
    });
    service = TestBed.inject(LabelService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
