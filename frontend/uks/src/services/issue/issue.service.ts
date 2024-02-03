import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { IssueDTO, IssueEventDTO, IssueEventRequest, IssueRequest, UserIssuesDTO } from 'src/models/issue/issue';
import { ChangeStateRequest } from 'src/models/state/state';
import { HttpRequestService } from 'src/utils/http-request.service';

@Injectable({
  providedIn: 'root'
})
export class IssueService {

  constructor(private httpRequestService: HttpRequestService) { }

  getAllRepoIssues(repoId: string): Observable<IssueDTO[]> {
    const url = environment.API_BASE_URL + `/issue/getAllRepoIssues/${repoId}`;
    return this.httpRequestService.get(url) as Observable<IssueDTO[]>;
  }
  getIssueEventHistory(issueId: string): Observable<IssueEventDTO[]> {
    const url = environment.API_BASE_URL + `/issue/getIssueEventHistory/${issueId}`;
    return this.httpRequestService.get(url) as Observable<IssueEventDTO[]>;
  }
  getById(issueId: string): Observable<IssueDTO> {
    const url = environment.API_BASE_URL + `/issue/getById/${issueId}`;
    return this.httpRequestService.get(url) as Observable<IssueDTO>;
  }

  getMyIssues(userId: string): Observable<UserIssuesDTO> {
    const url = environment.API_BASE_URL + `/issue/getMyIssues/${userId}`;
    return this.httpRequestService.get(url) as Observable<UserIssuesDTO>;
  }

  createNewIssue(issueRequest: IssueRequest): Observable<IssueDTO | null> {
    const url = environment.API_BASE_URL + "/issue/create";
    const body = JSON.stringify(issueRequest);

    return this.httpRequestService.post(url, body) as Observable<IssueDTO | null>;
  }

  updateIssue(eventRequest: IssueEventRequest): Observable<IssueDTO | null> {
    const url = environment.API_BASE_URL + "/issue/update";
    const body = JSON.stringify(eventRequest);

    return this.httpRequestService.put(url, body) as Observable<IssueDTO | null>;
  }


  deleteIssue(issueId: string): Observable<void> {
    const url = environment.API_BASE_URL + `/issue/delete/${issueId}`;
    return this.httpRequestService.delete(url) as Observable<void>;
  }
}
