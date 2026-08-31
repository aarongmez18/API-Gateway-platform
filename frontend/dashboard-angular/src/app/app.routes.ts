import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
  { path: 'dashboard', canActivate: [authGuard], loadComponent: () => import('./features/dashboard/dashboard.page').then((m) => m.DashboardPage) },
  { path: 'apis', canActivate: [authGuard], loadComponent: () => import('./features/apis/apis.page').then((m) => m.ApisPage) },
  { path: 'clients', canActivate: [authGuard], loadComponent: () => import('./features/clients/clients.page').then((m) => m.ClientsPage) },
  { path: 'api-keys', canActivate: [authGuard], loadComponent: () => import('./features/api-keys/api-keys.page').then((m) => m.ApiKeysPage) },
  { path: 'permissions', canActivate: [authGuard], loadComponent: () => import('./features/permissions/permissions.page').then((m) => m.PermissionsPage) },
  { path: 'requests', canActivate: [authGuard], loadComponent: () => import('./features/requests/requests.page').then((m) => m.RequestsPage) },
  { path: '**', redirectTo: 'dashboard' }
];