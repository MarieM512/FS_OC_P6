import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PostRequest } from '../interfaces/request/postRequest.interface';
import { Post } from '../interfaces/response/post.interface';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private pathService = 'api/post'

  constructor(private httpClient: HttpClient) { }

  public create(postRequest: PostRequest): Observable<void> {
    return this.httpClient.post<void>(this.pathService, postRequest)
  }

  public getPosts(): Observable<Post[]> {
    return this.httpClient.get<Post[]>(this.pathService)
  }

  public getPostById(id: number): Observable<Post> {
    return this.httpClient.get<Post>(`${this.pathService}/${id}`)
  }
}
