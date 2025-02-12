import { Component, Input, OnInit } from '@angular/core';
import { Comment } from '../../models/comment.interface';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { Post } from '../../models/post.interface';
import { PostService } from '../../core/post.service';
import { map, take, tap } from 'rxjs';

@Component({
  selector: 'app-comment-list',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatInputModule, FormsModule],
  templateUrl: './comment-list.component.html',
  styleUrl: './comment-list.component.css'
})
export class CommentListComponent implements OnInit {

  @Input() post!: Post;

  comments: Comment[] = [];
  newComment = '';

  constructor(private readonly postService: PostService) { }

  ngOnInit(): void {
    this.comments = this.post.comments;
  }

  addComment(): void {
    if (!this.newComment.trim())
      return;

    this.postService.addCommentToPost(this.post.id, { content: this.newComment }).pipe(
      take(1),
      map(updatedPost => updatedPost.comments)
    ).subscribe({
      next: (updatedComments) => {
        this.comments = updatedComments;
        this.newComment = '';
      },
      error: (err) => console.error(err)
    });
  }
}
