// src/app/pages/rapport-chantier/rapport-chantier.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { PersonnelSectionComponent } from '../../components/personnel-section/personnel-section.component';
import { InterimSectionComponent } from '../../components/interim-section/interim-section.component';

import { RapportApi, Rapport as RapportDto } from '../../services/rapport.api';
import { ApiService } from '../../services/api.service';

import {
  WeeklyQtySectionComponent,
  WeeklyQtyItem
} from '../../components/weekly-qty-section/weekly-qty-section.component';

import { ReferentielService } from '../../services/referentiel.service';
import { firstValueFrom } from 'rxjs';

function toIsoDate(d: Date) { return d.toISOString().slice(0, 10); }
function mondayOf(d: Date) {
  const x = new Date(d); const day = x.getDay() || 7;
  if (day > 1) x.setDate(x.getDate() - (day - 1));
  return toIsoDate(x);
}

@Component({
  selector: 'app-rapport-chantier',
  standalone: true,
  imports: [
    CommonModule, FormsModule,
    PersonnelSectionComponent, InterimSectionComponent,
    WeeklyQtySectionComponent
  ],
  templateUrl: './rapport-chantier.component.html'
})
export class RapportChantierComponent implements OnInit {
  // ----------- En-tête infos ----------
  chantierId = 1;
  weekStart = mondayOf(new Date());
  dateJour = toIsoDate(new Date());
  weekNumber = this.getWeekNumber(new Date());
  chantierLabel = 'Nom du chantier';
  responsableNom = '';
  referenceRapport = '';
  observations = '';
  rapportDuJour?: RapportDto;

  private readonly DAY_KEYS = ['di','lu','ma','me','je','ve','sa'] as const;


  // ----------- Données brutes ----------
  personnelDisponible: any[] = []; // référentiel

  // Sélections (depuis les composants enfants)
  personnelEncadrant: any[] = [];
  personnelOperationnel: any[] = [];

  // UI historique (table encadrants – si tu t’en sers encore)
  openAddEncadrant = false;
  encadrants: Array<{
  id: string;
  personnel: any | null;
  taux?: number;
  lu: 'J' | 'GD' | 'N' | null;
  ma: 'J' | 'GD' | 'N' | null;
  me: 'J' | 'GD' | 'N' | null;
  je: 'J' | 'GD' | 'N' | null;
  ve: 'J' | 'GD' | 'N' | null;
  sa: 'J' | 'GD' | 'N' | null;
} & { [key: string]: any }> = [];

  encadrantToAdd: { personnelId: number | null; taux: number | null } = {
    personnelId: null,
    taux: 350
  };

  rapport!: any;

  // Sections “Excel”
  interim: any[] = [];
  fournisseurs: string[] = [];

  matInterne: WeeklyQtyItem[] = [];
  locSsCh: WeeklyQtyItem[] = [];
  locAvecCh: WeeklyQtyItem[] = [];
  transport: WeeklyQtyItem[] = [];
  prestaExt: WeeklyQtyItem[] = [];
  materiaux: WeeklyQtyItem[] = [];

  articlesMatInterne: any[] = [];
  articlesLocSansCh: any[] = [];
  articlesLocAvecCh: any[] = [];
  articlesTransport: any[] = [];
  articlesPrestaExt: any[] = [];
  articlesMateriaux: any[] = [];

  fournisseursLocSansCh: any[] = [];
  fournisseursLocAvecCh: any[] = [];
  fournisseursTransport: any[] = [];
  fournisseursPrestaExt: any[] = [];
  fournisseursMateriaux: any[] = [];

  // ----------- Totaux ----------
  totalPersonnel = 0;
  totalInterim = 0;
  totalGeneral = 0;

  constructor(
    private rapportApi: RapportApi,
    private api: ApiService,
    private refSrv: ReferentielService
  ) {
    this.rapport = {
      date: this.dateJour,
      semaine: this.weekNumber,
      chantier: this.chantierLabel,
      responsable: this.responsableNom,
      personnel: [],
      interim: [],
      observations: ''
    };
  }

