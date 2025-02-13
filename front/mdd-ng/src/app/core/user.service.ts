import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Topic } from "../models/topic.interface";
import { Observable } from "rxjs";
import { User } from "../models/user.interface";

@Injectable({
    providedIn: 'root'
})
export class UserService {

    private readonly apiUrl = 'http://localhost:3000/api/users';

    constructor(private readonly http: HttpClient) { }

    addSubscription(topic: Topic): Observable<User> {
        return this.http.post<User>(`${this.apiUrl}/me/topics/${topic.id}`, {});
    }

}