CREATE TABLE dte_gip (
    gip_grp_id integer NOT NULL,
    gip_name character varying(31) NOT NULL,
    gip_label character varying(63) NOT NULL,
    gip_optional boolean NOT NULL,
    gip_tpe_id integer NOT NULL,
    gip_desc text NOT NULL,

    CONSTRAINT dte_gip_pk PRIMARY KEY (gip_grp_id, gip_name),
    CONSTRAINT gip_grp_fk FOREIGN KEY (gip_grp_id) REFERENCES dte_grp (grp_id),
    CONSTRAINT gip_tpe_fk FOREIGN KEY (gip_tpe_id) REFERENCES dte_tpe (tpe_id),

    CONSTRAINT gip_label_uc UNIQUE (gip_grp_id, gip_label)
);

CREATE TABLE dte_gop (
    gop_grp_id integer NOT NULL,
    gop_name character varying(31) NOT NULL,
    gop_label character varying(63) NOT NULL,
    gop_shared boolean NOT NULL,
    gop_optional boolean NOT NULL,
    gop_tpe_id integer NOT NULL,
    gop_desc text NOT NULL,

    CONSTRAINT dte_gop_pk PRIMARY KEY (gop_grp_id, gop_name),
    CONSTRAINT gop_grp_fk FOREIGN KEY (gop_grp_id) REFERENCES dte_grp (grp_id),
    CONSTRAINT gop_tpe_fk FOREIGN KEY (gop_tpe_id) REFERENCES dte_tpe (tpe_id),

    CONSTRAINT gop_label_uc UNIQUE (gop_grp_id, gop_label)
);

