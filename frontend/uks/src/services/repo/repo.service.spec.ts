import { TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';

import { RepoService } from './repo.service';

describe('RepoService', () => {
  let service: RepoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [RepoService],
    });

    service = TestBed.inject(RepoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
