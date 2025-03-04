import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicsComponent } from './topics.component';
import { TopicService } from '../../core/topic.service';
import { UserService } from '../../core/user.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { Topic } from '../../models/topic.interface';

describe('TopicsComponent', () => {
  let component: TopicsComponent;
  let fixture: ComponentFixture<TopicsComponent>;
  let topicServiceMock: Partial<TopicService>;
  let userServiceMock: Partial<UserService>;
  let routerMock: Partial<Router>;

  const topics: Topic[] = [
    { id: 1, title: 'Java', description: 'Java est un langage de programmation de haut niveau, basé sur les classes et orienté objet.', subscribed: false },
    { id: 2, title: 'Spring', description: 'Spring est un framework puissant et riche en fonctionnalités pour développer des applications Java.', subscribed: false },
    { id: 3, title: 'Python', description: 'Python est un langage de programmation polyvalent et de haut niveau, connu pour sa simplicité et sa lisibilité.', subscribed: false }
  ];

  beforeEach(async () => {

    topicServiceMock = {
      getTopics: jest.fn().mockReturnValue(of(topics))
    };

    userServiceMock = {
      addSubscription: jest.fn().mockReturnValue(of(void 0)),
      removeSubscription: jest.fn().mockReturnValue(of(void 0))
    }

    routerMock = {
      navigate: jest.fn().mockResolvedValue(true),
      url: '/topics'
    };

    await TestBed.configureTestingModule({
      imports: [TopicsComponent],
      providers: [
        { provide: TopicService, useValue: topicServiceMock },
        { provide: UserService, useValue: userServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(TopicsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize topics', () => {

    jest.spyOn(topicServiceMock, 'getTopics');

    expect(topicServiceMock.getTopics).toHaveBeenCalled();
    expect(component.topics).toEqual(topics);
  });

  it('should allow user to subscribe to a topic', () => {

    jest.spyOn(userServiceMock, 'addSubscription');

    component.toggleSubscription(topics[0]);

    expect(userServiceMock.addSubscription).toHaveBeenCalledWith(topics[0].id);
    expect(topics[0].subscribed).toBe(true);
  });

  it('should allow user to unsubscribe to a topic', () => {

    jest.spyOn(userServiceMock, 'removeSubscription');

    topics[0].subscribed = true;
    component.toggleSubscription(topics[0]);

    expect(userServiceMock.removeSubscription).toHaveBeenCalledWith(topics[0].id);
    expect(topics[0].subscribed).toBe(false);
  });
});
