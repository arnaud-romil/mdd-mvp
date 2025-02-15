import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Router } from "@angular/router";
import { AuthService } from "./auth.service";
import { catchError, Observable, switchMap, throwError } from "rxjs";
import { Injectable } from "@angular/core";

@Injectable({ providedIn: 'root' })
export class TokenInterceptor implements HttpInterceptor {

    constructor(private readonly authService: AuthService, private readonly router: Router) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = this.authService.getToken();

        const excludedPaths = ['/refresh-token', '/login', '/register'];
        const isReqExcluded = !excludedPaths.some(path => req.url.includes(path));

        if (token && isReqExcluded) {
            req = req.clone({
                setHeaders: { Authorization: `Bearer ${token}` }
            });
        }

        return next.handle(req).pipe(
            catchError((error) => {
                if (error instanceof HttpErrorResponse && error.status === 401 && isReqExcluded) {
                    return this.handle401Error(req, next);
                }
                return throwError(() => error);
            })
        );
    }

    private handle401Error(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        return this.authService.refreshToken().pipe(
            switchMap(() => {
                const newToken = this.authService.getToken();
                return next.handle(req
                    .clone({
                        setHeaders: {
                            Authorization: `Bearer ${newToken}`
                        }
                    })
                );
            }),
            catchError((err) => {
                this.authService.logout();
                this.router.navigate(['/login']);
                return throwError(() => err);
            })
        )

    }
}