import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { ApiService } from './api.service';

// Types min côté front
export interface Rapport { id: number; jour: string; statut: string; }
export interface WeekRapportResponse { days: Array<{ jour: string; rapportId: number; statut: string }>; }
type LigneCommon = { rapportId: number; designation?: string; articleId?: number; fournisseurId?: number; quantite: number; pu: number; };

@Injectable({ providedIn: 'root' })
export class RapportApi {
  private base = `${environment.apiUrl}/rapports`;

  constructor(private http: HttpClient, private api: ApiService) {}

  private withAuth(headers?: HttpHeaders) {
    const token = this.api.getToken?.();
    let h = headers ?? new HttpHeaders();
    if (token && !h.has('Authorization')) h = h.set('Authorization', `Bearer ${token}`);
    return { headers: h };
  }

  // ======= SEMAINE / RAPPORT =======
  getWeek(chantierId: number, weekStart: string) {
    const params = new HttpParams().set('chantierId', String(chantierId)).set('weekStart', weekStart);
    return this.http.get<WeekRapportResponse>(`${this.base}/week`, { params, ...this.withAuth() });
  }

  getTotaux(rapportId: number) {
    return this.http.get<{ totalGeneral: number; totalPersonnel: number; totalInterim: number }>(
      `${this.base}/${rapportId}/totaux`,
      this.withAuth()
    );
  }

  openToday(data: { chantierId: number; prefillYesterday?: boolean }) {
    const params = new HttpParams()
      .set('chantierId', String(data.chantierId))
      .set('prefillYesterday', String(!!data.prefillYesterday));
    return this.http.post<Rapport>(`${this.base}/open-today`, {}, { params, ...this.withAuth() });
  }

  // ======= WORKFLOW JOUR =======
  soumettre(id: number) { return this.http.post<Rapport>(`${this.base}/${id}/soumettre`, {}, this.withAuth()); }
  valider(id: number)   { return this.http.post<Rapport>(`${this.base}/${id}/valider`,   {}, this.withAuth()); }
  submitDay(id: number) { return this.http.post<Rapport>(`${this.base}/${id}/submit-day`, {}, this.withAuth()); }

  // ======= WORKFLOW SEMAINE =======
  submitWeek(chantierId: number, weekStart: string) {
    const params = new HttpParams().set('chantierId', String(chantierId));
    return this.http.post<void>(`${this.base}/week/${weekStart}/submit`, {}, { params, ...this.withAuth() });
  }

  // ======= LISTES (chef / admin) =======
  getMyReports(params: { from?: string; to?: string; chantierId?: number; page?: number; size?: number }) {
    let p = new HttpParams();
    Object.entries(params).forEach(([k, v]) => v != null && (p = p.set(k, String(v))));
    return this.http.get<any>(`${this.base}/my`, { params: p, ...this.withAuth() });
  }
  getAllReports(params: { userId?: number; from?: string; to?: string; chantierId?: number; page?: number; size?: number }) {
    let p = new HttpParams();
    Object.entries(params).forEach(([k, v]) => v != null && (p = p.set(k, String(v))));
    return this.http.get<any>(`${this.base}`, { params: p, ...this.withAuth() });
  }

  addLigneInterim(p: {
  rapportId: number;
  fournisseurId: number;
  nom: string;
  quantite: number;
  pu: number;
}) {
  return this.http.post(
    `${this.base}/${p.rapportId}/lignes/interim`,
    {
      fournisseurId: p.fournisseurId,
      nom: p.nom,
      quantite: p.quantite,
      pu: p.pu
    },
    this.withAuth()
  );
}

  // ======= LIGNES (envoi mixte: IDs + libellés) =======
addLigneMatInterne(p: any) {
  return this.http.post(
    `${this.base}/${p.rapportId}/lignes/matInterne`,
    {
      articleId: p.articleId ?? null,
      designation: p.designation ?? null,
      quantite: p.quantite,
      pu: p.pu,
    },
    this.withAuth()
  );
}

addLigneLocSsCh(p: any) {
  return this.http.post(
    `${this.base}/${p.rapportId}/lignes/locSsCh`,
    {
      articleId: p.articleId ?? null,
      designation: p.designation ?? null,
      fournisseurId: p.fournisseurId ?? null,
      fournisseur: p.fournisseur ?? null,
      quantite: p.quantite,
      pu: p.pu,
    },
    this.withAuth()
  );
}

addLigneLocAvecCh(p: any) {
  return this.http.post(
    `${this.base}/${p.rapportId}/lignes/locAvecCh`,
    {
      articleId: p.articleId ?? null,
      designation: p.designation ?? null,
      fournisseurId: p.fournisseurId ?? null,
      fournisseur: p.fournisseur ?? null,
      quantite: p.quantite,
      pu: p.pu,
    },
    this.withAuth()
  );
}

addLigneTransport(p: any) {
  return this.http.post(
    `${this.base}/${p.rapportId}/lignes/transport`,
    {
      articleId: p.articleId ?? null,
      designation: p.designation ?? null,
      fournisseurId: p.fournisseurId ?? null,
      fournisseur: p.fournisseur ?? null,
      quantite: p.quantite,
      pu: p.pu,
    },
    this.withAuth()
  );
}

addLignePrestaExt(p: any) {
  return this.http.post(
    `${this.base}/${p.rapportId}/lignes/prestaExt`,
    {
      articleId: p.articleId ?? null,
      designation: p.designation ?? null,
      fournisseurId: p.fournisseurId ?? null,
      fournisseur: p.fournisseur ?? null,
      quantite: p.quantite,
      pu: p.pu,
    },
    this.withAuth()
  );
}

addLigneMateriaux(p: any) {
  return this.http.post(
    `${this.base}/${p.rapportId}/lignes/materiaux`,
    {
      articleId: p.articleId ?? null,
      designation: p.designation ?? null,
      fournisseurId: p.fournisseurId ?? null,
      fournisseur: p.fournisseur ?? null,
      quantite: p.quantite,
      pu: p.pu,
    },
    this.withAuth()
  );
}

    // ======= LIGNE PERSONNEL =======
  addLignePersonnel(p: {
    rapportId: number;
    utilisateurId: number;
    categorie: 'ENCADRANT' | 'OPERATIONNEL';
    typeTravail: 'J' | 'GD' | 'N';
    quantite: number;
    pu: number;
  }) {
    return this.http.post(`${this.base}/${p.rapportId}/lignes/personnel`, p, this.withAuth());
  }

}
