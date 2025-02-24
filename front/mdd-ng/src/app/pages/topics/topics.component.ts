import { Component, OnInit } from '@angular/core';
import { TopicService } from '../../core/topic.service';
import { CommonModule } from '@angular/common';
import { Topic } from '../../models/topic.interface';
import { UserService } from '../../core/user.service';
import { map, take } from 'rxjs';
import { Router } from '@angular/router';


@Component({
  selector: 'app-topics',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './topics.component.html',
  styleUrl: './topics.component.scss'
})
export class TopicsComponent implements OnInit {

  topics: Topic[] = [];

  constructor(private readonly topicService: TopicService, private readonly userService: UserService, private readonly router: Router) { }

  ngOnInit(): void {
    const currentRoute = this.router.url;
    if (currentRoute === '/topics') {
      this.topicService.getTopics()
        .pipe(take(1))
        .subscribe((topics) => this.topics = topics);
    }
    else if (currentRoute === '/profile') {
      this.topicService.getTopics()
        .pipe(
          take(1),
          map(topics => topics.filter((topic) => topic.subscribed))
        )
        .subscribe((topics) => this.topics = topics);
    }
  }

  toggleSubscription(topic: Topic): void {
    if (topic.subscribed) {
      this.unsubscribe(topic);
    }
    else {
      this.subscribe(topic);
    }
  }

  subscribe(topic: Topic): void {
    this.userService.addSubscription(topic.id)
      .pipe(
        take(1)
      ).subscribe({
        next: () => topic.subscribed = true
      });
  }

  unsubscribe(topic: Topic): void {
    this.userService.removeSubscription(topic.id)
      .pipe(
        take(1)
      ).subscribe({
        next: () => {
          topic.subscribed = false;
          if (this.router.url === '/profile') {
            this.topics = this.topics.filter((t) => t.id !== topic.id);
          }
        }
      });
  }
}
