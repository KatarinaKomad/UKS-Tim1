import { RepoUpdateRequest } from 'src/models/repo/repo';
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

import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class RepoService {
  constructor(
    private httpRequestService: HttpRequestService,
    private authService: AuthService
  ) {}

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

  getCanEditRepoItems(): Observable<boolean> {
    let repoRequest;
    this.authService.getLoggedUser().subscribe({
      next: (user: UserBasicInfo | undefined) => {
        repoRequest = this.createEditRepoRequest(user);
      },
      error: (e: any) => {
        console.log(e);
        return of(false);
      },
    });
    return repoRequest ? this.canEditRepoItems(repoRequest) : of(false);
  }
  private createEditRepoRequest(
    user: UserBasicInfo | undefined
  ): EditRepoRequest | null {
    const repoId = localStorage.getItem('repoId');
    if (!repoId || !user?.id) return null;
    return { repoId, userId: user.id };
  }
}
