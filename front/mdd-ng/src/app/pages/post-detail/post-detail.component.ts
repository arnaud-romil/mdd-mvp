import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { Post } from '../../models/post.interface';
import { ActivatedRoute } from '@angular/router';
import { PostService } from '../../core/post.service';
import { take } from 'rxjs';
import { CommentListComponent } from '../../shared/comment-list/comment-list.component';

@Component({
  selector: 'app-post-detail',
  standalone: true,
  imports: [CommonModule, MatCardModule, CommentListComponent],
  templateUrl: './post-detail.component.html',
  styleUrl: './post-detail.component.css'
})
export class PostDetailComponent implements OnInit {

  post!: Post;

  constructor(private readonly route: ActivatedRoute, private readonly postService: PostService) { }

  ngOnInit(): void {

    const postId = Number(this.route.snapshot.paramMap.get('id'));

    if (postId) {

      this.postService.getPostById(postId)
        .pipe(take(1))
        .subscribe({
          next: (data) => (this.post = data)
        });
    }
  }
}
