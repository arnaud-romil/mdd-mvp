import { ComponentFixture, TestBed } from "@angular/core/testing";
import { CreatePostComponent } from "../create-post/create-post.component";
import { Router } from "@angular/router";
import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { provideHttpClient } from "@angular/common/http";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { Topic } from "../../models/topic.interface";
import { By } from "@angular/platform-browser";
import { ReactiveFormsModule } from "@angular/forms";
import { MatSelect, MatSelectModule } from "@angular/material/select";
import { MatOptionModule } from "@angular/material/core";
import { MatFormFieldModule } from "@angular/material/form-field";
import { PostService } from "../../core/post.service";

describe('Post creation integration test', () => {
    let component: CreatePostComponent;
    let fixture: ComponentFixture<CreatePostComponent>;
    let router: Router;
    let httpTesting: HttpTestingController;
    let postService: PostService;

    const topics: Topic[] = [
        { id: 1, title: 'topic1', description: 'topic1 description', subscribed: true },
        { id: 2, title: 'topic2', description: 'topic2 description', subscribed: false }
    ];

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [
                CreatePostComponent,
                NoopAnimationsModule,
                ReactiveFormsModule,
                MatFormFieldModule,
                MatSelectModule,
                MatOptionModule
            ],
            providers: [
                provideHttpClient(),
                provideHttpClientTesting()
            ]

        }).compileComponents();

        fixture = TestBed.createComponent(CreatePostComponent);
        component = fixture.componentInstance;
        httpTesting = TestBed.inject(HttpTestingController);
        router = TestBed.inject(Router);
        postService = TestBed.inject(PostService);
        fixture.detectChanges();

        const req = httpTesting.expectOne({
            method: 'GET',
            url: 'api/topics'
        });

        req.flush(topics);
    });

    afterEach(() => {
        httpTesting.verify();
    });

    it('should create the component', () => {
        expect(component).toBeTruthy();
    });

    it('should create post', async () => {

        jest.spyOn(router, 'navigate');
        jest.spyOn(postService, 'createPost');

        // Set topic
        const selectDebugElement = fixture.debugElement.query(By.directive(MatSelect));
        const selectInstance = selectDebugElement.componentInstance;

        // Open select 
        selectInstance.open();
        fixture.detectChanges();
        await fixture.whenStable();

        // get all options
        const options = fixture.debugElement.queryAll(By.css('mat-option'));
        expect(options.length).toBe(2);

        // Click on the second option (topic 1)
        options[1].nativeElement.click();
        fixture.detectChanges();
        await fixture.whenStable();

        expect(component.postForm.value.topicId).toBe(1);

        // Set title
        const titleInput = fixture.debugElement.query(By.css('input[formcontrolname="title"]'));
        titleInput.nativeElement.value = 'New Post';
        titleInput.nativeElement.dispatchEvent(new Event('input'));

        // Set content
        const contentInput = fixture.debugElement.query(By.css('textarea[formcontrolname="content"]'));
        contentInput.nativeElement.value = 'This is a new post!';
        contentInput.nativeElement.dispatchEvent(new Event('input'));

        expect(component.postForm.valid).toBe(true);

        fixture.detectChanges();

        // Submit form
        const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
        submitButton.click();

        const req = httpTesting.expectOne({
            method: 'POST',
            url: 'api/posts'
        });

        req.flush({
            id: 1,
            title: 'New Post',
            content: 'This is a new post!',
            topic: 'topic1',
            author: 'john',
            comments: [],
            createdAt: new Date()
        });

        expect(postService.createPost).toHaveBeenCalled();
        expect(router.navigate).toHaveBeenCalledWith(['/feed']);
    });

});