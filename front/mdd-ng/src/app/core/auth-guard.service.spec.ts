import { Router } from "@angular/router";
import { AuthGuardService } from "./auth-guard.service";
import { AuthService } from "./auth.service";
import { TestBed } from "@angular/core/testing";

describe('AuthGuardService', () => {

    let authGuardService: AuthGuardService;
    let authServiceMock: Partial<AuthService>;
    let routerMock: Partial<Router>;

    beforeEach(() => {
        authServiceMock = {
            getToken: jest.fn()
        };
        routerMock = {
            navigate: jest.fn().mockResolvedValue(true)
        };

        TestBed.configureTestingModule({
            providers: [
                { provide: AuthService, useValue: authServiceMock },
                { provide: Router, useValue: routerMock }
            ]
        });

        authGuardService = TestBed.inject(AuthGuardService);
    });

    it('should be created', () => {
        expect(authGuardService).toBeTruthy();
    });

    it('should return true if token exists', () => {
        (authServiceMock.getToken as jest.Mock).mockReturnValue('accessToken');
        expect(authGuardService.canActivate()).toBe(true);
    });

    it('should return false if token does not exist', () => {
        (authServiceMock.getToken as jest.Mock).mockReturnValue('');
        expect(authGuardService.canActivate()).toBe(false);
        expect(routerMock.navigate).toHaveBeenCalledWith(['/login']);
    });

});