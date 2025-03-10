import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../core/auth.service';
import { Router } from '@angular/router';
import { take } from 'rxjs';
import { TopicsComponent } from "../topics/topics.component";
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProfileUpdateRequest } from '../../models/profile-update-request.interface';


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
      password: [null],
      newPassword: [null]
    });

    this.loadUserData();

    this.profileForm.get('updatePassword')?.valueChanges.subscribe((checked) => {
      this.togglePasswordValidators(checked);
    });
  }

  togglePasswordValidators(checked: boolean) {

    const passwordCtrl = this.profileForm.get('password');
    const newPasswordCtrl = this.profileForm.get('newPassword');

    if (checked) {
      passwordCtrl?.setValidators([Validators.required, this.authService.passwordValidator]);
      newPasswordCtrl?.setValidators([Validators.required, this.authService.passwordValidator]);
    } else {
      passwordCtrl?.clearValidators();
      newPasswordCtrl?.clearValidators();
    }

    passwordCtrl?.updateValueAndValidity();
    newPasswordCtrl?.updateValueAndValidity();
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

      const profileUpdate: ProfileUpdateRequest = {
        username: this.profileForm.get('username')?.value,
        email: this.profileForm.get('email')?.value,
        password: this.profileForm.get('updatePassword')?.value ? this.profileForm.get('password')?.value : null,
        newPassword: this.profileForm.get('updatePassword')?.value ? this.profileForm.get('newPassword')?.value : null
      };

      this.authService.updateUserProfile(profileUpdate)
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
