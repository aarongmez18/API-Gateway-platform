import { Component, input } from '@angular/core';

@Component({
  selector: 'app-empty-state',
  standalone: true,
  template: `
    <div class="empty-state">
      <div class="icon">□</div>
      <strong>{{ title() }}</strong>
      <span>{{ text() }}</span>
    </div>
  `,
  styles: [`
    .empty-state { min-height: 220px; display: grid; place-items: center; align-content: center; gap: 8px; border: 1px dashed rgba(255,255,255,.14); color: var(--muted); text-align: center; }
    .icon { font-size: 30px; color: #666; }
    strong { color: #ddd; font-size: 14px; }
    span { font-size: 12px; max-width: 380px; }
  `]
})
export class EmptyStateComponent {
  title = input.required<string>();
  text = input.required<string>();
}
