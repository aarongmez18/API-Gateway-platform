import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../../core/services/api.service';
import { ApiModel } from '../../core/models/api.model';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';

@Component({
  selector: 'app-apis-page',
  standalone: true,
  imports: [ReactiveFormsModule, EmptyStateComponent],
  templateUrl: './apis.page.html',
  styleUrl: './apis.page.css'
})
export class ApisPage {
  private readonly service = inject(ApiService);
  private readonly fb = inject(FormBuilder);

  readonly apis = signal<ApiModel[]>([]);
  readonly query = signal('');
  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly error = signal('');
  readonly drawerOpen = signal(false);
  readonly editingId = signal<number | null>(null);

  readonly filteredApis = computed(() => {
    const q = this.query().trim().toLowerCase();
    if (!q) return this.apis();
    return this.apis().filter((api) =>
      [api.name, api.path, api.targetUrl].some((value) => value.toLowerCase().includes(q))
    );
  });

  readonly form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(100)]],
    targetUrl: ['', Validators.required],
    path: ['', Validators.required],
    active: [true]
  });

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.findAll().subscribe({
      next: (data) => { this.apis.set(data); this.loading.set(false); },
      error: () => { this.error.set('No se pudieron cargar las APIs.'); this.loading.set(false); }
    });
  }

  openCreate(): void {
    this.editingId.set(null);
    this.form.reset({ name: '', targetUrl: '', path: '', active: true });
    this.drawerOpen.set(true);
  }

  openEdit(api: ApiModel): void {
    this.editingId.set(api.id);
    this.form.setValue({
      name: api.name,
      targetUrl: api.targetUrl,
      path: api.path,
      active: api.active
    });
    this.drawerOpen.set(true);
  }

  closeDrawer(): void {
    this.drawerOpen.set(false);
  }

  save(): void {
    if (this.form.invalid || this.saving()) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving.set(true);
    const request = this.form.getRawValue();
    const id = this.editingId();
    const operation = id === null ? this.service.create(request) : this.service.update(id, request);

    operation.subscribe({
      next: () => {
        this.saving.set(false);
        this.closeDrawer();
        this.load();
      },
      error: () => {
        this.error.set('No se pudo guardar la API. Revisa los datos y el backend.');
        this.saving.set(false);
      }
    });
  }

  delete(api: ApiModel): void {
    if (!confirm(`¿Eliminar la API "${api.name}"?`)) return;

    this.service.delete(api.id).subscribe({
      next: () => this.apis.update((items) => items.filter((item) => item.id !== api.id)),
      error: () => this.error.set('No se pudo eliminar la API.')
    });
  }
}
