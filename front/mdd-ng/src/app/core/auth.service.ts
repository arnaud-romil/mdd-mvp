import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, take, tap } from "rxjs";
import { AccessToken } from "../models/accessToken.interface";
import { LoginRequest } from "../models/loginRequest.interface";
import { User } from "../models/user.interface";
import { Message } from "../models/message.interface";
import { RegisterRequest } from "../models/registerRequest.interface";

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private readonly apiUrl = 'http://localhost:3000/api/auth';
    private readonly userSubject = new BehaviorSubject<User | null>(null);
    readonly user$ = this.userSubject.asObservable();

    constructor(private readonly http: HttpClient) { }

    login(credentials: LoginRequest): Observable<AccessToken> {
        return this.http.post<AccessToken>(`${this.apiUrl}/login`, credentials).pipe(
            tap(response => {
                this.setToken(response.accessToken)
            })
        );
    }

    register(registerRequest: RegisterRequest): Observable<Message> {
        return this.http.post<Message>(`${this.apiUrl}/register`, registerRequest);
    }

    fetchUser(): void {
        this.http.get<User>(`${this.apiUrl}/me`)
            .pipe(take(1))
            .subscribe({
                next: (user) => this.userSubject.next(user),
                error: () => this.userSubject.next(null)
            });
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