  async ngOnInit() {
    const start = async () => {
      this.chargerReferentiels();
      await firstValueFrom(this.rapportApi.openToday({ chantierId: this.chantierId, prefillYesterday: true }));
      this.chargerSemaineEtRapportDuJour();
    };

    const token = this.api.getToken?.();
    if (token) await start();
    else {
      const iv = setInterval(async () => {
        const t = this.api.getToken?.();
        if (t) { clearInterval(iv); clearTimeout(timeoutKill); await start(); }
      }, 200);
      const timeoutKill = setTimeout(() => clearInterval(iv), 10000);
    }
  }

  // ================== Référentiels ==================
  private chargerReferentiels() {
    this.api.getActiveFournisseurs().subscribe({
      next: (arr: any[]) =>
        this.fournisseurs = (arr || []).map(f => f?.nom ?? f?.libelle ?? f?.code ?? '').filter(Boolean),
      error: err => console.error('Erreur chargement fournisseurs:', err)
    });

    this.api.getPersonnel().subscribe({
      next: (list: any[]) => {
        this.personnelDisponible = (list || []).map(p => ({
          id: p.id,
          nom: p.nom,
          prenom: p.prenom,
          type: (p.profil || '').toUpperCase() === 'ENCADRANT' ? 'encadrant' : 'operationnel',
          tauxJournalier: Number(p.taux ?? 0)
        }));
      },
      error: err => console.error('Erreur chargement personnel:', err)
    });

    // Articles/Fournisseurs
    this.refSrv.getArticles({ cat: 'INTERNE' }).subscribe(a => this.articlesMatInterne = a);
    this.refSrv.getArticles({ type: 'MATERIEL_SANS_CHAUFFEUR' }).subscribe(a => this.articlesLocSansCh = a);
    this.refSrv.getArticles({ type: 'MATERIEL_AVEC_CHAUFFEUR' }).subscribe(a => this.articlesLocAvecCh = a);
    this.refSrv.getArticles({ type: 'TRANSPORT' }).subscribe(a => this.articlesTransport = a);
    this.refSrv.getArticles({ type: 'PRESTATION_EXT' }).subscribe(a => this.articlesPrestaExt = a);
    this.refSrv.getArticles({ type: 'MATERIAUX' }).subscribe(a => this.articlesMateriaux = a);

    this.refSrv.getFournisseurs({ type: 'LOCATION_SANS_CHAUFFEUR', actif: true }).subscribe(f => this.fournisseursLocSansCh = f);
    this.refSrv.getFournisseurs({ type: 'LOCATION_AVEC_CHAUFFEUR', actif: true }).subscribe(f => this.fournisseursLocAvecCh = f);
    this.refSrv.getFournisseurs({ type: 'LOUAGEUR', actif: true }).subscribe(f => this.fournisseursTransport = f);
    this.refSrv.getFournisseurs({ type: 'SOUS_TRAITANCE', actif: true }).subscribe(f => this.fournisseursPrestaExt = f);
    this.refSrv.getFournisseurs({ type: 'FOURNITURE', actif: true }).subscribe(f => this.fournisseursMateriaux = f);
  }

  // ================== Semaine / Rapport du jour ==================
  private chargerSemaineEtRapportDuJour() {
    this.rapportApi.getWeek(this.chantierId, this.weekStart).subscribe({
      next: res => {
        const today = this.dateJour;
        const found = (res as any).days?.find((d: any) => d.jour === today);
        if (found) {
          this.rapportDuJour = { id: found.rapportId, jour: today, statut: found.statut } as RapportDto;
          this.reloadTotals();
        }
      },
      error: err => console.error('[RC] getWeek error:', err)
    });
  }

