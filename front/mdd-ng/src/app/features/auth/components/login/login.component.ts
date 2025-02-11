import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { AuthService } from '../../../../core/auth.service';
import { Router } from '@angular/router';
import { take } from 'rxjs';
import { NgIf } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    NgIf],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  loginForm: FormGroup;

  constructor(private readonly formBuilder: FormBuilder, private readonly authService: AuthService, private readonly router: Router) {
    this.loginForm = this.formBuilder.group({
      login: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).pipe(
        take(1)
      ).subscribe({
        next: () => {
          this.router.navigate(['/feed'])
        },
        error: (err) => {
          console.error('Erreur de connexion', err)
        }
      });
    }
  }

}
