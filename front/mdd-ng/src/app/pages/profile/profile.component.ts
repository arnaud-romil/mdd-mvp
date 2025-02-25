import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../core/auth.service';
import { UserService } from '../../core/user.service';
import { Router } from '@angular/router';
import { take } from 'rxjs';
import { TopicsComponent } from "../topics/topics.component";


@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TopicsComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {

  profileForm!: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly authService: AuthService,
    private readonly userService: UserService,
    private readonly router: Router
  ) { }

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]]
    });

    this.loadUserData();
  }

  loadUserData(): void {
    this.authService.fetchUser().pipe(take(1))
      .subscribe((user) => {
        this.profileForm.patchValue({
          username: user.username,
          email: user.email
        });
      });
  }

  saveProfile(): void {
    if (this.profileForm.valid) {
      this.authService.updateUserProfile(this.profileForm.value);
      this.router.navigate(['/login']);
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
