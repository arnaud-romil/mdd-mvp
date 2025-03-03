import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../core/auth.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceMock: Partial<AuthService>;
  let routerMock: Partial<Router>;
  let matSnackBarMock: Partial<MatSnackBar>;

  beforeEach(async () => {

    authServiceMock = {
      register: jest.fn().mockReturnValue(of({ message: 'Success' })),
      passwordValidator: jest.fn().mockImplementation(() => null)
    };

    routerMock = {
      navigate: jest.fn().mockResolvedValue(true)
    };

    matSnackBarMock = {
      open: jest.fn().mockImplementation(() => { })
    }

    await TestBed.configureTestingModule({
      imports: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: MatSnackBar, useValue: matSnackBarMock }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize registerForm', () => {
    expect(component.registerForm).toBeDefined();
    expect(component.registerForm.get('username')?.value).toBe('');
    expect(component.registerForm.get('email')?.value).toBe('');
    expect(component.registerForm.get('password')?.value).toBe('');
  });

  it('should mark the form as invalid if username is empty', () => {
    component.registerForm.get('username')?.setValue('');
    expect(component.registerForm.valid).toBeFalsy();
  });

  it('should mark the form as invalid if email is empty', () => {
    component.registerForm.get('email')?.setValue('');
    expect(component.registerForm.valid).toBeFalsy();
  });

  it('should mark the form as invalid if password is empty', () => {
    component.registerForm.get('password')?.setValue('');
    expect(component.registerForm.valid).toBeFalsy();
  });

  it('should mark the form as valid if all fields are filled', () => {
    component.registerForm.get('username')?.setValue('user');
    component.registerForm.get('email')?.setValue('user@test.com');
    component.registerForm.get('password')?.setValue('userPassword123!');
    expect(component.registerForm.valid).toBeTruthy();
  });

  it('should allow user to go back', () => {
    jest.spyOn(routerMock, 'navigate');

    component.goBack();

    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  });

  it('should allow user to register', () => {
    jest.spyOn(authServiceMock, 'register');
    jest.spyOn(routerMock, 'navigate');
    jest.spyOn(matSnackBarMock, 'open');

    component.registerForm.get('username')?.setValue('user');
    component.registerForm.get('email')?.setValue('user@test.com');
    component.registerForm.get('password')?.setValue('userPassword123!');
    expect(component.registerForm.valid).toBeTruthy();

    component.register();

    expect(authServiceMock.register).toHaveBeenCalled();
    expect(matSnackBarMock.open).toHaveBeenCalledWith("Inscription réussie ! Redirection vers la connexion...", 'OK', { duration: 3000 });
    setTimeout(() => {
      expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
    }, 3000);
  });



});
