import { DatePipe } from '@angular/common';
import { Component, computed, signal } from '@angular/core';

import { RequestActivityService } from '../../core/services/request-activity.service';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';

@Component({
  selector: 'app-requests',
  standalone: true,
  imports: [
    DatePipe,
    EmptyStateComponent
  ],
  templateUrl: './requests.page.html',
  styleUrl: './requests.page.css'
})
export class RequestsPage {

  readonly methods = [
    'ALL',
    'GET',
    'POST',
    'PUT',
    'DELETE'
  ];

  readonly method = signal('ALL');

  readonly requests = computed(() => {

    const selectedMethod = this.method();
    const requests = this.activity.requests();

    if (selectedMethod === 'ALL') {
      return requests;
    }

    return requests.filter(
      request =>
        request.method.toUpperCase() === selectedMethod
    );
  });

  constructor(
    public readonly activity: RequestActivityService
  ) {
  }
}