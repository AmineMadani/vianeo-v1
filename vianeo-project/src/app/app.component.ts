import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, RouterOutlet, NavigationEnd } from '@angular/router';
import { AuthService } from './services/auth.service';
import { Observable, filter, map, startWith } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterModule],
  template: `
    <div class="app-container"
         [class.collapsed]="collapsed"
         [class.hide-sidebar]="(hideSidebar$ | async) === true">

      <!-- SIDEBAR (garde ton HTML interne si tu veux) -->
      <nav class="sidebar">
  <div class="logo">
    <button class="collapse-btn" (click)="toggleSidebar()" title="Réduire/Étendre">☰</button>
    <h2 class="brand">Vianeo</h2>
    <!-- bouton logout conservé -->
    <button class="btn-logout" (click)="logout()">Déconnexion</button>
  </div>

  <ul class="menu">
    <li>
      <a routerLink="/rapport-chantier" routerLinkActive="active">
        <span class="mi">🧾</span><span class="ml">Rapport de Chantier</span>
      </a>
    </li>
    <li>
      <a routerLink="/entites" routerLinkActive="active">
        <span class="mi">🏢</span><span class="ml">Entités</span>
      </a>
    </li>
    <li>
      <a routerLink="/fournisseurs" routerLinkActive="active">
        <span class="mi">🚚</span><span class="ml">Fournisseurs</span>
      </a>
    </li>
    <li>
      <a routerLink="/personnel" routerLinkActive="active">
        <span class="mi">👥</span><span class="ml">Personnel</span>
      </a>
    </li>
    <li>
      <a routerLink="/articles" routerLinkActive="active">
        <span class="mi">📦</span><span class="ml">Articles</span>
      </a>
    </li>
    <li>
      <a routerLink="/chantiers" routerLinkActive="active">
        <span class="mi">🏗️</span><span class="ml">Chantiers</span>
      </a>
    </li>
  </ul>
</nav>


      <!-- CONTENU -->
      <main class="content">
        <header class="topbar">
          <button class="burger" (click)="toggleSidebar()" aria-label="Toggle menu">☰</button>
          <div class="spacer"></div>
          <!-- zone actions globales si besoin -->
        </header>

        <section class="page">
          <router-outlet></router-outlet>
        </section>
      </main>
    </div>
  `,
  styles: [`
    /* CONTAINER */
.app-container {
  display: grid;
  grid-template-columns: 260px 1fr;
  min-height: 100vh;
  transition: grid-template-columns .2s ease;
  background: #f6f7fb;
}
.app-container.collapsed { grid-template-columns: 72px 1fr; }
.app-container.hide-sidebar { grid-template-columns: 1fr; }

/* SIDEBAR */
.sidebar {
  position: sticky; top: 0; height: 100vh; overflow: hidden;
  background: linear-gradient(180deg, #1e3a8a, #1d4ed8);
  color: #fff; padding: 10px;
  display: flex; flex-direction: column;
}
.logo {
  display: flex; align-items: center; gap: .6rem; padding: 6px 4px 10px 4px;
}
.collapse-btn {
  border: 0; background: #0ea5e9; color: #fff; border-radius: 8px;
  padding: 6px 10px; cursor: pointer;
}
.brand { font-size: 20px; margin: 0; line-height: 1; white-space: nowrap; }

.btn-logout {
  margin-left: auto;
  border: 0; border-radius: 10px; padding: 7px 12px; cursor: pointer;
  background: #ef4444; color: #fff; font-weight: 600;
  box-shadow: 0 1px 0 rgba(0,0,0,.2);
}

/* MENU */
.menu { list-style: none; margin: 8px 0 0; padding: 0; display: flex; flex-direction: column; gap: 8px; }
.menu a {
  display: flex; align-items: center; gap: 12px;
  color: #e5e7eb; text-decoration: none;
  padding: 12px 14px; border-radius: 10px; white-space: nowrap;
  transition: background .15s ease;
}
.menu a.active, .menu a:hover { background: rgba(255,255,255,.16); color: #fff; }

/* icône + label (mi = icon, ml = label) */
.menu .mi {
  width: 24px; min-width: 24px; text-align: center; font-size: 18px; line-height: 1;
}
.menu .ml {
  overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
  transition: opacity .15s ease, width .15s ease, margin .15s ease;
}

/* COLLAPSED: on masque uniquement le label, pas l’icône */
.app-container.collapsed .brand { display: none; } /* titre Vianeo masqué, bouton logout reste */
.app-container.collapsed .menu .ml {
  opacity: 0; width: 0; margin: 0; pointer-events: none;
}
.sidebar { overflow: hidden; } /* évite le texte qui déborde pendant l’anim */

/* CONTENT */
.content { display: flex; flex-direction: column; min-width: 0; }
.topbar {
  height: 56px; background: #fff; border-bottom: 1px solid #e5e7eb;
  display: flex; align-items: center; padding: 0 12px;
}
.burger {
  display: none; border: 0; background: #eef2ff; color: #111827;
  border-radius: 8px; padding: 6px 10px; cursor: pointer;
}
.page { padding: 16px; height: calc(100vh - 56px); overflow: auto; }

/* Responsive : grille compacte et burger visible */
@media (max-width: 900px) {
  .app-container { grid-template-columns: 72px 1fr; }
  .burger { display: inline-flex; }
}

/* Cache réellement la sidebar et les boutons quand hide-sidebar est actif */
.app-container.hide-sidebar .sidebar { display: none; }
.app-container.hide-sidebar .burger { display: none; }
.app-container.hide-sidebar .btn-logout { display: none; }
/* En mode collapsed : stack vertical + icônes centrées */
.app-container.collapsed .logo {
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

/* Transforme "Déconnexion" en icône ronde */
.app-container.collapsed .btn-logout {
  width: 36px; height: 36px;
  padding: 0;
  border-radius: 9999px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin-left: 0;      /* plus d'auto pour éviter les débordements */
  font-size: 0;        /* masque le texte */
}

/* Icône ⎋ (ou change pour 🔒/⏻) */
.app-container.collapsed .btn-logout::before {
  content: '⏻';
  font-size: 18px;
  line-height: 1;
  color: #fff;
}

.app-container.collapsed .menu a { justify-content: center; padding: 10px 0; }


  `]
})
export class AppComponent {
  isAuthenticated$: Observable<boolean>;
  collapsed = (typeof localStorage !== 'undefined') && localStorage.getItem('vianeo.sidebar.collapsed') === '1';

  /** Cache la sidebar seulement sur ces routes */
  hideSidebar$: Observable<boolean>;

  constructor(private authService: AuthService, private router: Router) {
    this.isAuthenticated$ = this.authService.isAuthenticated$;

    const HIDE_ON = ['/login', '/unauthorized'];
    const isHide = (url: string) => HIDE_ON.some(p => url.startsWith(p));

    this.hideSidebar$ = this.router.events.pipe(
      filter(e => e instanceof NavigationEnd),
      map(() => isHide(this.router.url)),
      startWith(isHide(this.router.url))   // démarre sur l’URL courante
    );

  }

  toggleSidebar() {
    this.collapsed = !this.collapsed;
    try { localStorage.setItem('vianeo.sidebar.collapsed', this.collapsed ? '1' : '0'); } catch {}
  }

  logout(): void {
    this.authService.logout();
  }
}
