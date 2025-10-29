# Vianeo Backend - Gestion de Chantiers

Backend Spring Boot pour l'application de gestion de chantiers Vianeo.

## Technologies

- **Java 21**
- **Spring Boot 3.2.1**
- **Spring Security** avec JWT
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**

## Configuration

### Variables d'environnement

Configurez les variables d'environnement suivantes :

```bash
# Base de données
DB_HOST=localhost
DB_PORT=5432
DB_NAME=vianeo
DB_USER=your_db_user
DB_PASSWORD=your_db_password

# JWT
JWT_SECRET=your-very-long-and-secure-secret-key-here-at-least-256-bits
JWT_EXPIRATION=86400

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:4200

# Serveur
PORT=8080
```

### Base de données

1. Créez une base PostgreSQL nommée `vianeo`
2. Exécutez le script SQL fourni pour créer le schéma et les tables
3. Configurez les variables d'environnement de connexion

## Authentification

### Rôles disponibles

- `ROLE_ADMIN` : Administrateur (accès complet)
- `ROLE_CDT` : Chef de Chantier (validation des rapports)
- `ROLE_CC` : Conducteur de Chantier (soumission des rapports)
- `ROLE_READONLY` : Lecture seule

### Endpoints d'authentification

#### POST `/api/auth/login`

Connexion avec username/password.

**Request:**
```json
{
  "username": "your_username",
  "password": "your_password"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400
}
```

## API Endpoints

### Chantiers

- `GET /api/chantiers` - Liste tous les chantiers actifs (authentifié)
- `GET /api/chantiers/{id}` - Détails d'un chantier (authentifié)
- `POST /api/chantiers` - Créer un chantier (ADMIN uniquement)
- `PUT /api/chantiers/{id}` - Modifier un chantier (ADMIN uniquement)

### Rapports

- `GET /api/rapports` - Liste tous les rapports (authentifié)
- `GET /api/rapports/{id}` - Détails d'un rapport (authentifié)
- `GET /api/rapports/{id}/lignes/personnel` - Lignes personnel d'un rapport
- `GET /api/rapports/{id}/lignes/interim` - Lignes intérim d'un rapport
- `GET /api/rapports/{id}/lignes/matInterne` - Lignes matériel interne d'un rapport
- `GET /api/rapports/{id}/lignes/locSsCh` - Lignes location sans chauffeur d'un rapport
- `GET /api/rapports/{id}/lignes/locAvecCh` - Lignes location avec chauffeur d'un rapport
- `GET /api/rapports/{id}/lignes/transport` - Lignes transport d'un rapport
- `GET /api/rapports/{id}/lignes/prestaExt` - Lignes prestation externe d'un rapport
- `GET /api/rapports/{id}/lignes/materiaux` - Lignes matériaux d'un rapport
- `POST /api/rapports/{id}/lignes/personnel` - Créer une ligne personnel (ADMIN, CDT, CC)
- `POST /api/rapports/{id}/lignes/interim` - Créer une ligne intérim (ADMIN, CDT, CC)
- `POST /api/rapports/{id}/lignes/matInterne` - Créer une ligne matériel interne (ADMIN, CDT, CC)
- `POST /api/rapports/{id}/lignes/locSsCh` - Créer une ligne location sans chauffeur (ADMIN, CDT, CC)
- `POST /api/rapports/{id}/lignes/locAvecCh` - Créer une ligne location avec chauffeur (ADMIN, CDT, CC)
- `POST /api/rapports/{id}/lignes/transport` - Créer une ligne transport (ADMIN, CDT, CC)
- `POST /api/rapports/{id}/lignes/prestaExt` - Créer une ligne prestation externe (ADMIN, CDT, CC)
- `POST /api/rapports/{id}/lignes/materiaux` - Créer une ligne matériaux (ADMIN, CDT, CC)
- `POST /api/rapports/{id}/soumettre` - Soumettre un rapport (ADMIN, CDT, CC)
- `POST /api/rapports/{id}/valider` - Valider un rapport (ADMIN, CDT)
- `POST /api/rapports/{id}/refuser` - Refuser un rapport (ADMIN, CDT)

### Référentiels

- `GET /api/ref/articles` - Liste tous les articles actifs
- `GET /api/ref/articles?cat=INTERNE` - Articles filtrés par catégorie
- `GET /api/ref/articles/{id}` - Détails d'un article
- `GET /api/ref/fournisseurs` - Liste tous les fournisseurs actifs
- `GET /api/ref/fournisseurs?type=INTERIM` - Fournisseurs filtrés par type
- `GET /api/ref/fournisseurs/{id}` - Détails d'un fournisseur

## Contraintes Métier

### Validation des lignes par type d'article

- **LigneMatInterne** : L'article doit avoir `cat = 'INTERNE'`
- **LigneLocSsCh** : L'article doit avoir `type = 'MATERIEL_SANS_CHAUFFEUR'`
- **LigneLocAvecCh** : L'article doit avoir `type = 'MATERIEL_AVEC_CHAUFFEUR'`
- **LigneTransport** : L'article doit avoir `type = 'TRANSPORT'`
- **LignePrestaExt** : L'article doit avoir `type = 'PRESTATION_EXT'`
- **LigneMateriaux** : L'article doit avoir `type = 'MATERIAUX'`, fournisseur optionnel

### Workflow des rapports

- **DRAFT** → **EN_ATTENTE_VALIDATION** (soumettre)
- **EN_ATTENTE_VALIDATION** → **VALIDE** (valider)
- **EN_ATTENTE_VALIDATION** → **REFUSE** (refuser avec motif)

### Calculs automatiques

- `total = quantite * pu` calculé automatiquement côté service et entité
- Contrainte d'unicité `(chantier_id, jour)` gérée en base de données

## Sécurité

- Authentification JWT stateless
- CORS configuré pour Angular
- Endpoints protégés par rôles
- Mots de passe hashés avec BCrypt

## Tests

Exécutez les tests avec :

```bash
mvn test
```

Les tests incluent :
- Tests d'authentification (succès/échec)
- Tests des endpoints sécurisés
- Tests des rôles et permissions

## Démarrage

1. Clonez le projet
2. Configurez les variables d'environnement
3. Créez la base de données PostgreSQL
4. Exécutez le script SQL pour créer le schéma
5. Lancez l'application :

```bash
mvn spring-boot:run
```

L'API sera disponible sur `http://localhost:8080`

## Structure du projet

```
src/
├── main/
│   ├── java/com/vianeo/
│   │   ├── controller/     # Controllers REST
│   │   ├── dto/           # Data Transfer Objects
│   │   │   ├── auth/      # DTOs d'authentification
│   │   │   ├── ligne/     # DTOs pour les lignes de rapport
│   │   │   └── rapport/   # DTOs pour les rapports
│   │   ├── model/         # Entités JPA et Enums
│   │   │   ├── entity/    # Entités JPA
│   │   │   └── enums/     # Énumérations
│   │   ├── repository/    # Repositories Spring Data
│   │   ├── security/      # Configuration sécurité et JWT
│   │   └── service/       # Services métier
│   └── resources/
│       └── application.properties
└── test/                  # Tests unitaires et intégration
     ├── controller/       # Tests des controllers
     └── service/          # Tests des services
```

## CORS

Le backend est configuré pour accepter les requêtes depuis votre application Angular. 
Modifiez la variable `CORS_ALLOWED_ORIGINS` selon vos besoins.