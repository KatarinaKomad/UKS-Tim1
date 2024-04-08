import { Injectable } from '@angular/core';
import { HttpRequestService } from 'src/utils/http-request.service';
import { Observable, of } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Comment, CommentRequest } from 'src/models/comment/comment';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  constructor(private httpRequestService: HttpRequestService) {}

  getAllItemComments(itemId: string): Observable<Comment[]> {
    const url =
      environment.API_BASE_URL + `/comment/getAllItemComments/${itemId}`;
    return this.httpRequestService.get(url) as Observable<Comment[]>;
  }

  createNewComment(
    authorId: string,
    itemId: string,
    request: CommentRequest
  ): Observable<Comment | null> {
    const url =
      environment.API_BASE_URL + `/comment/create/${itemId}/${authorId}`;
    const body = JSON.stringify(request);

    return this.httpRequestService.post(
      url,
      body
    ) as Observable<Comment | null>;
  }

  editComment(
    commentId: number,
    request: CommentRequest
  ): Observable<Comment | null> {
    const url = environment.API_BASE_URL + `/comment/edit/${commentId}`;

    const body = JSON.stringify(request);
    return this.httpRequestService.patch(
      url,
      body
    ) as Observable<Comment | null>;
  }

  deleteComment(commentId: number): Observable<void> {
    const url = environment.API_BASE_URL + `/comment/delete/${commentId}`;
    return this.httpRequestService.delete(url) as Observable<void>;
  }
}
