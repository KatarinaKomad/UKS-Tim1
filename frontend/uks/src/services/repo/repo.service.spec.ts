import { TestBed } from '@angular/core/testing';

import { RepoService } from './repo.service';
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from '../auth/auth.service';

describe('RepoService', () => {
  let service: RepoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [RepoService, AuthService],
    });
    service = TestBed.inject(RepoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
