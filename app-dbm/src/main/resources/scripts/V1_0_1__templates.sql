CREATE SEQUENCE dte_tpl_seq START 1 INCREMENT 1;

CREATE TABLE dte_tpl (
    tpl_id integer NOT NULL,
    tpl_type character(1) NOT NULL,
    tpl_version integer NOT NULL DEFAULT 0,
    tpl_label character varying(63) NOT NULL,
    tpl_name character varying(127) NOT NULL,
    tpl_grp_id integer NOT NULL,
    tpl_base_id integer,
    tpl_desc text NOT NULL,
    tpl_script text,

    CONSTRAINT dte_tpl_pk PRIMARY KEY (tpl_id),
    CONSTRAINT tpl_base_fk FOREIGN KEY (tpl_base_id) REFERENCES dte_tpl (tpl_id),
    CONSTRAINT tpl_grp_fk FOREIGN KEY (tpl_grp_id) REFERENCES dte_grp (grp_id),

    CONSTRAINT tpl_label_uc UNIQUE (tpl_grp_id, tpl_label),
    CONSTRAINT tpl_name_uc UNIQUE (tpl_grp_id, tpl_name)
);

ALTER TABLE dte_tpl ADD CONSTRAINT dte_tpl_type_cc CHECK (tpl_type IN ('B', 'U'));
ALTER TABLE dte_tpl ADD CONSTRAINT dte_tpl_script_cc CHECK (
    (tpl_type = 'U' AND tpl_script IS NOT NULL) OR (tpl_type <> 'U' AND tpl_script IS NULL)
);
ALTER TABLE dte_tpl ADD CONSTRAINT dte_tpl_base_cc CHECK (
    (tpl_type = 'U' AND tpl_base_id IS NOT NULL) OR (tpl_type <> 'U' AND tpl_base_id IS NULL)
);

INSERT INTO dte_tpl(tpl_id, tpl_type, tpl_grp_id, tpl_label, tpl_desc, tpl_name) VALUES
    (0, 'B', 0, 'Default', 'TODO', 'io.github.up2jakarta.dte.dsl.BaseScript')
;

