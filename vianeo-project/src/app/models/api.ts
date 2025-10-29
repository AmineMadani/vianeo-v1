export type StatutRapport = 'DRAFT' | 'EN_ATTENTE_VALIDATION' | 'VALIDE' | 'REFUSE';
export type TypeTravail = 'J' | 'GD' | 'N';

export interface Rapport {
  id: number;
  chantierId: number;
  jour: string;           // 'YYYY-MM-DD'
  statut: StatutRapport;
  auteurId: number;
  commentaireCdt?: string;
}

export interface LignePersonnel {
  id?: number;
  rapportId: number;
  personnelId: number;
  lu?: TypeTravail; ma?: TypeTravail; me?: TypeTravail; je?: TypeTravail; ve?: TypeTravail; sa?: TypeTravail;
  coutJournalier?: number;
}

export interface LigneInterim { /* idem avec fournisseurId etc. */ }
export interface LigneMateriaux { /* ... */ }
// … idem pour les 8 lignes au besoin

export interface WeekRapportResponse {
  weekStart: string;        // lundi
  rapports: Rapport[];      // 7 jours
}
