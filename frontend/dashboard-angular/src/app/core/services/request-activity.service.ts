import { Injectable, computed, signal } from '@angular/core';
import { RequestActivity } from '../models/request-activity.model';

@Injectable({
  providedIn: 'root'
})
export class RequestActivityService {

  private readonly _requests = signal<RequestActivity[]>([]);

  readonly requests = this._requests.asReadonly();

  readonly total = computed(() =>
    this._requests().length
  );

  readonly pending = computed(() =>
    this._requests()
      .filter(request => request.state === 'pending')
      .length
  );

  readonly errors = computed(() =>
    this._requests()
      .filter(request => request.state === 'error')
      .length
  );

  start(method: string, endpoint: string): string {

    const id = crypto.randomUUID();

    const request: RequestActivity = {
      id,
      method,
      endpoint,
      status: null,
      durationMs: null,
      startedAt: new Date(),
      state: 'pending'
    };

    this._requests.update(items => [
      request,
      ...items
    ]);

    return id;
  }

  finish(
    id: string,
    status: number,
    durationMs: number
  ): void {

    this._requests.update(items =>
      items.map(item => {

        if (item.id !== id) {
          return item;
        }

        const state: RequestActivity['state'] =
          status >= 200 && status < 400
            ? 'success'
            : 'error';

        return {
          ...item,
          status,
          durationMs,
          state
        };
      })
    );
  }

  clear(): void {
    this._requests.set([]);
  }
}