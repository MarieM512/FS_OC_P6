import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SignupRequest } from '../interfaces/request/signupRequest.interface';
import { Observable, map } from 'rxjs';
import { LoginRequest } from '../interfaces/request/loginRequest.interface';
import { LogResponse } from '../interfaces/response/logResponse.interface';
import { User } from '../interfaces/response/user.interface';
import { UserUpdate } from '../interfaces/response/userUpdate.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private pathService = 'api/auth'

  constructor(private httpClient: HttpClient) { }

  public register(signupRequest: SignupRequest): Observable<LogResponse> {
    return this.httpClient.post<LogResponse>(`${this.pathService}/register`, signupRequest)
  }

  public login(loginRequest: LoginRequest): Observable<LogResponse> {
    return this.httpClient.post<LogResponse>(`${this.pathService}/login`, loginRequest)
  }

  public getUser(): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/me`)
  }

  public updateUser(user: UserUpdate): Observable<void> {
    return this.httpClient.put<void>(`${this.pathService}/me`, user)
  }

  public isTokenValid(): Observable<boolean> {
    return this.httpClient.get<boolean>(`${this.pathService}/token`)
  }

  public isAuthenticated(): boolean {
    var isValid = false
    this.isTokenValid().subscribe({
      next: (value) => {
        isValid = value
      }
    })
    return (isValid || !!localStorage.getItem('token'))
  }

  public logout(): void {
    localStorage.removeItem('token');
  }
}
