import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { User } from "../models/user.interface";

@Injectable({
    providedIn: 'root'
})
export class UserService {

    private readonly apiUrl = 'http://localhost:3000/api/users';

    constructor(private readonly http: HttpClient) { }

    addSubscription(topicId: number): Observable<User> {
        return this.http.post<User>(`${this.apiUrl}/me/topics/${topicId}`, {});
    }

    removeSubscription(topicId: number): Observable<User> {
        return this.http.delete<User>(`${this.apiUrl}/me/topics/${topicId}`, {});
    }

}