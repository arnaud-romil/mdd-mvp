import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { AuthService } from '../../core/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';


describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock: Partial<AuthService>;
  let routerMock: Partial<Router>;

  beforeEach(async () => {

    authServiceMock = {
      login: jest.fn()
    };

    routerMock = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display the title', () => {
    const title = fixture.nativeElement.querySelector('h2');
    expect(title.textContent).toBe('Se connecter');
  });

  it('should initialize loginForm', () => {
    expect(component.loginForm).toBeDefined();
    expect(component.loginForm.get('login')?.value).toBe('');
    expect(component.loginForm.get('password')?.value).toBe('');
  });

  it('should mark the form as invalid if login is empty', () => {
    component.loginForm.get('title')?.setValue('');
    expect(component.loginForm.valid).toBeFalsy();
  });

  it('should mark the form as invalid if password is empty', () => {
    component.loginForm.get('password')?.setValue('');
    expect(component.loginForm.valid).toBeFalsy();
  });

  it('should mark the form as valid if all fields are filled', () => {
    component.loginForm.get('login')?.setValue('alice');
    component.loginForm.get('password')?.setValue('alicePassword123!');
    expect(component.loginForm.valid).toBeTruthy();
  });

  it('should allow user to log in', () => {
    jest.spyOn(authServiceMock, 'login').mockReturnValue(of({ accessToken: 'accessToken' }));
    jest.spyOn(routerMock, 'navigate');

    component.loginForm.get('login')?.setValue('alice');
    component.loginForm.get('password')?.setValue('alicePassword123!');
    component.onSubmit();

    expect(routerMock.navigate).toHaveBeenCalledWith(['/feed']);
  })

  it('should display error message when credentials are invalid', () => {
    jest.spyOn(authServiceMock, 'login')
      .mockReturnValue(
        throwError(() => ({
          status: 401,
          message: 'Unauthorized'
        }))
      );
    jest.spyOn(routerMock, 'navigate');

    component.loginForm.get('login')?.setValue('alice');
    component.loginForm.get('password')?.setValue('wrong-password');
    component.onSubmit();

    expect(component.errorMessage).toEqual("Une erreur s'est produite");
    expect(routerMock.navigate).not.toHaveBeenCalled();
  })

  it('should allow user to go back', () => {
    jest.spyOn(routerMock, 'navigate');
    component.goBack();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  });


});
