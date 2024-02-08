import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Page } from 'src/models/page/page';
import { SearchRequest, SearchResult } from 'src/models/search/search';
import { HttpRequestService } from 'src/utils/http-request.service';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  constructor(
    private httpRequestService: HttpRequestService,
  ) { }

  search(searchRequest: SearchRequest): Observable<Page<SearchResult>> {
    const url = environment.API_BASE_URL + '/search';
    const body = JSON.stringify(searchRequest);

    return this.httpRequestService.post(url, body) as Observable<Page<SearchResult>>;
  }

  getRepoCount(searchRequest: SearchRequest): Observable<number> {
    const url = environment.API_BASE_URL + '/search/repoCount';
    const body = JSON.stringify(searchRequest);

    return this.httpRequestService.post(url, body) as Observable<number>;
  }
  getIssueCount(searchRequest: SearchRequest): Observable<number> {
    const url = environment.API_BASE_URL + '/search/issueCount';
    const body = JSON.stringify(searchRequest);

    return this.httpRequestService.post(url, body) as Observable<number>;
  }
  getPrCount(searchRequest: SearchRequest): Observable<number> {
    const url = environment.API_BASE_URL + '/search/prCount';
    const body = JSON.stringify(searchRequest);

    return this.httpRequestService.post(url, body) as Observable<number>;
  }
  getUserCount(searchRequest: SearchRequest): Observable<number> {
    const url = environment.API_BASE_URL + '/search/userCount';
    const body = JSON.stringify(searchRequest);

    return this.httpRequestService.post(url, body) as Observable<number>;
  }

}
