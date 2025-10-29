--
-- PostgreSQL database dump
--

-- Dumped from database version 15.12
-- Dumped by pg_dump version 15.12

-- Started on 2025-10-29 06:59:48

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 1147178)
-- Name: vianeo; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA vianeo;


ALTER SCHEMA vianeo OWNER TO postgres;

--
-- TOC entry 873 (class 1247 OID 1147196)
-- Name: categorie_article; Type: TYPE; Schema: vianeo; Owner: postgres
--

CREATE TYPE vianeo.categorie_article AS ENUM (
    'INTERNE',
    'EXTERNE'
);


ALTER TYPE vianeo.categorie_article OWNER TO postgres;

--
-- TOC entry 885 (class 1247 OID 1147232)
-- Name: categorie_personnel; Type: TYPE; Schema: vianeo; Owner: postgres
--

CREATE TYPE vianeo.categorie_personnel AS ENUM (
    'ENCADRANT',
    'OPERATIONNEL'
);


ALTER TYPE vianeo.categorie_personnel OWNER TO postgres;

--
-- TOC entry 879 (class 1247 OID 1147214)
-- Name: statut_rapport; Type: TYPE; Schema: vianeo; Owner: postgres
--

CREATE TYPE vianeo.statut_rapport AS ENUM (
    'DRAFT',
    'EN_ATTENTE_VALIDATION',
    'VALIDE',
    'REFUSE'
);


ALTER TYPE vianeo.statut_rapport OWNER TO postgres;

--
-- TOC entry 876 (class 1247 OID 1147202)
-- Name: type_article; Type: TYPE; Schema: vianeo; Owner: postgres
--

CREATE TYPE vianeo.type_article AS ENUM (
    'MATERIEL_AVEC_CHAUFFEUR',
    'MATERIEL_SANS_CHAUFFEUR',
    'TRANSPORT',
    'PRESTATION_EXT',
    'MATERIAUX'
);


ALTER TYPE vianeo.type_article OWNER TO postgres;

--
-- TOC entry 870 (class 1247 OID 1147180)
-- Name: type_fournisseur; Type: TYPE; Schema: vianeo; Owner: postgres
--

CREATE TYPE vianeo.type_fournisseur AS ENUM (
    'INTERIM',
    'LOUAGEUR',
    'LOCATION_SANS_CHAUFFEUR',
    'LOCATION_AVEC_CHAUFFEUR',
    'SOUS_TRAITANCE',
    'FOURNITURE',
    'AUTRE'
);


ALTER TYPE vianeo.type_fournisseur OWNER TO postgres;

--
-- TOC entry 882 (class 1247 OID 1147224)
-- Name: type_travail; Type: TYPE; Schema: vianeo; Owner: postgres
--

CREATE TYPE vianeo.type_travail AS ENUM (
    'J',
    'GD',
    'N'
);


ALTER TYPE vianeo.type_travail OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 216 (class 1259 OID 1151696)
-- Name: affectation_personnel; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.affectation_personnel (
    date_debut date NOT NULL,
    date_fin date,
    chantier_id bigint NOT NULL,
    id bigint NOT NULL,
    personnel_id bigint NOT NULL
);


ALTER TABLE vianeo.affectation_personnel OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 1151695)
-- Name: affectation_personnel_id_seq; Type: SEQUENCE; Schema: vianeo; Owner: postgres
--

CREATE SEQUENCE vianeo.affectation_personnel_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vianeo.affectation_personnel_id_seq OWNER TO postgres;

--
-- TOC entry 3558 (class 0 OID 0)
-- Dependencies: 215
-- Name: affectation_personnel_id_seq; Type: SEQUENCE OWNED BY; Schema: vianeo; Owner: postgres
--

ALTER SEQUENCE vianeo.affectation_personnel_id_seq OWNED BY vianeo.affectation_personnel.id;


--
-- TOC entry 218 (class 1259 OID 1151703)
-- Name: article; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.article (
    actif boolean NOT NULL,
    id bigint NOT NULL,
    code character varying(80) NOT NULL,
    designation character varying(160) NOT NULL,
    cat character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    CONSTRAINT article_cat_check CHECK (((cat)::text = ANY ((ARRAY['INTERNE'::character varying, 'EXTERNE'::character varying])::text[]))),
    CONSTRAINT article_type_check CHECK (((type)::text = ANY ((ARRAY['MATERIEL_AVEC_CHAUFFEUR'::character varying, 'MATERIEL_SANS_CHAUFFEUR'::character varying, 'TRANSPORT'::character varying, 'PRESTATION_EXT'::character varying, 'MATERIAUX'::character varying])::text[])))
);


ALTER TABLE vianeo.article OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 1151702)
-- Name: article_id_seq; Type: SEQUENCE; Schema: vianeo; Owner: postgres
--

CREATE SEQUENCE vianeo.article_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vianeo.article_id_seq OWNER TO postgres;

--
-- TOC entry 3559 (class 0 OID 0)
-- Dependencies: 217
-- Name: article_id_seq; Type: SEQUENCE OWNED BY; Schema: vianeo; Owner: postgres
--

ALTER SEQUENCE vianeo.article_id_seq OWNED BY vianeo.article.id;


--
-- TOC entry 220 (class 1259 OID 1151716)
-- Name: chantier; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.chantier (
    actif boolean NOT NULL,
    entite_id bigint NOT NULL,
    id bigint NOT NULL,
    responsable_id bigint,
    code_postal character varying(12),
    code character varying(80) NOT NULL,
    ville character varying(80),
    libelle character varying(160) NOT NULL,
    adresse text
);


ALTER TABLE vianeo.chantier OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 1151715)
-- Name: chantier_id_seq; Type: SEQUENCE; Schema: vianeo; Owner: postgres
--

CREATE SEQUENCE vianeo.chantier_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vianeo.chantier_id_seq OWNER TO postgres;

--
-- TOC entry 3560 (class 0 OID 0)
-- Dependencies: 219
-- Name: chantier_id_seq; Type: SEQUENCE OWNED BY; Schema: vianeo; Owner: postgres
--

ALTER SEQUENCE vianeo.chantier_id_seq OWNED BY vianeo.chantier.id;


--
-- TOC entry 222 (class 1259 OID 1151727)
-- Name: compte; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.compte (
    actif boolean NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    id bigint NOT NULL,
    last_login timestamp(6) without time zone,
    personnel_id bigint NOT NULL,
    role character varying(50) NOT NULL,
    username character varying(100) NOT NULL,
    email character varying(150) NOT NULL,
    password_hash character varying(255) NOT NULL
);


ALTER TABLE vianeo.compte OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 1151726)
-- Name: compte_id_seq; Type: SEQUENCE; Schema: vianeo; Owner: postgres
--

CREATE SEQUENCE vianeo.compte_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vianeo.compte_id_seq OWNER TO postgres;

--
-- TOC entry 3561 (class 0 OID 0)
-- Dependencies: 221
-- Name: compte_id_seq; Type: SEQUENCE OWNED BY; Schema: vianeo; Owner: postgres
--

ALTER SEQUENCE vianeo.compte_id_seq OWNED BY vianeo.compte.id;


