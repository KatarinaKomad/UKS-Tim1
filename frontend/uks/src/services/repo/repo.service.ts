import { RepoForkRequest, RepoUpdateRequest } from 'src/models/repo/repo';
import {
  EditRepoRequest,
  RepoBasicInfoDTO,
  RepoRequest,
} from 'src/models/repo/repo';
import { Observable, of } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UserBasicInfo } from 'src/models/user/user';
import { HttpRequestService } from 'src/utils/http-request.service';

import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class RepoService {

  constructor(
    private httpRequestService: HttpRequestService,
  ) { }

  getAllPublic(): Observable<RepoBasicInfoDTO[]> {
    const url = environment.API_BASE_URL + '/repo/getAllPublic';
    return this.httpRequestService.get(url) as Observable<RepoBasicInfoDTO[]>;
  }

  getRepoMembers(repoId: string): Observable<UserBasicInfo[]> {
    const url = environment.API_BASE_URL + `/repo/getMembers/${repoId}`;
    return this.httpRequestService.get(url) as Observable<UserBasicInfo[]>;
  }

  getMyRepos(userId: string): Observable<RepoBasicInfoDTO[]> {
    const url = environment.API_BASE_URL + `/repo/getMyRepos/${userId}`;
    return this.httpRequestService.get(url) as Observable<RepoBasicInfoDTO[]>;
  }
  getById(repoId: string): Observable<RepoBasicInfoDTO | null> {
    const url = environment.API_BASE_URL + `/repo/getById/${repoId}`;
    return this.httpRequestService.get(url) as Observable<RepoBasicInfoDTO | null>;
  }


  createNewRepo(repoRequest: RepoRequest): Observable<RepoBasicInfoDTO | null> {
    const url = environment.API_BASE_URL + '/repo/create';
    const body = JSON.stringify(repoRequest);

    return this.httpRequestService.post(
      url,
      body
    ) as Observable<RepoBasicInfoDTO | null>;
  }

  updateRepo(
    id: string,
    request: RepoUpdateRequest
  ): Observable<RepoBasicInfoDTO | null> {
    const url = environment.API_BASE_URL + `/repo/update/${id}`;
    const body = JSON.stringify(request);

    return this.httpRequestService.put(
      url,
      body
    ) as Observable<RepoBasicInfoDTO | null>;
  }

  validateOverviewByRepoName(
    repoRequest: RepoRequest
  ): Observable<RepoBasicInfoDTO | null> {
    const url = environment.API_BASE_URL + '/repo/validateOverviewByRepoName';
    const body = JSON.stringify(repoRequest);

    return this.httpRequestService.post(
      url,
      body
    ) as Observable<RepoBasicInfoDTO | null>;
  }

  canEditRepoItems(repoRequest: EditRepoRequest): Observable<boolean> {
    const url = environment.API_BASE_URL + '/repo/canEditRepoItems';
    const body = JSON.stringify(repoRequest);

    return this.httpRequestService.post(url, body) as Observable<boolean>;
  }

  getCanEditRepoItems(): boolean {
    var storedBoolean = localStorage.getItem('canEditRepoItems');
    return storedBoolean === 'true' ? true : false;
  }

  getAllForked(repoId: string) {
    const url = environment.API_BASE_URL + `/repo/getAllForked/${repoId}`;
    return this.httpRequestService.get(url) as Observable<RepoBasicInfoDTO[]>;
  }

  newFork(forkRequest: RepoForkRequest) {
    const url = environment.API_BASE_URL + '/repo/fork';
    const body = JSON.stringify(forkRequest);

    return this.httpRequestService.post(url, body) as Observable<RepoBasicInfoDTO | null>;
  }
}
