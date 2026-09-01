import { DatePipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { ApiModel } from '../../core/models/api.model';
import { ClientModel } from '../../core/models/client.model';
import { RequestLog } from '../../core/models/request-log.model';
import { ApiService } from '../../core/services/api.service';
import { ClientService } from '../../core/services/client.service';
import { RequestLogService } from '../../core/services/request-log.service';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';

@Component({
  selector: 'app-requests',
  standalone: true,
  imports: [DatePipe, EmptyStateComponent],
  templateUrl: './requests.page.html',
  styleUrl: './requests.page.css'
})
export class RequestsPage {

  private readonly requestLogService = inject(RequestLogService);
  private readonly clientService = inject(ClientService);
  private readonly apiService = inject(ApiService);

  readonly requests = signal<RequestLog[]>([]);
  readonly clients = signal<ClientModel[]>([]);
  readonly apis = signal<ApiModel[]>([]);
  readonly loading = signal(true);
  readonly error = signal('');

  readonly clientId = signal<number | null>(null);
  readonly apiCode = signal<string | null>(null);
  readonly statusCode = signal<number | null>(null);

  readonly page = signal(0);
  readonly size = 25;
  readonly totalElements = signal(0);
  readonly totalPages = signal(0);

  readonly statuses = [200, 201, 204, 400, 401, 403, 404, 409, 429, 500, 502, 503];

  readonly pageErrors = computed(() => this.requests().filter(request => request.statusCode >= 400).length);

  readonly averageDuration = computed(() => {
    const requests = this.requests();
    if (requests.length === 0) return 0;
    return Math.round(requests.reduce((total, request) => total + request.durationMs, 0) / requests.length);
  });

  constructor() {
    this.loadFilters();
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');

    this.requestLogService.find({
      clientId: this.clientId(),
      apiCode: this.apiCode(),
      statusCode: this.statusCode(),
      page: this.page(),
      size: this.size
    }).subscribe({
      next: response => {
        this.requests.set(response.content);
        this.page.set(response.number);
        this.totalElements.set(response.totalElements);
        this.totalPages.set(response.totalPages);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar las peticiones.');
        this.loading.set(false);
      }
    });
  }

  private loadFilters(): void {
    this.clientService.findAll().subscribe({ next: clients => this.clients.set(clients) });
    this.apiService.findAll().subscribe({ next: apis => this.apis.set(apis) });
  }

  setClient(value: string): void { this.clientId.set(value ? Number(value) : null); this.applyFilters(); }
  setApi(value: string): void { this.apiCode.set(value || null); this.applyFilters(); }
  setStatus(value: string): void { this.statusCode.set(value ? Number(value) : null); this.applyFilters(); }

  applyFilters(): void { this.page.set(0); this.load(); }

  clearFilters(): void {
    this.clientId.set(null);
    this.apiCode.set(null);
    this.statusCode.set(null);
    this.page.set(0);
    this.load();
  }

  previousPage(): void {
    if (this.page() === 0) return;
    this.page.update(page => page - 1);
    this.load();
  }

  nextPage(): void {
    if (this.page() + 1 >= this.totalPages()) return;
    this.page.update(page => page + 1);
    this.load();
  }
}