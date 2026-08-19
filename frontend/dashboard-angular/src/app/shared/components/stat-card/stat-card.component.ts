import { Component, input } from '@angular/core';

@Component({
  selector: 'app-stat-card',
  standalone: true,
  template: `
    <article class="stat-card">
      <div class="label">{{ label() }}</div>
      <strong>{{ value() }}</strong>
      <span>{{ helper() }}</span>
    </article>
  `,
  styles: [`
    .stat-card { min-height: 136px; padding: 20px; border: 1px solid var(--border); background: rgba(255,255,255,.028); position: relative; overflow: hidden; }
    .stat-card::after { content: ''; position: absolute; inset: 0; background-image: linear-gradient(rgba(255,255,255,.025) 1px, transparent 1px), linear-gradient(90deg, rgba(255,255,255,.025) 1px, transparent 1px); background-size: 28px 28px; pointer-events: none; }
    .label { position: relative; z-index: 1; color: var(--muted); font-size: 11px; text-transform: uppercase; letter-spacing: .12em; }
    strong { position: relative; z-index: 1; display: block; margin: 18px 0 7px; font-size: 32px; line-height: 1; }
    span { position: relative; z-index: 1; color: #777; font-size: 11px; }
  `]
})
export class StatCardComponent {
  label = input.required<string>();
  value = input.required<string | number>();
  helper = input('');
}
