import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, take, tap } from "rxjs";
import { AccessToken } from "../models/accessToken.interface";
import { LoginRequest } from "../models/loginRequest.interface";

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private readonly apiUrl = 'http://localhost:3000/api/auth';

    constructor(private readonly http: HttpClient) { }

    login(credentials: LoginRequest): Observable<AccessToken> {
        return this.http.post<AccessToken>(`${this.apiUrl}/login`, credentials).pipe(
            tap(response => {
                this.setToken(response.accessToken);
            })
        );
    }

    refreshToken(): Observable<AccessToken> {
        return this.http.post<AccessToken>(`${this.apiUrl}/refresh-token`, {}, { withCredentials: true }).pipe(
            take(1),
            tap(response => {
                this.setToken(response.accessToken);
            })
        );
    }

    private setToken(token: string): void {
        sessionStorage.setItem('jwt', token);
    }

    getToken(): string | null {
        return sessionStorage.getItem('jwt');
    }

}