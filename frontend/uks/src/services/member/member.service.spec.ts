import { TestBed } from '@angular/core/testing';

import { MemberService } from './member.service';
import { HttpClientModule } from '@angular/common/http';

describe('MemberService', () => {
  let service: MemberService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [MemberService],
    });
    service = TestBed.inject(MemberService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
