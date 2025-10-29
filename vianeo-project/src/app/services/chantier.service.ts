import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { RapportChantier, Personnel, Interim, MaterielInterne, LocationMateriel, Transport, PrestationExterne, Materiaux } from '../models/chantier.models';

@Injectable({
  providedIn: 'root'
})
export class ChantierService {
  private currentRapport = new BehaviorSubject<RapportChantier>({
    id: '',
    date: new Date(),
    semaine: this.getWeekNumber(new Date()),
    chantier: '',
    responsable: '',
    personnel: [],
    interim: [],
    materielInterne: [],
    locationMateriel: [],
    transport: [],
    prestationExterne: [],
    materiaux: [],
    observations: '',
    status: 'brouillon'
  });

  private fournisseurs = [
    'ADECCO', 'ADYSSEI', 'ARPO INTERIM', 'ARTEMIS SUCCESS', 'MANPOWER', 'RANDSTAD'
  ];

  private personnelDisponible = [
    { nom: 'Dupont', prenom: 'Jean', type: 'encadrant' as const, tauxJournalier: 350 },
    { nom: 'Martin', prenom: 'Pierre', type: 'operationnel' as const, tauxJournalier: 280 },
    { nom: 'Durand', prenom: 'Marie', type: 'encadrant' as const, tauxJournalier: 375 },
    { nom: 'Bernard', prenom: 'Paul', type: 'operationnel' as const, tauxJournalier: 290 }
  ];

  getCurrentRapport(): Observable<RapportChantier> {
    return this.currentRapport.asObservable();
  }

  updateRapport(rapport: Partial<RapportChantier>): void {
    const current = this.currentRapport.value;
    this.currentRapport.next({ ...current, ...rapport });
  }

  addPersonnel(personnel: Personnel): void {
    const current = this.currentRapport.value;
    personnel.id = this.generateId();
    current.personnel.push(personnel);
    this.currentRapport.next(current);
  }

  removePersonnel(id: string): void {
    const current = this.currentRapport.value;
    current.personnel = current.personnel.filter(p => p.id !== id);
    this.currentRapport.next(current);
  }

  addInterim(interim: Interim): void {
    const current = this.currentRapport.value;
    interim.id = this.generateId();
    current.interim.push(interim);
    this.currentRapport.next(current);
  }

  removeInterim(id: string): void {
    const current = this.currentRapport.value;
    current.interim = current.interim.filter(i => i.id !== id);
    this.currentRapport.next(current);
  }

  addMaterielInterne(materiel: MaterielInterne): void {
    const current = this.currentRapport.value;
    materiel.id = this.generateId();
    current.materielInterne.push(materiel);
    this.currentRapport.next(current);
  }

  removeMaterielInterne(id: string): void {
    const current = this.currentRapport.value;
    current.materielInterne = current.materielInterne.filter(m => m.id !== id);
    this.currentRapport.next(current);
  }

  getFournisseurs(): string[] {
    return this.fournisseurs;
  }

  getPersonnelDisponible() {
    return this.personnelDisponible;
  }

  calculateTotalPersonnel(): number {
    const current = this.currentRapport.value;
    return current.personnel.reduce((total, p) => {
      const jours = (p.lundi?.heures || 0) + (p.mardi?.heures || 0) + (p.mercredi?.heures || 0) + 
                   (p.jeudi?.heures || 0) + (p.vendredi?.heures || 0) + (p.samedi?.heures || 0);
      return total + (jours * p.tauxJournalier);
    }, 0);
  }

  calculateTotalInterim(): number {
    const current = this.currentRapport.value;
    return current.interim.reduce((total, i) => {
      const jours = (i.lundi?.heures || 0) + (i.mardi?.heures || 0) + (i.mercredi?.heures || 0) + 
                   (i.jeudi?.heures || 0) + (i.vendredi?.heures || 0) + (i.samedi?.heures || 0);
      return total + (jours * i.tauxJournalier);
    }, 0);
  }

  private generateId(): string {
    return Math.random().toString(36).substr(2, 9);
  }

  private getWeekNumber(date: Date): number {
    const d = new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()));
    const dayNum = d.getUTCDay() || 7;
    d.setUTCDate(d.getUTCDate() + 4 - dayNum);
    const yearStart = new Date(Date.UTC(d.getUTCFullYear(), 0, 1));
    return Math.ceil((((d.getTime() - yearStart.getTime()) / 86400000) + 1) / 7);
  }
}