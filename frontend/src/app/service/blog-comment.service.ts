import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BlogComment } from '../models/blog-comment';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BlogCommentService {

  private readonly apiUrl = `${environment.apiUrl}/blog-posts`;

  constructor(private http: HttpClient) {}

  getComments(postId: number): Observable<BlogComment[]> {
    return this.http.get<BlogComment[]>(`${this.apiUrl}/${postId}/comments`);
  }

  createComment(postId: number, comment: BlogComment): Observable<BlogComment> {
    return this.http.post<BlogComment>(`${this.apiUrl}/${postId}/comments`, comment);
  }
}