--
-- TOC entry 224 (class 1259 OID 1151742)
-- Name: entite; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.entite (
    actif boolean NOT NULL,
    id bigint NOT NULL,
    code character varying(32) NOT NULL,
    libelle character varying(128) NOT NULL
);


ALTER TABLE vianeo.entite OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 1151741)
-- Name: entite_id_seq; Type: SEQUENCE; Schema: vianeo; Owner: postgres
--

CREATE SEQUENCE vianeo.entite_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vianeo.entite_id_seq OWNER TO postgres;

--
-- TOC entry 3562 (class 0 OID 0)
-- Dependencies: 223
-- Name: entite_id_seq; Type: SEQUENCE OWNED BY; Schema: vianeo; Owner: postgres
--

ALTER SEQUENCE vianeo.entite_id_seq OWNED BY vianeo.entite.id;


--
-- TOC entry 226 (class 1259 OID 1151751)
-- Name: fournisseur; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.fournisseur (
    actif boolean NOT NULL,
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    type character varying(255) NOT NULL,
    CONSTRAINT fournisseur_type_check CHECK (((type)::text = ANY ((ARRAY['INTERIM'::character varying, 'LOUAGEUR'::character varying, 'LOCATION_SANS_CHAUFFEUR'::character varying, 'LOCATION_AVEC_CHAUFFEUR'::character varying, 'SOUS_TRAITANCE'::character varying, 'FOURNITURE'::character varying, 'AUTRE'::character varying])::text[])))
);


ALTER TABLE vianeo.fournisseur OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 1151750)
-- Name: fournisseur_id_seq; Type: SEQUENCE; Schema: vianeo; Owner: postgres
--

CREATE SEQUENCE vianeo.fournisseur_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vianeo.fournisseur_id_seq OWNER TO postgres;

--
-- TOC entry 3563 (class 0 OID 0)
-- Dependencies: 225
-- Name: fournisseur_id_seq; Type: SEQUENCE OWNED BY; Schema: vianeo; Owner: postgres
--

ALTER SEQUENCE vianeo.fournisseur_id_seq OWNED BY vianeo.fournisseur.id;


--
-- TOC entry 228 (class 1259 OID 1151761)
-- Name: ligne_interim; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.ligne_interim (
    pu numeric(12,2) NOT NULL,
    quantite numeric(10,2) NOT NULL,
    total numeric(14,2),
    fournisseur_id bigint NOT NULL,
    id bigint NOT NULL,
    rapport_id bigint NOT NULL,
    nom character varying(80),
    prenom character varying(80),
    type_travail character varying(255) NOT NULL,
    CONSTRAINT ligne_interim_type_travail_check CHECK (((type_travail)::text = ANY ((ARRAY['J'::character varying, 'GD'::character varying, 'N'::character varying])::text[])))
);


ALTER TABLE vianeo.ligne_interim OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 1151760)
-- Name: ligne_interim_id_seq; Type: SEQUENCE; Schema: vianeo; Owner: postgres
--

CREATE SEQUENCE vianeo.ligne_interim_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vianeo.ligne_interim_id_seq OWNER TO postgres;

--
-- TOC entry 3564 (class 0 OID 0)
-- Dependencies: 227
-- Name: ligne_interim_id_seq; Type: SEQUENCE OWNED BY; Schema: vianeo; Owner: postgres
--

ALTER SEQUENCE vianeo.ligne_interim_id_seq OWNED BY vianeo.ligne_interim.id;


--
-- TOC entry 230 (class 1259 OID 1151769)
-- Name: ligne_loc_avec_ch; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.ligne_loc_avec_ch (
    pu numeric(12,2) NOT NULL,
    quantite numeric(10,2) NOT NULL,
    total numeric(14,2),
    article_id bigint NOT NULL,
    fournisseur_id bigint NOT NULL,
    id bigint NOT NULL,
    rapport_id bigint NOT NULL
);


ALTER TABLE vianeo.ligne_loc_avec_ch OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 1151768)
-- Name: ligne_loc_avec_ch_id_seq; Type: SEQUENCE; Schema: vianeo; Owner: postgres
--

CREATE SEQUENCE vianeo.ligne_loc_avec_ch_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vianeo.ligne_loc_avec_ch_id_seq OWNER TO postgres;

--
-- TOC entry 3565 (class 0 OID 0)
-- Dependencies: 229
-- Name: ligne_loc_avec_ch_id_seq; Type: SEQUENCE OWNED BY; Schema: vianeo; Owner: postgres
--

ALTER SEQUENCE vianeo.ligne_loc_avec_ch_id_seq OWNED BY vianeo.ligne_loc_avec_ch.id;


--
-- TOC entry 232 (class 1259 OID 1151776)
-- Name: ligne_loc_ss_ch; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.ligne_loc_ss_ch (
    pu numeric(12,2) NOT NULL,
    quantite numeric(10,2) NOT NULL,
    total numeric(14,2),
    article_id bigint NOT NULL,
    fournisseur_id bigint NOT NULL,
    id bigint NOT NULL,
    rapport_id bigint NOT NULL
);


ALTER TABLE vianeo.ligne_loc_ss_ch OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 1151775)
-- Name: ligne_loc_ss_ch_id_seq; Type: SEQUENCE; Schema: vianeo; Owner: postgres
--

CREATE SEQUENCE vianeo.ligne_loc_ss_ch_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vianeo.ligne_loc_ss_ch_id_seq OWNER TO postgres;

--
-- TOC entry 3566 (class 0 OID 0)
-- Dependencies: 231
-- Name: ligne_loc_ss_ch_id_seq; Type: SEQUENCE OWNED BY; Schema: vianeo; Owner: postgres
--

ALTER SEQUENCE vianeo.ligne_loc_ss_ch_id_seq OWNED BY vianeo.ligne_loc_ss_ch.id;


--
-- TOC entry 234 (class 1259 OID 1151783)
-- Name: ligne_mat_interne; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.ligne_mat_interne (
    pu numeric(12,2) NOT NULL,
    quantite numeric(10,2) NOT NULL,
    total numeric(14,2),
    article_id bigint NOT NULL,
    id bigint NOT NULL,
    rapport_id bigint NOT NULL
);


ALTER TABLE vianeo.ligne_mat_interne OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 1151782)
-- Name: ligne_mat_interne_id_seq; Type: SEQUENCE; Schema: vianeo; Owner: postgres
--

CREATE SEQUENCE vianeo.ligne_mat_interne_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vianeo.ligne_mat_interne_id_seq OWNER TO postgres;

--
-- TOC entry 3567 (class 0 OID 0)
-- Dependencies: 233
-- Name: ligne_mat_interne_id_seq; Type: SEQUENCE OWNED BY; Schema: vianeo; Owner: postgres
--

ALTER SEQUENCE vianeo.ligne_mat_interne_id_seq OWNED BY vianeo.ligne_mat_interne.id;


