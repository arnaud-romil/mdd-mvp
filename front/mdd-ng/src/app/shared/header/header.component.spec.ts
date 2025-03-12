import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeaderComponent } from './header.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let routerMock: Partial<Router>;

  beforeEach(async () => {

    routerMock = {
      events: of(),
      createUrlTree: jest.fn(),
      serializeUrl: jest.fn(),
      get url() { return '/'; }
    }

    await TestBed.configureTestingModule({
      imports: [HeaderComponent, BrowserAnimationsModule],
      providers: [
        { provide: Router, useValue: routerMock },
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

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set isMobile to false', () => {

    Object.defineProperty(window, 'innerWidth', { writable: true, configurable: true, value: 1280 });

    component.updateIsMobile();

    expect(component.isMobile).toBe(false);
  });

  it('should set isMobile to true', () => {

    Object.defineProperty(window, 'innerWidth', { writable: true, configurable: true, value: 375 });

    component.updateIsMobile();

    expect(component.isMobile).toBe(true);
  });

  it('should not show header on home page', () => {

    Object.defineProperty(window, 'innerWidth', { writable: true, configurable: true, value: 1280 });
    Object.defineProperty(routerMock, 'url', { get: () => '/' });

    component.ngOnInit();
    expect(component.showHeader).toBe(false);
    expect(component.showLinks).toBe(false);

    Object.defineProperty(window, 'innerWidth', { writable: true, configurable: true, value: 375 });

    component.ngOnInit();
    expect(component.showHeader).toBe(false);
    expect(component.showLinks).toBe(false);
  });

  it('should display header without links on login page and on register page', () => {

    Object.defineProperty(window, 'innerWidth', { writable: true, configurable: true, value: 1280 });
    Object.defineProperty(routerMock, 'url', { get: () => '/login' });

    component.ngOnInit();
    expect(component.showHeader).toBe(true);
    expect(component.showLinks).toBe(false);

    Object.defineProperty(routerMock, 'url', { get: () => '/register' });

    component.ngOnInit();
    expect(component.showHeader).toBe(true);
    expect(component.showLinks).toBe(false);
  });

  it('should not display header on login page and register page for mobile devices', () => {

    Object.defineProperty(window, 'innerWidth', { writable: true, configurable: true, value: 375 });
    Object.defineProperty(routerMock, 'url', { get: () => '/login' });

    component.ngOnInit();
    expect(component.showHeader).toBe(false);
    expect(component.showLinks).toBe(false);

    Object.defineProperty(routerMock, 'url', { get: () => '/register' });

    component.ngOnInit();
    expect(component.showHeader).toBe(false);
    expect(component.showLinks).toBe(false);
  });

  it('should display header with links on user feed', () => {

    Object.defineProperty(window, 'innerWidth', { writable: true, configurable: true, value: 1280 });
    Object.defineProperty(routerMock, 'url', { get: () => '/feed' });

    component.ngOnInit();
    expect(component.showHeader).toBe(true);
    expect(component.showLinks).toBe(true);
  });

});
