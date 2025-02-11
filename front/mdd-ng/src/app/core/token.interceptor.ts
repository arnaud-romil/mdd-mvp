import { HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Router } from "@angular/router";
import { AuthService } from "./auth.service";
import { catchError, throwError } from "rxjs";

export class TokenInterceptor implements HttpInterceptor {

    constructor(private readonly authService: AuthService, private readonly router: Router) { }

    intercept(req: HttpRequest<any>, next: HttpHandler) {
        const token = this.authService.getToken;

        if (token) {
            req = req.clone({
                setHeaders: { Authorization: `Bearer ${token}` }
            });
        }

        return next.handle(req).pipe(
            catchError((error) => {
                if (error instanceof HttpErrorResponse && error.status === 401) {
                    // this.authService.logout();
                    this.router.navigate(['/login']);
                }
                return throwError(() => error);
            })
        );

    }


}