  /*
  get canSave(): boolean {
  // Ajuste la condition à ton besoin : au moins 1 ligne valide à persister
  return !!this.rapportDuJour && (
    (this.personnelEncadrant?.some(p => Number(p.heures) > 0) ?? false) ||
    (this.personnelOperationnel?.some(p => Number(p.heures) > 0) ?? false) ||
    (this.interim?.some(i => Number(i.quantite) > 0 && i.fournisseurId) ?? false)
    // idem pour les autres sections si besoin…
  );
} */

// ---------- CONDITIONS GLOBALES ----------

canSave(): boolean {
  return !!this.rapportDuJour && (
    this.canSavePersonnelEncadrant() ||
    this.canSavePersonnelOperationnel() ||
    this.canSaveInterim() ||
    this.canSaveWeekly('matInterne') ||
    this.canSaveWeekly('locSsCh') ||
    this.canSaveWeekly('locAvecCh') ||
    this.canSaveWeekly('transport') ||
    this.canSaveWeekly('prestaExt') ||
    this.canSaveWeekly('materiaux')
  );
}

// ---------- PERSONNEL (Conditions + Save ciblés) ----------

private _personnelNewRowReadyAnyDay(row: any): boolean {
  const days = ['lundi','mardi','mercredi','jeudi','vendredi','samedi'];
  // il faut un "utilisateur" sélectionné (on tolère nom/prenom si pas d'id)
  const hasUser =
    !!row?.utilisateurId || !!row?.utilisateurId ||
    !!row?.id || !!row?.nom; // selon ce que tu stockes quand tu choisis un nom

  if (!hasUser) return false;

  for (const d of days) {
    const c = row?.[d];
    const h = Number(c?.heures || 0);
    const t = c?.typePrestation as ('J'|'GD'|'N'|undefined|null);
    if (h > 0 && t) return true;
  }
  return false;
}



canSavePersonnelEncadrant(): boolean {
  if (!this.rapportDuJour) return false;
  return (this.personnelEncadrant || []).some(r => this._personnelNewRowReadyAnyDay(r));
}

canSavePersonnelOperationnel(): boolean {
  if (!this.rapportDuJour) return false;
  return (this.personnelOperationnel || []).some(r => this._personnelNewRowReadyAnyDay(r));
}

async savePersonnelEncadrant() {
  if (!this.rapportDuJour) return;
  const rapportId = this.rapportDuJour.id;
  const ops: Promise<any>[] = [];
  const days = ['lundi','mardi','mercredi','jeudi','vendredi','samedi'];

  for (const row of (this.personnelEncadrant || [])) {
    const userId = row?.utilisateurId ?? row?.utilisateurId ?? row?.id;
    if (!userId) continue;

    for (const d of days) {
      const cell = row?.[d];
      const h = Number(cell?.heures || 0);
      const t = cell?.typePrestation as ('J'|'GD'|'N'|null);
      if (h > 0 && t) {
        ops.push(firstValueFrom(this.rapportApi.addLignePersonnel({
          rapportId,
          utilisateurId: userId,
          categorie: 'ENCADRANT', // ou 'OPERATIONNEL'
          typeTravail: t,
          quantite: h,
          pu: Number(row.tauxJournalier ?? 0) || 0
        })));
      }
    }
  }

  if (ops.length === 0) { console.warn('[SAVE encadrant] 0 requête'); return; }
  await Promise.all(ops);
  this.reloadTotals();
}

async savePersonnelOperationnel() {
  if (!this.rapportDuJour) return;
  const rapportId = this.rapportDuJour.id;
  const ops: Promise<any>[] = [];
  const days = ['lundi','mardi','mercredi','jeudi','vendredi','samedi'];

  for (const row of (this.personnelOperationnel || [])) {
    const userId = row?.utilisateurId ?? row?.utilisateurId ?? row?.id;
    if (!userId) continue;

    for (const d of days) {
      const cell = row?.[d];
      const h = Number(cell?.heures || 0);
      const t = cell?.typePrestation as ('J'|'GD'|'N'|null);
      if (h > 0 && t) {
        ops.push(firstValueFrom(this.rapportApi.addLignePersonnel({
          rapportId,
          utilisateurId: userId,
          categorie: 'OPERATIONNEL',
          typeTravail: t,
          quantite: h,
          pu: Number(row?.tauxJournalier ?? row?.taux ?? 0) || 0
        })));
      }
    }
  }

  if (ops.length === 0) { console.warn('[SAVE operationnel] 0 requête'); return; }
  await Promise.all(ops);
  this.reloadTotals();
}



// ---------- INTERIM (Condition + Save ciblé) ----------

canSaveInterim(): boolean {
  if (!this.rapportDuJour) return false;
  return (this.interim || []).some(it => {
    if (!it?.fournisseurId || !(it?.nom?.trim()?.length > 0)) return false;
    return this.DAY_KEYS.some(k => Number(it?.[k]?.qte || 0) > 0);
  });
}


async saveInterim() {
  if (!this.rapportDuJour) return;
  const rapportId = this.rapportDuJour.id;
  const ops: Promise<any>[] = [];

  for (const it of (this.interim || [])) {
    if (!it?.fournisseurId || !(it?.nom?.trim()?.length > 0)) continue;

    for (const k of this.DAY_KEYS) {
      const d: any = it?.[k];
      const q = Number(d?.qte || 0);
      if (q > 0) {
        ops.push(firstValueFrom(this.rapportApi.addLigneInterim({
          rapportId,
          fournisseurId: it.fournisseurId,
          nom: it.nom,
          quantite: q,
          pu: Number(it.pu || 0)
        })));
      }
    }
  }

  if (ops.length === 0) { console.warn('[SAVE interim] 0 requête'); return; }
  await Promise.all(ops);
  this.reloadTotals();
}


// ---------- WEEKLY (Condition + Save ciblé) ----------

private _weeklyItemReadyAnyDay(section: string, item: any): boolean {
  if (!item?.articleId) return false;
  const requiresFournisseur = (section !== 'matInterne'); // matériaux : toujours facultatif ici
  if (requiresFournisseur && !item?.fournisseurId) return false;

  return this.DAY_KEYS.some(k => Number(item?.[k]?.qte || 0) > 0);
}


canSaveWeekly(section: 'matInterne'|'locSsCh'|'locAvecCh'|'transport'|'prestaExt'|'materiaux'): boolean {
  if (!this.rapportDuJour) return false;
  const list = (this as any)[section] as WeeklyQtyItem[];
  return (list || []).some(it => this._weeklyItemReadyAnyDay(section, it));
}


async saveWeeklySection(section: 'matInterne'|'locSsCh'|'locAvecCh'|'transport'|'prestaExt'|'materiaux') {
  if (!this.rapportDuJour) return;
  const rapportId = this.rapportDuJour.id;
  const list = (this as any)[section] as WeeklyQtyItem[];
  const calls: Promise<any>[] = [];

  for (const item of (list || [])) {
    if (!this._weeklyItemReadyAnyDay(section, item)) continue;

    for (const k of this.DAY_KEYS) {
      const d: any = (item as any)[k];
      const q = Number(d?.qte || 0);
      if (q <= 0) continue;

      const pCommon = { rapportId, articleId: item.articleId!, quantite: q, pu: Number(item.pu || 0) };

      switch (section) {
        case 'matInterne':
          calls.push(firstValueFrom(this.rapportApi.addLigneMatInterne(pCommon))); break;
        case 'locSsCh':
          calls.push(firstValueFrom(this.rapportApi.addLigneLocSsCh({ ...pCommon, fournisseurId: item.fournisseurId! }))); break;
        case 'locAvecCh':
          calls.push(firstValueFrom(this.rapportApi.addLigneLocAvecCh({ ...pCommon, fournisseurId: item.fournisseurId! }))); break;
        case 'transport':
          calls.push(firstValueFrom(this.rapportApi.addLigneTransport({ ...pCommon, fournisseurId: item.fournisseurId! }))); break;
        case 'prestaExt':
          calls.push(firstValueFrom(this.rapportApi.addLignePrestaExt({ ...pCommon, fournisseurId: item.fournisseurId! }))); break;
        case 'materiaux':
          calls.push(firstValueFrom(this.rapportApi.addLigneMateriaux({ ...pCommon, ...(item.fournisseurId ? { fournisseurId: item.fournisseurId } : {}) }))); break;
      }
    }
  }

  if (calls.length === 0) { console.warn(`[SAVE ${section}] 0 requête`); return; }
  await Promise.all(calls);
  this.reloadTotals();
}




