import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';

type BackType =
  | 'INTERIM'
  | 'AUTRE'
  | 'FOURNITURE'
  | 'LOCATION_SANS_CHAUFFEUR'
  | 'SOUS_TRAITANCE'
  | 'LOUAGEUR'
  | 'LOCATION_AVEC_CHAUFFEUR';

interface Fournisseur {
  id?: number;
  /** côté back: code (alias JSON: nom) */
  nom: string;
  /** côté back: type (enum), alias JSON: type_fournisseur */
  type_fournisseur: BackType;
  // Champs affichés seulement (pas persistés avec ton entity actuelle)
  adresse?: string;
  ville?: string;
  code_postal?: string;
  telephone?: string;
  email?: string;
  contact_principal?: string;
  actif: boolean;
  created_at?: string;
  updated_at?: string;
}

@Component({
  selector: 'app-fournisseurs',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <header class="page-header">
        <h1>Gestion des Fournisseurs</h1>
        <button class="btn-primary" (click)="showAddForm = true">
          <span>+</span> Nouveau Fournisseur
        </button>
      </header>

      <!-- Formulaire d'ajout/modification -->
      <div class="form-card" *ngIf="showAddForm || editingFournisseur">
        <h2>{{ editingFournisseur ? 'Modifier' : 'Ajouter' }} un fournisseur</h2>

        <form (ngSubmit)="saveFournisseur()" #fournisseurForm="ngForm">
          <div class="form-grid">
            <div class="form-field">
              <label for="nom">Nom *</label>
              <input
                type="text"
                id="nom"
                [(ngModel)]="currentFournisseur.nom"
                name="nom"
                required
                placeholder="Nom du fournisseur"
              />
            </div>

            <div class="form-field">
              <label for="type_fournisseur">Type de fournisseur</label>
              <select
                id="type_fournisseur"
                [(ngModel)]="currentFournisseur.type_fournisseur"
                name="type_fournisseur"
                required
              >
                <option *ngFor="let t of types" [value]="t">
                  {{ getTypeLabel(t) }}
                </option>
              </select>
            </div>

            <div class="form-field">
              <label for="contact_principal">Contact principal</label>
              <input
                type="text"
                id="contact_principal"
                [(ngModel)]="currentFournisseur.contact_principal"
                name="contact_principal"
                placeholder="Nom du contact principal"
              />
            </div>

            <div class="form-field">
              <label for="telephone">Téléphone</label>
              <input
                type="tel"
                id="telephone"
                [(ngModel)]="currentFournisseur.telephone"
                name="telephone"
                placeholder="Numéro de téléphone"
              />
            </div>

            <div class="form-field">
              <label for="email">Email</label>
              <input
                type="email"
                id="email"
                [(ngModel)]="currentFournisseur.email"
                name="email"
                placeholder="Adresse email"
              />
            </div>

            <div class="form-field">
              <label for="adresse">Adresse</label>
              <input
                type="text"
                id="adresse"
                [(ngModel)]="currentFournisseur.adresse"
                name="adresse"
                placeholder="Adresse complète"
              />
            </div>

            <div class="form-field">
              <label for="ville">Ville</label>
              <input
                type="text"
                id="ville"
                [(ngModel)]="currentFournisseur.ville"
                name="ville"
                placeholder="Ville"
              />
            </div>

            <div class="form-field">
              <label for="code_postal">Code Postal</label>
              <input
                type="text"
                id="code_postal"
                [(ngModel)]="currentFournisseur.code_postal"
                name="code_postal"
                placeholder="Code postal"
              />
            </div>

            <div class="form-field checkbox-field">
              <label>
                <input
                  type="checkbox"
                  [(ngModel)]="currentFournisseur.actif"
                  name="actif"
                />
                Fournisseur actif
              </label>
            </div>
          </div>

          <div class="form-actions">
            <button type="submit" class="btn-primary" [disabled]="!fournisseurForm.valid">
              {{ editingFournisseur ? 'Modifier' : 'Ajouter' }}
            </button>
            <button type="button" class="btn-secondary" (click)="cancelEdit()">Annuler</button>
          </div>
        </form>
      </div>

      <!-- Filtres -->
      <div class="filters-card">
        <div class="filters-grid">
          <div class="form-field">
            <label for="filterType">Filtrer par type</label>
            <select id="filterType" [(ngModel)]="filterType" (ngModelChange)="applyFilters()">
              <option value="">Tous les types</option>
              <option *ngFor="let t of types" [value]="t">{{ getTypeLabel(t) }}</option>
            </select>
          </div>

          <div class="form-field">
            <label for="filterActif">Filtrer par statut</label>
            <select id="filterActif" [(ngModel)]="filterActif" (ngModelChange)="applyFilters()">
              <option value="">Tous</option>
              <option value="true">Actifs</option>
              <option value="false">Inactifs</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Liste -->
      <div class="table-card">
        <h2>Liste des fournisseurs ({{ filteredFournisseurs.length }})</h2>

        <div class="table-container">
          <table>
            <thead>
              <tr>
                <th>Nom</th>
                <th>Type</th>
                <th>Contact</th>
                <th>Téléphone</th>
                <th>Email</th>
                <th>Ville</th>
                <th>Statut</th>
                <th>Actions</th>
              </tr>
            </thead>

            <tbody>
              <tr *ngFor="let fournisseur of filteredFournisseurs">
                <td>{{ fournisseur.nom }}</td>

                <td>
                  <span
                    class="type-badge"
                    [ngClass]="'type-' + ((fournisseur.type_fournisseur || 'AUTRE') | lowercase)"
                  >
                    {{ getTypeLabel(fournisseur.type_fournisseur) }}
                  </span>
                </td>

                <td>{{ fournisseur.contact_principal || '-' }}</td>
                <td>{{ fournisseur.telephone || '-' }}</td>
                <td>{{ fournisseur.email || '-' }}</td>
                <td>{{ fournisseur.ville || '-' }}</td>

                <td>
                  <span
                    class="status-badge"
                    [class]="fournisseur.actif ? 'status-active' : 'status-inactive'"
                  >
                    {{ fournisseur.actif ? 'Actif' : 'Inactif' }}
                  </span>
                </td>

                <td class="actions">
                  <button class="btn-edit" (click)="editFournisseur(fournisseur)">✏️</button>
                  <button class="btn-delete" (click)="deleteFournisseur(fournisseur)">🗑️</button>
                </td>
              </tr>

              <tr *ngIf="filteredFournisseurs.length === 0">
                <td colspan="8" class="no-data">Aucun fournisseur trouvé</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .container { padding: 16px; }
    .page-header { display:flex; align-items:center; justify-content:space-between; margin-bottom: 16px; }
    .btn-primary { background:#2563eb; color:#fff; border:none; padding:8px 12px; border-radius:8px; cursor:pointer; }
    .btn-secondary { background:#e5e7eb; border:none; padding:8px 12px; border-radius:8px; cursor:pointer; }
    .form-card, .filters-card, .table-card { background:#fff; border:1px solid #e5e7eb; border-radius:12px; padding:16px; margin-bottom:16px; }
    .form-grid { display:grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap:12px; }
    .form-field { display:flex; flex-direction:column; gap:6px; }
    .form-actions { display:flex; gap:8px; margin-top: 12px; }
    .table-container { overflow:auto; }
    table { width:100%; border-collapse: collapse; }
    th, td { padding: 8px 10px; border-bottom: 1px solid #eee; text-align:left; }
    .actions { display:flex; gap:6px; }
    .btn-edit, .btn-delete { border:none; background:transparent; cursor:pointer; }
    .type-badge { display:inline-block; padding:2px 8px; border-radius:999px; border:1px solid #e5e7eb; font-size:12px; }
    .status-badge { display:inline-block; padding:2px 8px; border-radius:999px; font-size:12px; }
    .status-active { background:#ecfdf5; color:#065f46; }
    .status-inactive { background:#fef2f2; color:#991b1b; }
    .no-data { text-align:center; color:#6b7280; }
  `]
})
export class FournisseursComponent implements OnInit {
  fournisseurs: Fournisseur[] = [];
  filteredFournisseurs: Fournisseur[] = [];
  currentFournisseur: Fournisseur = this.getEmptyFournisseur();
  showAddForm = false;
  editingFournisseur = false;

  filterType = '';
  filterActif = '';

  // valeurs EXACTES du back
  types: BackType[] = [
    'INTERIM',
    'AUTRE',
    'FOURNITURE',
    'LOCATION_SANS_CHAUFFEUR',
    'SOUS_TRAITANCE',
    'LOUAGEUR',
    'LOCATION_AVEC_CHAUFFEUR'
  ];

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.loadFournisseurs();
  }

  /** ====== READ ====== */
  loadFournisseurs() {
  this.api.getFournisseurs().subscribe({
    next: (res: any) => {
      const raw = Array.isArray(res) ? res : (res?.content || []);
      this.fournisseurs = raw.map((b: any) => ({
        id: b.id,
        nom: b.nom ?? b.code ?? '',                                     // <-- code -> nom
        type_fournisseur: (b.type_fournisseur ?? b.type ?? 'AUTRE') as BackType, // <-- type -> type_fournisseur
        telephone: b.telephone ?? '',
        email: b.email ?? '',
        ville: b.ville ?? '',
        adresse: b.adresse ?? '',
        code_postal: b.code_postal ?? b.codePostal ?? '',
        contact_principal: b.contact_principal ?? b.contact ?? '',
        actif: b.actif ?? true,
      }));
      this.applyFilters();
    },
    error: (err) => {
      console.error('Erreur chargement fournisseurs:', err);
      alert('Erreur lors du chargement des fournisseurs');
    }
  });
}


  /** ====== FILTERS ====== */
  applyFilters() {
    this.filteredFournisseurs = this.fournisseurs.filter(f => {
      const typeMatch = !this.filterType || f.type_fournisseur === (this.filterType as BackType);
      const actifMatch = !this.filterActif || String(f.actif) === this.filterActif;
      return typeMatch && actifMatch;
    });
  }

  /** ====== CREATE / UPDATE ====== */
  saveFournisseur() {
  const f = this.currentFournisseur;

  // ce que le back comprend aujourd'hui (entity): code/type/actif (+ id pour PUT)
  const payloadBack: any = {
    id: f.id ?? undefined,
    code: f.nom,                              // <-- nom -> code
    type: f.type_fournisseur,                 // <-- enum back
    actif: f.actif
    // les autres champs n'existent pas en base -> ignorés
  };

  if (this.editingFournisseur && f.id) {
    this.api.updateFournisseur(String(f.id), payloadBack).subscribe({
      next: () => { alert('Fournisseur modifié avec succès'); this.cancelEdit(); this.loadFournisseurs(); },
      error: (err) => { console.error('Erreur modification fournisseur:', err); alert('Erreur lors de la modification'); }
    });
  } else {
    delete payloadBack.id; // sécurité
    this.api.createFournisseur(payloadBack).subscribe({
      next: () => { alert('Fournisseur ajouté avec succès'); this.cancelEdit(); this.loadFournisseurs(); },
      error: (err) => { console.error('Erreur création fournisseur:', err); alert('Erreur lors de la création'); }
    });
  }
}


  editFournisseur(fournisseur: Fournisseur) {
    this.currentFournisseur = { ...fournisseur };
    this.editingFournisseur = true;
    this.showAddForm = false;
  }

  /** ====== DELETE ====== */
  deleteFournisseur(fournisseur: Fournisseur) {
    if (!fournisseur.id) return;
    if (!confirm('Êtes-vous sûr de vouloir supprimer ce fournisseur ?')) return;

    this.api.deleteFournisseur(String(fournisseur.id)).subscribe({
      next: () => {
        alert('Fournisseur supprimé avec succès');
        this.loadFournisseurs();
      },
      error: (err) => {
        console.error('Erreur suppression fournisseur:', err);
        alert('Erreur lors de la suppression');
      }
    });
  }

  /** ====== UI helpers ====== */
  cancelEdit() {
    this.currentFournisseur = this.getEmptyFournisseur();
    this.showAddForm = false;
    this.editingFournisseur = false;
  }

  getTypeLabel(type?: BackType): string {
    const map: Record<BackType, string> = {
      INTERIM: 'Intérim',
      AUTRE: 'Autre',
      FOURNITURE: 'Fourniture',
      LOCATION_SANS_CHAUFFEUR: 'Location sans chauffeur',
      LOCATION_AVEC_CHAUFFEUR: 'Location avec chauffeur',
      LOUAGEUR: 'Louageur',
      SOUS_TRAITANCE: 'Sous-traitance'
    };
    return type ? map[type] : 'Autre';
  }

  private getEmptyFournisseur(): Fournisseur {
    return {
      nom: '',
      type_fournisseur: 'AUTRE', // valeur par défaut existante côté back
      adresse: '',
      ville: '',
      code_postal: '',
      telephone: '',
      email: '',
      contact_principal: '',
      actif: true
    };
  }
}
