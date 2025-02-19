import { Component } from '@angular/core';
import { TopicService } from '../../core/topic.service';
import { AsyncPipe, CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { Topic } from '../../models/topic.interface';
import { UserService } from '../../core/user.service';
import { take } from 'rxjs';


@Component({
  selector: 'app-topics',
  standalone: true,
  imports: [CommonModule, MatCardModule, AsyncPipe],
  templateUrl: './topics.component.html',
  styleUrl: './topics.component.scss'
})
export class TopicsComponent {

  topics$ = this.topicService.getTopics();

  constructor(private readonly topicService: TopicService, private readonly userService: UserService) { }

  subscribe(topic: Topic): void {
    this.userService.addSubscription(topic)
      .pipe(
        take(1)
      ).subscribe({
        next: () => topic.subscribed = true
      });
  }
}
