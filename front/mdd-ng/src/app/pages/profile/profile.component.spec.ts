import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProfileComponent } from './profile.component';
import { AuthService } from '../../core/auth.service';
import { User } from '../../models/user.interface';
import { of } from 'rxjs';
import { TopicService } from '../../core/topic.service';
import { UserService } from '../../core/user.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let authServiceMock: Partial<AuthService>;
  let topicServiceMock: Partial<TopicService>;
  let userServiceMock: Partial<UserService>;
  let routerMock: Partial<Router>;
  let matSnackBarMock: Partial<MatSnackBar>;

  const user: User = {
    username: 'alice',
    email: 'alice@test.com',
    topics: []
  };

  beforeEach(async () => {

    authServiceMock = {
      passwordValidator: jest.fn().mockImplementation(() => null),
      fetchUser: jest.fn().mockReturnValue(of(user)),
      logout: jest.fn().mockImplementation(() => { }),
      updateUserProfile: jest.fn().mockReturnValue(of(void 0))
    };

    topicServiceMock = {
      getTopics: jest.fn().mockReturnValue(of([]))
    };

    userServiceMock = {
      addSubscription: jest.fn(),
      removeSubscription: jest.fn()
    };

    routerMock = {
      navigate: jest.fn().mockResolvedValue(true)
    };

    matSnackBarMock = {
      open: jest.fn().mockImplementation(() => { })
    }

    await TestBed.configureTestingModule({
      imports: [ProfileComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: TopicService, useValue: topicServiceMock },
        { provide: UserService, useValue: userServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: MatSnackBar, useValue: matSnackBarMock }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize profile form', () => {
    jest.spyOn(authServiceMock, 'fetchUser');

    expect(component.profileForm).toBeDefined();
    expect(component.profileForm.get('username')?.value).toBe(user.username);
    expect(component.profileForm.get('email')?.value).toBe(user.email);
    expect(authServiceMock.fetchUser).toHaveBeenCalled();
  });

  it('should allow user to log out', () => {
    jest.spyOn(authServiceMock, 'logout');
    jest.spyOn(routerMock, 'navigate');

    component.logout();

    expect(authServiceMock.logout).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should allow user to save profile', () => {
    jest.spyOn(authServiceMock, 'updateUserProfile');
    jest.spyOn(matSnackBarMock, 'open');
    jest.spyOn(routerMock, 'navigate');

    component.profileForm.get('username')?.setValue('aliceNewUsername');
    component.profileForm.get('email')?.setValue('alice@test.com');
    expect(component.profileForm.valid).toBeTruthy();

    component.saveProfile();


    expect(authServiceMock.updateUserProfile).toHaveBeenCalled();
    expect(matSnackBarMock.open).toHaveBeenCalledWith("Une reconnexion est nécessaire ! Déconnexion en cours ...", 'OK', { duration: 3000 });
    setTimeout(() => {
      expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
    }, 3000);
  });

});
