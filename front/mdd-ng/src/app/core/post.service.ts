import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Post } from '../models/post.interface';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private readonly apiUrl = 'http://localhost:3000/api/posts';

  constructor(private readonly http: HttpClient) { }

  getUserFeed(): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.apiUrl}`);
  }

  getPostById(postId: number): Observable<Post> {
    return this.http.get<Post>(`${this.apiUrl}/${postId}`);
  }

  createPost(postData: Post): Observable<Post> {
    return this.http.post<Post>(`${this.apiUrl}`, postData);
  }

  addCommentToPost(postId: number, commentContent: { content: string }): Observable<Post> {
    return this.http.post<Post>(`${this.apiUrl}/${postId}/comments`, commentContent);
  }
}
