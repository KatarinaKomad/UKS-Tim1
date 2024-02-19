import { Injectable } from '@angular/core';
import { HttpRequestService } from 'src/utils/http-request.service';
import { AuthService } from '../auth/auth.service';
import { Observable } from 'rxjs';
import { UserDTO, UserUpdateRequest } from 'src/models/user/user';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(
    private httpRequestService: HttpRequestService,
    private authService: AuthService
  ) {}

  getProfile(userID: string): Observable<UserDTO> {
    const url = environment.API_BASE_URL + `/profile/getProfileInfo/${userID}`;
    return this.httpRequestService.get(url) as Observable<UserDTO>;
  }

  updateProfile(
    userID: string,
    request: UserUpdateRequest
  ): Observable<UserDTO | null> {
    const url = environment.API_BASE_URL + `/profile/updateMyProfile/${userID}`;
    const body = JSON.stringify(request);

    return this.httpRequestService.put(url, body) as Observable<UserDTO | null>;
  }

  deleteAccount(userID: string): Observable<void> {
    const url = environment.API_BASE_URL + `/profile/delete/${userID}`;
    return this.httpRequestService.delete(url) as Observable<void>;
  }
}
