import { Component, computed, inject, signal } from '@angular/core';
import { forkJoin } from 'rxjs';
import { ApiModel } from '../../core/models/api.model';
import { ClientModel } from '../../core/models/client.model';
import { PermissionModel } from '../../core/models/permission.model';
import { ApiService } from '../../core/services/api.service';
import { ClientService } from '../../core/services/client.service';
import { PermissionService } from '../../core/services/permission.service';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';

@Component({
  selector: 'app-permissions-page',
  standalone: true,
  imports: [EmptyStateComponent],
  templateUrl: './permissions.page.html',
  styleUrl: './permissions.page.css'
})
export class PermissionsPage {
  private readonly apiService = inject(ApiService);
  private readonly clientService = inject(ClientService);
  private readonly permissionService = inject(PermissionService);

  readonly clients = signal<ClientModel[]>([]);
  readonly apis = signal<ApiModel[]>([]);
  readonly permissions = signal<PermissionModel[]>([]);
  readonly selectedClientId = signal<number | null>(null);
  readonly loading = signal(true);
  readonly loadingPermissions = signal(false);
  readonly error = signal('');
  readonly query = signal('');
  readonly pendingCodes = signal<Set<string>>(new Set());

  readonly selectedClient = computed(() => this.clients().find(client => client.id === this.selectedClientId()) ?? null);

  readonly allowedCodes = computed(() => new Set(this.permissions().map(permission => permission.apiCode)));

  readonly filteredApis = computed(() => {
    const query = this.query().trim().toLowerCase();
    return query ? this.apis().filter(api => api.name.toLowerCase().includes(query) || api.code.toLowerCase().includes(query) || api.path.toLowerCase().includes(query)) : this.apis();
  });

  readonly grantedCount = computed(() => this.apis().filter(api => this.allowedCodes().has(api.code)).length);

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');

    forkJoin({
      clients: this.clientService.findAll(),
      apis: this.apiService.findAll()
    }).subscribe({
      next: ({ clients, apis }) => {
        this.clients.set(clients);
        this.apis.set(apis);

        const firstClient = clients[0];

        if (firstClient) {
          this.selectClient(firstClient.id);
        }

        this.loading.set(false);
      },
      error: () => {
        this.error.set('No se pudo cargar la información necesaria para gestionar permisos.');
        this.loading.set(false);
      }
    });
  }

  selectClient(clientId: number): void {
    if (clientId === this.selectedClientId() && this.permissions().length) return;

    this.selectedClientId.set(clientId);
    this.permissions.set([]);
    this.loadingPermissions.set(true);
    this.error.set('');

    this.permissionService.findByClient(clientId).subscribe({
      next: permissions => {
        this.permissions.set(permissions);
        this.loadingPermissions.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar los permisos del cliente.');
        this.loadingPermissions.set(false);
      }
    });
  }

  hasPermission(apiCode: string): boolean {
    return this.allowedCodes().has(apiCode);
  }

  isPending(apiCode: string): boolean {
    return this.pendingCodes().has(apiCode);
  }

  togglePermission(api: ApiModel): void {
    const clientId = this.selectedClientId();

    if (clientId === null || this.isPending(api.code)) return;

    const allowed = this.hasPermission(api.code);

    this.setPending(api.code, true);
    this.error.set('');

    if (allowed) {
      this.permissionService.revoke(clientId, api.code).subscribe({
        next: () => {
          this.permissions.update(items => items.filter(permission => permission.apiCode !== api.code));
          this.setPending(api.code, false);
        },
        error: err => {
          this.error.set(err?.error?.message ?? `No se pudo revocar el acceso a ${api.name}.`);
          this.setPending(api.code, false);
        }
      });

      return;
    }

    this.permissionService.grant({ clientId, apiCode: api.code }).subscribe({
      next: permission => {
        this.permissions.update(items => [...items, permission]);
        this.setPending(api.code, false);
      },
      error: err => {
        this.error.set(err?.error?.message ?? `No se pudo conceder el acceso a ${api.name}.`);
        this.setPending(api.code, false);
      }
    });
  }

  private setPending(apiCode: string, pending: boolean): void {
    this.pendingCodes.update(current => {
      const next = new Set(current);
      pending ? next.add(apiCode) : next.delete(apiCode);
      return next;
    });
  }
}