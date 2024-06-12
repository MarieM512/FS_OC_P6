import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comment } from '../interfaces/response/comment.interface';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private pathService = 'api/comment'

  constructor(private httpClient: HttpClient) { }

  public create(id: number, content: string): Observable<void> {    
    return this.httpClient.post<void>(`${this.pathService}/${id}`, content)
  }

  public getCommentsById(id: number): Observable<Comment[]> {    
    return this.httpClient.get<Comment[]>(`${this.pathService}/${id}`)
  }
}
