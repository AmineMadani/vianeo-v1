import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';

import { environment } from '../../environments/environment';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  type: string;
  id: number;
  email: string;
  nom: string;
  prenom: string;
  role: string;
}

export interface User {
  id?: number;
  nom: string;
  prenom: string;
  email: string;
  telephone?: string;
  role: 'ADMIN' | 'CHEF_CHANTIER';
  active: boolean;
  emailVerified: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface TokenResponse {
  accessToken: string;      // correspond à ton TokenResponse backend (accessToken)
  expirationTime?: number;  // optionnel si tu veux le lire
}


const TOKEN_KEY = 'auth.token';


@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly API_URL = environment.apiUrl; // assure-toi que ça finit par /api
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  private readonly TOKEN_KEY = 'vianeo.token';

  // ➜ snapshot lisible publiquement
  get currentUserSnapshot(): User | null {
    return this.currentUserSubject.getValue();
  }


  constructor(private http: HttpClient) {
    const token = this.getToken();
    if (token) {
      this.fetchMe().subscribe({
        next: (u) => this.currentUserSubject.next(u),
        error: () => this.logout()
      });
    }
  }

  // ---------- AUTH ----------
  login(credentials: LoginRequest): Observable<TokenResponse> {
  return this.http.post<TokenResponse>(`${this.API_URL}/auth/login`, {
    username: credentials.email,
    password: credentials.password
  }).pipe(
    tap(res => {
      const token = res?.accessToken ?? (res as any)?.token ?? (res as any)?.jwt;
      if (!token) throw new Error('Pas de token dans /auth/login');
      this.setToken(token);
    })
  );
}


  fetchMe(): Observable<User> {
    // ⚠️ c'était /api/me dans ton brouillon → corrige en /auth/me
    return this.http.get<User>(`${this.API_URL}/auth/me`);
  }

  // Hydrate currentUser$ à partir de /auth/me
  hydrateFromMe(): Observable<User> {
    return this.fetchMe().pipe(
      tap(user => this.currentUserSubject.next(user))
    );
  }

  // setter public (facultatif mais utile)
  setCurrentUser(u: User | null): void {
    this.currentUserSubject.next(u);
  }

  logout(): void {
    try { localStorage.removeItem(this.TOKEN_KEY); } catch {}
    this.currentUserSubject.next(null);
  }

  // ---------- TOKEN ----------
  setToken(token: string) {
    try { localStorage.setItem(this.TOKEN_KEY, token); } catch {}
  }
  getToken(): string | null {
    try { return localStorage.getItem(this.TOKEN_KEY); } catch { return null; }
  }

  // Headers avec authentification
  private getAuthHeaders(): HttpHeaders {
  let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
  const token = this.getToken();
  if (token) {
    headers = headers.set('Authorization', `Bearer ${token}`);
  }
  return headers;
}

