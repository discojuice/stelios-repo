import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RequestItem } from '../models/request';

@Injectable({
    providedIn: 'root'
})
export class RequestService {

    private apiUrl = 'http://localhost:8080/api/requests';

    constructor(private http: HttpClient) { }

    getAllRequests(): Observable<RequestItem[]> {
        return this.http.get<RequestItem[]>(this.apiUrl);
    }

    searchRequests(question: string, answer: string): Observable<RequestItem[]> {
        let params = new HttpParams();

        if (question) {
            params = params.set('question', question);
        }

        if (answer) {
            params = params.set('answer', answer);
        }

        return this.http.get<RequestItem[]>(`${this.apiUrl}/search`, { params });
    }

    createRequest(request: Partial<RequestItem>): Observable<void> {
        return this.http.post<void>(this.apiUrl, request);
    }

    updateRequest(id: number, request: Partial<RequestItem>): Observable<void> {
        return this.http.put<void>(`${this.apiUrl}/${id}`, request);
    }

    deleteRequest(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}