import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { ApiKeyService } from '../../core/services/api-key.service';
import { ClientService } from '../../core/services/client.service';
import { ApiKeyCreatedModel, ApiKeyModel } from '../../core/models/api-key.model';
import { ClientModel } from '../../core/models/client.model';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { ConfirmDialogComponent } from 'src/app/shared/components/confirm-dialog/confirm-dialog';

@Component({
  selector: 'app-api-keys-page',
  standalone: true,
  imports: [ReactiveFormsModule, EmptyStateComponent, ConfirmDialogComponent],
  templateUrl: './api-keys.page.html',
  styleUrl: './api-keys.page.css'
})
export class ApiKeysPage {

  private readonly fb = inject(FormBuilder);
  private readonly clientService = inject(ClientService);
  private readonly apiKeyService = inject(ApiKeyService);
  

  readonly query = signal('');
  readonly error = signal('');
  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly copied = signal(false);
  readonly deleting = signal(false);
  readonly deleteError = signal('');
  readonly drawerOpen = signal(false);
  readonly apiKeys = signal<ApiKeyModel[]>([]);
  readonly clients = signal<ClientModel[]>([]);
  readonly editingId = signal<number | null>(null);
  readonly apiKeyToDelete = signal<ApiKeyModel | null>(null);
  readonly createdApiKey = signal<ApiKeyCreatedModel | null>(null);

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
    this.error.set('');

    const request = this.form.getRawValue();
    const id = this.editingId();

    if (id === null) {
      this.apiKeyService.create(request).subscribe({
        next: (created) => {
          this.saving.set(false);
          this.closeDrawer();

          this.createdApiKey.set(created);
          this.copied.set(false);

          this.load();
        },
        error: () => {
          this.error.set('No se pudo generar la API Key.');
          this.saving.set(false);
        }
      });

      return;
    }

    this.apiKeyService.update(id, request).subscribe({
      next: () => {
        this.saving.set(false);
        this.closeDrawer();
        this.load();
      },
      error: () => {
        this.error.set('No se pudo guardar la API Key.');
        this.saving.set(false);
      }
    });
  }

  closeCreatedApiKey(): void {
    this.createdApiKey.set(null);
    this.copied.set(false);
  }

  async copyCreatedApiKey(): Promise<void> {
    const apiKey = this.createdApiKey()?.apiKey;

    if (!apiKey) return;

    await navigator.clipboard.writeText(apiKey);
    this.copied.set(true);
  }

openDeleteConfirm(apiKey: ApiKeyModel): void {
  this.deleteError.set('');
  this.apiKeyToDelete.set(apiKey);
}

closeDeleteConfirm(): void {
  if (this.deleting()) return;

  this.apiKeyToDelete.set(null);
  this.deleteError.set('');
}

confirmDelete(): void {
  const apiKey = this.apiKeyToDelete();

  if (!apiKey || this.deleting()) return;

  this.deleting.set(true);
  this.deleteError.set('');

  this.apiKeyService.delete(apiKey.id).subscribe({
    next: () => {
      this.apiKeys.update((items) =>
        items.filter((item) => item.id !== apiKey.id)
      );

      this.deleting.set(false);
      this.apiKeyToDelete.set(null);
    },

    error: (err) => {
      this.deleting.set(false);

      this.deleteError.set(
        err?.error?.message ??
        'No se pudo eliminar la API Key.'
      );
    }
  });
}
}
