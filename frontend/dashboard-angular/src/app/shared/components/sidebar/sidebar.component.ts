import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {
  readonly items = [
    { label: 'Dashboard', route: '/dashboard', icon: '▦' },
    { label: 'APIs', route: '/apis', icon: '⌁' },
    { label: 'Clientes', route: '/clients', icon: '◎' },
    { label: 'API Keys', route: '/api-keys', icon: '⌘' },
    { label: 'Peticiones', route: '/requests', icon: '↗' }
  ];
}
