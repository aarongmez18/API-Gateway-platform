import { Injectable } from '@angular/core';
import { keycloak } from './keycloak';

interface RealmAccess { roles?: string[]; }

@Injectable({ providedIn: 'root' })
export class AuthService {
  get authenticated(): boolean { return !!keycloak.authenticated; }
  get username(): string { return keycloak.tokenParsed?.['preferred_username'] ?? ''; }
  get roles(): string[] { return (keycloak.tokenParsed?.['realm_access'] as RealmAccess | undefined)?.roles ?? []; }

  hasRole(role: string): boolean { return this.roles.includes(role); }
  isAdmin(): boolean { return this.hasRole('ADMIN'); }
  login(): Promise<void> { return keycloak.login({ redirectUri: window.location.origin }); }
  logout(): Promise<void> { return keycloak.logout({ redirectUri: window.location.origin }); }
}