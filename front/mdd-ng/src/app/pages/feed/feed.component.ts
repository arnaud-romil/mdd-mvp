import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { PostService } from '../../core/post.service';
import { RouterModule } from '@angular/router';
import { Post } from '../../models/post.interface';
import { take } from 'rxjs';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule, MatCardModule, RouterModule],
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.scss'
})
export class FeedComponent implements OnInit {

  posts: Post[] = [];

  constructor(private readonly postService: PostService) { }

  ngOnInit(): void {
    this.postService.getUserFeed()
      .pipe(take(1))
      .subscribe(
        (posts) => this.posts = posts
      );
  }

}
