import { TestBed } from '@angular/core/testing';

import { SshkeyService } from './sshkey.service';

describe('SshkeyService', () => {
  let service: SshkeyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SshkeyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
