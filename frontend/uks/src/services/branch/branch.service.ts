import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { BranchBasicInfoDTO } from 'src/models/branch/branch';
import { HttpRequestService } from 'src/utils/http-request.service';

import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class BranchService {
  constructor(private httpRequestService: HttpRequestService) {}

  getRepoBranches(repoId: string): Observable<BranchBasicInfoDTO[]> {
    const url = environment.API_BASE_URL + `/branch/getRepoBranches/${repoId}`;
    return this.httpRequestService.get(url) as Observable<BranchBasicInfoDTO[]>;
  }
}
