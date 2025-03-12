import { Comment } from "./comment.interface";

export interface Post {
    id: number;
    title: string;
    content: string;
    topic: string;
    author: string;
    comments: Comment[];
    createdAt: Date;
}