--
-- TOC entry 236 (class 1259 OID 1151790)
-- Name: ligne_materiaux; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.ligne_materiaux (
    pu numeric(12,2) NOT NULL,
    quantite numeric(12,3) NOT NULL,
    total numeric(14,2),
    article_id bigint NOT NULL,
    fournisseur_id bigint,
    id bigint NOT NULL,
    rapport_id bigint NOT NULL
);


ALTER TABLE vianeo.ligne_materiaux OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 1151789)
-- Name: ligne_materiaux_id_seq; Type: SEQUENCE; Schema: vianeo; Owner: postgres
--

CREATE SEQUENCE vianeo.ligne_materiaux_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vianeo.ligne_materiaux_id_seq OWNER TO postgres;

--
-- TOC entry 3568 (class 0 OID 0)
-- Dependencies: 235
-- Name: ligne_materiaux_id_seq; Type: SEQUENCE OWNED BY; Schema: vianeo; Owner: postgres
--

ALTER SEQUENCE vianeo.ligne_materiaux_id_seq OWNED BY vianeo.ligne_materiaux.id;


--
-- TOC entry 238 (class 1259 OID 1151797)
-- Name: ligne_personnel; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.ligne_personnel (
    pu numeric(12,2) NOT NULL,
    quantite numeric(10,2) NOT NULL,
    total numeric(14,2),
    id bigint NOT NULL,
    personnel_id bigint NOT NULL,
    rapport_id bigint NOT NULL,
    categorie character varying(255) NOT NULL,
    type_travail character varying(255) NOT NULL,
    CONSTRAINT ligne_personnel_categorie_check CHECK (((categorie)::text = ANY ((ARRAY['ENCADRANT'::character varying, 'OPERATIONNEL'::character varying])::text[]))),
    CONSTRAINT ligne_personnel_type_travail_check CHECK (((type_travail)::text = ANY ((ARRAY['J'::character varying, 'GD'::character varying, 'N'::character varying])::text[])))
);


ALTER TABLE vianeo.ligne_personnel OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 1151796)
-- Name: ligne_personnel_id_seq; Type: SEQUENCE; Schema: vianeo; Owner: postgres
--

CREATE SEQUENCE vianeo.ligne_personnel_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vianeo.ligne_personnel_id_seq OWNER TO postgres;

--
-- TOC entry 3569 (class 0 OID 0)
-- Dependencies: 237
-- Name: ligne_personnel_id_seq; Type: SEQUENCE OWNED BY; Schema: vianeo; Owner: postgres
--

ALTER SEQUENCE vianeo.ligne_personnel_id_seq OWNED BY vianeo.ligne_personnel.id;


--
-- TOC entry 240 (class 1259 OID 1151808)
-- Name: ligne_presta_ext; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.ligne_presta_ext (
    pu numeric(12,2) NOT NULL,
    quantite numeric(10,2) NOT NULL,
    total numeric(14,2),
    article_id bigint NOT NULL,
    fournisseur_id bigint NOT NULL,
    id bigint NOT NULL,
    rapport_id bigint NOT NULL
);


ALTER TABLE vianeo.ligne_presta_ext OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 1151807)
-- Name: ligne_presta_ext_id_seq; Type: SEQUENCE; Schema: vianeo; Owner: postgres
--

CREATE SEQUENCE vianeo.ligne_presta_ext_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vianeo.ligne_presta_ext_id_seq OWNER TO postgres;

--
-- TOC entry 3570 (class 0 OID 0)
-- Dependencies: 239
-- Name: ligne_presta_ext_id_seq; Type: SEQUENCE OWNED BY; Schema: vianeo; Owner: postgres
--

ALTER SEQUENCE vianeo.ligne_presta_ext_id_seq OWNED BY vianeo.ligne_presta_ext.id;


--
-- TOC entry 242 (class 1259 OID 1151815)
-- Name: ligne_transport; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.ligne_transport (
    pu numeric(12,2) NOT NULL,
    quantite numeric(10,2) NOT NULL,
    total numeric(14,2),
    article_id bigint NOT NULL,
    fournisseur_id bigint NOT NULL,
    id bigint NOT NULL,
    rapport_id bigint NOT NULL
);


ALTER TABLE vianeo.ligne_transport OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 1151814)
-- Name: ligne_transport_id_seq; Type: SEQUENCE; Schema: vianeo; Owner: postgres
--

CREATE SEQUENCE vianeo.ligne_transport_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vianeo.ligne_transport_id_seq OWNER TO postgres;

--
-- TOC entry 3571 (class 0 OID 0)
-- Dependencies: 241
-- Name: ligne_transport_id_seq; Type: SEQUENCE OWNED BY; Schema: vianeo; Owner: postgres
--

ALTER SEQUENCE vianeo.ligne_transport_id_seq OWNED BY vianeo.ligne_transport.id;


--
-- TOC entry 244 (class 1259 OID 1151822)
-- Name: personnel; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.personnel (
    actif boolean NOT NULL,
    taux numeric(12,2),
    entite_id bigint NOT NULL,
    id bigint NOT NULL,
    profil character varying(32) NOT NULL,
    metier character varying(64) NOT NULL,
    nom character varying(80) NOT NULL,
    prenom character varying(80) NOT NULL
);


ALTER TABLE vianeo.personnel OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 1151821)
-- Name: personnel_id_seq; Type: SEQUENCE; Schema: vianeo; Owner: postgres
--

CREATE SEQUENCE vianeo.personnel_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vianeo.personnel_id_seq OWNER TO postgres;

--
-- TOC entry 3572 (class 0 OID 0)
-- Dependencies: 243
-- Name: personnel_id_seq; Type: SEQUENCE OWNED BY; Schema: vianeo; Owner: postgres
--

ALTER SEQUENCE vianeo.personnel_id_seq OWNED BY vianeo.personnel.id;


--
-- TOC entry 246 (class 1259 OID 1151829)
-- Name: rapport; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.rapport (
    jour date NOT NULL,
    auteur_id bigint NOT NULL,
    chantier_id bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    id bigint NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    commentaire_cdt text,
    statut character varying(255) NOT NULL,
    CONSTRAINT rapport_statut_check CHECK (((statut)::text = ANY ((ARRAY['DRAFT'::character varying, 'EN_ATTENTE_VALIDATION'::character varying, 'VALIDE'::character varying, 'REFUSE'::character varying])::text[])))
);


ALTER TABLE vianeo.rapport OWNER TO postgres;

--
-- TOC entry 245 (class 1259 OID 1151828)
-- Name: rapport_id_seq; Type: SEQUENCE; Schema: vianeo; Owner: postgres
--

CREATE SEQUENCE vianeo.rapport_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vianeo.rapport_id_seq OWNER TO postgres;

--
-- TOC entry 3573 (class 0 OID 0)
-- Dependencies: 245
-- Name: rapport_id_seq; Type: SEQUENCE OWNED BY; Schema: vianeo; Owner: postgres
--

ALTER SEQUENCE vianeo.rapport_id_seq OWNED BY vianeo.rapport.id;


--
-- TOC entry 247 (class 1259 OID 1151840)
-- Name: v_rapport_totaux; Type: TABLE; Schema: vianeo; Owner: postgres
--

CREATE TABLE vianeo.v_rapport_totaux (
    total_general numeric(38,2),
    rapport_id bigint NOT NULL
);


