import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { BranchBasicInfoDTO, BranchDTO, CommitsResponseDto, OriginTargetBranchRequest, TargetBranchRequest } from 'src/models/branch/branch';
import { HttpRequestService } from 'src/utils/http-request.service';

import { Injectable } from '@angular/core';
import { FileRequest } from 'src/models/files/files';

@Injectable({
  providedIn: 'root',
})
export class BranchService {

  constructor(private httpRequestService: HttpRequestService) { }

  getRepoBranches(repoId: string): Observable<BranchDTO[]> {
    const url = environment.API_BASE_URL + `/branch/getRepoBranches/${repoId}`;
    return this.httpRequestService.get(url) as Observable<BranchDTO[]>;
  }

  createNewBranch(request: OriginTargetBranchRequest): Observable<BranchDTO> {
    const url = environment.API_BASE_URL + `/branch/newBranch`;
    const body = JSON.stringify(request);

    return this.httpRequestService.post(url, body) as Observable<BranchDTO>;
  }

  deleteBranch(request: TargetBranchRequest) {
    const url = environment.API_BASE_URL + `/branch/delete`;
    const body = JSON.stringify(request);

    return this.httpRequestService.post(url, body) as Observable<BranchBasicInfoDTO>;
  }

  getBranchCount(repoId: string): Observable<number> {
    const url = environment.API_BASE_URL + `/branch/getRepoBranchesCount/${repoId}`;
    return this.httpRequestService.get(url) as Observable<number>;
  }

  renameBranch(request: OriginTargetBranchRequest): Observable<BranchDTO> {
    const url = environment.API_BASE_URL + `/branch/renameBranch`;
    const body = JSON.stringify(request);

    return this.httpRequestService.post(url, body) as Observable<BranchDTO>;
  }

}
