import { Router } from "@angular/router";
import { TokenInterceptorService } from "./token-interceptor.service";
import { TestBed } from "@angular/core/testing";
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from "@angular/common/http";
import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { AuthService } from "./auth.service";
import { firstValueFrom } from "rxjs";

describe('TokenInterceptorService', () => {

    let authService: AuthService;
    let routerMock: Partial<Router>;
    let httpTesting: HttpTestingController;

    beforeEach(() => {

        routerMock = {
            navigate: jest.fn().mockResolvedValue(true)
        };

        TestBed.configureTestingModule({
            providers: [
                { provide: Router, useValue: routerMock },
                provideHttpClient(withInterceptorsFromDi()),
                provideHttpClientTesting(),
                { provide: HTTP_INTERCEPTORS, useClass: TokenInterceptorService, multi: true }
            ]
        });
        authService = TestBed.inject(AuthService);
        httpTesting = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        httpTesting.verify();
    });

    it('should add token to request headers', () => {

        jest.spyOn(authService, 'getToken').mockReturnValue('accessToken');

        firstValueFrom(authService.fetchUser());

        const req = httpTesting.expectOne({ method: 'GET', url: 'api/auth/me' });

        req.flush({}, { status: 200, statusText: 'OK' });

        expect(req.request.headers.get('Authorization')).toBe('Bearer accessToken');

    });

    it('should not add token to request headers', () => {

        jest.spyOn(authService, 'getToken').mockReturnValue('');

        firstValueFrom(authService.login({ login: 'login', password: 'password' }));

        const req = httpTesting.expectOne({ method: 'POST', url: 'api/auth/login' });

        req.flush({}, { status: 200, statusText: 'OK' });

        expect(req.request.headers.get('Authorization')).toBeNull();

    });

    it('should handle 401 error', () => {

        jest.spyOn(authService, 'getToken')
            .mockReturnValueOnce('accessToken')
            .mockReturnValueOnce('newAccessToken');

        firstValueFrom(authService.fetchUser());

        let req = httpTesting.expectOne({ method: 'GET', url: 'api/auth/me' });

        expect(req.request.headers.get('Authorization')).toBe('Bearer accessToken');

        req.flush({}, { status: 401, statusText: 'Unauthorized' });

        req = httpTesting.expectOne({ method: 'POST', url: 'api/auth/refresh-token' });

        req.flush({ accessToken: 'newAccessToken' }, { status: 200, statusText: 'OK' });

        req = httpTesting.expectOne({ method: 'GET', url: 'api/auth/me' });

        req.flush({}, { status: 200, statusText: 'OK' });

        expect(req.request.headers.get('Authorization')).toBe('Bearer newAccessToken');

    });

    it('should handle 401 error and logout', () => {

        jest.spyOn(authService, 'getToken').mockReturnValue('accessToken');
        jest.spyOn(authService, 'logout').mockImplementation(() => { });

        firstValueFrom(authService.fetchUser());

        let req = httpTesting.expectOne({ method: 'GET', url: 'api/auth/me' });

        expect(req.request.headers.get('Authorization')).toBe('Bearer accessToken');

        req.flush({}, { status: 401, statusText: 'Unauthorized' });

        req = httpTesting.expectOne({ method: 'POST', url: 'api/auth/refresh-token' });

        req.flush({}, { status: 401, statusText: 'Unauthorized' });

        expect(authService.logout).toHaveBeenCalled();
        expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
    });

    it('should handle 403 error', () => {

        firstValueFrom(authService.fetchUser());

        const req = httpTesting.expectOne({ method: 'GET', url: 'api/auth/me' });

        req.flush({}, { status: 403, statusText: 'Forbidden' });

        expect(routerMock.navigate).toHaveBeenCalledWith(['/forbidden']);

    });

    it('should handle 404 error', () => {

        firstValueFrom(authService.fetchUser());

        const req = httpTesting.expectOne({ method: 'GET', url: 'api/auth/me' });

        req.flush({}, { status: 404, statusText: 'Not Found' });

        expect(routerMock.navigate).toHaveBeenCalledWith(['/not-found']);
    });

});