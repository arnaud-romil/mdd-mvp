import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Topic } from "../models/topic.interface";
import { HttpClient } from "@angular/common/http";

@Injectable({
    providedIn: 'root'
})
export class TopicService {

    constructor(private readonly http: HttpClient) { }

    readonly topics$: Observable<Topic[]> = this.http.get<Topic[]>('http://localhost:3000/api/topics');

}