  private reloadTotals() {
    if (!this.rapportDuJour) return;
    this.rapportApi.getTotaux(this.rapportDuJour.id).subscribe(t => {
      this.totalPersonnel = t.totalPersonnel ?? 0;
      this.totalInterim   = t.totalInterim ?? 0;
      this.totalGeneral   = t.totalGeneral ?? (this.totalPersonnel + this.totalInterim);
    });
  }

  // ================== Statut & workflow ==================
  get statutRapport(): string | undefined { return this.rapportDuJour?.statut; }
  get canSoumettre(): boolean { return this.statutRapport === 'DRAFT'; }
  get canValider(): boolean   { return this.statutRapport === 'EN_ATTENTE_VALIDATION'; }

  onSoumettre() {
    if (!this.rapportDuJour || !this.canSoumettre) return;
    this.rapportApi.soumettre(this.rapportDuJour.id).subscribe({
      next: () => this.chargerSemaineEtRapportDuJour(),
      error: (e) => {
        console.error('[Soumettre] échec', e);
        alert("Impossible de soumettre : statut ou champs obligatoires manquants.");
      }
    });
  }

  async onValider() {
    if (!this.rapportDuJour) return;
    const id = this.rapportDuJour.id;

    try {
      if (this.statutRapport === 'DRAFT') {
        await firstValueFrom(this.rapportApi.soumettre(id));
        this.chargerSemaineEtRapportDuJour();
      }
      if (this.statutRapport === 'EN_ATTENTE_VALIDATION') {
        await firstValueFrom(this.rapportApi.valider(id));
        this.chargerSemaineEtRapportDuJour();
      } else if (this.statutRapport !== 'VALIDE') {
        alert("Le rapport n'est pas dans un état validable.");
      }
    } catch (e) {
      console.error('[Valider] ERROR', e);
      alert("Validation impossible (vérifie les champs obligatoires).");
    }
  }

