import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { MilestoneDTO, MilestoneRequest } from 'src/models/milestone/milestone';
import { ChangeStateRequest } from 'src/models/state/state';
import { HttpRequestService } from 'src/utils/http-request.service';

@Injectable({
  providedIn: 'root'
})
export class MilestoneService {


  constructor(private httpRequestService: HttpRequestService) { }

  getAllRepoMilestones(repoId: string): Observable<MilestoneDTO[]> {
    const url = environment.API_BASE_URL + `/milestone/getAllRepoMilestones/${repoId}`;
    return this.httpRequestService.get(url) as Observable<MilestoneDTO[]>;
  }

  createNewMilestone(milestoneRequest: MilestoneRequest): Observable<MilestoneDTO | null> {
    const url = environment.API_BASE_URL + "/milestone/create";
    const body = JSON.stringify(milestoneRequest);

    return this.httpRequestService.post(url, body) as Observable<MilestoneDTO | null>;
  }

  updateMilestone(milestoneRequest: MilestoneRequest): Observable<MilestoneDTO | null> {
    const url = environment.API_BASE_URL + "/milestone/update";
    const body = JSON.stringify(milestoneRequest);

    return this.httpRequestService.put(url, body) as Observable<MilestoneDTO | null>;
  }
  changeStateMilestone(changeStateRequest: ChangeStateRequest): Observable<void> {
    const url = environment.API_BASE_URL + "/milestone/changeState";
    const body = JSON.stringify(changeStateRequest);

    return this.httpRequestService.put(url, body) as Observable<void>;
  }

  deleteMilestone(milestoneId: string): Observable<void> {
    const url = environment.API_BASE_URL + `/milestone/delete/${milestoneId}`;
    return this.httpRequestService.delete(url) as Observable<void>;
  }
}
