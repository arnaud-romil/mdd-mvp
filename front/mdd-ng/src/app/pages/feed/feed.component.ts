import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { PostService } from '../../core/post.service';
import { RouterModule } from '@angular/router';
import { Post } from '../../models/post.interface';
import { take } from 'rxjs';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './feed.component.html',
  styleUrl: './feed.component.scss'
})
export class FeedComponent implements OnInit {

  sortByPostDateDesc: boolean = true;

  posts: Post[] = [];

  constructor(private readonly postService: PostService) { }

  ngOnInit(): void {
    this.postService.getUserFeed()
      .pipe(take(1))
      .subscribe(
        (posts) => {
          this.posts = posts;
        }
      );
  }

  toggleSort(): void {
    this.sortByPostDateDesc = !this.sortByPostDateDesc;
    if (this.sortByPostDateDesc) {
      this.posts.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
    }
    else {
      this.posts.sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime());
    }
  }
}
