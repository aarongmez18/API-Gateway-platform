import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ClientService } from '../../core/services/client.service';
import { ClientModel } from '../../core/models/client.model';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';

@Component({
  selector: 'app-clients-page',
  standalone: true,
  imports: [ReactiveFormsModule, EmptyStateComponent],
  templateUrl: './clients.page.html',
  styleUrl: './clients.page.css'
})
export class ClientsPage {
  private readonly service = inject(ClientService);
  private readonly fb = inject(FormBuilder);

  readonly clients = signal<ClientModel[]>([]);
  readonly query = signal('');
  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly error = signal('');
  readonly drawerOpen = signal(false);
  readonly editingId = signal<number | null>(null);

  readonly filteredClients = computed(() => {
    const q = this.query().trim().toLowerCase();
    return q ? this.clients().filter((client) => client.name.toLowerCase().includes(q)) : this.clients();
  });

  readonly form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(100)]],
    active: [true]
  });

  constructor() { this.load(); }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.findAll().subscribe({
      next: (data) => { this.clients.set(data); this.loading.set(false); },
      error: () => { this.error.set('No se pudieron cargar los clientes.'); this.loading.set(false); }
    });
  }

  openCreate(): void {
    this.editingId.set(null);
    this.form.reset({ name: '', active: true });
    this.drawerOpen.set(true);
  }

  openEdit(client: ClientModel): void {
    this.editingId.set(client.id);
    this.form.setValue({ name: client.name, active: client.active });
    this.drawerOpen.set(true);
  }

  closeDrawer(): void { this.drawerOpen.set(false); }

  save(): void {
    if (this.form.invalid || this.saving()) return;
    this.saving.set(true);
    const request = this.form.getRawValue();
    const id = this.editingId();
    const operation = id === null ? this.service.create(request) : this.service.update(id, request);

    operation.subscribe({
      next: () => { this.saving.set(false); this.closeDrawer(); this.load(); },
      error: () => { this.error.set('No se pudo guardar el cliente.'); this.saving.set(false); }
    });
  }

  delete(client: ClientModel): void {
    if (!confirm(`¿Eliminar el cliente "${client.name}"?`)) return;

    this.service.delete(client.id).subscribe({
      next: () => this.clients.update((items) => items.filter((item) => item.id !== client.id)),
      error: (err) => {
        this.error.set(err?.error?.message ?? 'No se pudo eliminar el cliente.');
      }
    });
  }
}
