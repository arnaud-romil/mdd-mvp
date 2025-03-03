import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CreatePostComponent } from './create-post.component';
import { TopicService } from '../../core/topic.service';
import { of } from 'rxjs';
import { PostService } from '../../core/post.service';
import { Router } from '@angular/router';

describe('CreatePostComponent', () => {
  let component: CreatePostComponent;
  let fixture: ComponentFixture<CreatePostComponent>;
  let topicServiceMock: Partial<TopicService>;
  let postServiceMock: Partial<PostService>;
  let routerMock: Partial<Router>;

  beforeEach(async () => {
    topicServiceMock = {
      getTopics: jest.fn().mockReturnValue(of([
        { id: 1, title: 'topic1', description: 'topic1 description', subscribed: true },
        { id: 2, title: 'topic2', description: 'topic2 description', subscribed: false }
      ]))
    };
    postServiceMock = {
      createPost: jest.fn().mockReturnValue(of({
        id: 1,
        topicId: 1,
        title: 'title',
        content: 'content',
        comments: []
      }))
    };
    routerMock = {
      navigate: jest.fn()
    };
    await TestBed.configureTestingModule({
      imports: [CreatePostComponent, BrowserAnimationsModule],
      providers: [
        { provide: TopicService, useValue: topicServiceMock },
        { provide: PostService, useValue: postServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CreatePostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display the title', () => {
    const title = fixture.nativeElement.querySelector('h2');
    expect(title.textContent).toBe('Créer un nouvel article');
  })

  it('should initialize postForm', () => {
    expect(component.postForm).toBeDefined();
    expect(component.postForm.get('title')?.value).toBe('');
    expect(component.postForm.get('content')?.value).toBe('');
    expect(component.postForm.get('topicId')?.value).toBe('');
  });

  it('should mark the form as invalid if title is empty', () => {
    component.postForm.get('title')?.setValue('');
    expect(component.postForm.valid).toBeFalsy();
  });

  it('should mark the form as invalid if content is empty', () => {
    component.postForm.get('content')?.setValue('');
    expect(component.postForm.valid).toBeFalsy();
  });

  it('should mark the form as invalid if topicId is empty', () => {
    component.postForm.get('topicId')?.setValue('');
    expect(component.postForm.valid).toBeFalsy();
  });

  it('should mark the form as valid if all fields are filled', () => {
    component.postForm.get('title')?.setValue('title');
    component.postForm.get('content')?.setValue('content');
    component.postForm.get('topicId')?.setValue('topicId');
    expect(component.postForm.valid).toBeTruthy();
  });

  it('should load topics on init', () => {
    component.ngOnInit();
    expect(topicServiceMock.getTopics).toHaveBeenCalled();
    expect(component.topics.length).toBe(1);
    expect(component.topics[0]).toEqual({ id: 1, title: 'topic1', description: 'topic1 description', subscribed: true });
  });

  it('should create post', () => {
    jest.spyOn(component, 'goBack');
    component.postForm.get('title')?.setValue('title');
    component.postForm.get('content')?.setValue('content');
    component.postForm.get('topicId')?.setValue(1);

    component.submitPost();

    expect(postServiceMock.createPost).toHaveBeenCalledWith({ topicId: 1, title: 'title', content: 'content' });
    expect(component.goBack).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/feed']);
  });



});
