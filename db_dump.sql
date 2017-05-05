--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.2
-- Dumped by pg_dump version 9.6.2

-- Started on 2017-05-05 13:12:00

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12387)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2145 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 188 (class 1259 OID 16419)
-- Name: classes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE classes (
    id integer NOT NULL,
    name character varying(150) NOT NULL,
    lcom5 integer,
    cbo integer,
    cboi integer,
    nii integer,
    noi integer,
    cllc real,
    loc integer,
    cd real,
    ad real,
    wmc integer,
    tnm integer,
    nl integer,
    code_coverage_index integer DEFAULT 0,
    bug_count integer DEFAULT 0,
    project_id integer NOT NULL,
    warning_count integer DEFAULT 0
);


ALTER TABLE classes OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 16417)
-- Name: classes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE classes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE classes_id_seq OWNER TO postgres;

--
-- TOC entry 2146 (class 0 OID 0)
-- Dependencies: 187
-- Name: classes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE classes_id_seq OWNED BY classes.id;


--
-- TOC entry 186 (class 1259 OID 16396)
-- Name: projects; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE projects (
    id integer NOT NULL,
    name character varying(100)
);


ALTER TABLE projects OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 16394)
-- Name: projects_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE projects_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE projects_id_seq OWNER TO postgres;

--
-- TOC entry 2147 (class 0 OID 0)
-- Dependencies: 185
-- Name: projects_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE projects_id_seq OWNED BY projects.id;


--
-- TOC entry 2009 (class 2604 OID 16422)
-- Name: classes id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY classes ALTER COLUMN id SET DEFAULT nextval('classes_id_seq'::regclass);


--
-- TOC entry 2008 (class 2604 OID 16399)
-- Name: projects id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY projects ALTER COLUMN id SET DEFAULT nextval('projects_id_seq'::regclass);


--
-- TOC entry 2138 (class 0 OID 16419)
-- Dependencies: 188
-- Data for Name: classes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY classes (id, name, lcom5, cbo, cboi, nii, noi, cllc, loc, cd, ad, wmc, tnm, nl, code_coverage_index, bug_count, project_id, warning_count) FROM stdin;
\.


--
-- TOC entry 2148 (class 0 OID 0)
-- Dependencies: 187
-- Name: classes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('classes_id_seq', 1, true);


--
-- TOC entry 2136 (class 0 OID 16396)
-- Dependencies: 186
-- Data for Name: projects; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY projects (id, name) FROM stdin;
\.


--
-- TOC entry 2149 (class 0 OID 0)
-- Dependencies: 185
-- Name: projects_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('projects_id_seq', 1, true);


--
-- TOC entry 2016 (class 2606 OID 16424)
-- Name: classes classes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY classes
    ADD CONSTRAINT classes_pkey PRIMARY KEY (id);


--
-- TOC entry 2014 (class 2606 OID 16401)
-- Name: projects projects_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY projects
    ADD CONSTRAINT projects_pkey PRIMARY KEY (id);


--
-- TOC entry 2017 (class 2606 OID 16425)
-- Name: classes project___fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY classes
    ADD CONSTRAINT project___fk FOREIGN KEY (project_id) REFERENCES projects(id);


-- Completed on 2017-05-05 13:12:00

--
-- PostgreSQL database dump complete
--

