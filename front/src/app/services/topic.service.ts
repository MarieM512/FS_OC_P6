import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Topic } from '../interfaces/response/topic.interface';

@Injectable({
  providedIn: 'root'
})
export class TopicService {

  private pathService = 'api/topic';

  constructor(private httpClient: HttpClient) { }

  public getTopics(): Observable<Topic[]> {
    return this.httpClient.get<Topic[]>(this.pathService);
  }

  public subscribe(topic: Topic): Observable<void> {
    return this.httpClient.put<void>(`${this.pathService}/subscribe`, topic)
  }

  public unsubscribe(topic: Topic): Observable<void> {
    return this.httpClient.put<void>(`${this.pathService}/unsubscribe`, topic)
  }
}
