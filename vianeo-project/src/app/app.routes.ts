import { Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  // Public
  {
    path: 'login',
    loadComponent: () =>
      import('./pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'unauthorized',
    loadComponent: () =>
      import('./pages/unauthorized/unauthorized.component').then(m => m.UnauthorizedComponent)
  },


  // Portail Admin
  {
    path: 'entites',
    loadComponent: () =>
      import('./pages/entites/entites.component').then(m => m.EntitesComponent),
    canActivate: [AuthGuard],
    data: { role: 'ADMIN' }
  },
  {
  path: 'rapport-chantier',
  loadComponent: () =>
    import('./pages/rapport-chantier/rapport-chantier.component').then(m => m.RapportChantierComponent)
  // canActivate: [],  <-- retiré
  // data: { }         <-- retiré
},

  {
    path: 'personnel',
    loadComponent: () =>
      import('./pages/personnel/personnel.component').then(m => m.PersonnelComponent),
    canActivate: [AuthGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'articles',
    loadComponent: () =>
      import('./pages/articles/articles.component').then(m => m.ArticlesComponent),
    canActivate: [AuthGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'chantiers',
    loadComponent: () =>
      import('./pages/chantiers/chantiers.component').then(m => m.ChantiersComponent),
    canActivate: [AuthGuard],
    data: { role: 'ADMIN' }
  },
  {
    path: 'fournisseurs',
    loadComponent: () =>
      import('./pages/fournisseurs/fournisseurs.component').then(m => m.FournisseursComponent),
    canActivate: [AuthGuard],
    data: { role: 'ADMIN' }
  },

  { path: '**', redirectTo: '/login' }
];
