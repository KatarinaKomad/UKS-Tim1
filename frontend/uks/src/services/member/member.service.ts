import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { RepoUserRequest } from 'src/models/repo/repo';
import { ChangeMemberRoleRequest, RepoMemberDTO } from 'src/models/user/member';
import { HttpRequestService } from 'src/utils/http-request.service';

@Injectable({
  providedIn: 'root'
})
export class MemberService {

  constructor(
    private httpRequestService: HttpRequestService,
  ) { }


  getRepoMembers(repoId: string): Observable<RepoMemberDTO[]> {
    const url = environment.API_BASE_URL + `/member/getMembers/${repoId}`;
    return this.httpRequestService.get(url) as Observable<RepoMemberDTO[]>;
  }

  inviteUser(request: RepoUserRequest) {
    const url = environment.API_BASE_URL + `/member/inviteUser`;
    const body = JSON.stringify(request);

    return this.httpRequestService.post(url, body) as Observable<RepoMemberDTO>;
  }

  confirmInvitation(link: string) {
    const url = environment.API_BASE_URL + `/member/acceptInvitation/${link}`;

    return this.httpRequestService.post(url, null) as Observable<RepoMemberDTO>;
  }

  removeMember(request: RepoUserRequest) {
    const url = environment.API_BASE_URL + `/member/removeMember`;
    const body = JSON.stringify(request);

    return this.httpRequestService.post(url, body) as Observable<void>;
  }

  changeRole(request: ChangeMemberRoleRequest) {
    const url = environment.API_BASE_URL + `/member/changeRole`;
    const body = JSON.stringify(request);

    return this.httpRequestService.post(url, body) as Observable<RepoMemberDTO>;
  }
}
