import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean | UrlTree {
    const token = localStorage.getItem('token');
    const authRoutes = ['signin', 'signup', 'forgot-password', 'reset-password']
    const isAuthRoute = authRoutes.includes(route.routeConfig?.path || '');

    if (token && isAuthRoute) {
      return this.router.createUrlTree(['/dashboard']);
    } else if (!token && !isAuthRoute) {
      return this.router.createUrlTree(['/signin']);
    }

    return true;
  }
}