ALTER TABLE vianeo.v_rapport_totaux OWNER TO postgres;

--
-- TOC entry 3271 (class 2604 OID 1151699)
-- Name: affectation_personnel id; Type: DEFAULT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.affectation_personnel ALTER COLUMN id SET DEFAULT nextval('vianeo.affectation_personnel_id_seq'::regclass);


--
-- TOC entry 3272 (class 2604 OID 1151706)
-- Name: article id; Type: DEFAULT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.article ALTER COLUMN id SET DEFAULT nextval('vianeo.article_id_seq'::regclass);


--
-- TOC entry 3273 (class 2604 OID 1151719)
-- Name: chantier id; Type: DEFAULT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.chantier ALTER COLUMN id SET DEFAULT nextval('vianeo.chantier_id_seq'::regclass);


--
-- TOC entry 3274 (class 2604 OID 1151730)
-- Name: compte id; Type: DEFAULT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.compte ALTER COLUMN id SET DEFAULT nextval('vianeo.compte_id_seq'::regclass);


--
-- TOC entry 3275 (class 2604 OID 1151745)
-- Name: entite id; Type: DEFAULT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.entite ALTER COLUMN id SET DEFAULT nextval('vianeo.entite_id_seq'::regclass);


--
-- TOC entry 3276 (class 2604 OID 1151754)
-- Name: fournisseur id; Type: DEFAULT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.fournisseur ALTER COLUMN id SET DEFAULT nextval('vianeo.fournisseur_id_seq'::regclass);


--
-- TOC entry 3277 (class 2604 OID 1151764)
-- Name: ligne_interim id; Type: DEFAULT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_interim ALTER COLUMN id SET DEFAULT nextval('vianeo.ligne_interim_id_seq'::regclass);


--
-- TOC entry 3278 (class 2604 OID 1151772)
-- Name: ligne_loc_avec_ch id; Type: DEFAULT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_loc_avec_ch ALTER COLUMN id SET DEFAULT nextval('vianeo.ligne_loc_avec_ch_id_seq'::regclass);


--
-- TOC entry 3279 (class 2604 OID 1151779)
-- Name: ligne_loc_ss_ch id; Type: DEFAULT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_loc_ss_ch ALTER COLUMN id SET DEFAULT nextval('vianeo.ligne_loc_ss_ch_id_seq'::regclass);


--
-- TOC entry 3280 (class 2604 OID 1151786)
-- Name: ligne_mat_interne id; Type: DEFAULT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_mat_interne ALTER COLUMN id SET DEFAULT nextval('vianeo.ligne_mat_interne_id_seq'::regclass);


--
-- TOC entry 3281 (class 2604 OID 1151793)
-- Name: ligne_materiaux id; Type: DEFAULT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_materiaux ALTER COLUMN id SET DEFAULT nextval('vianeo.ligne_materiaux_id_seq'::regclass);


--
-- TOC entry 3282 (class 2604 OID 1151800)
-- Name: ligne_personnel id; Type: DEFAULT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_personnel ALTER COLUMN id SET DEFAULT nextval('vianeo.ligne_personnel_id_seq'::regclass);


--
-- TOC entry 3283 (class 2604 OID 1151811)
-- Name: ligne_presta_ext id; Type: DEFAULT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_presta_ext ALTER COLUMN id SET DEFAULT nextval('vianeo.ligne_presta_ext_id_seq'::regclass);


--
-- TOC entry 3284 (class 2604 OID 1151818)
-- Name: ligne_transport id; Type: DEFAULT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_transport ALTER COLUMN id SET DEFAULT nextval('vianeo.ligne_transport_id_seq'::regclass);


--
-- TOC entry 3285 (class 2604 OID 1151825)
-- Name: personnel id; Type: DEFAULT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.personnel ALTER COLUMN id SET DEFAULT nextval('vianeo.personnel_id_seq'::regclass);


--
-- TOC entry 3286 (class 2604 OID 1151832)
-- Name: rapport id; Type: DEFAULT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.rapport ALTER COLUMN id SET DEFAULT nextval('vianeo.rapport_id_seq'::regclass);


--
-- TOC entry 3521 (class 0 OID 1151696)
-- Dependencies: 216
-- Data for Name: affectation_personnel; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.affectation_personnel (date_debut, date_fin, chantier_id, id, personnel_id) FROM stdin;
2025-10-01	\N	1	1	2
\.


--
-- TOC entry 3523 (class 0 OID 1151703)
-- Dependencies: 218
-- Data for Name: article; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.article (actif, id, code, designation, cat, type) FROM stdin;
t	1	ART-INT-001	Mini-pelle interne	INTERNE	MATERIEL_SANS_CHAUFFEUR
t	2	ART-LOCSS-001	Plaque vibrante (SS ch.)	EXTERNE	MATERIEL_SANS_CHAUFFEUR
t	3	ART-TRANSP-001	Transport camion 8x4	EXTERNE	TRANSPORT
t	4	ART-PREST-001	Sous-traitance maçonnerie	EXTERNE	PRESTATION_EXT
t	5	ART-MAT-001	Gravier 0/31.5	EXTERNE	MATERIAUX
\.


--
-- TOC entry 3525 (class 0 OID 1151716)
-- Dependencies: 220
-- Data for Name: chantier; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.chantier (actif, entite_id, id, responsable_id, code_postal, code, ville, libelle, adresse) FROM stdin;
t	1	1	2	59000	CH-0001	Lille	Chantier Test	1 rue du Test
t	1	2	2	34000	CH-0002	Montpellier	Chantier Sept	13 rue du Test
\.


--
-- TOC entry 3527 (class 0 OID 1151727)
-- Dependencies: 222
-- Data for Name: compte; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.compte (actif, created_at, id, last_login, personnel_id, role, username, email, password_hash) FROM stdin;
t	2025-10-10 16:27:19.560933	2	2025-10-10 16:52:42.958697	2	ROLE_CDT	chef	chef@vianeo.test	$2a$12$2CFhXJ6pWgOtXIm3ByeLEeKqL8hc/hwHQRAOO6JW9cCDe1ffsDs56
t	2025-10-08 21:39:43.552014	1	2025-10-28 22:55:07.807833	1	ROLE_ADMIN	admin	admin@vianeo.test	$2a$12$2CFhXJ6pWgOtXIm3ByeLEeKqL8hc/hwHQRAOO6JW9cCDe1ffsDs56
\.


--
-- TOC entry 3529 (class 0 OID 1151742)
-- Dependencies: 224
-- Data for Name: entite; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.entite (actif, id, code, libelle) FROM stdin;
t	1	AG01	Agence Nord
\.


--
-- TOC entry 3531 (class 0 OID 1151751)
-- Dependencies: 226
-- Data for Name: fournisseur; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.fournisseur (actif, id, code, type) FROM stdin;
t	1	FOUR-INT-01	INTERIM
t	2	FOUR-LOCSS-01	LOCATION_SANS_CHAUFFEUR
t	3	FOUR-PREST-01	SOUS_TRAITANCE
t	4	Amine Madani	AUTRE
f	5	Fournisseur 2 Transpo	AUTRE
f	8	Mohamed Madani	LOUAGEUR
t	9	Fourisseur FN	FOURNITURE
\.


