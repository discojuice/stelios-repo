import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { BlogPost } from '../models/blog-post'; // adjust path to match your folder structure

@Injectable({
  providedIn: 'root'
})
export class BlogPostService {

  private apiUrl = `${environment.apiUrl}/blog-posts`;

  constructor(private http: HttpClient) { }

  getPost(id: number): Observable<BlogPost> {
    return this.http.get<BlogPost>(`${this.apiUrl}/${id}`);
  }

  createPost(post: BlogPost): Observable<BlogPost> {
    return this.http.post<BlogPost>(this.apiUrl, post);
  }

  updatePost(id: number, post: BlogPost): Observable<BlogPost> {
    return this.http.put<BlogPost>(`${this.apiUrl}/${id}`, post);
  }

  deletePost(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // blog-post.service.ts
  getPosts(page: number, size: number = 5): Observable<BlogPost[]> {
  return this.http.get<BlogPost[]>(`${this.apiUrl}?page=${page}&size=${size}`);
}
}