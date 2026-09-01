import { HttpInterceptorFn } from '@angular/common/http';
import { from, switchMap } from 'rxjs';
import { keycloak } from '../auth/keycloak';

const SECURED_PATHS = ['/api-management', '/users-management', '/model-management', '/requests-management'];

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const securedRequest = SECURED_PATHS.some((path) => req.url.startsWith(path));

  if (!securedRequest || !keycloak.authenticated) return next(req);

  return from(keycloak.updateToken(30)).pipe(
    switchMap(() => {
      if (!keycloak.token) return next(req);
      return next(req.clone({ setHeaders: { Authorization: `Bearer ${keycloak.token}` } }));
    })
  );
};