import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, take, tap } from "rxjs";
import { AccessToken } from "../models/access-token.interface";
import { LoginRequest } from "../models/login-request.interface";
import { User } from "../models/user.interface";
import { Message } from "../models/message.interface";
import { RegisterRequest } from "../models/register-request.interface";
import { ProfileUpdateRequest } from "../models/profile-update-request.interface";

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private readonly apiUrl = 'http://localhost:3000/api/auth';

    constructor(private readonly http: HttpClient) { }

    login(credentials: LoginRequest): Observable<AccessToken> {
        return this.http.post<AccessToken>(`${this.apiUrl}/login`, credentials, { withCredentials: true }).pipe(
            tap(response => {
                this.setToken(response.accessToken)
            })
        );
    }

    register(registerRequest: RegisterRequest): Observable<Message> {
        return this.http.post<Message>(`${this.apiUrl}/register`, registerRequest);
    }

    fetchUser(): Observable<User> {
        return this.http.get<User>(`${this.apiUrl}/me`);
    }

    updateUserProfile(userProfile: ProfileUpdateRequest): Observable<void> {
        return this.http.put<void>(`${this.apiUrl}/me`, userProfile)
            .pipe(
                take(1),
                tap(() => this.setToken(''))
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

    logout(): void {
        this.http.post<void>(`${this.apiUrl}/logout`, {})
            .pipe(take(1))
            .subscribe(() => {
                this.setToken('');
            });
    }

    private setToken(token: string): void {
        sessionStorage.setItem('jwt', token);
    }

    getToken(): string | null {
        return sessionStorage.getItem('jwt');
    }

}