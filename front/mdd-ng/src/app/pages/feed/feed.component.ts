import { AsyncPipe, CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { PostService } from '../../core/post.service';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule, MatCardModule, AsyncPipe],
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.css'
})
export class FeedComponent {

  posts$ = this.postService.getUserFeed();

  constructor(private readonly postService: PostService) { }

}
