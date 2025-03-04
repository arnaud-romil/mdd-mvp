import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeComponent } from './home.component';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({}), // Mock route parameters
            snapshot: {
              paramMap: {
                get: (key: string) => null // Mock snapshot paramMap
              }
            }
          }
        }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display login and register buttons', () => {

    const loginBtn = fixture.debugElement.query(By.css('button[routerLink="/login"]'));
    expect(loginBtn).not.toBeNull();
    expect(loginBtn.nativeElement.textContent.trim()).toEqual("Se connecter");

    const registerBtn = fixture.debugElement.query(By.css('button[routerLink="/register"]'));
    expect(registerBtn).not.toBeNull();
    expect(registerBtn.nativeElement.textContent.trim()).toEqual("S'inscrire");
  });

});
