import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TopicService } from '../../core/topic.service';
import { PostService } from '../../core/post.service';
import { Router } from '@angular/router';
import { Topic } from '../../models/topic.interface';
import { map, take } from 'rxjs';
import { CommonModule } from '@angular/common';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatSelectModule],
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.scss'
})
export class CreatePostComponent implements OnInit {

  postForm!: FormGroup;
  topics: Topic[] = [];

  constructor(
    private readonly fb: FormBuilder,
    private readonly topicService: TopicService,
    private readonly postService: PostService,
    private readonly router: Router
  ) { }

  ngOnInit(): void {
    this.postForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(5)]],
      content: ['', [Validators.required, Validators.minLength(5)]],
      topicId: ['', Validators.required]
    });

    this.loadTopics();
  }

  loadTopics(): void {
    this.topicService.getTopics()
      .pipe(
        take(1),
        map((topics) => topics.filter((topic) => topic.subscribed))
      )
      .subscribe({
        next: (topics) => this.topics = topics
      });
  }

  submitPost(): void {
    if (this.postForm.valid) {
      this.postService.createPost(this.postForm.value).pipe(take(1))
        .subscribe(() => {
          this.goBack()
        });
    }
  }

  goBack(): void {
    this.router.navigate(['/feed']);
  }
}
