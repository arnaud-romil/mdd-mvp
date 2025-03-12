import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PostDetailComponent } from './post-detail.component';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { PostService } from '../../core/post.service';
import { Comment } from '../../models/comment.interface';
import { Post } from '../../models/post.interface';

describe('PostDetailComponent', () => {
  let component: PostDetailComponent;
  let fixture: ComponentFixture<PostDetailComponent>;
  let postServiceMock: Partial<PostService>;
  let routerMock: Partial<Router>;
  let activatedRouteMock: any;

  const post: Post = {
    id: 1,
    title: 'title1',
    content: 'content1',
    topic: 'topic1',
    author: 'author1',
    comments: [],
    createdAt: new Date()
  }

  beforeEach(async () => {

    activatedRouteMock = {
      params: of({ id: '123' }), // Mock route parameters
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('123')
        }
      }
    };

    postServiceMock = {
      getPostById: jest.fn().mockReturnValue(of(post)),
      addCommentToPost: jest.fn()
    };

    routerMock = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [PostDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: activatedRouteMock
        },
        { provide: PostService, useValue: postServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    })
      .compileComponents();
    fixture = TestBed.createComponent(PostDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize post', () => {
    jest.spyOn(postServiceMock, 'getPostById');
    jest.spyOn(routerMock, 'navigate');

    component.ngOnInit();

    expect(component.post).toEqual(post);
    expect(postServiceMock.getPostById).toHaveBeenCalledWith(123);
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });

  it('should redirect to not found component when post id is NaN', () => {
    jest.spyOn(routerMock, 'navigate');
    (activatedRouteMock.snapshot?.paramMap?.get as jest.Mock).mockReturnValue('test');

    component.ngOnInit();

    expect(routerMock.navigate).toHaveBeenCalledWith(['/not-found']);

  });

  it('should allow user to add a comment', () => {
    const comment: Comment = {
      id: 1,
      content: 'This is a new comment',
      author: 'author',
      createdAt: new Date()
    }
    post.comments.push(comment);
    jest.spyOn(postServiceMock, 'addCommentToPost').mockReturnValue(of(post));

    component.newComment = 'This is a new comment';

    component.addComment();

    expect(component.post).toEqual(post);
    expect(component.newComment).toBeFalsy();
  });

  it('should allow user to go back', () => {
    jest.spyOn(routerMock, 'navigate');
    component.goBack();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/feed']);
  });

});
