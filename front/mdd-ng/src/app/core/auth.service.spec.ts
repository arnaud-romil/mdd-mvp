import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { AuthService } from "./auth.service";
import { TestBed } from "@angular/core/testing";
import { provideHttpClient } from "@angular/common/http";
import { AccessToken } from "../models/access-token.interface";
import { firstValueFrom } from "rxjs";

describe('AuthService', () => {

    let authService: AuthService;
    let httpTesting: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [
                provideHttpClient(),
                provideHttpClientTesting()
            ]
        });
        authService = TestBed.inject(AuthService);
        httpTesting = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        httpTesting.verify();
    });

    it('should be created', () => {
        expect(authService).toBeTruthy();
    });

    it('should allow login', async () => {

        const credentials = {
            login: 'alice@test.com',
            password: 'alicePassword123!'
        };

        const expectedToken: AccessToken = {
            accessToken: "newAccessToken"
        };

        const accessToken$ = authService.login(credentials);
        const accessTokenPromise = firstValueFrom(accessToken$);

        const req = httpTesting.expectOne({
            method: 'POST',
            url: 'api/auth/login'
        });

        req.flush(expectedToken);

        expect(req.request.body).toEqual(credentials);
        expect(await accessTokenPromise).toEqual(expectedToken);
        expect(authService.getToken()).toEqual(expectedToken.accessToken);
    });

    it('should allow registration', async () => {

        const registerRequest = {
            email: 'alice@test.com',
            username: 'Alice',
            password: 'alicePassword123!'
        };

        const expectedMessage = {
            message: 'success'
        };

        const message$ = authService.register(registerRequest);
        const messagePromise = firstValueFrom(message$);

        const req = httpTesting.expectOne({
            method: 'POST',
            url: 'api/auth/register'
        });

        req.flush(expectedMessage);

        expect(req.request.body).toEqual(registerRequest);
        expect(await messagePromise).toEqual(expectedMessage);
    });

    it('should allow fetching user', async () => {

        const expectedUser = {
            email: 'alice@test.com',
            username: 'Alice',
            topics: []
        };

        const user$ = authService.fetchUser();
        const userPromise = firstValueFrom(user$);

        const req = httpTesting.expectOne({
            method: 'GET',
            url: 'api/auth/me'
        });

        req.flush(expectedUser);

        expect(await userPromise).toEqual(expectedUser);
    });

    it('should allow updating user profile', async () => {

        const userProfile = {
            email: 'alice-updated@test.com',
            username: 'Alice Updated'
        };

        const userProfile$ = authService.updateUserProfile(userProfile);
        const userProfilePromise = firstValueFrom(userProfile$);

        const req = httpTesting.expectOne({
            method: 'PUT',
            url: 'api/auth/me'
        });

        req.flush({}, { status: 204, statusText: 'No Content' });

        expect(req.request.body).toEqual(userProfile);
        expect(authService.getToken()).toEqual('');
        expect(await userProfilePromise).toBeTruthy();
    });

    it('should allow refreshing token', async () => {

        const expectedToken: AccessToken = {
            accessToken: "newAccessToken"
        };

        const accessToken$ = authService.refreshToken();
        const accessTokenPromise = firstValueFrom(accessToken$);

        const req = httpTesting.expectOne({
            method: 'POST',
            url: 'api/auth/refresh-token'
        });

        req.flush(expectedToken);

        expect(await accessTokenPromise).toEqual(expectedToken);
        expect(authService.getToken()).toEqual(expectedToken.accessToken);
    });

    it('should allow logout', async () => {

        authService.logout();

        const req = httpTesting.expectOne({
            method: 'POST',
            url: 'api/auth/logout'
        });

        req.flush({}, { status: 204, statusText: 'No Content' });

        expect(authService.getToken()).toEqual('');
    });

    it('should validate password', () => {

        // Password has no digit
        let password = {
            value: 'userPassword!'
        };
        let passwordError = authService.passwordValidator(password as any);
        expect(passwordError).toBeTruthy();

        // Password has no uppercase letter
        password = {
            value: 'userpassword1!'
        };
        passwordError = authService.passwordValidator(password as any);
        expect(passwordError).toBeTruthy();

        // Password has no lowercase letter
        password = {
            value: 'USERPASSWORD1!'
        };
        passwordError = authService.passwordValidator(password as any);
        expect(passwordError).toBeTruthy();

        // Password has no special character
        password = {
            value: 'UserPassword1'
        };
        passwordError = authService.passwordValidator(password as any);
        expect(passwordError).toBeTruthy();

        // Password has less than 8 characters
        password = {
            value: 'UserP1!'
        };
        passwordError = authService.passwordValidator(password as any);
        expect(passwordError).toBeTruthy();

        // Password has all required characters
        password = {
            value: 'UserPassword1!'
        };
        passwordError = authService.passwordValidator(password as any);
        expect(passwordError).toBeNull();
    });

});