--
-- TOC entry 3533 (class 0 OID 1151761)
-- Dependencies: 228
-- Data for Name: ligne_interim; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.ligne_interim (pu, quantite, total, fournisseur_id, id, rapport_id, nom, prenom, type_travail) FROM stdin;
\.


--
-- TOC entry 3535 (class 0 OID 1151769)
-- Dependencies: 230
-- Data for Name: ligne_loc_avec_ch; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.ligne_loc_avec_ch (pu, quantite, total, article_id, fournisseur_id, id, rapport_id) FROM stdin;
\.


--
-- TOC entry 3537 (class 0 OID 1151776)
-- Dependencies: 232
-- Data for Name: ligne_loc_ss_ch; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.ligne_loc_ss_ch (pu, quantite, total, article_id, fournisseur_id, id, rapport_id) FROM stdin;
\.


--
-- TOC entry 3539 (class 0 OID 1151783)
-- Dependencies: 234
-- Data for Name: ligne_mat_interne; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.ligne_mat_interne (pu, quantite, total, article_id, id, rapport_id) FROM stdin;
0.05	1.00	\N	1	5	6
0.05	1.00	\N	1	6	6
0.05	0.50	\N	1	7	6
0.05	1.00	\N	1	8	6
1.00	1.00	\N	1	9	6
10.00	1.00	\N	1	11	6
0.05	1.50	\N	1	3	6
10.00	1.00	\N	1	10	6
10.00	1.00	\N	1	12	6
0.05	0.50	\N	1	4	6
\.


--
-- TOC entry 3541 (class 0 OID 1151790)
-- Dependencies: 236
-- Data for Name: ligne_materiaux; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.ligne_materiaux (pu, quantite, total, article_id, fournisseur_id, id, rapport_id) FROM stdin;
\.


--
-- TOC entry 3543 (class 0 OID 1151797)
-- Dependencies: 238
-- Data for Name: ligne_personnel; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.ligne_personnel (pu, quantite, total, id, personnel_id, rapport_id, categorie, type_travail) FROM stdin;
\.


--
-- TOC entry 3545 (class 0 OID 1151808)
-- Dependencies: 240
-- Data for Name: ligne_presta_ext; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.ligne_presta_ext (pu, quantite, total, article_id, fournisseur_id, id, rapport_id) FROM stdin;
\.


--
-- TOC entry 3547 (class 0 OID 1151815)
-- Dependencies: 242
-- Data for Name: ligne_transport; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.ligne_transport (pu, quantite, total, article_id, fournisseur_id, id, rapport_id) FROM stdin;
\.


--
-- TOC entry 3549 (class 0 OID 1151822)
-- Dependencies: 244
-- Data for Name: personnel; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.personnel (actif, taux, entite_id, id, profil, metier, nom, prenom) FROM stdin;
t	450.00	1	1	ROLE_ADMIN	CDT	Admin	Alice
t	350.00	1	2	ROLE_CDT	CDT	Chef	Bob
f	200.00	1	3	OPERATIONNEL	Encadrant	OperPersonnel2	OpeNomPersonnel2
t	50.00	1	4	ENCADRANT	Qlfncadrant	NmEncadrant	PrNmEncadrant
\.


--
-- TOC entry 3551 (class 0 OID 1151829)
-- Dependencies: 246
-- Data for Name: rapport; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.rapport (jour, auteur_id, chantier_id, created_at, id, updated_at, commentaire_cdt, statut) FROM stdin;
2025-10-08	2	1	2025-10-08 21:39:43.566973	1	2025-10-08 21:39:43.566973	\N	DRAFT
2025-10-06	1	1	2025-10-10 10:04:55.658547	3	2025-10-10 10:04:55.658547	\N	DRAFT
2025-10-07	1	1	2025-10-10 10:04:55.825066	4	2025-10-10 10:04:55.825066	\N	DRAFT
2025-10-09	1	1	2025-10-10 10:04:55.842069	5	2025-10-10 10:04:55.842069	\N	DRAFT
2025-10-11	1	1	2025-10-10 10:04:55.857077	7	2025-10-10 10:04:55.857077	\N	DRAFT
2025-10-12	1	1	2025-10-10 10:04:55.865071	8	2025-10-10 10:04:55.865071	\N	DRAFT
2025-10-14	1	1	2025-10-10 12:23:33.105158	10	2025-10-10 12:23:33.105158	\N	DRAFT
2025-10-15	1	1	2025-10-10 12:23:33.115692	11	2025-10-10 12:23:33.115692	\N	DRAFT
2025-10-16	1	1	2025-10-10 12:23:33.123734	12	2025-10-10 12:23:33.123734	\N	DRAFT
2025-10-17	1	1	2025-10-10 12:23:33.130924	13	2025-10-10 12:23:33.130924	\N	DRAFT
2025-10-18	1	1	2025-10-10 12:23:33.140983	14	2025-10-10 12:23:33.140983	\N	DRAFT
2025-10-19	1	1	2025-10-10 12:23:33.150486	15	2025-10-10 12:23:33.150486	\N	DRAFT
2025-10-13	1	1	2025-10-10 12:23:33.085854	9	2025-10-10 12:24:29.107763	\N	EN_ATTENTE_VALIDATION
2025-10-10	1	1	2025-10-10 10:04:55.850068	6	2025-10-10 15:26:43.219813	\N	VALIDE
2025-10-28	1	1	2025-10-28 08:46:39.903019	16	2025-10-28 08:46:39.903019	\N	DRAFT
2025-10-27	1	1	2025-10-28 08:46:50.256867	17	2025-10-28 08:46:50.256867	\N	DRAFT
2025-10-29	1	1	2025-10-28 08:46:50.343259	18	2025-10-28 08:46:50.343259	\N	DRAFT
2025-10-30	1	1	2025-10-28 08:46:50.359034	19	2025-10-28 08:46:50.359034	\N	DRAFT
2025-10-31	1	1	2025-10-28 08:46:50.370306	20	2025-10-28 08:46:50.370306	\N	DRAFT
2025-11-01	1	1	2025-10-28 08:46:50.378481	21	2025-10-28 08:46:50.378481	\N	DRAFT
2025-11-02	1	1	2025-10-28 08:46:50.39091	22	2025-10-28 08:46:50.39091	\N	DRAFT
\.


--
-- TOC entry 3552 (class 0 OID 1151840)
-- Dependencies: 247
-- Data for Name: v_rapport_totaux; Type: TABLE DATA; Schema: vianeo; Owner: postgres
--

COPY vianeo.v_rapport_totaux (total_general, rapport_id) FROM stdin;
\.


--
-- TOC entry 3574 (class 0 OID 0)
-- Dependencies: 215
-- Name: affectation_personnel_id_seq; Type: SEQUENCE SET; Schema: vianeo; Owner: postgres
--

SELECT pg_catalog.setval('vianeo.affectation_personnel_id_seq', 1, true);


