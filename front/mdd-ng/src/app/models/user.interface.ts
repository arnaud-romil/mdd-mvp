import { Topic } from "./topic.interface";

export interface User {

    username: string;
    email: string;
    topics: Topic[];
}