import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PostRequest } from '../interfaces/request/postRequest.interface';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private pathService = 'api/post';

  constructor(private httpClient: HttpClient) { }

  public create(postRequest: PostRequest): Observable<PostRequest> {
    return this.httpClient.post<PostRequest>(`${this.pathService}/post`, postRequest);
  }
}
