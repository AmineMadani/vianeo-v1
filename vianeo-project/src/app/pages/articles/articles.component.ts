import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';

interface Article {
  id?: number;
  designation: string;
  type_article: string;             // ex: 'materiel_interne' | 'location_materiel' | ...
  unite: string;                    // ex: 'unité', 'heure', 'jour', ...
  prix_unitaire: number;
  fournisseur_id?: number | null;
  description?: string;
  actif: boolean;
  created_at?: string;
  updated_at?: string;
  fournisseur?: { id?: number; nom: string };
  // fallback si ton backend renvoie un DTO plat
  fournisseur_nom?: string;
}

interface Fournisseur {
  id: number;
  nom: string;
}

@Component({
  selector: 'app-articles',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <header class="page-header">
        <h1>Gestion des Articles</h1>
        <button class="btn-primary" (click)="showAddForm = true">
          <span>+</span> Nouvel Article
        </button>
      </header>

      <!-- Formulaire d'ajout/modification -->
      <div class="form-card" *ngIf="showAddForm || editingArticle">
        <h2>{{ editingArticle ? 'Modifier' : 'Ajouter' }} un article</h2>
        <form (ngSubmit)="saveArticle()" #articleForm="ngForm">
          <div class="form-grid">
            <div class="form-field">
              <label for="designation">Désignation *</label>
              <input 
                type="text" 
                id="designation"
                [(ngModel)]="currentArticle.designation" 
                name="designation"
                required
                placeholder="Désignation de l'article">
            </div>
            
            <div class="form-field">
              <label for="type_article">Type d'article *</label>
              <select 
                id="type_article"
                [(ngModel)]="currentArticle.type_article" 
                name="type_article"
                required>
                <option value="materiel_interne">Matériel interne</option>
                <option value="location_materiel">Location matériel</option>
                <option value="transport">Transport</option>
                <option value="prestation_externe">Prestation externe</option>
                <option value="materiaux">Matériaux</option>
              </select>
            </div>
            
            <div class="form-field">
              <label for="unite">Unité</label>
              <select 
                id="unite"
                [(ngModel)]="currentArticle.unite" 
                name="unite">
                <option value="unité">Unité</option>
                <option value="heure">Heure</option>
                <option value="jour">Jour</option>
                <option value="kg">Kilogramme</option>
                <option value="m">Mètre</option>
                <option value="m²">Mètre carré</option>
                <option value="m³">Mètre cube</option>
                <option value="litre">Litre</option>
                <option value="tonne">Tonne</option>
              </select>
            </div>
            
            <div class="form-field">
              <label for="prix_unitaire">Prix unitaire (€) *</label>
              <input 
                type="number" 
                id="prix_unitaire"
                [(ngModel)]="currentArticle.prix_unitaire" 
                name="prix_unitaire"
                required
                min="0"
                step="0.01"
                placeholder="0.00">
            </div>
            
            <div class="form-field">
              <label for="fournisseur_id">Fournisseur</label>
              <select 
                id="fournisseur_id"
                [(ngModel)]="currentArticle.fournisseur_id" 
                name="fournisseur_id">
                <option [ngValue]="null">Aucun fournisseur</option>
                <option *ngFor="let fournisseur of fournisseurs" [ngValue]="fournisseur.id">
                  {{ fournisseur.nom }}
                </option>
              </select>
            </div>
            
            <div class="form-field full-width">
              <label for="description">Description</label>
              <textarea 
                id="description"
                [(ngModel)]="currentArticle.description" 
                name="description"
                rows="3"
                placeholder="Description détaillée de l'article">
              </textarea>
            </div>
            
            <div class="form-field checkbox-field">
              <label>
                <input 
                  type="checkbox" 
                  [(ngModel)]="currentArticle.actif" 
                  name="actif">
                Article actif
              </label>
            </div>
          </div>
          
          <div class="form-actions">
            <button type="submit" class="btn-primary" [disabled]="!articleForm.valid">
              {{ editingArticle ? 'Modifier' : 'Ajouter' }}
            </button>
            <button type="button" class="btn-secondary" (click)="cancelEdit()">
              Annuler
            </button>
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
              <option value="materiel_interne">Matériel interne</option>
              <option value="location_materiel">Location matériel</option>
              <option value="transport">Transport</option>
              <option value="prestation_externe">Prestation externe</option>
              <option value="materiaux">Matériaux</option>
            </select>
          </div>
          
          <div class="form-field">
            <label for="filterFournisseur">Filtrer par fournisseur</label>
            <select id="filterFournisseur" [(ngModel)]="filterFournisseur" (ngModelChange)="applyFilters()">
              <option value="">Tous les fournisseurs</option>
              <option *ngFor="let fournisseur of fournisseurs" [value]="fournisseur.id">
                {{ fournisseur.nom }}
              </option>
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
          <h3>Total Articles</h3>
          <div class="stat-value">{{ articles.length }}</div>
        </div>
        <div class="stat-card">
          <h3>Articles Actifs</h3>
          <div class="stat-value">{{ getActiveArticles().length }}</div>
        </div>
        <div class="stat-card">
          <h3>Prix Moyen</h3>
          <div class="stat-value">{{ getAveragePrice() | currency:'EUR':'symbol':'1.0-0' }}</div>
        </div>
        <div class="stat-card">
          <h3>Types Différents</h3>
          <div class="stat-value">{{ getUniqueTypes().length }}</div>
        </div>
      </div>

      <!-- Liste des articles -->
      <div class="table-card">
        <h2>Liste des articles ({{ filteredArticles.length }})</h2>
        
        <div class="table-container">
          <table>
            <thead>
              <tr>
                <th>Désignation</th>
                <th>Type</th>
                <th>Unité</th>
                <th>Prix unitaire</th>
                <th>Fournisseur</th>
                <th>Description</th>
                <th>Statut</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let article of filteredArticles">
                <td>{{ article.designation }}</td>
                <td>
                  <span class="type-badge" [class]="'type-' + article.type_article.replace('_', '-')">
                    {{ getTypeLabel(article.type_article) }}
                  </span>
                </td>
                <td>{{ article.unite }}</td>
                <td>{{ article.prix_unitaire | currency:'EUR':'symbol':'1.2-2' }}</td>
                <td>{{ article.fournisseur?.nom || '-' }}</td>
                <td class="description">{{ article.description || '-' }}</td>
                <td>
                  <span class="status-badge" [class]="article.actif ? 'status-active' : 'status-inactive'">
                    {{ article.actif ? 'Actif' : 'Inactif' }}
                  </span>
                </td>
                <td class="actions">
                  <button class="btn-edit" (click)="editArticle(article)">✏️</button>
                  <button class="btn-delete" (click)="deleteArticle(article)">🗑️</button>
                </td>
              </tr>
              <tr *ngIf="filteredArticles.length === 0">
                <td colspan="8" class="no-data">Aucun article trouvé</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `,
  styles: [`
    /* garde tes styles existants; rien de spécifique à changer ici */
  `]
})
export class ArticlesComponent implements OnInit {
  articles: Article[] = [];
  filteredArticles: Article[] = [];
  fournisseurs: Fournisseur[] = [];
  currentArticle: Article = this.getEmptyArticle();
  showAddForm = false;
  editingArticle = false;
  filterType = '';
  filterFournisseur = '';
  filterActif = '';

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.loadFournisseurs();
    this.loadArticles();
  }

  /** ====== READ ====== */
  loadFournisseurs() {
    // on charge uniquement les fournisseurs actifs (utile dans un select)
    this.api.getActiveFournisseurs().subscribe({
      next: (list) => this.fournisseurs = (list || []) as Fournisseur[],
      error: (err) => console.error('Erreur chargement fournisseurs actifs:', err)
    });
  }

  loadArticles() {
    this.api.getArticles().subscribe({
      next: (res: any) => {
        const items: any[] = Array.isArray(res) ? res : (res?.content || []);
        this.articles = items.map((a: any) => ({
          ...a,
          fournisseur: a.fournisseur ?? (a.fournisseur_nom ? { id: a.fournisseur_id ?? null, nom: a.fournisseur_nom } : undefined)
        })) as Article[];
        this.applyFilters();
      },
      error: (err) => {
        console.error('Erreur chargement articles:', err);
        alert('Erreur lors du chargement des articles');
      }
    });
  }

  /** ====== FILTERS ====== */
  applyFilters() {
    this.filteredArticles = this.articles.filter(article => {
      const typeMatch = !this.filterType || article.type_article === this.filterType;
      const fournisseurMatch = !this.filterFournisseur || (article.fournisseur_id ?? article.fournisseur?.id) === +this.filterFournisseur;
      const actifMatch = !this.filterActif || String(article.actif) === this.filterActif;
      return typeMatch && fournisseurMatch && actifMatch;
    });
  }

  /** ====== CREATE / UPDATE ====== */
  saveArticle() {
    const payload: Article = {
      ...this.currentArticle,
      fournisseur_id: this.currentArticle.fournisseur_id ?? null
    };

    if (this.editingArticle && payload.id) {
      this.api.updateArticle(String(payload.id), payload).subscribe({
        next: () => {
          alert('Article modifié avec succès');
          this.cancelEdit();
          this.loadArticles();
        },
        error: (err) => {
          console.error('Erreur modification article:', err);
          alert('Erreur lors de la modification');
        }
      });
    } else {
      this.api.createArticle(payload).subscribe({
        next: () => {
          alert('Article ajouté avec succès');
          this.cancelEdit();
          this.loadArticles();
        },
        error: (err) => {
          console.error('Erreur création article:', err);
          alert('Erreur lors de la création');
        }
      });
    }
  }

  editArticle(article: Article) {
    this.currentArticle = {
      ...article,
      fournisseur_id: article.fournisseur_id ?? article.fournisseur?.id ?? null
    };
    this.editingArticle = true;
    this.showAddForm = false;
  }

  /** ====== DELETE ====== */
  deleteArticle(article: Article) {
    if (!article.id) return;
    if (!confirm('Êtes-vous sûr de vouloir supprimer cet article ?')) return;

    this.api.deleteArticle(String(article.id)).subscribe({
      next: () => {
        alert('Article supprimé avec succès');
        this.loadArticles();
      },
      error: (err) => {
        console.error('Erreur suppression article:', err);
        alert('Erreur lors de la suppression');
      }
    });
  }

  /** ====== UI helpers ====== */
  cancelEdit() {
    this.currentArticle = this.getEmptyArticle();
    this.showAddForm = false;
    this.editingArticle = false;
  }

  getTypeLabel(type: string): string {
    const labels: Record<string, string> = {
      materiel_interne: 'Matériel interne',
      location_materiel: 'Location matériel',
      transport: 'Transport',
      prestation_externe: 'Prestation externe',
      materiaux: 'Matériaux'
    };
    return labels[type] || type;
  }

  getActiveArticles(): Article[] {
    return this.articles.filter(a => a.actif);
  }

  getAveragePrice(): number {
    if (this.articles.length === 0) return 0;
    const total = this.articles.reduce((sum, a) => sum + (a.prix_unitaire || 0), 0);
    return Math.round((total / this.articles.length) || 0);
  }

  getUniqueTypes(): string[] {
    return [...new Set(this.articles.map(a => a.type_article))];
  }

  private getEmptyArticle(): Article {
    return {
      designation: '',
      type_article: 'materiel_interne',
      unite: 'unité',
      prix_unitaire: 0,
      fournisseur_id: null,
      description: '',
      actif: true
    };
  }
}
