import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { EditRepoRequest, RepoBasicInfoDTO, RepoRequest } from 'src/models/repo/repo';
import { HttpRequestService } from 'src/utils/http-request.service';

@Injectable({
  providedIn: 'root'
})
export class RepoService {

  constructor(private httpRequestService: HttpRequestService) { }

  getAllPublic(): Observable<RepoBasicInfoDTO[]> {
    const url = environment.API_BASE_URL + "/repo/getAllPublic";
    return this.httpRequestService.get(url) as Observable<RepoBasicInfoDTO[]>;
  }

  getMyRepos(userId: string): Observable<RepoBasicInfoDTO[]> {
    const url = environment.API_BASE_URL + `/repo/getMyRepos/${userId}`;
    return this.httpRequestService.get(url) as Observable<RepoBasicInfoDTO[]>;
  }

  createNewRepo(repoRequest: RepoRequest): Observable<RepoBasicInfoDTO | null> {
    const url = environment.API_BASE_URL + "/repo/create";
    const body = JSON.stringify(repoRequest);

    return this.httpRequestService.post(url, body) as Observable<RepoBasicInfoDTO | null>;
  }

  validateOverviewByRepoName(repoRequest: RepoRequest): Observable<RepoBasicInfoDTO | null> {
    const url = environment.API_BASE_URL + "/repo/validateOverviewByRepoName";
    const body = JSON.stringify(repoRequest);

    return this.httpRequestService.post(url, body) as Observable<RepoBasicInfoDTO | null>;
  }

  canEditRepoItems(repoRequest: EditRepoRequest): Observable<boolean> {
    const url = environment.API_BASE_URL + "/repo/canEditRepoItems";
    const body = JSON.stringify(repoRequest);

    return this.httpRequestService.post(url, body) as Observable<boolean>;
  }
}
