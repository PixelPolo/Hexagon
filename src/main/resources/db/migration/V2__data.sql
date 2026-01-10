-- VA Categories
INSERT INTO public."category" ("category_id", "name")
VALUES
    -- Particuliers
    (1, 'Mobilité'),
    (2, 'Logement'),
    (3, 'Prévoyance et hypothèque'),
    (4, 'Assistance et Voyage'),
    (5, 'Services'),
    -- Entreprise
    (6, 'Protection des personnes'),
    (7, 'Protection des biens'),
    (8, 'Protection de l''activité'),
    (9, 'Assistance & services')
;

ALTER TABLE public."category"
ALTER COLUMN "category_id" RESTART WITH 10;

-- VA Products
INSERT INTO public."product" ("product_id", "category_id", "name", "description", "created_at", "updated_at")
VALUES
-- Mobilité
(1, 1, 'Assurance voiture',
 'Responsabilité civile, casco partielle ou collision, occupants et protection juridique',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, 'Assurance moto et scooter',
 'Responsabilité civile, casco partielle ou collision, occupants et protection juridique',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 1, 'Assurance oldtimer',
 'Responsabilité civile, casco partielle ou collision, occupants et protection juridique',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 1, 'Assurance camping-car',
 'Responsabilité civile, casco partielle ou collision, occupants et protection juridique',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 1, 'Assurance bateau',
 'Responsabilité civile, casco partielle ou collision, occupants et protection juridique',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 1, 'Assurancce vélo e-bike', 'Protection complète et frais de transport',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 1, 'Protection juridique circulation',
 'Orion, défense pénale, droit des dommages et intérêt, droit des contrats relatifs à un véhicule et retrait de permis',
 CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
;

ALTER TABLE public."product"
ALTER COLUMN "product_id" RESTART WITH 8;