  // ================== Changement de date ==================
  onChangeDate() {
    this.weekStart = mondayOf(new Date(this.dateJour));
    this.weekNumber = this.getWeekNumber(new Date(this.dateJour));
    this.chargerSemaineEtRapportDuJour();
  }

  private getWeekNumber(date: Date): number {
    const target = new Date(date.valueOf());
    const dayNr = (date.getDay() + 6) % 7;
    target.setDate(target.getDate() - dayNr + 3);
    const firstThursday = new Date(target.getFullYear(), 0, 4);
    const diff = (target.getTime() - firstThursday.getTime()) / 86400000;
    return 1 + Math.floor(diff / 7);
  }

  // ================== Helpers “jour courant” ==================
  currentDayKey(): 'di'|'lu'|'ma'|'me'|'je'|'ve'|'sa' {
  const weekday = new Date(this.dateJour).getDay(); // 0..6 (0=Dim)
  return ['di','lu','ma','me','je','ve','sa'][weekday] as any;
}
prettyDayLabel(): string {
  return new Date(this.dateJour)
    .toLocaleDateString(undefined, { weekday: 'short', day: '2-digit', month: '2-digit' });
}


  // ================== Encadrants (UI historique) ==================
  get availablePersonnelEncadrant() {
    return (this.personnelDisponible || []).filter((p: any) => p.type === 'encadrant');
  }
  get availablePersonnelOperationnel() {
    return (this.personnelDisponible || []).filter((p: any) => p.type === 'operationnel');
  }

  trackById = (_: number, row: any) => row.id;

  onAddEncadrant(form: { personnelId: number | null; taux: number | null }) {
  if (!form.personnelId) return;
  const p = this.availablePersonnelEncadrant.find((x: any) => x.id === form.personnelId);
  if (!p) return;

  const r: any = {
    id: Math.random().toString(36).slice(2),
    personnel: p,
    taux: Number(form.taux ?? p.tauxJournalier ?? 0),
    // heures (H) + type (T) pour chaque jour
    luH: 0, luT: null,
    maH: 0, maT: null,
    meH: 0, meT: null,
    jeH: 0, jeT: null,
    veH: 0, veT: null,
    saH: 0, saT: null,
  };

  this.encadrants.push(r);
  this.encadrantToAdd = { personnelId: null, taux: 350 };
  this.openAddEncadrant = false;
}


  removeEncadrant(row: any) {
    this.encadrants = this.encadrants.filter(r => r !== row);
  }

async onDayChange(row: any, dayKey: 'lu'|'ma'|'me'|'je'|'ve'|'sa', value: 'J'|'GD'|'N'|null) {
  row[dayKey] = value;
  if (!value) return;
  if (!this.rapportDuJour || !row?.utilisateurId) return;

  const rapportId = this.rapportDuJour.id; // ← capture

  try {
    await firstValueFrom(this.rapportApi.addLignePersonnel({
      rapportId,
      utilisateurId: row.personnel.id,
      categorie: 'ENCADRANT',
      typeTravail: value,
      quantite: 1,
      pu: Number(row.tauxJournalier ?? 0) || 0
    }));
    this.reloadTotals();
  } catch (e) {
    console.error('[addLignePersonnel] ERROR', e);
  }
}

  get totalJoursEncadrant(): number {
    return this.encadrants.reduce((acc, r) =>
      acc
      + (r.lu ? 1 : 0)
      + (r.ma ? 1 : 0)
      + (r.me ? 1 : 0)
      + (r.je ? 1 : 0)
      + (r.ve ? 1 : 0)
      + (r.sa ? 1 : 0)
    , 0);
  }