--
-- TOC entry 3575 (class 0 OID 0)
-- Dependencies: 217
-- Name: article_id_seq; Type: SEQUENCE SET; Schema: vianeo; Owner: postgres
--

SELECT pg_catalog.setval('vianeo.article_id_seq', 2, true);


--
-- TOC entry 3576 (class 0 OID 0)
-- Dependencies: 219
-- Name: chantier_id_seq; Type: SEQUENCE SET; Schema: vianeo; Owner: postgres
--

SELECT pg_catalog.setval('vianeo.chantier_id_seq', 2, true);


--
-- TOC entry 3577 (class 0 OID 0)
-- Dependencies: 221
-- Name: compte_id_seq; Type: SEQUENCE SET; Schema: vianeo; Owner: postgres
--

SELECT pg_catalog.setval('vianeo.compte_id_seq', 2, true);


--
-- TOC entry 3578 (class 0 OID 0)
-- Dependencies: 223
-- Name: entite_id_seq; Type: SEQUENCE SET; Schema: vianeo; Owner: postgres
--

SELECT pg_catalog.setval('vianeo.entite_id_seq', 1, true);


--
-- TOC entry 3579 (class 0 OID 0)
-- Dependencies: 225
-- Name: fournisseur_id_seq; Type: SEQUENCE SET; Schema: vianeo; Owner: postgres
--

SELECT pg_catalog.setval('vianeo.fournisseur_id_seq', 9, true);


--
-- TOC entry 3580 (class 0 OID 0)
-- Dependencies: 227
-- Name: ligne_interim_id_seq; Type: SEQUENCE SET; Schema: vianeo; Owner: postgres
--

SELECT pg_catalog.setval('vianeo.ligne_interim_id_seq', 2, true);


--
-- TOC entry 3581 (class 0 OID 0)
-- Dependencies: 229
-- Name: ligne_loc_avec_ch_id_seq; Type: SEQUENCE SET; Schema: vianeo; Owner: postgres
--

SELECT pg_catalog.setval('vianeo.ligne_loc_avec_ch_id_seq', 2, true);


--
-- TOC entry 3582 (class 0 OID 0)
-- Dependencies: 231
-- Name: ligne_loc_ss_ch_id_seq; Type: SEQUENCE SET; Schema: vianeo; Owner: postgres
--

SELECT pg_catalog.setval('vianeo.ligne_loc_ss_ch_id_seq', 2, true);


--
-- TOC entry 3583 (class 0 OID 0)
-- Dependencies: 233
-- Name: ligne_mat_interne_id_seq; Type: SEQUENCE SET; Schema: vianeo; Owner: postgres
--

SELECT pg_catalog.setval('vianeo.ligne_mat_interne_id_seq', 12, true);


--
-- TOC entry 3584 (class 0 OID 0)
-- Dependencies: 235
-- Name: ligne_materiaux_id_seq; Type: SEQUENCE SET; Schema: vianeo; Owner: postgres
--

SELECT pg_catalog.setval('vianeo.ligne_materiaux_id_seq', 2, true);


--
-- TOC entry 3585 (class 0 OID 0)
-- Dependencies: 237
-- Name: ligne_personnel_id_seq; Type: SEQUENCE SET; Schema: vianeo; Owner: postgres
--

SELECT pg_catalog.setval('vianeo.ligne_personnel_id_seq', 2, true);


--
-- TOC entry 3586 (class 0 OID 0)
-- Dependencies: 239
-- Name: ligne_presta_ext_id_seq; Type: SEQUENCE SET; Schema: vianeo; Owner: postgres
--

SELECT pg_catalog.setval('vianeo.ligne_presta_ext_id_seq', 2, true);


--
-- TOC entry 3587 (class 0 OID 0)
-- Dependencies: 241
-- Name: ligne_transport_id_seq; Type: SEQUENCE SET; Schema: vianeo; Owner: postgres
--

SELECT pg_catalog.setval('vianeo.ligne_transport_id_seq', 2, true);


--
-- TOC entry 3588 (class 0 OID 0)
-- Dependencies: 243
-- Name: personnel_id_seq; Type: SEQUENCE SET; Schema: vianeo; Owner: postgres
--

SELECT pg_catalog.setval('vianeo.personnel_id_seq', 4, true);


--
-- TOC entry 3589 (class 0 OID 0)
-- Dependencies: 245
-- Name: rapport_id_seq; Type: SEQUENCE SET; Schema: vianeo; Owner: postgres
--

SELECT pg_catalog.setval('vianeo.rapport_id_seq', 22, true);


--
-- TOC entry 3295 (class 2606 OID 1151701)
-- Name: affectation_personnel affectation_personnel_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.affectation_personnel
    ADD CONSTRAINT affectation_personnel_pkey PRIMARY KEY (id);


--
-- TOC entry 3298 (class 2606 OID 1151714)
-- Name: article article_code_key; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.article
    ADD CONSTRAINT article_code_key UNIQUE (code);


--
-- TOC entry 3300 (class 2606 OID 1151712)
-- Name: article article_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.article
    ADD CONSTRAINT article_pkey PRIMARY KEY (id);


--
-- TOC entry 3302 (class 2606 OID 1151725)
-- Name: chantier chantier_code_key; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.chantier
    ADD CONSTRAINT chantier_code_key UNIQUE (code);


--
-- TOC entry 3304 (class 2606 OID 1151723)
-- Name: chantier chantier_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.chantier
    ADD CONSTRAINT chantier_pkey PRIMARY KEY (id);


--
-- TOC entry 3306 (class 2606 OID 1151740)
-- Name: compte compte_email_key; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.compte
    ADD CONSTRAINT compte_email_key UNIQUE (email);


--
-- TOC entry 3308 (class 2606 OID 1151736)
-- Name: compte compte_personnel_id_key; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.compte
    ADD CONSTRAINT compte_personnel_id_key UNIQUE (personnel_id);


--
-- TOC entry 3310 (class 2606 OID 1151734)
-- Name: compte compte_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.compte
    ADD CONSTRAINT compte_pkey PRIMARY KEY (id);


--
-- TOC entry 3312 (class 2606 OID 1151738)
-- Name: compte compte_username_key; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.compte
    ADD CONSTRAINT compte_username_key UNIQUE (username);


--
-- TOC entry 3314 (class 2606 OID 1151749)
-- Name: entite entite_code_key; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.entite
    ADD CONSTRAINT entite_code_key UNIQUE (code);


--
-- TOC entry 3316 (class 2606 OID 1151747)
-- Name: entite entite_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.entite
    ADD CONSTRAINT entite_pkey PRIMARY KEY (id);


--
-- TOC entry 3318 (class 2606 OID 1151759)
-- Name: fournisseur fournisseur_code_key; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.fournisseur
    ADD CONSTRAINT fournisseur_code_key UNIQUE (code);


--
-- TOC entry 3320 (class 2606 OID 1151757)
-- Name: fournisseur fournisseur_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.fournisseur
    ADD CONSTRAINT fournisseur_pkey PRIMARY KEY (id);


