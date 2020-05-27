CREATE SEQUENCE dte_btn_seq START 1 INCREMENT 25;

CREATE TABLE dte_btn (
    btn_id bigint NOT NULL,
    btn_type character(1) NOT NULL,
    btn_version integer NOT NULL DEFAULT 0,
    btn_label character varying(63) NOT NULL,
    btn_negated boolean NOT NULL,
    btn_shared boolean,
    btn_depth integer,
    btn_order integer,
    btn_operator character(1),
    btn_parent_id bigint,
    btn_root_id bigint,
    btn_grp_id integer,
    btn_tpl_id integer,
    btn_btn_id bigint,
    btn_desc text,
    btn_script text,

    CONSTRAINT dte_btn_pk PRIMARY KEY (btn_id),

    CONSTRAINT btn_btn_id FOREIGN KEY (btn_btn_id) REFERENCES dte_btn (btn_id),
    CONSTRAINT btn_grp_fk FOREIGN KEY (btn_grp_id) REFERENCES dte_grp (grp_id),
    CONSTRAINT btn_parent_fk FOREIGN KEY (btn_parent_id) REFERENCES dte_btn (btn_id),
    CONSTRAINT btn_root_fk FOREIGN KEY (btn_root_id) REFERENCES dte_btn (btn_id),
    CONSTRAINT btn_tpl_fk FOREIGN KEY (btn_tpl_id) REFERENCES dte_tpl (tpl_id),

    CONSTRAINT btn_btn_uc UNIQUE (btn_parent_id, btn_btn_id, btn_negated),
    CONSTRAINT btn_label_uc UNIQUE (btn_grp_id, btn_label),
    CONSTRAINT btn_order_uc UNIQUE (btn_parent_id, btn_order)
);

ALTER TABLE dte_btn ADD CONSTRAINT dte_btn_type_cc CHECK (btn_type IN('S', 'C', 'L', 'P', 'T'));
ALTER TABLE dte_btn ADD CONSTRAINT dte_btn_operator_cc CHECK (
    btn_operator IN('O', 'A', 'X') AND (
        (btn_type IN ('C', 'P') AND btn_operator IS NOT NULL) OR (btn_type NOT IN ('C', 'P') AND btn_operator IS NULL)
    )
);
ALTER TABLE dte_btn ADD CONSTRAINT dte_btn_grp_cc CHECK (
    (btn_type IN ('C', 'S', 'T') AND btn_grp_id IS NOT NULL) OR (btn_type NOT IN ('C', 'S', 'T') AND btn_grp_id IS NULL)
);
ALTER TABLE dte_btn ADD CONSTRAINT dte_btn_tpl_cc CHECK (
    (btn_type IN ('S', 'T') AND btn_tpl_id IS NOT NULL) OR (btn_type NOT IN ('S', 'T') AND btn_tpl_id IS NULL)
);
ALTER TABLE dte_btn ADD CONSTRAINT dte_btn_parent_cc CHECK (
    (btn_type NOT IN ('S', 'C') AND btn_parent_id IS NOT NULL) OR (btn_type IN ('S', 'C') AND btn_parent_id IS NULL)
);
ALTER TABLE dte_btn ADD CONSTRAINT dte_btn_root_cc CHECK (
    (btn_type NOT IN ('S', 'C') AND btn_root_id IS NOT NULL) OR (btn_type IN ('S', 'C') AND btn_root_id IS NULL)
);
ALTER TABLE dte_btn ADD CONSTRAINT dte_btn_shared_cc CHECK (
    (btn_type IN ('C', 'S', 'T') AND btn_shared IS NOT NULL) OR (btn_type NOT IN ('C', 'S', 'T') AND btn_shared IS NULL)
);
ALTER TABLE dte_btn ADD CONSTRAINT dte_btn_order_cc CHECK (
    (btn_type IN ('S', 'C') AND btn_order IS NULL) OR (
        btn_type NOT IN ('S', 'C') AND btn_order IS NOT NULL AND btn_order >= 0
    )
);
ALTER TABLE dte_btn ADD CONSTRAINT dte_btn_depth_cc CHECK (
    (btn_type = 'S' AND btn_depth IS NULL) OR
    (btn_type = 'C' AND btn_depth = 0) OR
    (btn_type NOT IN ('S', 'C') AND btn_depth IS NOT NULL AND btn_depth > 0)
);
ALTER TABLE dte_btn ADD CONSTRAINT dte_btn_desc_cc CHECK (
    (btn_type IN ('L', 'P') AND btn_desc IS NULL) OR btn_type NOT IN ('L', 'P')
);
ALTER TABLE dte_btn ADD CONSTRAINT dte_btn_script_cc CHECK (
    (btn_type IN ('S', 'T') AND btn_script IS NOT NULL) OR (btn_type NOT IN ('S', 'T') AND btn_script IS NULL)
);
ALTER TABLE dte_btn ADD CONSTRAINT dte_btn_btn_cc CHECK (
    (btn_type = 'L' AND btn_btn_id IS NOT NULL) OR (btn_type <> 'L' AND btn_btn_id IS NULL)
);
