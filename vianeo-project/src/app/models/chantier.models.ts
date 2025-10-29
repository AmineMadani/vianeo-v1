// --- Types utilitaires pour les jours ---
export type JourSemaine = 'lundi' | 'mardi' | 'mercredi' | 'jeudi' | 'vendredi' | 'samedi';
export const JOURS_SEMAINE: JourSemaine[] = [
  'lundi', 'mardi', 'mercredi', 'jeudi', 'vendredi', 'samedi'
];

export interface JourTravail {
  heures: number;
  typePrestation: 'J' | 'N' | 'GD';
}

// ===== Personnel =====
interface PersonnelFixed {
  id: string;
  nom: string;
  prenom: string;
  type: 'encadrant' | 'operationnel';
  tauxJournalier: number;
  /** ← AJOUT : l’utilisateur réel (backend) choisi dans la liste */
  utilisateurId?: number | null;
}


// ⚠️ mapped type séparé, puis on fait une intersection:
export type Personnel = PersonnelFixed & Record<JourSemaine, JourTravail>;

// ===== Intérim =====
interface InterimFixed {
  id: string;
  nom: string;
  prenom: string;
  fournisseur: string;
  tauxJournalier: number;
}

export type Interim = InterimFixed & Record<JourSemaine, JourTravail>;


/* export interface Personnel {
  id: string;
  nom: string;
  prenom: string;
  type: 'encadrant' | 'operationnel';
  lundi: { heures: number; typePrestation: 'J' | 'N' | 'GD' };
  mardi: { heures: number; typePrestation: 'J' | 'N' | 'GD' };
  mercredi: { heures: number; typePrestation: 'J' | 'N' | 'GD' };
  jeudi: { heures: number; typePrestation: 'J' | 'N' | 'GD' };
  vendredi: { heures: number; typePrestation: 'J' | 'N' | 'GD' };
  samedi: { heures: number; typePrestation: 'J' | 'N' | 'GD' };
  tauxJournalier: number;
} 



export interface Interim {
  id: string;
  nom: string;
  prenom: string;
  fournisseur: string;
  lundi: { heures: number; typePrestation: 'J' | 'N' | 'GD' };
  mardi: { heures: number; typePrestation: 'J' | 'N' | 'GD' };
  mercredi: { heures: number; typePrestation: 'J' | 'N' | 'GD' };
  jeudi: { heures: number; typePrestation: 'J' | 'N' | 'GD' };
  vendredi: { heures: number; typePrestation: 'J' | 'N' | 'GD' };
  samedi: { heures: number; typePrestation: 'J' | 'N' | 'GD' };
  tauxJournalier: number;
}*/

export interface MaterielInterne {
  id: string;
  designation: string;
  fournisseur: string;
  quantite: number;
  pu: number;
}

export interface LocationMateriel {
  id: string;
  designation: string;
  fournisseur: string;
  quantite: number;
  pu: number;
  avecChauffeur: boolean;
}

export interface Transport {
  id: string;
  designation: string;
  fournisseur: string;
  quantite: number;
  pu: number;
}

export interface PrestationExterne {
  id: string;
  designation: string;
  fournisseur: string;
  quantite: number;
  pu: number;
}

export interface Materiaux {
  id: string;
  designation: string;
  fournisseur: string;
  quantite: number;
  pu: number;
}

export interface RapportChantier {
  id: string;
  date: Date;
  semaine: number;
  chantier: string;
  responsable: string;
  personnel: Personnel[];
  interim: Interim[];
  materielInterne: MaterielInterne[];
  locationMateriel: LocationMateriel[];
  transport: Transport[];
  prestationExterne: PrestationExterne[];
  materiaux: Materiaux[];
  observations: string;
  status: 'brouillon' | 'valide' | 'soumis';
}

export interface ChantierInfo {
  nom: string;
  adresse: string;
  ville: string;
  codePostal: string;
  responsable: string;
  dateDebut: Date;
  dateFin?: Date;
}

// === UI uniquement ===
export type JourCode = 'lu' | 'ma' | 'me' | 'je' | 've' | 'sa';

export interface QteJour {
  qte: number;       // quantité pour le jour
}

export interface WeeklyQtyBase {
  id: string;
  designation: string;
  pu: number;
  lu: QteJour; ma: QteJour; me: QteJour; je: QteJour; ve: QteJour; sa: QteJour;
}

export interface WeeklyQtyWithSupplier extends WeeklyQtyBase {
  fournisseur: string;
}