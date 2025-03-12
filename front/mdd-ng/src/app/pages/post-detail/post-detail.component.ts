import { CommonModule, DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Post } from '../../models/post.interface';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../../core/post.service';
import { take } from 'rxjs';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-post-detail',
  standalone: true,
  imports: [CommonModule, DatePipe, FormsModule],
  templateUrl: './post-detail.component.html',
  styleUrl: './post-detail.component.scss'
})
export class PostDetailComponent implements OnInit {

  post!: Post | undefined;
  newComment = '';

  constructor(private readonly route: ActivatedRoute, private readonly postService: PostService, private readonly router: Router) { }

  ngOnInit(): void {

    const postId = Number(this.route.snapshot.paramMap.get('id'));

    if (isNaN(postId)) {
      this.router.navigate(['/not-found']);
      return;
    }

    if (postId) {

      this.postService.getPostById(postId)
        .pipe(take(1))
        .subscribe({
          next: (data) => (this.post = data),
          error: () => { this.post = undefined; }
        });
    }
  }

  addComment(): void {
    if (!this.newComment.trim() || !this.post)
      return;

    this.postService.addCommentToPost(this.post.id, { content: this.newComment }).pipe(
      take(1)
    ).subscribe({
      next: (updatedPost) => {
        this.post = updatedPost;
        this.newComment = '';
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/feed']);
  }
}
