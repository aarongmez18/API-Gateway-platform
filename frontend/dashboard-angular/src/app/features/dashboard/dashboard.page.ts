import { Component, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { ApiService } from '../../core/services/api.service';
import { ClientService } from '../../core/services/client.service';
import { ApiKeyService } from '../../core/services/api-key.service';
import { ApiModel } from '../../core/models/api.model';
import { ClientModel } from '../../core/models/client.model';
import { StatCardComponent } from '../../shared/components/stat-card/stat-card.component';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [StatCardComponent],
  templateUrl: './dashboard.page.html',
  styleUrl: './dashboard.page.css'
})
export class DashboardPage {
  private readonly apiService = inject(ApiService);
  private readonly clientService = inject(ClientService);
  private readonly apiKeyService = inject(ApiKeyService);

  readonly loading = signal(true);
  readonly error = signal('');
  readonly apis = signal<ApiModel[]>([]);
  readonly clients = signal<ClientModel[]>([]);
  readonly apiKeyCount = signal(0);

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');

    forkJoin({
      apis: this.apiService.findAll(),
      clients: this.clientService.findAll(),
      keys: this.apiKeyService.findAll()
    }).subscribe({
      next: ({ apis, clients, keys }) => {
        this.apis.set(apis);
        this.clients.set(clients);
        this.apiKeyCount.set(keys.length);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('No se pudo cargar el resumen. Comprueba si el servicio está disponible.');
        this.loading.set(false);
      }
    });
  }

  activeApis(): number {
    return this.apis().filter((api) => api.active).length;
  }

  activeClients(): number {
    return this.clients().filter((client) => client.active).length;
  }
}
