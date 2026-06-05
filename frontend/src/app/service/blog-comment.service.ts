import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BlogComment } from '../models/blog-comment';

@Injectable({
  providedIn: 'root'
})
export class BlogCommentService {

  private readonly apiUrl = 'http://localhost:8080/api/blog-posts';

  constructor(private http: HttpClient) {}

  getComments(postId: number): Observable<BlogComment[]> {
    return this.http.get<BlogComment[]>(`${this.apiUrl}/${postId}/comments`);
  }

  createComment(postId: number, comment: BlogComment): Observable<BlogComment> {
    return this.http.post<BlogComment>(`${this.apiUrl}/${postId}/comments`, comment);
  }
}