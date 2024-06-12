import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CommitsResponseDto } from 'src/models/commit/commit';
import { FileDTO, FileRequest } from 'src/models/files/files';
import { HttpRequestService } from 'src/utils/http-request.service';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor(
    private httpRequestService: HttpRequestService,
  ) { }

  getFiles(request: FileRequest): Observable<FileDTO[]> {
    const url = environment.API_BASE_URL + `/file/getFiles`;
    const body = JSON.stringify(request);

    return this.httpRequestService.post(url, body) as Observable<FileDTO[]>;
  }

  getFileCommits(request: FileRequest): Observable<CommitsResponseDto[]> {
    const url = environment.API_BASE_URL + `/file/commits`;
    const body = JSON.stringify(request);

    return this.httpRequestService.post(url, body) as Observable<CommitsResponseDto[]>;
  }

}
