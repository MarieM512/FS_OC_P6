import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PostRequest } from '../interfaces/request/postRequest.interface';
import { Post } from '../interfaces/response/post.interface';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private pathService = 'api/post';

  constructor(private httpClient: HttpClient) { }

  public create(postRequest: PostRequest): Observable<Post> {
    return this.httpClient.post<Post>(`${this.pathService}/post`, postRequest);
  }
}
