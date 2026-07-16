import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  template: `
    <header style="background:#1976d2;color:white;padding:16px 24px;display:flex;align-items:center;gap:16px;">
      <h1 style="font-size:20px;">Sales Tracker</h1>
      <a routerLink="/" style="color:white;text-decoration:none;">All Sales</a>
      <a routerLink="/new" style="color:white;text-decoration:none;">+ New Sale</a>
    </header>
    <main style="padding:24px;">
      <router-outlet />
    </main>
  `
})
export class AppComponent {}
