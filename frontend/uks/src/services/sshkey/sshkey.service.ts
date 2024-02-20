import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { KeyRequest } from 'src/models/sshkey/sshkey';
import { HttpRequestService } from 'src/utils/http-request.service';

@Injectable({
  providedIn: 'root'
})
export class SshkeyService {

  constructor(private httpRequestService: HttpRequestService) { }

  createKey(request: KeyRequest): Observable<void> {
    const url = environment.API_BASE_URL + '/key';
    const body = JSON.stringify(request);

    return this.httpRequestService.post(url, body) as Observable<void>;
  }
}
