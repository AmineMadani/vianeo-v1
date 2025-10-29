import { Injectable, signal } from '@angular/core';

const LS_KEY = 'vianeo.sidebar.collapsed';

@Injectable({ providedIn: 'root' })
export class SidebarService {
  collapsed = signal<boolean>(this.read());

  toggle() {
    const v = !this.collapsed();
    this.collapsed.set(v);
    localStorage.setItem(LS_KEY, v ? '1' : '0');
  }

  private read(): boolean {
    return localStorage.getItem(LS_KEY) === '1';
  }
}
