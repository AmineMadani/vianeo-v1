import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';

type ProfilBack = 'ENCADRANT' | 'OPERATIONNEL';

interface Personnel {
  id?: number;
  nom: string;
  prenom: string;
  /** valeurs EXACTES côté back */
  type_personnel: ProfilBack;
  /** affichage front, mappe vers back.taux */
  taux_journalier: number;
  /** affichage front, mappe vers back.metier */
  qualification?: string;
  actif: boolean;

  /** requis par le back (Utilisateur.entite NOT NULL) */
  entiteId?: number;

  // Ces deux champs n'existent pas dans l'entity actuelle -> affichage only
  telephone?: string;
  email?: string;

  created_at?: string;
  updated_at?: string;
}

@Component({
  selector: 'app-personnel',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <header class="page-header">
        <h1>Gestion du Personnel</h1>
        <button class="btn-primary" (click)="showAddForm = true">
          <span>+</span> Nouveau Personnel
        </button>
      </header>

      <!-- Formulaire d'ajout/modification -->
      <div class="form-card" *ngIf="showAddForm || editingPersonnel">
        <h2>{{ editingPersonnel ? 'Modifier' : 'Ajouter' }} un membre du personnel</h2>

        <form (ngSubmit)="savePersonnel()" #personnelForm="ngForm">
          <div class="form-grid">
            <div class="form-field">
              <label for="nom">Nom *</label>
              <input id="nom" type="text" [(ngModel)]="currentPersonnel.nom" name="nom" required placeholder="Nom de famille" />
            </div>

            <div class="form-field">
              <label for="prenom">Prénom *</label>
              <input id="prenom" type="text" [(ngModel)]="currentPersonnel.prenom" name="prenom" required placeholder="Prénom" />
            </div>

            <div class="form-field">
              <label for="type_personnel">Type de personnel *</label>
              <select id="type_personnel" [(ngModel)]="currentPersonnel.type_personnel" name="type_personnel" required>
                <option value="ENCADRANT">Encadrant</option>
                <option value="OPERATIONNEL">Opérationnel</option>
              </select>
            </div>

            <div class="form-field">
              <label for="taux_journalier">Taux journalier (€) *</label>
              <input id="taux_journalier" type="number" [(ngModel)]="currentPersonnel.taux_journalier" name="taux_journalier" required min="0" step="0.01" placeholder="0.00" />
            </div>

            <div class="form-field">
              <label for="qualification">Qualification</label>
              <input id="qualification" type="text" [(ngModel)]="currentPersonnel.qualification" name="qualification" placeholder="Qualification professionnelle" />
            </div>

            <!-- Entité (requis par le back). Remplace 1..N par tes vraies entités quand tu auras l'API -->
            <div class="form-field">
              <label for="entite">Entité (obligatoire)</label>
              <input id="entite" type="number" [(ngModel)]="currentPersonnel.entiteId" name="entiteId" required min="1" placeholder="ID entité (ex: 1)" />
            </div>

            <div class="form-field">
              <label for="telephone">Téléphone</label>
              <input id="telephone" type="tel" [(ngModel)]="currentPersonnel.telephone" name="telephone" placeholder="Numéro de téléphone" />
            </div>

            <div class="form-field">
              <label for="email">Email</label>
              <input id="email" type="email" [(ngModel)]="currentPersonnel.email" name="email" placeholder="Adresse email" />
            </div>

            <div class="form-field checkbox-field">
              <label>
                <input type="checkbox" [(ngModel)]="currentPersonnel.actif" name="actif" />
                Actif
              </label>
            </div>
          </div>

          <div class="form-actions">
            <button type="submit" class="btn-primary" [disabled]="!personnelForm.valid">
              {{ editingPersonnel ? 'Modifier' : 'Ajouter' }}
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
              <option value="ENCADRANT">Encadrant</option>
              <option value="OPERATIONNEL">Opérationnel</option>
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

      <!-- Statistiques -->
      <div class="stats-grid">
        <div class="stat-card">
          <h3>Total Personnel</h3>
          <div class="stat-value">{{ personnel.length }}</div>
        </div>
        <div class="stat-card">
          <h3>Encadrants</h3>
          <div class="stat-value">{{ getPersonnelByType('ENCADRANT').length }}</div>
        </div>
        <div class="stat-card">
          <h3>Opérationnels</h3>
          <div class="stat-value">{{ getPersonnelByType('OPERATIONNEL').length }}</div>
        </div>
        <div class="stat-card">
          <h3>Taux moyen</h3>
          <div class="stat-value">{{ getAverageTaux() | currency:'EUR':'symbol':'1.0-0' }}</div>
        </div>
      </div>

      <!-- Liste -->
      <div class="table-card">
        <h2>Liste du personnel ({{ filteredPersonnel.length }})</h2>

        <div class="table-container">
          <table>
            <thead>
              <tr>
                <th>Nom</th><th>Prénom</th><th>Type</th><th>Taux journalier</th>
                <th>Qualification</th><th>Téléphone</th><th>Email</th><th>Statut</th><th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let person of filteredPersonnel">
                <td>{{ person.nom }}</td>
                <td>{{ person.prenom }}</td>
                <td>
                  <span class="type-badge" [class]="'type-' + (person.type_personnel | lowercase)">
                    {{ person.type_personnel | titlecase }}
                  </span>
                </td>
                <td>{{ person.taux_journalier | currency:'EUR':'symbol':'1.2-2' }}</td>
                <td>{{ person.qualification || '-' }}</td>
                <td>{{ person.telephone || '-' }}</td>
                <td>{{ person.email || '-' }}</td>
                <td>
                  <span class="status-badge" [class]="person.actif ? 'status-active' : 'status-inactive'">
                    {{ person.actif ? 'Actif' : 'Inactif' }}
                  </span>
                </td>
                <td class="actions">
                  <button class="btn-edit" (click)="editPersonnel(person)">✏️</button>
                  <button class="btn-delete" (click)="deletePersonnel(person)">🗑️</button>
                </td>
              </tr>
              <tr *ngIf="filteredPersonnel.length === 0">
                <td colspan="9" class="no-data">Aucun personnel trouvé</td>
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
    .stats-grid { display:grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap:12px; margin-bottom:16px; }
    .stat-card { background:#f9fafb; border:1px solid #e5e7eb; border-radius:12px; padding:12px; }
    .stat-value { font-size:20px; font-weight:700; }
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
export class PersonnelComponent implements OnInit {
  personnel: Personnel[] = [];
  filteredPersonnel: Personnel[] = [];
  currentPersonnel: Personnel = this.getEmptyPersonnel();
  showAddForm = false;
  editingPersonnel = false;

  filterType = '';
  filterActif = '';

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.loadPersonnel();
  }

  /** ===== READ ===== */
  loadPersonnel() {
    this.api.getPersonnel().subscribe({
      next: (list: any[]) => {
        this.personnel = (list || []).map((b: any) => ({
          id: b.id,
          nom: b.nom,
          prenom: b.prenom,
          type_personnel: (b.type_personnel || b.profil || 'OPERATIONNEL') as ProfilBack,
          taux_journalier: Number(b.taux_journalier ?? b.taux ?? 0),
          qualification: b.qualification ?? b.metier ?? '',
          actif: b.actif ?? true,
          entiteId: b.entiteId ?? 1, // TODO: remplacer par sélection réelle d’entité
          telephone: b.telephone ?? '',
          email: b.email ?? ''
        }));
        this.applyFilters();
      },
      error: (err) => {
        console.error('Erreur chargement personnel:', err);
        alert('Erreur lors du chargement du personnel');
      }
    });
  }

  /** ===== CREATE / UPDATE ===== */
  savePersonnel() {
    const f = this.currentPersonnel;

    // payload attendu par le back (DTO)
    const payloadBack: any = {
      id: f.id ?? undefined,
      nom: f.nom,
      prenom: f.prenom,
      profil: (f.type_personnel || 'OPERATIONNEL').toUpperCase(),
      taux: f.taux_journalier,
      metier: f.qualification || '',
      actif: f.actif,
      entiteId: f.entiteId ?? 1
    };

    if (this.editingPersonnel && f.id) {
      this.api.updatePersonnel(String(f.id), payloadBack).subscribe({
        next: () => { alert('Personnel modifié avec succès'); this.cancelEdit(); this.loadPersonnel(); },
        error: (err) => { console.error('Erreur update personnel:', err); alert('Erreur lors de la modification'); }
      });
    } else {
      delete payloadBack.id;
      this.api.createPersonnel(payloadBack).subscribe({
        next: () => { alert('Personnel ajouté avec succès'); this.cancelEdit(); this.loadPersonnel(); },
        error: (err) => { console.error('Erreur create personnel:', err); alert("Erreur lors de l'ajout"); }
      });
    }
  }

  editPersonnel(person: Personnel) {
    this.currentPersonnel = { ...person };
    this.editingPersonnel = true;
    this.showAddForm = false;
  }

  deletePersonnel(person: Personnel) {
    if (!person.id) return;
    if (!confirm('Supprimer ce membre du personnel ?')) return;

    this.api.deletePersonnel(String(person.id)).subscribe({
      next: () => { alert('Personnel supprimé avec succès'); this.loadPersonnel(); },
      error: (err) => { console.error('Erreur suppression personnel:', err); alert('Erreur lors de la suppression'); }
    });
  }

  cancelEdit() {
    this.currentPersonnel = this.getEmptyPersonnel();
    this.showAddForm = false;
    this.editingPersonnel = false;
  }

  /** ===== Filtres & stats ===== */
  applyFilters() {
    this.filteredPersonnel = this.personnel.filter(person => {
      const typeMatch = !this.filterType || person.type_personnel === (this.filterType as ProfilBack);
      const actifMatch = !this.filterActif || String(person.actif) === this.filterActif;
      return typeMatch && actifMatch;
    });
  }

  getPersonnelByType(type: ProfilBack): Personnel[] {
    return this.personnel.filter(p => p.type_personnel === type && p.actif);
  }

  getAverageTaux(): number {
    if (this.personnel.length === 0) return 0;
    const total = this.personnel.reduce((sum, p) => sum + (p.taux_journalier || 0), 0);
    return Math.round((total / this.personnel.length) || 0);
  }

  private getEmptyPersonnel(): Personnel {
    return {
      nom: '',
      prenom: '',
      type_personnel: 'OPERATIONNEL',
      taux_journalier: 0,
      qualification: '',
      actif: true,
      entiteId: 1,     // TODO: remplacer par l’entité par défaut réelle
      telephone: '',
      email: ''
    };
  }
}
