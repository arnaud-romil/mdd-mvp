import { Component, Input } from '@angular/core';
import { Topic } from '../models/topic.interface';

@Component({
  selector: 'app-topic',
  standalone: true,
  imports: [],
  templateUrl: './topic.component.html',
  styleUrl: './topic.component.css'
})
export class TopicComponent {
  @Input() topic!: Topic;
}
