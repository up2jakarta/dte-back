CREATE TABLE dte_pmr (
  pmr_grp_id integer NOT NULL,
  pmr_desc text NOT NULL,
  pmr_label character varying(63) NOT NULL,
  pmr_tpe_id integer NOT NULL,
  pmr_value character varying(511) NOT NULL,
  pmr_name character varying(127) NOT NULL,

  CONSTRAINT dte_pmr_pk PRIMARY KEY (pmr_grp_id, pmr_name),
  CONSTRAINT pmr_grp_fk FOREIGN KEY (pmr_grp_id) REFERENCES dte_grp (grp_id),
  CONSTRAINT pmr_tpe_fk FOREIGN KEY (pmr_tpe_id) REFERENCES dte_tpe (tpe_id)
);
