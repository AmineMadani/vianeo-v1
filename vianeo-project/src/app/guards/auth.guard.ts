import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, map, switchMap, take } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';
import { ApiService } from '../services/api.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot, _state: RouterStateSnapshot): Observable<boolean> {
    const skip = route.data['skipRoleCheck'];
    if (skip) return of(true);

    if (!this.auth.isAuthenticated()) {
      this.router.navigate(['/login']);
      return of(false);
    }

    return this.auth.loadUserIfNeeded().pipe(
      map(user => {
        if (!user) {
          this.router.navigate(['/login']);
          return false;
        }

        const requiredRole  = route.data['role']  as string | undefined;
        const requiredRoles = route.data['roles'] as string[] | undefined;
        if (!requiredRole && (!requiredRoles || requiredRoles.length === 0)) return true;

        const norm = (r: string) => {
          let v = (r || '').toUpperCase();
          if (v.startsWith('ROLE_')) v = v.slice(5);
          if (v === 'CDT' || v === 'CC') v = 'CHEF_CHANTIER';
          return v;
        };

        const userRole = norm(user.role as any);
        const ok =
          (requiredRoles && requiredRoles.map(norm).includes(userRole)) ||
          (requiredRole && norm(requiredRole) === userRole);

        if (ok) return true;
        this.router.navigate(['/unauthorized']);
        return false;
      }),
      catchError(() => {
        this.auth.logout();
        return of(false);
      })
    );
  }
}
