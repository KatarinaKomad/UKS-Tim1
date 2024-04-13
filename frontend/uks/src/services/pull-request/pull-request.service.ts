import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { IssueEventRequest, IssueDTO, IssueEventDTO } from 'src/models/issue/issue';
import { PullRequestDTO, PullRequestEventDTO, PullRequestEventRequest, PullRequestRequest } from 'src/models/pull-request/pull-request';
import { HttpRequestService } from 'src/utils/http-request.service';
import { getUsernameFromToken } from 'src/utils/jwtTokenUtils';

@Injectable({
  providedIn: 'root'
})
export class PullRequestService {

  constructor(private httpRequestService: HttpRequestService) { }

  getUserPullRequests(state: string, repoId: string): Observable<any> {
    const username = getUsernameFromToken();
    const url = environment.API_BASE_URL + `/pull-request/getUserPRs/${username}?state=${state}&repoId=${repoId}`;
    return this.httpRequestService.get(url) as Observable<any>;
  }

  getById(prId: string): Observable<PullRequestDTO> {
    const url = environment.API_BASE_URL + `/pull-request/getPR/${prId}`;
    return this.httpRequestService.get(url) as Observable<PullRequestDTO>;
  }

  update(eventRequest: PullRequestEventRequest): Observable<PullRequestDTO | null> {
    const url = environment.API_BASE_URL + "/pull-request/update";
    const body = JSON.stringify(eventRequest);

    return this.httpRequestService.put(url, body) as Observable<PullRequestDTO | null>;
  }

  delete(id: string): Observable<void> {
    const url = environment.API_BASE_URL + `/pull-request/delete/${id}`;
    return this.httpRequestService.delete(url) as Observable<void>;
  }

  getPREventHistory(id: string): Observable<PullRequestEventDTO[]> {
    const url = environment.API_BASE_URL + `/pull-request/getPREventHistory/${id}`;
    return this.httpRequestService.get(url) as Observable<PullRequestEventDTO[]>;
  }

  create(prRequest: PullRequestRequest): Observable<PullRequestDTO | null> {
    const url = environment.API_BASE_URL + "/pull-request/create";
    const body = JSON.stringify(prRequest);

    return this.httpRequestService.post(url, body) as Observable<PullRequestDTO | null>;
  }
}