  // Entités
  getEntites(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/entites`, {
      headers: this.getAuthHeaders()
    });
  }

  createEntite(entite: any): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/entites`, entite, {
      headers: this.getAuthHeaders()
    });
  }

  updateEntite(id: string, entite: any): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/entites/${id}`, entite, {
      headers: this.getAuthHeaders()
    });
  }

  deleteEntite(id: string): Observable<any> {
    return this.http.delete(`${this.API_URL}/entites/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  // Fournisseurs
  getFournisseurs(params?: any): Observable<any> {
    let httpParams = new HttpParams();
    if (params) {
      Object.keys(params).forEach(key => {
        if (params[key] !== null && params[key] !== undefined) {
          httpParams = httpParams.set(key, params[key]);
        }
      });
    }

    return this.http.get<any>(`${this.API_URL}/ref/fournisseurs`, {
      headers: this.getAuthHeaders(),
      params: httpParams
    });
  }

  getActiveFournisseurs(type?: string): Observable<any[]> {
    let httpParams = new HttpParams();
    if (type) {
      httpParams = httpParams.set('type', type);
    }

    return this.http.get<any[]>(`${this.API_URL}/ref/fournisseurs/active`, {
      headers: this.getAuthHeaders(),
      params: httpParams
    });
  }

  createFournisseur(fournisseur: any): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/ref/fournisseurs`, fournisseur, {
      headers: this.getAuthHeaders()
    });
  }

  updateFournisseur(id: string, fournisseur: any): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/ref/fournisseurs/${id}`, fournisseur, {
      headers: this.getAuthHeaders()
    });
  }

  deleteFournisseur(id: string): Observable<any> {
    return this.http.delete(`${this.API_URL}/ref/fournisseurs/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  // Personnel
  getPersonnel(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/personnel`, {
      headers: this.getAuthHeaders()
    });
  }

  createPersonnel(personnel: any): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/personnel`, personnel, {
      headers: this.getAuthHeaders()
    });
  }

  updatePersonnel(id: string, personnel: any): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/personnel/${id}`, personnel, {
      headers: this.getAuthHeaders()
    });
  }

  deletePersonnel(id: string): Observable<any> {
    return this.http.delete(`${this.API_URL}/personnel/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  // Articles
  getArticles(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/ref/articles`, {
      headers: this.getAuthHeaders()
    });
  }

  createArticle(article: any): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/ref/articles`, article, {
      headers: this.getAuthHeaders()
    });
  }

  updateArticle(id: string, article: any): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/ref/articles/${id}`, article, {
      headers: this.getAuthHeaders()
    });
  }

  deleteArticle(id: string): Observable<any> {
    return this.http.delete(`${this.API_URL}/ref/articles/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  // Chantiers
  getChantiers(params?: any): Observable<any> {
    let httpParams = new HttpParams();
    if (params) {
      Object.keys(params).forEach(key => {
        if (params[key] !== null && params[key] !== undefined) {
          httpParams = httpParams.set(key, params[key]);
        }
      });
    }

    return this.http.get<any>(`${this.API_URL}/chantiers`, {
      headers: this.getAuthHeaders(),
      params: httpParams
    });
  }

  getMyChantiers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/chantiers/my-chantiers`, {
      headers: this.getAuthHeaders()
    });
  }

  createChantier(chantier: any): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/chantiers`, chantier, {
      headers: this.getAuthHeaders()
    });
  }

  updateChantier(id: string, chantier: any): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/chantiers/${id}`, chantier, {
      headers: this.getAuthHeaders()
    });
  }

  deleteChantier(id: string): Observable<any> {
    return this.http.delete(`${this.API_URL}/chantiers/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  // Rapports
  getRapports(params?: any): Observable<any> {
    let httpParams = new HttpParams();
    if (params) {
      Object.keys(params).forEach(key => {
        if (params[key] !== null && params[key] !== undefined) {
          httpParams = httpParams.set(key, params[key]);
        }
      });
    }

    return this.http.get<any>(`${this.API_URL}/rapports`, {
      headers: this.getAuthHeaders(),
      params: httpParams
    });
  }

  getRapportById(id: string): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/rapports/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  createRapport(rapport: any): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/rapports`, rapport, {
      headers: this.getAuthHeaders()
    });
  }

  updateRapport(id: string, rapport: any): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/rapports/${id}`, rapport, {
      headers: this.getAuthHeaders()
    });
  }

  submitRapport(id: string): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/rapports/${id}/submit`, {}, {
      headers: this.getAuthHeaders()
    });
  }

  validateRapport(id: string): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/rapports/${id}/validate`, {}, {
      headers: this.getAuthHeaders()
    });
  }

  deleteRapport(id: string): Observable<any> {
    return this.http.delete(`${this.API_URL}/rapports/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  getTodayRapport(chantierId: string): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/rapports/today`, {
      headers: this.getAuthHeaders(),
      params: new HttpParams().set('chantierId', chantierId)
    });
  }

  // Export
  exportRapportsToExcel(params: any): Observable<Blob> {
    let httpParams = new HttpParams();
    Object.keys(params).forEach(key => {
      if (params[key] !== null && params[key] !== undefined) {
        httpParams = httpParams.set(key, params[key]);
      }
    });

    return this.http.get(`${this.API_URL}/export/rapports/excel`, {
      headers: this.getAuthHeaders(),
      params: httpParams,
      responseType: 'blob'
    });
  }

  exportWeeklyRapports(weekStart: string, chantierId?: string): Observable<Blob> {
    let httpParams = new HttpParams().set('weekStart', weekStart);
    if (chantierId) {
      httpParams = httpParams.set('chantierId', chantierId);
    }

    return this.http.get(`${this.API_URL}/export/rapports/weekly`, {
      headers: this.getAuthHeaders(),
      params: httpParams,
      responseType: 'blob'
    });
  }

  // Admin
  getDashboard(): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/admin/dashboard`, {
      headers: this.getAuthHeaders()
    });
  }

  getAllUsers(params?: any): Observable<any> {
    let httpParams = new HttpParams();
    if (params) {
      Object.keys(params).forEach(key => {
        if (params[key] !== null && params[key] !== undefined) {
          httpParams = httpParams.set(key, params[key]);
        }
      });
    }

    return this.http.get<any>(`${this.API_URL}/admin/users`, {
      headers: this.getAuthHeaders(),
      params: httpParams
    });
  }

  inviteUser(user: any): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/admin/users/invite`, user, {
      headers: this.getAuthHeaders()
    });
  }

  activateUser(id: string): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/admin/users/${id}/activate`, {}, {
      headers: this.getAuthHeaders()
    });
  }

  deactivateUser(id: string): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/admin/users/${id}/deactivate`, {}, {
      headers: this.getAuthHeaders()
    });
  }

  deleteUser(id: string): Observable<any> {
    return this.http.delete(`${this.API_URL}/admin/users/${id}`, {
      headers: this.getAuthHeaders()
    });
  }

  // 1) Récupère le profil courant (depuis le token porté par l'AuthInterceptor)
fetchCurrentUser(): Observable<User> {
  // ⚠️ adapte l’URL si différent chez toi : /api/me, /auth/me, /users/me...
  return this.http.get<User>(`${this.API_URL}/auth/me`);
}


}