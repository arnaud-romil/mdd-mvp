import { ComponentFixture, TestBed } from "@angular/core/testing";
import { LoginComponent } from "../login/login.component";
import { provideHttpClient } from "@angular/common/http";
import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { Router } from "@angular/router";

describe('Login integration test suite', () => {
    let component: LoginComponent;
    let fixture: ComponentFixture<LoginComponent>;
    let router: Router;
    let httpTesting: HttpTestingController;

    let loginInput: HTMLInputElement;
    let passwordInput: HTMLInputElement;
    let submitButton: HTMLButtonElement;

    function initHTMLElements(fixture: ComponentFixture<LoginComponent>) {
        loginInput = fixture.nativeElement.querySelector('input[formcontrolname="login"]');
        passwordInput = fixture.nativeElement.querySelector('input[formcontrolname="password"]');
        submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    }

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [LoginComponent],
            providers: [
                provideHttpClient(),
                provideHttpClientTesting()
            ]

        }).compileComponents();


        fixture = TestBed.createComponent(LoginComponent);
        component = fixture.componentInstance;
        httpTesting = TestBed.inject(HttpTestingController);
        router = TestBed.inject(Router);
        fixture.detectChanges();
        initHTMLElements(fixture);
    });

    afterEach(() => {
        httpTesting.verify();
    });

    it('should create the login component', () => {
        expect(component).toBeTruthy();
    });

    it('should enable submit button when credentials are valid', () => {

        // provide valid inputs for email and password
        loginInput.value = 'user@email.com';
        loginInput.dispatchEvent(new Event('input'));
        passwordInput.value = 'userPassword123!';
        passwordInput.dispatchEvent(new Event('input'));

        fixture.detectChanges();

        // the form is valid and the submit button is enabled
        expect(component.loginForm.valid).toBe(true);
        expect(submitButton.disabled).toBe(false);
    });


    it('should disable submit button when login is empty', () => {

        loginInput.value = ''; //login is empty
        loginInput.dispatchEvent(new Event('input'));
        passwordInput.value = 'userPassword123!';
        passwordInput.dispatchEvent(new Event('input'));

        fixture.detectChanges();

        expect(component.loginForm.valid).toBe(false);
        expect(submitButton.disabled).toBe(true); // submit button is disabled
    });

    it('should allow user to login', () => {

        jest.spyOn(router, 'navigate');

        loginInput.value = 'user@email.com';
        loginInput.dispatchEvent(new Event('input'));
        passwordInput.value = 'userPassword123!';
        passwordInput.dispatchEvent(new Event('input'));

        fixture.detectChanges();

        submitButton.click();

        const req = httpTesting.expectOne({
            method: 'POST',
            url: 'api/auth/login'
        });

        req.flush({ accessToken: 'accessToken' });

        fixture.detectChanges();

        expect(router.navigate).toHaveBeenCalledWith(['/feed']);
        expect(component.errorMessage).toBeFalsy();
    });

    it('should display error message on invalid credentials', () => {

        jest.spyOn(router, 'navigate');

        loginInput.value = 'user@email.com';
        loginInput.dispatchEvent(new Event('input'));
        passwordInput.value = 'userPassword123!';
        passwordInput.dispatchEvent(new Event('input'));

        fixture.detectChanges();

        submitButton.click();

        const req = httpTesting.expectOne({
            method: 'POST',
            url: 'api/auth/login'
        });

        req.flush({}, { status: 401, statusText: 'Bad credentials' });

        fixture.detectChanges();

        const errorParagraph = fixture.nativeElement.querySelector('p[class="error-message"]');

        expect(errorParagraph.textContent).toBe("Une erreur s'est produite");
        expect(router.navigate).not.toHaveBeenCalled();
    })




});