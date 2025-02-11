import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, tap } from "rxjs";
import { AccessToken } from "../features/auth/interfaces/accessToken.interface";
import { LoginRequest } from "../features/auth/interfaces/loginRequest.interface";

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private readonly apiUrl = 'http://localhost:3000/api/auth/login';

    constructor(private readonly http: HttpClient) { }

    login(credentials: LoginRequest): Observable<AccessToken> {
        return this.http.post<AccessToken>(this.apiUrl, credentials).pipe(
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