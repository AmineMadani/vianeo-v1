import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

type DayKey = 'lu'|'ma'|'me'|'je'|'ve'|'sa';

export interface WeeklyQtyItem {
  id: string;

  // ▶️ IDs alignés avec le backend
  articleId?: number;
  fournisseurId?: number;

  // affichage seulement (optionnel)
  designation?: string;

  pu?: number;

  // quantités par jour (mêmes clés que chez toi)
  lu?: { qte?: number };
  ma?: { qte?: number };
  me?: { qte?: number };
  je?: { qte?: number };
  ve?: { qte?: number };
  sa?: { qte?: number };
}

@Component({
  selector: 'app-weekly-qty-section',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
  <div class="section-card">
    <div class="section-header" (click)="toggle()">
      <span>{{ title }} ({{ items.length }})</span>
      <span class="expand-icon" [class.expanded]="expanded">▼</span>
    </div>

    <div class="section-content" *ngIf="expanded">
      <!-- Header -->
      <div class="personnel-header"
           [style.gridTemplateColumns]="gridCols"
           style="display:grid; gap:8px; font-weight:600; margin-bottom:12px; padding:0 12px; color:#6b7280;">
        <span>Désignation</span>
        <span *ngIf="showSupplier">Fournisseur</span>
        <span>Lu</span><span>Ma</span><span>Me</span><span>Je</span><span>Ve</span><span>Sa</span>
        <span>Total</span>
        <span>P.U.</span>
        <span>Actions</span>
      </div>

      <!-- Rows -->
      <div *ngFor="let it of items; trackBy: trackById"
           [style.gridTemplateColumns]="gridCols"
           style="display:grid; gap:8px; align-items:center; padding:12px; background:white; border-radius:8px; margin-bottom:8px; border:1px solid #e5e7eb;">

        <!-- Désignation = liste d'Articles (envoie articleId) -->
        <select class="form-control"
                [ngModel]="it.articleId ?? null"
                (ngModelChange)="onArticleChange(it, $event)">
          <option [ngValue]="null">Sélectionner...</option>
          <option *ngFor="let a of articles" [ngValue]="a.id">
            {{ a.designation || a.code }}
          </option>
        </select>

        <!-- Fournisseur (optionnel selon section) -->
        <ng-container *ngIf="showSupplier">
          <select class="form-control"
                  [ngModel]="it.fournisseurId ?? null"
                  (ngModelChange)="onFournisseurChange(it, $event)">
            <option [ngValue]="null">Sélectionner...</option>
            <option *ngFor="let f of fournisseurs" [ngValue]="f.id">
              {{ f.nom || f.code }}
            </option>
          </select>
        </ng-container>

        <!-- Jours -->
        <ng-container *ngFor="let d of dayKeys">
          <input type="number" min="0" step="0.5"
                 [ngModel]="getQte(it, d)"
                 (ngModelChange)="setQte(it, d, $event)"
                 class="form-control" placeholder="0">
        </ng-container>

        <!-- Total ligne (qté * PU) -->
        <span>{{ rowQty(it) * (it.pu || 0) | number:'1.2-2' }}</span>

        <!-- PU -->
        <input type="number" min="0" step="0.01"
               [(ngModel)]="it.pu" (ngModelChange)="emit()" class="form-control" placeholder="P.U.">

        <button class="remove-button" (click)="remove(it.id)">×</button>
      </div>

      <button class="add-button" (click)="add()"><span>+</span> Ajouter</button>

      <!-- Totaux section -->
      <div class="totals-section" *ngIf="items.length">
        <div class="total-row">
          <span>Total section:</span>
          <span>{{ sectionTotal() | currency:'EUR':'symbol':'1.2-2' }}</span>
        </div>
      </div>
    </div>
  </div>
  `,
  styles: [`
    .form-control { width: 100%; }
    .remove-button {
      background:#ef4444; color:#fff; border:none; border-radius:8px;
      width:32px; height:32px; line-height:32px; cursor:pointer;
    }
    .add-button {
      margin-top:8px; background:#16a34a; color:#fff; border:none;
      border-radius:8px; padding:8px 12px; cursor:pointer;
    }
  `]
})
export class WeeklyQtySectionComponent {
  @Input() title = '';
  @Input() items: WeeklyQtyItem[] = [];

  /** cache/affiche la colonne fournisseur (false pour Mat Interne) */
  @Input() showSupplier = true;

  /** listes référentiel */
  @Input() articles: { id: number; code: string; designation?: string }[] = [];
  @Input() fournisseurs: { id: number; nom?: string; code?: string }[] = [];

  @Output() itemsChange = new EventEmitter<WeeklyQtyItem[]>();

  expanded = true;
  dayKeys: DayKey[] = ['lu','ma','me','je','ve','sa'];

  get gridCols(): string {
    return `2fr ${this.showSupplier ? '1.2fr ' : ''} repeat(6,1fr) 0.9fr 0.9fr auto`;
  }

  toggle() { this.expanded = !this.expanded; }
  trackById = (_: number, it: WeeklyQtyItem) => it.id;

  add() {
    const id = Math.random().toString(36).slice(2, 9);
    this.items.push({
      id,
      articleId: undefined,
      fournisseurId: this.showSupplier ? undefined : undefined,
      designation: '',
      pu: 0,
      lu:{qte:0}, ma:{qte:0}, me:{qte:0}, je:{qte:0}, ve:{qte:0}, sa:{qte:0},
    });
    this.emit();
  }

  remove(id: string) {
    this.items = this.items.filter(x => x.id !== id);
    this.emit();
  }

  onArticleChange(row: WeeklyQtyItem, articleId: number | null) {
    row.articleId = articleId ?? undefined;
    const a = this.articles.find(x => x.id === articleId!);
    row.designation = a?.designation || a?.code || '';
    this.emit();
  }

  onFournisseurChange(row: WeeklyQtyItem, fournisseurId: number | null) {
    row.fournisseurId = fournisseurId ?? undefined;
    this.emit();
  }

  getQte(it: WeeklyQtyItem, d: DayKey): number {
    return Number(it[d]?.qte || 0);
  }
  setQte(it: WeeklyQtyItem, d: DayKey, v: number) {
    it[d] = it[d] || {};
    it[d]!.qte = Number(v || 0);
    this.emit();
  }

  rowQty(it: WeeklyQtyItem): number {
    return this.dayKeys.reduce((s, d) => s + this.getQte(it, d), 0);
  }

  sectionTotal(): number {
    return (this.items || []).reduce((s, it) => s + this.rowQty(it) * Number(it.pu || 0), 0);
  }

  emit() { this.itemsChange.emit(this.items); }
}
