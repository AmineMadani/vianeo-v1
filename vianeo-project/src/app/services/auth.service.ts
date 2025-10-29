import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map, tap, catchError } from 'rxjs/operators';
import { ApiService, LoginRequest, User } from './api.service';

@Injectable({ providedIn: 'root' })
export class AuthService {
  isAuthenticated$ = this.apiService.currentUser$.pipe(map(u => !!u));

  constructor(private apiService: ApiService, private router: Router) {}

  login(credentials: LoginRequest) {
    return this.apiService.login(credentials);
  }

  hydrateMe() {
  return this.apiService.hydrateFromMe(); // renvoie Observable<User>
}

  logout() {
    this.apiService.logout();
    this.router.navigate(['/login']);
  }

  getCurrentUser(): Observable<User | null> {
    return this.apiService.currentUser$;
  }

  isAuthenticated(): boolean {
    return !!this.apiService.getToken();
  }

  // ⇩⇩ Ajoute/Remplace cette méthode
  loadUserIfNeeded(): Observable<User | null> {
    // déjà chargé → direct
    const cached = this.apiService.currentUserSnapshot;
    if (cached) return of(cached);

    // pas de token → null
    const token = this.apiService.getToken();
    if (!token) return of(null);

    // sinon /auth/me (le Bearer sera ajouté par l’interceptor)
    return this.apiService.fetchMe().pipe(
      tap(u => this.apiService.setCurrentUser(u)),
      catchError(() => of(null))
    );
  }
}
