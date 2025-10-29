import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class ReferentielService {
  // ✅ base correcte d'après tes logs Spring
  private readonly base = `${environment.apiUrl}/ref`;

  constructor(private http: HttpClient, private api: ApiService) {}

  /** Ajoute Authorization si token disponible */
  private withAuth() {
    const token = this.api.getToken?.();
    let headers = new HttpHeaders();
    if (token) headers = headers.set('Authorization', `Bearer ${token}`);
    return { headers };
  }

  /** Articles : /api/ref/articles?cat=INTERNE  ou  ?type=TRANSPORT */
  getArticles(params: { cat?: string; type?: string; search?: string }) {
    let p = new HttpParams();
    if (params.cat)   p = p.set('cat', params.cat);
    if (params.type)  p = p.set('type', params.type);
    if (params.search) p = p.set('search', params.search);
    return this.http.get<any[]>(`${this.base}/articles`, { params: p, ...this.withAuth() });
  }

  /**
   * Fournisseurs :
   * - si actif=true -> /api/ref/fournisseurs/active?type=...
   * - sinon         -> /api/ref/fournisseurs?type=...
   */
  getFournisseurs(params: { type?: string; actif?: boolean }) {
    let p = new HttpParams();
    if (params.type)  p = p.set('type', params.type);
    const url = params.actif ? `${this.base}/fournisseurs/active`
                             : `${this.base}/fournisseurs`;
    return this.http.get<any[]>(url, { params: p, ...this.withAuth() });
  }
}
