/*
  # Insertion des données de référence

  1. Données de base
    - Fournisseurs d'intérim
    - Personnel de base
    - Articles/matériels courants
    - Entité exemple

  2. Notes
    - Données basées sur le code existant et les documents fournis
    - Facilement modifiables via l'interface admin
*/

-- Insertion d'une entité exemple
INSERT INTO entites (nom, adresse, ville, code_postal, telephone, email) VALUES
('VIANEO CONSTRUCTION', '123 Rue de la Construction', 'Paris', '75001', '01.23.45.67.89', 'contact@vianeo.fr')
ON CONFLICT DO NOTHING;

-- Insertion des fournisseurs d'intérim (basé sur le code existant)
INSERT INTO fournisseurs (nom, type_fournisseur, actif) VALUES
('ADECCO', 'INTERIM', true),
('ADYSSEI', 'INTERIM', true),
('ARPO INTERIM', 'INTERIM', true),
('ARTEMIS SUCCESS', 'INTERIM', true),
('MANPOWER', 'INTERIM', true),
('RANDSTAD', 'INTERIM', true)
ON CONFLICT DO NOTHING;

-- Insertion de fournisseurs de matériel
INSERT INTO fournisseurs (nom, type_fournisseur, actif) VALUES
('KILOUTOU', 'LOCATION_MATERIEL', true),
('LOXAM', 'LOCATION_MATERIEL', true),
('POINT P', 'MATERIAUX', true),
('GEDIMAT', 'MATERIAUX', true),
('TRANSPORT MARTIN', 'TRANSPORT', true),
('ENTREPRISE DUPONT', 'PRESTATION_EXTERNE', true)
ON CONFLICT DO NOTHING;

-- Insertion du personnel de base (basé sur le code existant)
INSERT INTO personnel (nom, prenom, type_personnel, taux_journalier, qualification, actif) VALUES
('DUPONT', 'Jean', 'encadrant', 350.00, 'Chef d''équipe', true),
('MARTIN', 'Pierre', 'operationnel', 280.00, 'Ouvrier qualifié', true),
('DURAND', 'Marie', 'encadrant', 375.00, 'Conducteur de travaux', true),
('BERNARD', 'Paul', 'operationnel', 290.00, 'Maçon', true),
('MOREAU', 'Sophie', 'encadrant', 360.00, 'Chef de chantier', true),
('PETIT', 'Luc', 'operationnel', 275.00, 'Coffreur', true)
ON CONFLICT DO NOTHING;

-- Insertion d'articles de matériel interne
INSERT INTO articles (designation, type_article, unite, prix_unitaire, description, actif) VALUES
('Bétonnière 350L', 'materiel_interne', 'jour', 45.00, 'Bétonnière électrique 350 litres', true),
('Perceuse à percussion', 'materiel_interne', 'jour', 25.00, 'Perceuse à percussion professionnelle', true),
('Échafaudage mobile', 'materiel_interne', 'jour', 35.00, 'Échafaudage mobile 2m x 1.5m', true),
('Compacteur plaque', 'materiel_interne', 'jour', 55.00, 'Compacteur plaque vibrante', true),
('Scie circulaire', 'materiel_interne', 'jour', 30.00, 'Scie circulaire portative', true)
ON CONFLICT DO NOTHING;

-- Insertion d'articles de location avec fournisseurs
DO $$
DECLARE
    kiloutou_id uuid;
    loxam_id uuid;
BEGIN
    SELECT id INTO kiloutou_id FROM fournisseurs WHERE nom = 'KILOUTOU' LIMIT 1;
    SELECT id INTO loxam_id FROM fournisseurs WHERE nom = 'LOXAM' LIMIT 1;
    
    INSERT INTO articles (designation, type_article, unite, prix_unitaire, fournisseur_id, description, actif) VALUES
    ('Pelle 8T', 'location_materiel', 'jour', 280.00, kiloutou_id, 'Pelle hydraulique 8 tonnes', true),
    ('Grue mobile 25T', 'location_materiel', 'jour', 450.00, loxam_id, 'Grue mobile 25 tonnes', true),
    ('Nacelle 12m', 'location_materiel', 'jour', 180.00, kiloutou_id, 'Nacelle élévatrice 12 mètres', true),
    ('Dumper 3T', 'location_materiel', 'jour', 120.00, loxam_id, 'Dumper tout terrain 3 tonnes', true);
END $$;

-- Insertion d'articles de transport
DO $$
DECLARE
    transport_id uuid;
BEGIN
    SELECT id INTO transport_id FROM fournisseurs WHERE nom = 'TRANSPORT MARTIN' LIMIT 1;
    
    INSERT INTO articles (designation, type_article, unite, prix_unitaire, fournisseur_id, description, actif) VALUES
    ('Camion benne 19T', 'transport', 'jour', 320.00, transport_id, 'Camion benne 19 tonnes', true),
    ('Camion grue', 'transport', 'jour', 380.00, transport_id, 'Camion avec grue auxiliaire', true),
    ('Fourgon utilitaire', 'transport', 'jour', 85.00, transport_id, 'Fourgon utilitaire 3.5T', true);
END $$;

-- Insertion de matériaux
DO $$
DECLARE
    pointp_id uuid;
    gedimat_id uuid;
BEGIN
    SELECT id INTO pointp_id FROM fournisseurs WHERE nom = 'POINT P' LIMIT 1;
    SELECT id INTO gedimat_id FROM fournisseurs WHERE nom = 'GEDIMAT' LIMIT 1;
    
    INSERT INTO articles (designation, type_article, unite, prix_unitaire, fournisseur_id, description, actif) VALUES
    ('Béton C25/30', 'materiaux', 'm3', 95.00, pointp_id, 'Béton prêt à l''emploi C25/30', true),
    ('Parpaing 20x20x50', 'materiaux', 'unité', 1.85, gedimat_id, 'Parpaing creux 20x20x50 cm', true),
    ('Ciment CEM II 32.5', 'materiaux', 'tonne', 145.00, pointp_id, 'Ciment CEM II/A-LL 32.5 R', true),
    ('Sable 0/4', 'materiaux', 'tonne', 28.00, gedimat_id, 'Sable fin 0/4 lavé', true),
    ('Gravier 4/20', 'materiaux', 'tonne', 32.00, pointp_id, 'Gravier concassé 4/20', true);
END $$;

-- Insertion de prestations externes
DO $$
DECLARE
    prestation_id uuid;
BEGIN
    SELECT id INTO prestation_id FROM fournisseurs WHERE nom = 'ENTREPRISE DUPONT' LIMIT 1;
    
    INSERT INTO articles (designation, type_article, unite, prix_unitaire, fournisseur_id, description, actif) VALUES
    ('Terrassement', 'prestation_externe', 'm3', 15.00, prestation_id, 'Terrassement et évacuation terres', true),
    ('Maçonnerie générale', 'prestation_externe', 'm2', 45.00, prestation_id, 'Maçonnerie générale au m2', true),
    ('Étanchéité toiture', 'prestation_externe', 'm2', 35.00, prestation_id, 'Étanchéité membrane EPDM', true);
END $$;