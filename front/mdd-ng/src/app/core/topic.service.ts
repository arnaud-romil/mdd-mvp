import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Topic } from "../models/topic.interface";

@Injectable({
    providedIn: 'root'
})
export class TopicService {

    private readonly apiUrl = 'api/topics';

    constructor(private readonly http: HttpClient) { }

    getTopics(): Observable<Topic[]> {
        return this.http.get<Topic[]>(this.apiUrl);
    }
}