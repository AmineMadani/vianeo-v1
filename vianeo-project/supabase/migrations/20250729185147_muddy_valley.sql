/*
  # Création des tables de base pour Vianeo

  1. Tables principales
    - `chantiers` - Informations des chantiers
    - `personnel` - Personnel disponible
    - `fournisseurs` - Liste des fournisseurs
    - `articles` - Articles/matériels disponibles
    - `entites` - Entités/entreprises

  2. Sécurité
    - Enable RLS sur toutes les tables
    - Politiques pour les utilisateurs authentifiés
*/

-- Table des entités/entreprises
CREATE TABLE IF NOT EXISTS entites (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nom text NOT NULL,
  adresse text,
  ville text,
  code_postal text,
  telephone text,
  email text,
  siret text,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

-- Table des fournisseurs
CREATE TABLE IF NOT EXISTS fournisseurs (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nom text NOT NULL,
  type_fournisseur text DEFAULT 'GENERAL',
  adresse text,
  ville text,
  code_postal text,
  telephone text,
  email text,
  contact_principal text,
  actif boolean DEFAULT true,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

-- Table du personnel
CREATE TABLE IF NOT EXISTS personnel (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nom text NOT NULL,
  prenom text NOT NULL,
  type_personnel text NOT NULL CHECK (type_personnel IN ('encadrant', 'operationnel')),
  taux_journalier numeric(10,2) DEFAULT 0,
  telephone text,
  email text,
  qualification text,
  actif boolean DEFAULT true,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

-- Table des articles/matériels
CREATE TABLE IF NOT EXISTS articles (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  designation text NOT NULL,
  type_article text NOT NULL CHECK (type_article IN ('materiel_interne', 'location_materiel', 'transport', 'prestation_externe', 'materiaux')),
  unite text DEFAULT 'unité',
  prix_unitaire numeric(10,2) DEFAULT 0,
  fournisseur_id uuid REFERENCES fournisseurs(id),
  description text,
  actif boolean DEFAULT true,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

-- Table des chantiers
CREATE TABLE IF NOT EXISTS chantiers (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nom text NOT NULL,
  adresse text,
  ville text,
  code_postal text,
  responsable text,
  date_debut date,
  date_fin date,
  statut text DEFAULT 'en_cours' CHECK (statut IN ('en_preparation', 'en_cours', 'termine', 'suspendu')),
  entite_id uuid REFERENCES entites(id),
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

-- Enable RLS
ALTER TABLE entites ENABLE ROW LEVEL SECURITY;
ALTER TABLE fournisseurs ENABLE ROW LEVEL SECURITY;
ALTER TABLE personnel ENABLE ROW LEVEL SECURITY;
ALTER TABLE articles ENABLE ROW LEVEL SECURITY;
ALTER TABLE chantiers ENABLE ROW LEVEL SECURITY;

-- Politiques RLS pour les utilisateurs authentifiés
CREATE POLICY "Users can read all entites"
  ON entites FOR SELECT
  TO authenticated
  USING (true);

CREATE POLICY "Users can manage entites"
  ON entites FOR ALL
  TO authenticated
  USING (true);

CREATE POLICY "Users can read all fournisseurs"
  ON fournisseurs FOR SELECT
  TO authenticated
  USING (true);

CREATE POLICY "Users can manage fournisseurs"
  ON fournisseurs FOR ALL
  TO authenticated
  USING (true);

CREATE POLICY "Users can read all personnel"
  ON personnel FOR SELECT
  TO authenticated
  USING (true);

CREATE POLICY "Users can manage personnel"
  ON personnel FOR ALL
  TO authenticated
  USING (true);

CREATE POLICY "Users can read all articles"
  ON articles FOR SELECT
  TO authenticated
  USING (true);

CREATE POLICY "Users can manage articles"
  ON articles FOR ALL
  TO authenticated
  USING (true);

CREATE POLICY "Users can read all chantiers"
  ON chantiers FOR SELECT
  TO authenticated
  USING (true);

CREATE POLICY "Users can manage chantiers"
  ON chantiers FOR ALL
  TO authenticated
  USING (true);