import { Injectable } from '@angular/core';
import {
  HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ApiService } from '../services/api.service';
import { environment } from '../../environments/environment';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
    private apiService: ApiService
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const apiBase = environment.apiUrl; // ex: http://localhost:8080/api
    const isApiCall = req.url.startsWith(apiBase);
    const isLogin   = isApiCall && req.url.endsWith('/auth/login');
    const token     = this.apiService.getToken();

    let r = req;

    // Toujours laisser passer OPTIONS
    if (req.method === 'OPTIONS') {
      return next.handle(r);
    }

    // Injecte le Bearer pour toutes les routes API SAUF /auth/login
    if (isApiCall && !isLogin && token) {
      r = r.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
    }

    return next.handle(r).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          // Token expiré/invalide → logout (nettoie storage + redirige /login)
          this.authService.logout();
        }
        return throwError(() => error);
      })
    );
    console.debug('[INT]', { url: req.url, isApiCall, isLogin, hasToken: !!token });

  }
  
}
