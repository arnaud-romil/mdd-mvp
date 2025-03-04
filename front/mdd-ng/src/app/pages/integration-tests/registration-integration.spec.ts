import { ComponentFixture, TestBed } from "@angular/core/testing";
import { RegisterComponent } from "../register/register.component";
import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { provideHttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { MatSnackBar } from "@angular/material/snack-bar";

describe('Registration integration test suite', () => {
    let component: RegisterComponent;
    let fixture: ComponentFixture<RegisterComponent>;
    let httpTesting: HttpTestingController;
    let router: Router;
    let matSnackBar: MatSnackBar;

    let usernameInput: HTMLInputElement;
    let emailInput: HTMLInputElement;
    let passwordInput: HTMLInputElement;
    let submitButton: HTMLButtonElement;

    function initHTMLElements(fixture: ComponentFixture<RegisterComponent>) {
        usernameInput = fixture.nativeElement.querySelector('input[formcontrolname="username"]');
        emailInput = fixture.nativeElement.querySelector('input[formcontrolname="email"]');
        passwordInput = fixture.nativeElement.querySelector('input[formcontrolname="password"]');
        submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    }

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [RegisterComponent],
            providers: [
                provideHttpClient(),
                provideHttpClientTesting()
            ]

        }).compileComponents();

        fixture = TestBed.createComponent(RegisterComponent);
        component = fixture.componentInstance;
        httpTesting = TestBed.inject(HttpTestingController);
        router = TestBed.inject(Router);
        matSnackBar = TestBed.inject(MatSnackBar);
        fixture.detectChanges();
        initHTMLElements(fixture);
    });

    afterEach(() => {
        httpTesting.verify();
    });

    it('should create the register component', () => {
        expect(component).toBeTruthy();
    });

    it('should enable submit button when inputs are valid', () => {

        // provide valid inputs 
        usernameInput.value = 'user';
        usernameInput.dispatchEvent(new Event('input'));
        emailInput.value = 'user@email.com';
        emailInput.dispatchEvent(new Event('input'));
        passwordInput.value = 'userPassword123!';
        passwordInput.dispatchEvent(new Event('input'));

        fixture.detectChanges();

        // the form is valid and the submit button is enabled
        expect(component.registerForm.valid).toBe(true);
        expect(submitButton.disabled).toBe(false);
    });

    it('should disable submit button when username is empty', () => {

        usernameInput.value = ''; //login is empty
        usernameInput.dispatchEvent(new Event('input'));
        emailInput.value = 'user@email.com';
        emailInput.dispatchEvent(new Event('input'));
        passwordInput.value = 'userPassword123!';
        passwordInput.dispatchEvent(new Event('input'));

        fixture.detectChanges();

        expect(component.registerForm.valid).toBe(false);
        expect(submitButton.disabled).toBe(true); // submit button is disabled
    });

    it('should allow user to register', () => {

        jest.spyOn(router, 'navigate');
        jest.spyOn(matSnackBar, 'open');

        usernameInput.value = 'user';
        usernameInput.dispatchEvent(new Event('input'));
        emailInput.value = 'user@email.com';
        emailInput.dispatchEvent(new Event('input'));
        passwordInput.value = 'userPassword123!';
        passwordInput.dispatchEvent(new Event('input'));

        fixture.detectChanges();

        submitButton.click();

        const req = httpTesting.expectOne({
            method: 'POST',
            url: 'api/auth/register'
        });

        req.flush({ message: 'Success' });

        fixture.detectChanges();

        expect(matSnackBar.open).toHaveBeenCalledWith("Inscription réussie ! Redirection vers la connexion...", 'OK', { duration: 3000 });
        setTimeout(() => {
            expect(router.navigate).toHaveBeenCalledWith(['/login']);
        }, 3000);
    });

})