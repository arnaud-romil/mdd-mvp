import { Component } from '@angular/core';
import { TopicService } from '../services/topic.service';
import { AsyncPipe } from '@angular/common';
import { TopicComponent } from "../topic/topic.component";
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-topic-list',
  standalone: true,
  imports: [
    MatButtonModule
    /*
    AsyncPipe, 
    TopicComponent
    */
  ],
  templateUrl: './topic-list.component.html',
  styleUrl: './topic-list.component.css'
})
export class TopicListComponent {


  //constructor(private readonly topicService: TopicService) { }

  //topics$ = this.topicService.topics$;

}
