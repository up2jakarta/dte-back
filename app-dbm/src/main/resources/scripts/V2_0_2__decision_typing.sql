CREATE TABLE dte_dip (
    dip_dtn_id bigint NOT NULL,
    dip_name character varying(31) NOT NULL,
    dip_label character varying(63) NOT NULL,
    dip_optional boolean NOT NULL,
    dip_tpe_id integer NOT NULL,
    dip_desc text NOT NULL,

    CONSTRAINT dte_dip_pk PRIMARY KEY (dip_dtn_id, dip_name),
    CONSTRAINT dip_dtn_fk FOREIGN KEY (dip_dtn_id) REFERENCES dte_dtn (dtn_id),
    CONSTRAINT dip_tpe_fk FOREIGN KEY (dip_tpe_id) REFERENCES dte_tpe (tpe_id),

    CONSTRAINT dip_label_uc UNIQUE (dip_dtn_id, dip_label)
);

CREATE TABLE dte_dop (
  dop_dtn_id bigint NOT NULL,
  dop_name character varying(31) NOT NULL,
  dop_label character varying(63) NOT NULL,
  dop_shared boolean NOT NULL,
  dop_optional boolean NOT NULL,
  dop_tpe_id integer NOT NULL,
  dop_desc text NOT NULL,

  CONSTRAINT dte_dop_pk PRIMARY KEY (dop_dtn_id, dop_name),
  CONSTRAINT dop_dtn_fk FOREIGN KEY (dop_dtn_id) REFERENCES dte_dtn (dtn_id),
  CONSTRAINT dop_tpe_fk FOREIGN KEY (dop_tpe_id) REFERENCES dte_tpe (tpe_id),
  CONSTRAINT dop_label_uc UNIQUE (dop_dtn_id, dop_label)
);
