--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.11
-- Dumped by pg_dump version 11.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: group_codes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.group_codes (
    group_code character varying NOT NULL,
    created_timestamp character varying,
    owner_goingok_id uuid
);


ALTER TABLE public.group_codes OWNER TO postgres;

--
-- Name: group_permissions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.group_permissions (
    goingok_id uuid,
    group_code character varying,
    permission character varying NOT NULL
);


ALTER TABLE public.group_permissions OWNER TO postgres;

--
-- Name: logins; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.logins (
    id integer NOT NULL,
    "timestamp" character varying,
    goingok_id uuid,
    stage character varying
);


ALTER TABLE public.logins OWNER TO postgres;

--
-- Name: logins_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.logins_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.logins_id_seq OWNER TO postgres;

--
-- Name: logins_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.logins_id_seq OWNED BY public.logins.id;


--
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.messages (
    "timestamp" character varying NOT NULL,
    title character varying NOT NULL,
    text character varying NOT NULL,
    value jsonb NOT NULL,
    goingok_id uuid NOT NULL
);


ALTER TABLE public.messages OWNER TO postgres;

--
-- Name: pseudonyms; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pseudonyms (
    pseudonym character varying NOT NULL,
    allocated boolean
);


ALTER TABLE public.pseudonyms OWNER TO postgres;

--
-- Name: reflections; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reflections (
    "timestamp" character varying NOT NULL,
    point double precision NOT NULL,
    text character varying NOT NULL,
    goingok_id uuid NOT NULL
);


ALTER TABLE public.reflections OWNER TO postgres;

--
-- Name: user_auths; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_auths (
    goingok_id uuid NOT NULL,
    google_id character varying NOT NULL,
    google_email character varying NOT NULL,
    init_timestamp character varying
);


ALTER TABLE public.user_auths OWNER TO postgres;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    goingok_id uuid NOT NULL,
    pseudonym character varying NOT NULL,
    research_code character varying,
    research_consent boolean,
    supervisor boolean NOT NULL,
    admin boolean NOT NULL,
    group_code character varying,
    register_timestamp character varying
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: logins id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.logins ALTER COLUMN id SET DEFAULT nextval('public.logins_id_seq'::regclass);


--
-- Name: group_codes group_codes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.group_codes
    ADD CONSTRAINT group_codes_pkey PRIMARY KEY (group_code);


--
-- Name: logins logins_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.logins
    ADD CONSTRAINT logins_pkey PRIMARY KEY (id);


--
-- Name: pseudonyms pseudonyms_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pseudonyms
    ADD CONSTRAINT pseudonyms_pkey PRIMARY KEY (pseudonym);


--
-- Name: user_auths user_auth_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_auths
    ADD CONSTRAINT user_auth_pkey PRIMARY KEY (goingok_id);


--
-- Name: users user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_pkey PRIMARY KEY (goingok_id);


--
-- Name: pseudonyms_pseudonym_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX pseudonyms_pseudonym_uindex ON public.pseudonyms USING btree (pseudonym);


--
-- Name: user_auths_google_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX user_auths_google_id_uindex ON public.user_auths USING btree (google_id);


--
-- Name: group_permissions group_codes_group_code; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.group_permissions
    ADD CONSTRAINT group_codes_group_code FOREIGN KEY (group_code) REFERENCES public.group_codes(group_code);


--
-- Name: messages user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages
    ADD CONSTRAINT user_fkey FOREIGN KEY (goingok_id) REFERENCES public.users(goingok_id);


--
-- Name: reflections user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reflections
    ADD CONSTRAINT user_fkey FOREIGN KEY (goingok_id) REFERENCES public.users(goingok_id);


--
-- Name: group_permissions users_goingok_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.group_permissions
    ADD CONSTRAINT users_goingok_id FOREIGN KEY (goingok_id) REFERENCES public.users(goingok_id);


--
-- Name: users users_group_codes_group_code_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_group_codes_group_code_fk FOREIGN KEY (group_code) REFERENCES public.group_codes(group_code);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--


GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

