import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { ApiKeyService } from '../../core/services/api-key.service';
import { ClientService } from '../../core/services/client.service';
import { ApiKeyModel } from '../../core/models/api-key.model';
import { ClientModel } from '../../core/models/client.model';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';

@Component({
  selector: 'app-api-keys-page',
  standalone: true,
  imports: [ReactiveFormsModule, EmptyStateComponent],
  templateUrl: './api-keys.page.html',
  styleUrl: './api-keys.page.css'
})
export class ApiKeysPage {
  private readonly apiKeyService = inject(ApiKeyService);
  private readonly clientService = inject(ClientService);
  private readonly fb = inject(FormBuilder);

  readonly apiKeys = signal<ApiKeyModel[]>([]);
  readonly clients = signal<ClientModel[]>([]);
  readonly query = signal('');
  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly error = signal('');
  readonly drawerOpen = signal(false);
  readonly editingId = signal<number | null>(null);

  readonly filteredApiKeys = computed(() => {
    const q = this.query().trim().toLowerCase();
    return q ? this.apiKeys().filter((key) => key.clientName.toLowerCase().includes(q) || String(key.id).includes(q)) : this.apiKeys();
  });

  readonly form = this.fb.nonNullable.group({
    clientId: [0, [Validators.required, Validators.min(1)]],
    active: [true]
  });

  constructor() { this.load(); }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    forkJoin({ keys: this.apiKeyService.findAll(), clients: this.clientService.findAll() }).subscribe({
      next: ({ keys, clients }) => { this.apiKeys.set(keys); this.clients.set(clients); this.loading.set(false); },
      error: () => { this.error.set('No se pudieron cargar las API Keys.'); this.loading.set(false); }
    });
  }

  openCreate(): void {
    this.editingId.set(null);
    this.form.reset({ clientId: this.clients()[0]?.id ?? 0, active: true });
    this.drawerOpen.set(true);
  }

  openEdit(apiKey: ApiKeyModel): void {
    this.editingId.set(apiKey.id);
    this.form.setValue({ clientId: apiKey.clientId, active: apiKey.active });
    this.drawerOpen.set(true);
  }

  closeDrawer(): void { this.drawerOpen.set(false); }

  save(): void {
    if (this.form.invalid || this.saving()) return;
    this.saving.set(true);
    const request = this.form.getRawValue();
    const id = this.editingId();
    const operation = id === null ? this.apiKeyService.create(request) : this.apiKeyService.update(id, request);

    operation.subscribe({
      next: () => { this.saving.set(false); this.closeDrawer(); this.load(); },
      error: () => { this.error.set('No se pudo guardar la API Key.'); this.saving.set(false); }
    });
  }

  delete(apiKey: ApiKeyModel): void {
    if (!confirm(`¿Eliminar la API Key #${apiKey.id}?`)) return;
    this.apiKeyService.delete(apiKey.id).subscribe({
      next: () => this.apiKeys.update((items) => items.filter((item) => item.id !== apiKey.id)),
      error: () => this.error.set('No se pudo eliminar la API Key.')
    });
  }
}
