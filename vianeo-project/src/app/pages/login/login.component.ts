import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { switchMap, take, finalize } from 'rxjs/operators';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <h1>Vianeo</h1>
          <p>Connexion à votre espace</p>
        </div>

        <form (ngSubmit)="onSubmit()" #loginForm="ngForm" class="login-form">
          <div class="form-field">
            <label for="email">Email ou identifiant</label>
            <input 
              type="email" 
              id="email"
              [(ngModel)]="credentials.email" 
              name="email"
              required
              placeholder="email ou identifiant"
              [class.error]="showError">
          </div>

          <div class="form-field">
            <label for="password">Mot de passe</label>
            <input 
              type="password" 
              id="password"
              [(ngModel)]="credentials.password" 
              name="password"
              required
              placeholder="Votre mot de passe"
              [class.error]="showError">
          </div>

          <div class="error-message" *ngIf="errorMessage">
            {{ errorMessage }}
          </div>

          <button 
            type="submit" 
            class="login-button"
            [disabled]="!loginForm.valid || isLoading">
            <span *ngIf="isLoading">Connexion...</span>
            <span *ngIf="!isLoading">Se connecter</span>
          </button>
        </form>

        <div class="login-footer">
          <p>Compte par défaut :</p>
          <p><strong>Email:</strong> admin </p>
          <p><strong>Mot de passe:</strong> Admin#123 </p>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      padding: 20px;
    }

    .login-card {
      background: white;
      border-radius: 16px;
      box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
      padding: 40px;
      width: 100%;
      max-width: 400px;
    }

    .login-header {
      text-align: center;
      margin-bottom: 32px;
    }

    .login-header h1 {
      color: #1e40af;
      font-size: 32px;
      font-weight: 700;
      margin: 0 0 8px 0;
    }

    .login-header p {
      color: #6b7280;
      margin: 0;
    }

    .login-form {
      display: flex;
      flex-direction: column;
      gap: 20px;
    }

    .form-field {
      display: flex;
      flex-direction: column;
    }

    .form-field label {
      font-weight: 500;
      margin-bottom: 8px;
      color: #374151;
    }

    .form-field input {
      padding: 12px 16px;
      border: 2px solid #e5e7eb;
      border-radius: 8px;
      font-size: 16px;
      transition: all 0.3s ease;
    }

    .form-field input:focus {
      outline: none;
      border-color: #3b82f6;
      box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
    }

    .form-field input.error {
      border-color: #ef4444;
    }

    .error-message {
      background: #fee2e2;
      color: #dc2626;
      padding: 12px;
      border-radius: 8px;
      font-size: 14px;
      text-align: center;
    }

    .login-button {
      background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
      color: white;
      border: none;
      padding: 14px 24px;
      border-radius: 8px;
      font-size: 16px;
      font-weight: 500;
      cursor: pointer;
      transition: all 0.3s ease;
    }

    .login-button:hover:not(:disabled) {
      background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
      transform: translateY(-1px);
    }

    .login-button:disabled {
      opacity: 0.6;
      cursor: not-allowed;
      transform: none;
    }

    .login-footer {
      margin-top: 24px;
      padding-top: 24px;
      border-top: 1px solid #e5e7eb;
      text-align: center;
      font-size: 14px;
      color: #6b7280;
    }

    .login-footer p {
      margin: 4px 0;
    }
  `]
})
export class LoginComponent {
  credentials = {
    email: '',
    password: ''
  };

  isLoading = false;
  errorMessage = '';
  showError = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  // imports en haut du fichier (si pas déjà présents)

onSubmit(): void {
  if (this.isLoading) return;

  this.isLoading = true;
  this.errorMessage = '';
  this.showError = false;

  this.authService.login(this.credentials).pipe(
    // ⬇️ tout de suite après login, hydrate le user via /auth/me
    switchMap(() => this.authService.hydrateMe()),
    finalize(() => { this.isLoading = false; })
  ).subscribe({
    next: (user) => {
      if (!user) {
        this.showError = true;
        this.errorMessage = 'Utilisateur introuvable après connexion.';
        return;
      }
      const target = user.role === 'ADMIN' ? '/chantiers' : '/rapport-chantier';
      this.router.navigate([target]);
    },
    error: (error) => {
      console.error('Erreur de connexion:', error);
      this.showError = true;
      if (error.status === 401) {
        this.errorMessage = 'Email ou mot de passe incorrect';
      } else if (error.status === 0) {
        this.errorMessage = 'Impossible de se connecter au serveur.';
      } else {
        this.errorMessage = 'Une erreur est survenue lors de la connexion';
      }
    }
  });
}



}