-- ===== ENTITE =====
INSERT INTO vianeo.entite (id, code, libelle, actif) VALUES
  (1, 'AG01', 'Agence Nord', TRUE)
ON CONFLICT (id) DO NOTHING;

-- ===== PERSONNEL =====
INSERT INTO vianeo.personnel (id, nom, prenom, metier, profil, actif, taux, entite_id) VALUES
  (1, 'Admin', 'Alice', 'CDT', 'ROLE_ADMIN', TRUE, 450.00, 1),
  (2, 'Chef',  'Bob',   'CDT', 'ROLE_CDT',   TRUE, 350.00, 1)
ON CONFLICT (id) DO NOTHING;

-- ===== COMPTE (utilisé pour l’auth) =====
-- Remplace <BCrypt_ADMIN> par un hash BCrypt valide (ex: mot de passe "Admin#123")
INSERT INTO vianeo.compte (id, personnel_id, username, email, password_hash, role, actif, created_at)
VALUES
  (1, 1, 'admin', 'admin@vianeo.test', '$2a$12$2CFhXJ6pWgOtXIm3ByeLEeKqL8hc/hwHQRAOO6JW9cCDe1ffsDs56', 'ROLE_ADMIN', TRUE, now())
ON CONFLICT (id) DO NOTHING;

-- ===== FOURNISSEUR (type = enum simple) =====
-- type_fournisseur: 'INTERIM','LOUAGEUR','LOCATION_SANS_CHAUFFEUR','LOCATION_AVEC_CHAUFFEUR','SOUS_TRAITANCE','FOURNITURE','AUTRE'
INSERT INTO vianeo.fournisseur (id, code, type, actif) VALUES
  (1, 'FOUR-INT-01', 'INTERIM', TRUE),
  (2, 'FOUR-LOCSS-01', 'LOCATION_SANS_CHAUFFEUR', TRUE),
  (3, 'FOUR-PREST-01', 'SOUS_TRAITANCE', TRUE)
ON CONFLICT (id) DO NOTHING;

-- ===== ARTICLE =====
-- categorie_article: 'INTERNE','EXTERNE'
-- type_article: 'MATERIEL_AVEC_CHAUFFEUR','MATERIEL_SANS_CHAUFFEUR','TRANSPORT','PRESTATION_EXT','MATERIAUX'
INSERT INTO vianeo.article (id, code, designation, cat, type, actif) VALUES
  (1, 'ART-INT-001', 'Mini-pelle interne', 'INTERNE', 'MATERIEL_SANS_CHAUFFEUR', TRUE),
  (2, 'ART-LOCSS-001', 'Plaque vibrante (SS ch.)', 'EXTERNE', 'MATERIEL_SANS_CHAUFFEUR', TRUE),
  (3, 'ART-TRANSP-001', 'Transport camion 8x4', 'EXTERNE', 'TRANSPORT', TRUE),
  (4, 'ART-PREST-001', 'Sous-traitance maçonnerie', 'EXTERNE', 'PRESTATION_EXT', TRUE),
  (5, 'ART-MAT-001', 'Gravier 0/31.5', 'EXTERNE', 'MATERIAUX', TRUE)
ON CONFLICT (id) DO NOTHING;

-- ===== CHANTIER =====
INSERT INTO vianeo.chantier (id, code, libelle, adresse, ville, code_postal, responsable_id, actif, entite_id)
VALUES (1, 'CH-0001', 'Chantier Test', '1 rue du Test', 'Lille', '59000', 2, TRUE, 1)
ON CONFLICT (id) DO NOTHING;

-- ===== AFFECTATION PERSONNEL =====
INSERT INTO vianeo.affectation_personnel (id, chantier_id, personnel_id, date_debut, date_fin) VALUES
  (1, 1, 2, CURRENT_DATE - INTERVAL '7 day', NULL)
ON CONFLICT (id) DO NOTHING;

-- ===== RAPPORT (DRAFT) =====
INSERT INTO vianeo.rapport (id, chantier_id, jour, auteur_id, statut, commentaire_cdt, created_at, updated_at)
VALUES (1, 1, CURRENT_DATE, 2, 'DRAFT', NULL, now(), now())
ON CONFLICT (id) DO NOTHING;
