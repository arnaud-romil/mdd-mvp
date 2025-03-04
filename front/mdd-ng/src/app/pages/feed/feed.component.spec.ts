import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FeedComponent } from './feed.component';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { PostService } from '../../core/post.service';

describe('FeedComponent', () => {
  let component: FeedComponent;
  let fixture: ComponentFixture<FeedComponent>;
  let postServiceMock: Partial<PostService>;

  const posts = [
    { id: 1, topic: 'topic1', title: 'title1', content: 'content1', comments: [], author: 'author1', createdAt: new Date(2025, 1, 15) },
    { id: 2, topic: 'topic2', title: 'title2', content: 'content2', comments: [], author: 'author2', createdAt: new Date(2025, 1, 10) }
  ];

  beforeEach(async () => {

    postServiceMock = {
      getUserFeed: jest.fn().mockReturnValue(of(posts))
    };

    await TestBed.configureTestingModule({
      imports: [FeedComponent],
      providers: [
        { provide: PostService, useValue: postServiceMock },
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({}), // Mock route parameters
            snapshot: {
              paramMap: {
                get: (key: string) => null // Mock snapshot paramMap
              }
            }
          }
        }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FeedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize posts', () => {
    component.ngOnInit();
    expect(postServiceMock.getUserFeed).toHaveBeenCalled();
    expect(component.posts).toEqual(posts);
  });

  it('should toggle sort', () => {
    component.ngOnInit();

    component.toggleSort();
    expect(component.sortByPostDateDesc).toBe(false);
    expect(component.posts[0].id).toBe(2);
    expect(component.posts[1].id).toBe(1);
    component.toggleSort();
    expect(component.sortByPostDateDesc).toBe(true);
    expect(component.posts[0].id).toBe(1);
    expect(component.posts[1].id).toBe(2);
  });


});
