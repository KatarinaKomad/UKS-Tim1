import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { RepoBasicInfoDTO } from 'src/models/repo/repo';
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
}
