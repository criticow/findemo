import { Routes } from '@angular/router';
import { AuthGuard } from './common/auth-guard/auth.guard';

export const routes: Routes = [
  {
    path: 'signin',
    loadComponent: () => import('./signin/signin.component')
      .then((m) => m.SigninComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'signup',
    loadComponent: () => import('./signup/signup.component')
      .then((m) => m.SignupComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'forgot-password',
    loadComponent: () => import('./forgot-password/forgot-password.component')
      .then((m) => m.ForgotPasswordComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'reset-password',
    loadComponent: () => import('./reset-password/reset-password.component')
      .then((m) => m.ResetPasswordComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./dashboard/dashboard.component')
      .then((m) => m.DashboardComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'transactions',
    loadComponent: () => import('./transactions/transactions.component')
      .then((m) => m.TransactionsComponent),
    canActivate: [AuthGuard]
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'dashboard'
  },
  {
    path: '**',
    loadComponent: () => import('./dashboard/dashboard.component')
      .then((m) => m.DashboardComponent)
  },
];