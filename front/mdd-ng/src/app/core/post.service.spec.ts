import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { PostService } from "./post.service";
import { TestBed } from "@angular/core/testing";
import { provideHttpClient } from "@angular/common/http";
import { firstValueFrom } from "rxjs";
import { Post } from "../models/post.interface";

describe('PostService', () => {

    let postService: PostService;
    let httpTesting: HttpTestingController;

    const posts: Post[] = [
        {
            id: 1,
            title: 'Introduction à Java',
            content: "Java est un langage de programmation populaire utilisé pour le developpement d'applications d'entreprise, mobiles et web.",
            comments: [],
            topic: 'Java',
            author: 'John',
            createdAt: new Date()
        },
        {
            id: 2,
            title: 'Les classes et objets en Java',
            content: "Découvrez comment créer et utiliser des classes et objets en Java pour une programmation orientée objet efficace.",
            comments: [],
            topic: 'Java',
            author: 'John',
            createdAt: new Date()
        }
    ];

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [
                provideHttpClient(),
                provideHttpClientTesting()
            ]
        });
        postService = TestBed.inject(PostService);
        httpTesting = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        httpTesting.verify();
    });

    it('should be created', () => {
        expect(postService).toBeTruthy();
    });

    it('should return post by id', async () => {

        const postId = 1;
        const post$ = postService.getPostById(postId);
        const postPromise = firstValueFrom(post$);

        const req = httpTesting.expectOne({
            method: 'GET',
            url: `api/posts/${postId}`
        });

        const expectedPost: Post = posts[0]

        req.flush(expectedPost);

        expect(await postPromise).toEqual(expectedPost);

    });

    it('should return user feed', async () => {

        const posts$ = postService.getUserFeed();
        const postsPromise = firstValueFrom(posts$);

        const req = httpTesting.expectOne({
            method: 'GET',
            url: `api/posts`
        });

        req.flush(posts);

        expect(await postsPromise).toEqual(posts);

    });

    it('should create post', async () => {

        const formValue = {
            topicId: 1,
            title: 'Les interfaces en Java',
            content: "Découvrez comment créer et utiliser des interfaces en Java pour une programmation orientée objet efficace."
        };

        const post$ = postService.createPost(formValue);

        const postPromise = firstValueFrom(post$);

        const req = httpTesting.expectOne({
            method: 'POST',
            url: `api/posts`
        });

        const newPost: Post = {
            id: 3,
            title: 'Les interfaces en Java',
            content: "Découvrez comment créer et utiliser des interfaces en Java pour une programmation orientée objet efficace.",
            comments: [],
            topic: 'Java',
            author: 'John',
            createdAt: new Date()
        };

        req.flush(newPost);

        expect(req.request.body).toEqual(formValue);
        expect(await postPromise).toEqual(newPost);
    });

    it('should add comment to post', async () => {

        const postId = 1;
        const commentContent = {
            content: 'Super article !'
        };

        const post$ = postService.addCommentToPost(postId, commentContent);
        const postPromise = firstValueFrom(post$);

        const req = httpTesting.expectOne({
            method: 'POST',
            url: `api/posts/${postId}/comments`
        });

        const updatedPost: Post = {
            id: 1,
            title: 'Introduction à Java',
            content: "Java est un langage de programmation populaire utilisé pour le developpement d'applications d'entreprise, mobiles et web.",
            comments: [
                {
                    id: 1,
                    content: 'Super article !',
                    author: 'Alice',
                    createdAt: new Date()
                }
            ],
            topic: 'Java',
            author: 'John',
            createdAt: new Date()
        };

        req.flush(updatedPost);

        expect(req.request.body).toEqual(commentContent);
        expect(await postPromise).toEqual(updatedPost);
    });
})