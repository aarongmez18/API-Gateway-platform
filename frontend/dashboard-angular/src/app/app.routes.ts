import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'dashboard'
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./features/dashboard/dashboard.page').then((m) => m.DashboardPage)
  },
  {
    path: 'apis',
    loadComponent: () => import('./features/apis/apis.page').then((m) => m.ApisPage)
  },
  {
    path: 'clients',
    loadComponent: () => import('./features/clients/clients.page').then((m) => m.ClientsPage)
  },
  {
    path: 'api-keys',
    loadComponent: () => import('./features/api-keys/api-keys.page').then((m) => m.ApiKeysPage)
  },
  {
    path: 'permissions',
    loadComponent: () => import('./features/permissions/permissions.page').then((m) => m.PermissionsPage)
  },
  {
    path: 'requests',
    loadComponent: () => import('./features/requests/requests.page').then((m) => m.RequestsPage)
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];