--
-- TOC entry 3323 (class 2606 OID 1151767)
-- Name: ligne_interim ligne_interim_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_interim
    ADD CONSTRAINT ligne_interim_pkey PRIMARY KEY (id);


--
-- TOC entry 3325 (class 2606 OID 1151774)
-- Name: ligne_loc_avec_ch ligne_loc_avec_ch_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_loc_avec_ch
    ADD CONSTRAINT ligne_loc_avec_ch_pkey PRIMARY KEY (id);


--
-- TOC entry 3327 (class 2606 OID 1151781)
-- Name: ligne_loc_ss_ch ligne_loc_ss_ch_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_loc_ss_ch
    ADD CONSTRAINT ligne_loc_ss_ch_pkey PRIMARY KEY (id);


--
-- TOC entry 3329 (class 2606 OID 1151788)
-- Name: ligne_mat_interne ligne_mat_interne_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_mat_interne
    ADD CONSTRAINT ligne_mat_interne_pkey PRIMARY KEY (id);


--
-- TOC entry 3331 (class 2606 OID 1151795)
-- Name: ligne_materiaux ligne_materiaux_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_materiaux
    ADD CONSTRAINT ligne_materiaux_pkey PRIMARY KEY (id);


--
-- TOC entry 3334 (class 2606 OID 1151806)
-- Name: ligne_personnel ligne_personnel_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_personnel
    ADD CONSTRAINT ligne_personnel_pkey PRIMARY KEY (id);


--
-- TOC entry 3336 (class 2606 OID 1151813)
-- Name: ligne_presta_ext ligne_presta_ext_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_presta_ext
    ADD CONSTRAINT ligne_presta_ext_pkey PRIMARY KEY (id);


--
-- TOC entry 3338 (class 2606 OID 1151820)
-- Name: ligne_transport ligne_transport_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_transport
    ADD CONSTRAINT ligne_transport_pkey PRIMARY KEY (id);


--
-- TOC entry 3340 (class 2606 OID 1151827)
-- Name: personnel personnel_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.personnel
    ADD CONSTRAINT personnel_pkey PRIMARY KEY (id);


--
-- TOC entry 3342 (class 2606 OID 1151839)
-- Name: rapport rapport_chantier_id_jour_key; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.rapport
    ADD CONSTRAINT rapport_chantier_id_jour_key UNIQUE (chantier_id, jour);


--
-- TOC entry 3344 (class 2606 OID 1151837)
-- Name: rapport rapport_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.rapport
    ADD CONSTRAINT rapport_pkey PRIMARY KEY (id);


--
-- TOC entry 3346 (class 2606 OID 1151995)
-- Name: rapport ukd9lkw53quxrm9aw6a4op68doe; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.rapport
    ADD CONSTRAINT ukd9lkw53quxrm9aw6a4op68doe UNIQUE (chantier_id, jour);


--
-- TOC entry 3348 (class 2606 OID 1151844)
-- Name: v_rapport_totaux v_rapport_totaux_pkey; Type: CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.v_rapport_totaux
    ADD CONSTRAINT v_rapport_totaux_pkey PRIMARY KEY (rapport_id);


--
-- TOC entry 3296 (class 1259 OID 1151845)
-- Name: idx_affectation_chantier_user; Type: INDEX; Schema: vianeo; Owner: postgres
--

CREATE INDEX idx_affectation_chantier_user ON vianeo.affectation_personnel USING btree (chantier_id, personnel_id);


--
-- TOC entry 3321 (class 1259 OID 1151846)
-- Name: idx_ligne_interim_rapport; Type: INDEX; Schema: vianeo; Owner: postgres
--

CREATE INDEX idx_ligne_interim_rapport ON vianeo.ligne_interim USING btree (rapport_id);


--
-- TOC entry 3332 (class 1259 OID 1151847)
-- Name: idx_ligne_pers_rapport; Type: INDEX; Schema: vianeo; Owner: postgres
--

CREATE INDEX idx_ligne_pers_rapport ON vianeo.ligne_personnel USING btree (rapport_id);


--
-- TOC entry 3362 (class 2606 OID 1151913)
-- Name: ligne_mat_interne fk2aajdq3iniin52i63by0twd91; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_mat_interne
    ADD CONSTRAINT fk2aajdq3iniin52i63by0twd91 FOREIGN KEY (article_id) REFERENCES vianeo.article(id);


--
-- TOC entry 3364 (class 2606 OID 1151928)
-- Name: ligne_materiaux fk3eb7oqvriukp9y01sc2digudh; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_materiaux
    ADD CONSTRAINT fk3eb7oqvriukp9y01sc2digudh FOREIGN KEY (fournisseur_id) REFERENCES vianeo.fournisseur(id);


--
-- TOC entry 3375 (class 2606 OID 1151978)
-- Name: personnel fk4gn6ekx5w8cgith1hg9w9qwhv; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.personnel
    ADD CONSTRAINT fk4gn6ekx5w8cgith1hg9w9qwhv FOREIGN KEY (entite_id) REFERENCES vianeo.entite(id);


--
-- TOC entry 3356 (class 2606 OID 1151888)
-- Name: ligne_loc_avec_ch fk5e5c7tygqtvtidvlc572nudkn; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_loc_avec_ch
    ADD CONSTRAINT fk5e5c7tygqtvtidvlc572nudkn FOREIGN KEY (fournisseur_id) REFERENCES vianeo.fournisseur(id);


--
-- TOC entry 3353 (class 2606 OID 1151868)
-- Name: compte fk6ond6en3rfegjksm1c974ts9b; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.compte
    ADD CONSTRAINT fk6ond6en3rfegjksm1c974ts9b FOREIGN KEY (personnel_id) REFERENCES vianeo.personnel(id);


--
-- TOC entry 3357 (class 2606 OID 1151883)
-- Name: ligne_loc_avec_ch fk786q9axwcl3ta3qkfivih9k1p; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_loc_avec_ch
    ADD CONSTRAINT fk786q9axwcl3ta3qkfivih9k1p FOREIGN KEY (article_id) REFERENCES vianeo.article(id);


--
-- TOC entry 3351 (class 2606 OID 1151858)
-- Name: chantier fk7kmf4ql8i2wg06mc9ajs0rapf; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.chantier
    ADD CONSTRAINT fk7kmf4ql8i2wg06mc9ajs0rapf FOREIGN KEY (entite_id) REFERENCES vianeo.entite(id);


--
-- TOC entry 3358 (class 2606 OID 1151893)
-- Name: ligne_loc_avec_ch fk8u1hemfxvq0mpt5gu3acg9cqx; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_loc_avec_ch
    ADD CONSTRAINT fk8u1hemfxvq0mpt5gu3acg9cqx FOREIGN KEY (rapport_id) REFERENCES vianeo.rapport(id);


--
-- TOC entry 3376 (class 2606 OID 1151988)
-- Name: rapport fk8u2laqpnyrlenjt4jolqhp8v9; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.rapport
    ADD CONSTRAINT fk8u2laqpnyrlenjt4jolqhp8v9 FOREIGN KEY (chantier_id) REFERENCES vianeo.chantier(id);