  // ================== Sauvegarde des SECTIONS ==================
  private findArticleLabel(id?: number): string | null {
    const pools = [
      this.articlesMatInterne, this.articlesLocSansCh, this.articlesLocAvecCh,
      this.articlesTransport, this.articlesPrestaExt, this.articlesMateriaux
    ];
    for (const arr of pools) {
      const a = (arr || []).find((x: any) => x?.id === id);
      if (a) return a.libelle ?? a.designation ?? a.code ?? null;
    }
    return null;
  }

  private findFournisseurLabel(id?: number): string | null {
    const pools = [
      this.fournisseursLocSansCh, this.fournisseursLocAvecCh,
      this.fournisseursTransport, this.fournisseursPrestaExt, this.fournisseursMateriaux
    ];
    for (const arr of pools) {
      const f = (arr || []).find((x: any) => x?.id === id);
      if (f) return f.nom ?? f.libelle ?? f.code ?? null;
    }
    return null;
  }

  private explainWhyNotSaved(section: string, list: any[], key: string) {
    console.group(`[SAVE ${section}] diagnostic`);
    console.log('Jour courant (key):', key, '| dateJour:', this.dateJour);
    console.log('Nb items:', list?.length ?? 0);
    (list || []).forEach((it, i) => {
      const d = (it as any)[key];
      console.log(`#${i+1}`, {
        articleId: it?.articleId,
        fournisseurId: it?.fournisseurId,
        pu: it?.pu,
        dayObject: d,
        qte: d?.qte
      });
    });
    console.warn('→ Aucune requête créée. Causes possibles: qte=0/undefined pour le jour courant, article non choisi, fournisseur manquant pour cette section.');
    console.groupEnd();
  }

  // ---------- Sauvegarde du PERSONNEL (encadrant + opérationnel) ----------
  private readDayFromPersonnelRow(row: any, key: string): { heures: number; type: 'J'|'GD'|'N'|null } {
    if (row?.[key]?.heures != null || row?.[key]?.typePrestation) {
      return {
        heures: Number(row[key]?.heures ?? 0),
        type: (row[key]?.typePrestation ?? null) as any
      };
    }
    return {
      heures: Number(row?.[`${key}Heures`] ?? 1),
      type: (row?.[key] ?? null) as any
    };
  }

private async savePersonnel() {
  if (!this.rapportDuJour) return;
  const rapportId = this.rapportDuJour.id; // ← capture sûre

  const k = this.currentDayKey();
  const qtyKey = `${k}H`;
  const typeKey = `${k}T`;

  const poster = async (rows: any[], categorie: 'ENCADRANT'|'OPERATIONNEL') => {
    const ops: Array<Promise<any>> = [];
    for (const row of (rows || [])) {
      const h = Number(row?.[qtyKey] || 0);
      const t = row?.[typeKey] as ('J'|'GD'|'N'|null);
      const userId = row?.utilisateurId;
      if (h > 0 && t && userId) {
        ops.push(firstValueFrom(this.rapportApi.addLignePersonnel({
          rapportId, // ← utilise la constante
          utilisateurId: userId,
          categorie,
          typeTravail: t,
          quantite: h,
          pu: Number(row.taux ?? row.personnel?.tauxJournalier ?? 0) || 0
        })));
      }
    }
    if (ops.length === 0) {
      console.warn(`[SAVE personnel/${categorie}] 0 requête (heures=0 ? type manquant ? id utilisateur manquant ?)`);
      return;
    }
    await Promise.all(ops);
    console.info(`[SAVE personnel/${categorie}] OK (${ops.length})`);
  };

  await poster(this.encadrants, 'ENCADRANT');
  await poster(this.personnelOperationnel, 'OPERATIONNEL');
}



