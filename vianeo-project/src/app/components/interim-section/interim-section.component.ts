import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Interim, JourSemaine, JOURS_SEMAINE } from '../../models/chantier.models';

@Component({
  selector: 'app-interim-section',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './interim-section.component.html',
  styleUrls: ['./interim-section.component.scss']
})
export class InterimSectionComponent {
  @Input() interim: Interim[] = [];
  @Input() fournisseurs: string[] = [];
  @Output() interimChange = new EventEmitter<Interim[]>();

  expanded = false;
  joursSemaine: JourSemaine[] = JOURS_SEMAINE;

  toggle(): void { this.expanded = !this.expanded; }

  // Accès sûr à la cellule d’un jour
  cell(i: Interim, day: JourSemaine) {
    return i[day]; // ton modèle Interim = Record<JourSemaine, JourTravail>
  }

  // Exclusif: recliquer => 'J'
  toggleMode(i: Interim, day: JourSemaine, mode: 'N' | 'GD') {
    const c = this.cell(i, day);
    if (!c) return;
    c.typePrestation = (c.typePrestation === mode) ? 'J' : mode;
    this.updateInterim();
  }

  addInterim(): void {
    const id = Math.random().toString(36).slice(2);
    const baseDay = () => ({ heures: 0, typePrestation: 'J' as const });
    const i: Interim = {
      id, nom: '', prenom: '',
      fournisseur: '',
      tauxJournalier: 320,
      lundi: baseDay(), mardi: baseDay(), mercredi: baseDay(),
      jeudi: baseDay(), vendredi: baseDay(), samedi: baseDay(),
    };
    this.interim.push(i);
    this.updateInterim();
  }

  removeInterim(id: string): void {
    this.interim = this.interim.filter(x => x.id !== id);
    this.updateInterim();
  }

  updateInterim(): void { this.interimChange.emit(this.interim); }

  // Totaux (heures cumulées de la semaine)
  getTotalJours(): number {
    return this.interim.reduce((tot, i) =>
      tot
      + i.lundi.heures + i.mardi.heures + i.mercredi.heures
      + i.jeudi.heures + i.vendredi.heures + i.samedi.heures
    , 0);
  }

  getTotalCost(): number {
    return this.interim.reduce((tot, i) => {
      const h = i.lundi.heures + i.mardi.heures + i.mercredi.heures
              + i.jeudi.heures + i.vendredi.heures + i.samedi.heures;
      return tot + h * i.tauxJournalier;
    }, 0);
  }

  trackByInterim = (_: number, it: Interim) => it.id;
}
