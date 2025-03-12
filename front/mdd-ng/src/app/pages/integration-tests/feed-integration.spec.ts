import { ComponentFixture, TestBed } from "@angular/core/testing";
import { FeedComponent } from "../feed/feed.component";
import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { provideHttpClient } from "@angular/common/http";
import { provideRouter } from "@angular/router";
import { Post } from "../../models/post.interface";
import { By } from "@angular/platform-browser";


describe('User feed integration test suite', () => {
    let component: FeedComponent;
    let fixture: ComponentFixture<FeedComponent>;
    let httpTesting: HttpTestingController;

    const posts: Post[] = [
        {
            id: 1,
            title: 'Introduction à Java',
            content: "Java est un langage de programmation populaire utilisé pour le developpement d'applications d'entreprise, mobiles et web.",
            comments: [],
            topic: 'Java',
            author: 'John',
            createdAt: new Date(2025, 1, 15)
        },
        {
            id: 2,
            title: 'Les classes et objets en Java',
            content: "Découvrez comment créer et utiliser des classes et objets en Java pour une programmation orientée objet efficace.",
            comments: [],
            topic: 'Java',
            author: 'John',
            createdAt: new Date(2025, 1, 10)
        }
    ];

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [FeedComponent],
            providers: [
                provideHttpClient(),
                provideHttpClientTesting(),
                provideRouter([])
            ]
        }).compileComponents();


        fixture = TestBed.createComponent(FeedComponent);
        component = fixture.componentInstance;
        httpTesting = TestBed.inject(HttpTestingController);
        fixture.detectChanges();

        const req = httpTesting.expectOne({
            method: 'GET',
            url: 'api/posts'
        });

        req.flush(posts);
    });

    afterEach(() => {
        httpTesting.verify();
    });

    it('should create the feed component', () => {
        expect(component).toBeTruthy();
    });

    it('should initialize posts', () => {
        fixture.detectChanges();
        const postCards = fixture.debugElement.queryAll(By.css('.post'));
        expect(postCards.length).toBe(posts.length);
        expect(component.posts).toEqual(posts);
    });

    it('should toggle sort by date', () => {
        expect(component.posts).toEqual(posts);
        expect(component.sortByPostDateDesc).toBe(true);

        component.toggleSort();

        expect(component.sortByPostDateDesc).toBe(false);
        expect(component.posts[0].id).toBe(2);
        expect(component.posts[1].id).toBe(1);
    });

});