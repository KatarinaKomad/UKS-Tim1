import { TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';

import { CommentService } from './comment.service';

describe('CommentService', () => {
  let service: CommentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [CommentService],
    });
    service = TestBed.inject(CommentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
