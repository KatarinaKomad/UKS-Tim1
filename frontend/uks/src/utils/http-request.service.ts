import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { getToken } from './jwtTokenUtils';

@Injectable({
    providedIn: 'root'
})

export class HttpRequestService {
    constructor(
        private httpClient: HttpClient,
    ) { }


    private createHeaders(token: string | null) {
        if (token) {
            return new HttpHeaders({ 'Content-type': 'application/json', 'Authorization': `Bearer ${token}` });
        }
        return new HttpHeaders({ 'Content-type': 'application/json' });
    }
    private createOptions() {
        const token = getToken();
        const headers = this.createHeaders(token);
        return { headers, withCredentials: true };
    }

    post(url: string, body: any): Observable<any> {
        return this.httpClient.post(url, body, this.createOptions())
    }

    get(url: string): Observable<any> {
        return this.httpClient.get(url, this.createOptions())
    }

    patch(url: string, body: any): Observable<any> {
        return this.httpClient.patch(url, body, this.createOptions())
    }

    put(url: string, body: any): Observable<any> {
        return this.httpClient.put(url, body, this.createOptions())
    }

    delete(url: string): Observable<any> {
        return this.httpClient.delete(url, this.createOptions())
    }
}