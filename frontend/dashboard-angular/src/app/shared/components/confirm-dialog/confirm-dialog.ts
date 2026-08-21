import { Component, HostListener, input, output } from '@angular/core';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [],
  templateUrl: './confirm-dialog.html',
  styleUrl: './confirm-dialog.css'
})
export class ConfirmDialogComponent {

  readonly title = input.required<string>();
  readonly description = input.required<string>();

  readonly resourceLabel = input<string>('Recurso');
  readonly resourceValue = input.required<string>();

  readonly secondaryLabel = input<string>('');
  readonly secondaryValue = input<string>('');

  readonly warning = input<string>('');
  readonly confirmText = input<string>('Eliminar');

  readonly loading = input(false);
  readonly error = input('');

  readonly confirmed = output<void>();
  readonly cancelled = output<void>();

  @HostListener('document:keydown.escape')
  onEscape(): void {
    if (!this.loading()) {
      this.cancelled.emit();
    }
  }
}