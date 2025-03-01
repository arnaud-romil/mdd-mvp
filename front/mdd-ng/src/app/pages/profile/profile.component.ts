import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../core/auth.service';
import { Router } from '@angular/router';
import { take } from 'rxjs';
import { TopicsComponent } from "../topics/topics.component";
import { MatSnackBar } from '@angular/material/snack-bar';


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
    private readonly router: Router,
    private readonly matSnackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      updatePassword: [false, []],
      password: [null, [this.authService.passwordValidator]],
      newPassword: [null, [this.authService.passwordValidator]]
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

      this.authService.updateUserProfile(this.profileForm.value)
        .pipe(take(1))
        .subscribe({
          next: () => {
            this.matSnackBar.open("Une reconnexion est nécessaire ! Déconnexion en cours ...", 'OK', { duration: 3000 });
            setTimeout(() => {
              this.router.navigate(['/login']);
            }, 3000);
          },
          error: () => { this.matSnackBar.open("Une erreur est survenue !", 'OK', { duration: 3000 }); }
        });
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
