import { Component } from '@angular/core';

@Component({
  selector: 'app-topbar',
  standalone: true,
  templateUrl: './topbar.component.html',
  styleUrl: './topbar.component.css'
})
export class TopbarComponent {
  readonly today = new Intl.DateTimeFormat('es-ES', {
    weekday: 'short', day: '2-digit', month: 'short'
  }).format(new Date());
}
