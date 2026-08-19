import { HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { RequestActivityService } from '../services/request-activity.service';

export const requestActivityInterceptor: HttpInterceptorFn = (req, next) => {
  const activity = inject(RequestActivityService);
  const startedAt = performance.now();
  const id = activity.start(req.method, req.urlWithParams.replace('/gateway', ''));

  return next(req).pipe(
    tap((event) => {
      if (event instanceof HttpResponse) {
        activity.finish(id, event.status, Math.round(performance.now() - startedAt));
      }
    }),
    catchError((error) => {
      activity.finish(id, error.status ?? 500, Math.round(performance.now() - startedAt));
      return throwError(() => error);
    })
  );
};