  // ---------- Sauvegarde des sections “tableur” ----------
async onSectionChange(section: 'matInterne'|'locSsCh'|'locAvecCh'|'transport'|'prestaExt'|'materiaux') {
  if (!this.rapportDuJour) return;
  const rapportId = this.rapportDuJour.id; // ← capture

  const key = this.currentDayKey();
  const list = (this as any)[section] as WeeklyQtyItem[];
  const calls: Promise<any>[] = [];

  for (const item of (list || [])) {
    const day: any = (item as any)[key];
    const q = Number(day?.qte || 0);
    const articleOk = !!item.articleId;
    const fournisseurOk = section === 'matInterne' ? true : !!item.fournisseurId;

    if (q > 0 && articleOk && fournisseurOk) {
      const pCommon = { rapportId, articleId: item.articleId!, quantite: q, pu: Number(item.pu || 0) };
      switch (section) {
        case 'matInterne':  calls.push(firstValueFrom(this.rapportApi.addLigneMatInterne(pCommon))); break;
        case 'locSsCh':     calls.push(firstValueFrom(this.rapportApi.addLigneLocSsCh({ ...pCommon, fournisseurId: item.fournisseurId! }))); break;
        case 'locAvecCh':   calls.push(firstValueFrom(this.rapportApi.addLigneLocAvecCh({ ...pCommon, fournisseurId: item.fournisseurId! }))); break;
        case 'transport':   calls.push(firstValueFrom(this.rapportApi.addLigneTransport({ ...pCommon, fournisseurId: item.fournisseurId! }))); break;
        case 'prestaExt':   calls.push(firstValueFrom(this.rapportApi.addLignePrestaExt({ ...pCommon, fournisseurId: item.fournisseurId! }))); break;
        case 'materiaux':   calls.push(firstValueFrom(this.rapportApi.addLigneMateriaux({ ...pCommon, ...(item.fournisseurId ? { fournisseurId: item.fournisseurId } : {}) }))); break;
      }
    }
  }

  if (calls.length === 0) { this.explainWhyNotSaved(section, list || [], key); return; }
  await Promise.all(calls);
  console.info(`[SAVE ${section}] OK (${calls.length} requête(s))`);
  this.reloadTotals();
}


  // ================== SAVE ALL ==================
  saveBusy = false;
/*
 async saveAll() {
  if (!this.rapportDuJour || this.saveBusy) return;
  this.saveBusy = true;
  try {
    await this.savePersonnel(); // ← d’abord le personnel

    const sections: Array<'matInterne'|'locSsCh'|'locAvecCh'|'transport'|'prestaExt'|'materiaux'> =
      ['matInterne','locSsCh','locAvecCh','transport','prestaExt','materiaux'];
    for (const s of sections) await this.onSectionChange(s);

    console.info('[SAVE ALL] OK');
  } catch (e) {
    console.error('[SAVE ALL] ERROR', e);
    alert('Une erreur est survenue pendant l’enregistrement.');
  } finally {
    this.saveBusy = false;
    this.reloadTotals();
  }
}*/
async saveAll() {
  if (!this.rapportDuJour || this.saveBusy) return;
  this.saveBusy = true;
  try {
    // 1) Personnel
    await this.savePersonnelEncadrant();
    await this.savePersonnelOperationnel();

    // 2) Interim
    await this.saveInterim();

    // 3) Weekly sections
    await this.saveWeeklySection('matInterne');
    await this.saveWeeklySection('locSsCh');
    await this.saveWeeklySection('locAvecCh');
    await this.saveWeeklySection('transport');
    await this.saveWeeklySection('prestaExt');
    await this.saveWeeklySection('materiaux');

    console.info('[SAVE ALL] OK');
  } catch (e) {
    console.error('[SAVE ALL] ERROR', e);
    alert('Une erreur est survenue pendant l’enregistrement.');
  } finally {
    this.saveBusy = false;
    this.reloadTotals();
  }
}



  // ==== Hooks des composants (si tu les utilises côté UI composants) ====
  updatePersonnelEncadrant(personnel: any[]) {
    this.personnelEncadrant = personnel || [];
    this.rebuildRapportPersonnel();
  }

  updatePersonnelOperationnel(personnel: any[]) {
    this.personnelOperationnel = personnel || [];
    this.rebuildRapportPersonnel();
  }

  updateInterim(list: any[]) {
  this.interim = list || [];
  // Si tu veux rafraîchir les totaux côté back tout de suite :
  this.reloadTotals();
}

  private rebuildRapportPersonnel() {
    this.rapport.personnel = [
      ...(this.personnelEncadrant || []).map((p: any) => ({ ...p, type: 'encadrant' })),
      ...(this.personnelOperationnel || []).map((p: any) => ({ ...p, type: 'operationnel' }))
    ];
    this.reloadTotals();
  }
}
