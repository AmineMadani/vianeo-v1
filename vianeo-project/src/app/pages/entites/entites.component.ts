import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';

interface Entite {
  id?: string;
  nom: string;
  adresse?: string;
  ville?: string;
  code_postal?: string;
  telephone?: string;
  email?: string;
  siret?: string;
  created_at?: string;
  updated_at?: string;
}

@Component({
  selector: 'app-entites',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <header class="page-header">
        <h1>Gestion des Entités</h1>
        <button class="btn-primary" (click)="showAddForm = true">
          <span>+</span> Nouvelle Entité
        </button>
      </header>

      <!-- Formulaire d'ajout/modification -->
      <div class="form-card" *ngIf="showAddForm || editingEntite">
        <h2>{{ editingEntite ? 'Modifier' : 'Ajouter' }} une entité</h2>
        <form (ngSubmit)="saveEntite()" #entiteForm="ngForm">
          <div class="form-grid">
            <div class="form-field">
              <label for="nom">Nom *</label>
              <input 
                type="text" 
                id="nom"
                [(ngModel)]="currentEntite.nom" 
                name="nom"
                required
                placeholder="Nom de l'entité">
            </div>
            
            <div class="form-field">
              <label for="siret">SIRET</label>
              <input 
                type="text" 
                id="siret"
                [(ngModel)]="currentEntite.siret" 
                name="siret"
                placeholder="Numéro SIRET">
            </div>
            
            <div class="form-field">
              <label for="adresse">Adresse</label>
              <input 
                type="text" 
                id="adresse"
                [(ngModel)]="currentEntite.adresse" 
                name="adresse"
                placeholder="Adresse complète">
            </div>
            
            <div class="form-field">
              <label for="ville">Ville</label>
              <input 
                type="text" 
                id="ville"
                [(ngModel)]="currentEntite.ville" 
                name="ville"
                placeholder="Ville">
            </div>
            
            <div class="form-field">
              <label for="code_postal">Code Postal</label>
              <input 
                type="text" 
                id="code_postal"
                [(ngModel)]="currentEntite.code_postal" 
                name="code_postal"
                placeholder="Code postal">
            </div>
            
            <div class="form-field">
              <label for="telephone">Téléphone</label>
              <input 
                type="tel" 
                id="telephone"
                [(ngModel)]="currentEntite.telephone" 
                name="telephone"
                placeholder="Numéro de téléphone">
            </div>
            
            <div class="form-field">
              <label for="email">Email</label>
              <input 
                type="email" 
                id="email"
                [(ngModel)]="currentEntite.email" 
                name="email"
                placeholder="Adresse email">
            </div>
          </div>
          
          <div class="form-actions">
            <button type="submit" class="btn-primary" [disabled]="!entiteForm.valid">
              {{ editingEntite ? 'Modifier' : 'Ajouter' }}
            </button>
            <button type="button" class="btn-secondary" (click)="cancelEdit()">
              Annuler
            </button>
          </div>
        </form>
      </div>

      <!-- Liste des entités -->
      <div class="table-card">
        <h2>Liste des entités ({{ entites.length }})</h2>
        
        <div class="table-container">
          <table>
            <thead>
              <tr>
                <th>Nom</th>
                <th>SIRET</th>
                <th>Ville</th>
                <th>Téléphone</th>
                <th>Email</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let entite of entites">
                <td>{{ entite.nom }}</td>
                <td>{{ entite.siret || '-' }}</td>
                <td>{{ entite.ville || '-' }}</td>
                <td>{{ entite.telephone || '-' }}</td>
                <td>{{ entite.email || '-' }}</td>
                <td class="actions">
                  <button class="btn-edit" (click)="editEntite(entite)">
                    ✏️
                  </button>
                  <button class="btn-delete" (click)="deleteEntite(entite.id!)">
                    🗑️
                  </button>
                </td>
              </tr>
              <tr *ngIf="entites.length === 0">
                <td colspan="6" class="no-data">Aucune entité trouvée</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 20px;
    }

    .page-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 30px;
      padding-bottom: 20px;
      border-bottom: 2px solid #e5e7eb;
    }

    .page-header h1 {
      color: #1e40af;
      margin: 0;
    }

    .form-card, .table-card {
      background: white;
      border-radius: 12px;
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
      margin-bottom: 20px;
      padding: 24px;
      border: 1px solid #e1e8ed;
    }

    .form-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 20px;
      margin-bottom: 20px;
    }

    .form-field {
      display: flex;
      flex-direction: column;
    }

    .form-field label {
      font-weight: 500;
      margin-bottom: 8px;
      color: #374151;
    }

    .form-field input {
      padding: 12px 16px;
      border: 2px solid #e5e7eb;
      border-radius: 8px;
      font-size: 14px;
      transition: all 0.3s ease;
    }

    .form-field input:focus {
      outline: none;
      border-color: #3b82f6;
      box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
    }

    .form-actions {
      display: flex;
      gap: 12px;
      justify-content: flex-end;
    }

    .btn-primary, .btn-secondary, .btn-edit, .btn-delete {
      padding: 12px 24px;
      border: none;
      border-radius: 8px;
      cursor: pointer;
      font-weight: 500;
      transition: all 0.3s ease;
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .btn-primary {
      background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
      color: white;
    }

    .btn-primary:hover:not(:disabled) {
      background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
      transform: translateY(-1px);
    }

    .btn-primary:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }

    .btn-secondary {
      background: white;
      color: #6b7280;
      border: 2px solid #e5e7eb;
    }

    .btn-secondary:hover {
      border-color: #d1d5db;
      background: #f9fafb;
    }

    .table-container {
      overflow-x: auto;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 16px;
    }

    th, td {
      padding: 12px;
      text-align: left;
      border-bottom: 1px solid #e5e7eb;
    }

    th {
      background: #f8fafc;
      font-weight: 600;
      color: #374151;
    }

    .actions {
      display: flex;
      gap: 8px;
    }

    .btn-edit, .btn-delete {
      padding: 8px;
      font-size: 14px;
    }

    .btn-edit {
      background: #f59e0b;
      color: white;
    }

    .btn-edit:hover {
      background: #d97706;
    }

    .btn-delete {
      background: #ef4444;
      color: white;
    }

    .btn-delete:hover {
      background: #dc2626;
    }

    .no-data {
      text-align: center;
      color: #6b7280;
      font-style: italic;
    }

    @media (max-width: 768px) {
      .form-grid {
        grid-template-columns: 1fr;
      }
      
      .page-header {
        flex-direction: column;
        gap: 16px;
        align-items: stretch;
      }
      
      .form-actions {
        flex-direction: column;
      }
    }
  `]
})
export class EntitesComponent implements OnInit {
  entites: Entite[] = [];
  currentEntite: Entite = this.getEmptyEntite();
  showAddForm = false;
  editingEntite = false;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.loadEntites();
  }

  async loadEntites() {
    try {
      this.apiService.getEntites().subscribe({
        next: (data) => {
          this.entites = data || [];
        },
        error: (error) => {
          console.error('Erreur lors du chargement des entités:', error);
          alert('Erreur lors du chargement des entités');
        }
      });
    } catch (error) {
      console.error('Erreur lors du chargement des entités:', error);
      alert('Erreur lors du chargement des entités');
    }
  }

  async saveEntite() {
    try {
      if (this.editingEntite) {
        this.apiService.updateEntite(this.currentEntite.id!, this.currentEntite).subscribe({
          next: () => {
            alert('Entité modifiée avec succès');
            this.cancelEdit();
            this.loadEntites();
          },
          error: (error) => {
            console.error('Erreur lors de la modification:', error);
            alert('Erreur lors de la modification');
          }
        });
      } else {
        this.apiService.createEntite(this.currentEntite).subscribe({
          next: () => {
            alert('Entité ajoutée avec succès');
            this.cancelEdit();
            this.loadEntites();
          },
          error: (error) => {
            console.error('Erreur lors de l\'ajout:', error);
            alert('Erreur lors de l\'ajout');
          }
        });
      }
    } catch (error) {
      console.error('Erreur lors de la sauvegarde:', error);
      alert('Erreur lors de la sauvegarde');
    }
  }

  editEntite(entite: Entite) {
    this.currentEntite = { ...entite };
    this.editingEntite = true;
    this.showAddForm = false;
  }

  async deleteEntite(id: string) {
    if (!confirm('Êtes-vous sûr de vouloir supprimer cette entité ?')) {
      return;
    }

    try {
      this.apiService.deleteEntite(id).subscribe({
        next: () => {
          alert('Entité supprimée avec succès');
          this.loadEntites();
        },
        error: (error) => {
          console.error('Erreur lors de la suppression:', error);
          alert('Erreur lors de la suppression');
        }
      });
    } catch (error) {
      console.error('Erreur lors de la suppression:', error);
      alert('Erreur lors de la suppression');
    }
  }

  cancelEdit() {
    this.currentEntite = this.getEmptyEntite();
    this.showAddForm = false;
    this.editingEntite = false;
  }

  private getEmptyEntite(): Entite {
    return {
      nom: '',
      adresse: '',
      ville: '',
      code_postal: '',
      telephone: '',
      email: '',
      siret: ''
    };
  }
}