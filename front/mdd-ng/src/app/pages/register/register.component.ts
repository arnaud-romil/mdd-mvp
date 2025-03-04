import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../core/auth.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent implements OnInit {

  registerForm!: FormGroup;
  errorMessage: string = '';

  constructor(
    private readonly fb: FormBuilder,
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly matSnackBar: MatSnackBar
  ) { }

  ngOnInit(): void {

    this.registerForm = this.fb.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, this.authService.passwordValidator]]
    });
  }

  register() {
    if (this.registerForm.valid) {
      this.authService.register(this.registerForm.value).subscribe({
        next: () => {
          this.matSnackBar.open("Inscription réussie ! Redirection vers la connexion...", 'OK', { duration: 3000 });
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 3000);
        },
        error: () => {
          this.errorMessage = "Une erreur s'est produite";
        }
      })
    }
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}
