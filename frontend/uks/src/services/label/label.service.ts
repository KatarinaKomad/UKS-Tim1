import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LabelDTO, LabelRequest } from 'src/models/label/label';
import { HttpRequestService } from 'src/utils/http-request.service';

@Injectable({
  providedIn: 'root'
})
export class LabelService {

  constructor(private httpRequestService: HttpRequestService) { }

  getAllRepoLabels(repoId: string): Observable<LabelDTO[]> {
    const url = environment.API_BASE_URL + `/label/getAllRepoLabels/${repoId}`;
    return this.httpRequestService.get(url) as Observable<LabelDTO[]>;
  }

  createNewLabel(labelRequest: LabelRequest): Observable<LabelDTO | null> {
    const url = environment.API_BASE_URL + "/label/create";
    const body = JSON.stringify(labelRequest);

    return this.httpRequestService.post(url, body) as Observable<LabelDTO | null>;
  }

  editLabel(labelRequest: LabelRequest): Observable<LabelDTO | null> {
    const url = environment.API_BASE_URL + "/label/edit";
    const body = JSON.stringify(labelRequest);

    return this.httpRequestService.patch(url, body) as Observable<LabelDTO | null>;
  }

  deleteLabel(labelId: string): Observable<void> {
    const url = environment.API_BASE_URL + `/label/delete/${labelId}`;
    return this.httpRequestService.delete(url) as Observable<void>;
  }
}
