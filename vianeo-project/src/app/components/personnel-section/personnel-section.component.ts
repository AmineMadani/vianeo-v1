import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Personnel, JourSemaine, JOURS_SEMAINE } from '../../models/chantier.models';

@Component({
  selector: 'app-personnel-section',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './personnel-section.component.html',
  styleUrls: ['./personnel-section.component.scss']
})
export class PersonnelSectionComponent {
  @Input() title: string = '';
  @Input() personnel: Personnel[] = [];
  @Input() availablePersonnel: any[] = [];
  @Input() type: 'encadrant' | 'operationnel' = 'operationnel';
  @Output() personnelChange = new EventEmitter<Personnel[]>();

  expanded = false;
  joursSemaine = JOURS_SEMAINE;               // ['lundi','mardi','mercredi','jeudi','vendredi','samedi']
  fractionsEncadrant: Array<0 | 0.5 | 1> = [0, 0.5, 1];

  toggle(): void {
    this.expanded = !this.expanded;
  }

  /** Accès sécurisé à la cellule d’un jour (évite les erreurs d’indexation TS) */
  cell(person: Personnel, day: JourSemaine) {
    return person[day];
  }

  /** Encadrant : cases exclusives (Nuit / GD). Si décoché → 'J' */
  toggleEncadrant(person: Personnel, day: JourSemaine, mode: 'N'|'GD'): void {
    const c = this.cell(person, day);
    if (!c) return;
    if (c.typePrestation === mode) {
      c.typePrestation = 'J'; // re-cliquer -> revient à Jour
    } else {
      c.typePrestation = mode; // exclusif
    }
    this.updatePersonnel();
  }

  /** Opérationnel/Intérim : cases exclusives (Nuit / GD). Si décoché → 'J' (aucune) */
  toggleBinaire(person: Personnel, day: JourSemaine, mode: 'N'|'GD'): void {
    const c = this.cell(person, day);
    if (!c) return;
    if (c.typePrestation === mode) {
      c.typePrestation = 'J'; // aucune sélection = Jour (par défaut)
    } else {
      c.typePrestation = mode; // exclusif
    }
    this.updatePersonnel();
  }

  addPersonnel(): void {
  const newPersonnel: Personnel = {
    id: crypto.randomUUID?.() ?? Math.random().toString(36).slice(2),
    utilisateurId: null,                 // ← AJOUT
    nom: '',
    prenom: '',
    type: this.type,
    lundi:     { heures: 0, typePrestation: 'J' },
    mardi:     { heures: 0, typePrestation: 'J' },
    mercredi:  { heures: 0, typePrestation: 'J' },
    jeudi:     { heures: 0, typePrestation: 'J' },
    vendredi:  { heures: 0, typePrestation: 'J' },
    samedi:    { heures: 0, typePrestation: 'J' },
    tauxJournalier: this.type === 'encadrant' ? 350 : 280
  };
  this.personnel.push(newPersonnel);
  this.updatePersonnel();
}

/** Quand l'utilisateur change, on recopie (affichage + PU) */
onSelectUser(person: Personnel): void {
  const p = this.availablePersonnel.find((x: any) => x.id === person.utilisateurId);
  if (p) {
    person.nom = p.nom ?? '';
    person.prenom = p.prenom ?? '';
    // si ton référentiel a un taux côté front
    if (p.tauxJournalier != null) person.tauxJournalier = Number(p.tauxJournalier);
  }
  this.updatePersonnel();
}


  removePersonnel(id: string): void {
    this.personnel = this.personnel.filter(p => p.id !== id);
    this.updatePersonnel();
  }

  updatePersonnel(): void {
    this.personnelChange.emit(this.personnel);
  }

  /** Totaux (restent comme aujourd’hui — tu peux affiner conversion heures→jours si besoin) */
  getTotalJours(): number {
    return this.personnel.reduce((total, p) => {
      return total
        + (p.lundi?.heures || 0)
        + (p.mardi?.heures || 0)
        + (p.mercredi?.heures || 0)
        + (p.jeudi?.heures || 0)
        + (p.vendredi?.heures || 0)
        + (p.samedi?.heures || 0);
    }, 0);
  }

  getTotalCost(): number {
    return this.personnel.reduce((total, p) => {
      const jours = (p.lundi?.heures || 0) + (p.mardi?.heures || 0) + (p.mercredi?.heures || 0)
                  + (p.jeudi?.heures || 0) + (p.vendredi?.heures || 0) + (p.samedi?.heures || 0);
      return total + (jours * (p.tauxJournalier ?? 0));
    }, 0);
  }

  trackByPersonnel(index: number, item: Personnel): string {
    return item.id || index.toString();
  }
}
