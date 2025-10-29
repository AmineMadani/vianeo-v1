import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';

interface Chantier {
  id?: number;
  nom: string;
  adresse?: string;
  ville?: string;
  code_postal?: string;
  responsable?: string;
  date_debut?: string;   // ISO 'YYYY-MM-DD'
  date_fin?: string;     // ISO
  statut: 'en_preparation' | 'en_cours' | 'termine' | 'suspendu';
  entite_id?: number | null;
  created_at?: string;
  updated_at?: string;
  entite?: { id?: number; nom: string };
}

interface Entite {
  id: number;
  nom: string;
}

@Component({
  selector: 'app-chantiers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <header class="page-header">
        <h1>Gestion des Chantiers</h1>
        <button class="btn-primary" (click)="showAddForm = true">
          <span>+</span> Nouveau Chantier
        </button>
      </header>

      <!-- Formulaire d'ajout/modification -->
      <div class="form-card" *ngIf="showAddForm || editingChantier">
        <h2>{{ editingChantier ? 'Modifier' : 'Ajouter' }} un chantier</h2>
        <form (ngSubmit)="saveChantier()" #chantierForm="ngForm">
          <div class="form-grid">
            <div class="form-field">
              <label for="nom">Nom du chantier *</label>
              <input 
                type="text" id="nom"
                [(ngModel)]="currentChantier.nom" name="nom"
                required placeholder="Nom du chantier">
            </div>
            
            <div class="form-field">
              <label for="responsable">Responsable</label>
              <input 
                type="text" id="responsable"
                [(ngModel)]="currentChantier.responsable" name="responsable"
                placeholder="Nom du responsable">
            </div>
            
            <div class="form-field">
              <label for="entite_id">Entité</label>
              <select id="entite_id" [(ngModel)]="currentChantier.entite_id" name="entite_id">
                <option [ngValue]="null">Aucune entité</option>
                <option *ngFor="let entite of entites" [ngValue]="entite.id">
                  {{ entite.nom }}
                </option>
              </select>
            </div>
            
            <div class="form-field">
              <label for="statut">Statut *</label>
              <select id="statut" [(ngModel)]="currentChantier.statut" name="statut" required>
                <option value="en_preparation">En préparation</option>
                <option value="en_cours">En cours</option>
                <option value="termine">Terminé</option>
                <option value="suspendu">Suspendu</option>
              </select>
            </div>
            
            <div class="form-field">
              <label for="date_debut">Date de début</label>
              <input type="date" id="date_debut" [(ngModel)]="currentChantier.date_debut" name="date_debut">
            </div>
            
            <div class="form-field">
              <label for="date_fin">Date de fin</label>
              <input type="date" id="date_fin" [(ngModel)]="currentChantier.date_fin" name="date_fin">
            </div>
            
            <div class="form-field">
              <label for="adresse">Adresse</label>
              <input type="text" id="adresse" [(ngModel)]="currentChantier.adresse" name="adresse" placeholder="Adresse du chantier">
            </div>
            
            <div class="form-field">
              <label for="ville">Ville</label>
              <input type="text" id="ville" [(ngModel)]="currentChantier.ville" name="ville" placeholder="Ville">
            </div>
            
            <div class="form-field">
              <label for="code_postal">Code Postal</label>
              <input type="text" id="code_postal" [(ngModel)]="currentChantier.code_postal" name="code_postal" placeholder="Code postal">
            </div>
          </div>
          
          <div class="form-actions">
            <button type="submit" class="btn-primary" [disabled]="!chantierForm.valid">
              {{ editingChantier ? 'Modifier' : 'Ajouter' }}
            </button>
            <button type="button" class="btn-secondary" (click)="cancelEdit()">Annuler</button>
          </div>
        </form>
      </div>

      <!-- Filtres -->
      <div class="filters-card">
        <div class="filters-grid">
          <div class="form-field">
            <label for="filterStatut">Filtrer par statut</label>
            <select id="filterStatut" [(ngModel)]="filterStatut" (ngModelChange)="applyFilters()">
              <option value="">Tous les statuts</option>
              <option value="en_preparation">En préparation</option>
              <option value="en_cours">En cours</option>
              <option value="termine">Terminé</option>
              <option value="suspendu">Suspendu</option>
            </select>
          </div>
          
          <div class="form-field">
            <label for="filterEntite">Filtrer par entité</label>
            <select id="filterEntite" [(ngModel)]="filterEntite" (ngModelChange)="applyFilters()">
              <option value="">Toutes les entités</option>
              <option *ngFor="let entite of entites" [value]="entite.id">
                {{ entite.nom }}
              </option>
            </select>
          </div>
        </div>
      </div>

      <!-- Statistiques -->
      <div class="stats-grid">
        <div class="stat-card">
          <h3>Total Chantiers</h3>
          <div class="stat-value">{{ chantiers.length }}</div>
        </div>
        <div class="stat-card">
          <h3>En Cours</h3>
          <div class="stat-value">{{ getChantiersByStatut('en_cours').length }}</div>
        </div>
        <div class="stat-card">
          <h3>En Préparation</h3>
          <div class="stat-value">{{ getChantiersByStatut('en_preparation').length }}</div>
        </div>
        <div class="stat-card">
          <h3>Terminés</h3>
          <div class="stat-value">{{ getChantiersByStatut('termine').length }}</div>
        </div>
      </div>

      <!-- Liste des chantiers -->
      <div class="table-card">
        <h2>Liste des chantiers ({{ filteredChantiers.length }})</h2>
        
        <div class="table-container">
          <table>
            <thead>
              <tr>
                <th>Nom</th>
                <th>Responsable</th>
                <th>Entité</th>
                <th>Ville</th>
                <th>Date début</th>
                <th>Date fin</th>
                <th>Statut</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let chantier of filteredChantiers">
                <td>{{ chantier.nom }}</td>
                <td>{{ chantier.responsable || '-' }}</td>
                <td>{{ chantier.entite?.nom || '-' }}</td>
                <td>{{ chantier.ville || '-' }}</td>
                <td>{{ chantier.date_debut ? (chantier.date_debut | date:'dd/MM/yyyy') : '-' }}</td>
                <td>{{ chantier.date_fin ? (chantier.date_fin | date:'dd/MM/yyyy') : '-' }}</td>
                <td>
                  <ng-container *ngIf="chantier?.statut as s">
                    <span class="status-badge" [class]="'status-' + s.replace('_','-')"></span>
                  </ng-container>
                </td>
                <td class="actions">
                  <button class="btn-edit" (click)="editChantier(chantier)">✏️</button>
                  <button class="btn-delete" (click)="deleteChantier(chantier)">🗑️</button>
                </td>
              </tr>
              <tr *ngIf="filteredChantiers.length === 0">
                <td colspan="8" class="no-data">Aucun chantier trouvé</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `,
  styles: [`
    /* (garde tes styles existants si tu veux; raccourci ici pour la réponse) */
  `]
})
export class ChantiersComponent implements OnInit {
  chantiers: Chantier[] = [];
  filteredChantiers: Chantier[] = [];
  entites: Entite[] = [];
  currentChantier: Chantier = this.getEmptyChantier();
  showAddForm = false;
  editingChantier = false;
  filterStatut = '';
  filterEntite = '';

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.loadEntites();
    this.loadChantiers();
  }

  /** ========= Lectures ========= */
  loadEntites() {
    this.api.getEntites().subscribe({
      next: (list) => this.entites = (list || []) as Entite[],
      error: (err) => console.error('Erreur chargement entités:', err)
    });
  }

  loadChantiers() {
  this.api.getChantiers().subscribe({
    next: (res: any) => {
      const items = Array.isArray(res) ? res : (res?.content || []);

      this.chantiers = items.map((c: any) => ({
        ...c,
        // si le back renvoie entite_nom/entite_id (DTO plat), on reconstruit un objet entite
        entite: c.entite ?? (c.entite_nom ? { id: c.entite_id ?? null, nom: c.entite_nom } : undefined)
      }));

      this.applyFilters();
    },
    error: (err) => {
      console.error('Erreur chargement chantiers:', err);
      alert('Erreur lors du chargement des chantiers');
    }
  });
}

  /** ========= Filtres ========= */
  applyFilters() {
    this.filteredChantiers = this.chantiers.filter(chantier => {
      const statutMatch = !this.filterStatut || chantier.statut === this.filterStatut;
      const entiteMatch = !this.filterEntite || (chantier.entite_id ?? chantier.entite?.id) === +this.filterEntite;
      return statutMatch && entiteMatch;
    });
  }

  /** ========= CRUD ========= */
  saveChantier() {
    const payload: any = {
      ...this.currentChantier,
      entite_id: this.currentChantier.entite_id ?? null
    };

    if (this.editingChantier && this.currentChantier.id) {
      this.api.updateChantier(String(this.currentChantier.id), payload).subscribe({
        next: () => {
          alert('Chantier modifié avec succès');
          this.cancelEdit();
          this.loadChantiers();
        },
        error: (err) => {
          console.error('Erreur modification chantier:', err);
          alert('Erreur lors de la modification');
        }
      });
    } else {
      this.api.createChantier(payload).subscribe({
        next: () => {
          alert('Chantier ajouté avec succès');
          this.cancelEdit();
          this.loadChantiers();
        },
        error: (err) => {
          console.error('Erreur création chantier:', err);
          alert('Erreur lors de la création');
        }
      });
    }
  }

  editChantier(chantier: Chantier) {
    this.currentChantier = {
      ...chantier,
      entite_id: chantier.entite_id ?? chantier.entite?.id ?? null
    };
    this.editingChantier = true;
    this.showAddForm = false;
  }

  deleteChantier(chantier: Chantier) {
    if (!chantier.id) return;
    if (!confirm('Êtes-vous sûr de vouloir supprimer ce chantier ?')) return;

    this.api.deleteChantier(String(chantier.id)).subscribe({
      next: () => {
        alert('Chantier supprimé avec succès');
        this.loadChantiers();
      },
      error: (err) => {
        console.error('Erreur suppression chantier:', err);
        alert('Erreur lors de la suppression');
      }
    });
  }

  cancelEdit() {
    this.currentChantier = this.getEmptyChantier();
    this.showAddForm = false;
    this.editingChantier = false;
  }

  /** ========= Helpers UI ========= */
  getStatutLabel(statut: string): string {
    const labels: Record<string, string> = {
      en_preparation: 'En préparation',
      en_cours: 'En cours',
      termine: 'Terminé',
      suspendu: 'Suspendu'
    };
    return labels[statut] ?? statut;
  }

  getChantiersByStatut(statut: 'en_preparation' | 'en_cours' | 'termine' | 'suspendu'): Chantier[] {
    return this.chantiers.filter(c => c.statut === statut);
  }

  private getEmptyChantier(): Chantier {
    return {
      nom: '',
      adresse: '',
      ville: '',
      code_postal: '',
      responsable: '',
      date_debut: '',
      date_fin: '',
      statut: 'en_preparation',
      entite_id: null
    };
  }
}