--
-- TOC entry 3352 (class 2606 OID 1151863)
-- Name: chantier fk8uebtlk85c9nq9eirmwpi8ygh; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.chantier
    ADD CONSTRAINT fk8uebtlk85c9nq9eirmwpi8ygh FOREIGN KEY (responsable_id) REFERENCES vianeo.personnel(id);


--
-- TOC entry 3359 (class 2606 OID 1151908)
-- Name: ligne_loc_ss_ch fk9eplsli5vv5uqrtaldrkyx8y; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_loc_ss_ch
    ADD CONSTRAINT fk9eplsli5vv5uqrtaldrkyx8y FOREIGN KEY (rapport_id) REFERENCES vianeo.rapport(id);


--
-- TOC entry 3363 (class 2606 OID 1151918)
-- Name: ligne_mat_interne fkans35htv9ed25pdoj3qctat10; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_mat_interne
    ADD CONSTRAINT fkans35htv9ed25pdoj3qctat10 FOREIGN KEY (rapport_id) REFERENCES vianeo.rapport(id);


--
-- TOC entry 3360 (class 2606 OID 1151903)
-- Name: ligne_loc_ss_ch fkb5l332yqg1hoyup5y6lxd8v8d; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_loc_ss_ch
    ADD CONSTRAINT fkb5l332yqg1hoyup5y6lxd8v8d FOREIGN KEY (fournisseur_id) REFERENCES vianeo.fournisseur(id);


--
-- TOC entry 3372 (class 2606 OID 1151973)
-- Name: ligne_transport fkb83kau3ln7tu5njxnaknpijse; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_transport
    ADD CONSTRAINT fkb83kau3ln7tu5njxnaknpijse FOREIGN KEY (rapport_id) REFERENCES vianeo.rapport(id);


--
-- TOC entry 3354 (class 2606 OID 1151878)
-- Name: ligne_interim fkcymt4rthe7n1g6fnph572o2jb; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_interim
    ADD CONSTRAINT fkcymt4rthe7n1g6fnph572o2jb FOREIGN KEY (rapport_id) REFERENCES vianeo.rapport(id);


--
-- TOC entry 3349 (class 2606 OID 1151848)
-- Name: affectation_personnel fkd64i7c8iylri0ptpi4ag6xj5b; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.affectation_personnel
    ADD CONSTRAINT fkd64i7c8iylri0ptpi4ag6xj5b FOREIGN KEY (chantier_id) REFERENCES vianeo.chantier(id);


--
-- TOC entry 3361 (class 2606 OID 1151898)
-- Name: ligne_loc_ss_ch fkevmiqt2i736pbv03mt0mao5uk; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_loc_ss_ch
    ADD CONSTRAINT fkevmiqt2i736pbv03mt0mao5uk FOREIGN KEY (article_id) REFERENCES vianeo.article(id);


--
-- TOC entry 3373 (class 2606 OID 1151968)
-- Name: ligne_transport fkj2uit9lt1nhd9ktcp19aufcpv; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_transport
    ADD CONSTRAINT fkj2uit9lt1nhd9ktcp19aufcpv FOREIGN KEY (fournisseur_id) REFERENCES vianeo.fournisseur(id);


--
-- TOC entry 3377 (class 2606 OID 1151983)
-- Name: rapport fkjerotmte0nqxudk5xstt5acix; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.rapport
    ADD CONSTRAINT fkjerotmte0nqxudk5xstt5acix FOREIGN KEY (auteur_id) REFERENCES vianeo.personnel(id);


--
-- TOC entry 3355 (class 2606 OID 1151873)
-- Name: ligne_interim fkjyfveh8kho6mqiqqb5o652w8q; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_interim
    ADD CONSTRAINT fkjyfveh8kho6mqiqqb5o652w8q FOREIGN KEY (fournisseur_id) REFERENCES vianeo.fournisseur(id);


--
-- TOC entry 3350 (class 2606 OID 1151853)
-- Name: affectation_personnel fkkbsugt57doajx9jvmajpnn12c; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.affectation_personnel
    ADD CONSTRAINT fkkbsugt57doajx9jvmajpnn12c FOREIGN KEY (personnel_id) REFERENCES vianeo.personnel(id);


--
-- TOC entry 3369 (class 2606 OID 1151948)
-- Name: ligne_presta_ext fkltgxne0co87hv47hw5bk57cl2; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_presta_ext
    ADD CONSTRAINT fkltgxne0co87hv47hw5bk57cl2 FOREIGN KEY (article_id) REFERENCES vianeo.article(id);


--
-- TOC entry 3365 (class 2606 OID 1151923)
-- Name: ligne_materiaux fknw83bwcy0e8jdpka84b3bdw6o; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_materiaux
    ADD CONSTRAINT fknw83bwcy0e8jdpka84b3bdw6o FOREIGN KEY (article_id) REFERENCES vianeo.article(id);


--
-- TOC entry 3366 (class 2606 OID 1151933)
-- Name: ligne_materiaux fkogb4bo43fabwkd2umrlvw8bm9; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_materiaux
    ADD CONSTRAINT fkogb4bo43fabwkd2umrlvw8bm9 FOREIGN KEY (rapport_id) REFERENCES vianeo.rapport(id);


--
-- TOC entry 3370 (class 2606 OID 1151953)
-- Name: ligne_presta_ext fkogy809nx2nxvj5yvt7ebl8ow1; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_presta_ext
    ADD CONSTRAINT fkogy809nx2nxvj5yvt7ebl8ow1 FOREIGN KEY (fournisseur_id) REFERENCES vianeo.fournisseur(id);


--
-- TOC entry 3374 (class 2606 OID 1151963)
-- Name: ligne_transport fkp1u31tatxc4e2riwyunkrnvuk; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_transport
    ADD CONSTRAINT fkp1u31tatxc4e2riwyunkrnvuk FOREIGN KEY (article_id) REFERENCES vianeo.article(id);


--
-- TOC entry 3367 (class 2606 OID 1151943)
-- Name: ligne_personnel fkp7yb03p6ufkfsaqnrnlmj2mb1; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_personnel
    ADD CONSTRAINT fkp7yb03p6ufkfsaqnrnlmj2mb1 FOREIGN KEY (personnel_id) REFERENCES vianeo.personnel(id);


--
-- TOC entry 3371 (class 2606 OID 1151958)
-- Name: ligne_presta_ext fkpjb3hn4qnj007y4d5or3qu56j; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_presta_ext
    ADD CONSTRAINT fkpjb3hn4qnj007y4d5or3qu56j FOREIGN KEY (rapport_id) REFERENCES vianeo.rapport(id);


--
-- TOC entry 3368 (class 2606 OID 1151938)
-- Name: ligne_personnel fkqt7tsygue48l5h2a627hfa6qm; Type: FK CONSTRAINT; Schema: vianeo; Owner: postgres
--

ALTER TABLE ONLY vianeo.ligne_personnel
    ADD CONSTRAINT fkqt7tsygue48l5h2a627hfa6qm FOREIGN KEY (rapport_id) REFERENCES vianeo.rapport(id);


-- Completed on 2025-10-29 06:59:48

--
-- PostgreSQL database dump complete
--

