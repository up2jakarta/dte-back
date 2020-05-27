CREATE SEQUENCE dte_dtn_seq START 1 INCREMENT 25;

CREATE TABLE dte_dtn (
    dtn_id bigint NOT NULL,
    dtn_type character(1) NOT NULL,
    tpl_version integer NOT NULL DEFAULT 0,
    dtn_label character varying(63) NOT NULL,
    dtn_depth integer,
    dtn_order integer,
    dtn_shared boolean,
    dtn_negated boolean,
    dtn_grp_id integer,
    dtn_tpl_id integer,
    dtn_parent_id bigint,
    dtn_root_id bigint,
    dtn_btn_id bigint,
    dtn_dtn_id bigint,
    dtn_desc text,
    dtn_script text,

    CONSTRAINT dte_dtn_pk PRIMARY KEY (dtn_id),
    CONSTRAINT dtn_btn_id FOREIGN KEY (dtn_btn_id) REFERENCES dte_btn (btn_id),
    CONSTRAINT dtn_dtn_fk FOREIGN KEY (dtn_dtn_id) REFERENCES dte_dtn (dtn_id),
    CONSTRAINT dtn_grp_fk FOREIGN KEY (dtn_grp_id) REFERENCES dte_grp (grp_id),
    CONSTRAINT dtn_parent_fk FOREIGN KEY (dtn_parent_id) REFERENCES dte_dtn (dtn_id),
    CONSTRAINT dtn_root_fk FOREIGN KEY (dtn_root_id) REFERENCES dte_dtn (dtn_id),
    CONSTRAINT dtn_tpl_fk FOREIGN KEY (dtn_tpl_id) REFERENCES dte_tpl (tpl_id),

    CONSTRAINT dtn_btn_uc UNIQUE (dtn_parent_id, dtn_btn_id, dtn_negated),
    CONSTRAINT dtn_label_uc UNIQUE (dtn_grp_id, dtn_label),
    CONSTRAINT dtn_order_uc UNIQUE (dtn_parent_id, dtn_order)
);

ALTER TABLE dte_dtn ADD CONSTRAINT dte_dtn_type_cc CHECK (dtn_type IN('S', 'C', 'E', 'D', 'R', 'T', 'B'));
ALTER TABLE dte_dtn ADD CONSTRAINT dte_dtn_grp_cc CHECK (
    (dtn_type IN ('S', 'C', 'T', 'B') AND dtn_grp_id IS NOT NULL) OR (dtn_type NOT IN ('S', 'C', 'T', 'B') AND dtn_grp_id IS NULL)
);
ALTER TABLE dte_dtn ADD CONSTRAINT dte_dtn_tpl_cc CHECK (
    (dtn_type IN ('S', 'T', 'B') AND dtn_tpl_id IS NOT NULL) OR (dtn_type NOT IN ('S', 'T', 'B') AND dtn_tpl_id IS NULL)
);
ALTER TABLE dte_dtn ADD CONSTRAINT dte_dtn_parent_cc CHECK (
    (dtn_type NOT IN ('S', 'C') AND dtn_parent_id IS NOT NULL) OR (dtn_type IN ('S', 'C') AND dtn_parent_id IS NULL)
);
ALTER TABLE dte_dtn ADD CONSTRAINT dte_dtn_root_cc CHECK (
    (dtn_type NOT IN ('S', 'C') AND dtn_root_id IS NOT NULL) OR (dtn_type IN ('S', 'C') AND dtn_root_id IS NULL)
);
ALTER TABLE dte_dtn ADD CONSTRAINT dte_dtn_desc_cc CHECK (
    (dtn_type IN ('E', 'D', 'R') AND dtn_desc IS NULL) OR dtn_type NOT IN ('E', 'D', 'R')
);
ALTER TABLE dte_dtn ADD CONSTRAINT dte_dtn_depth_cc CHECK (
    (dtn_type = 'S' AND dtn_depth IS NULL) OR
    (dtn_type = 'C' AND dtn_depth = 0) OR
    (dtn_type NOT IN ('S', 'C') AND dtn_depth IS NOT NULL  AND dtn_depth > 0)
);
ALTER TABLE dte_dtn ADD CONSTRAINT dte_dtn_order_cc CHECK (
    (dtn_type IN ('S', 'C') AND dtn_order IS NULL) OR (dtn_type NOT IN ('S', 'C') AND dtn_order IS NOT NULL AND dtn_order >= 0)
);
ALTER TABLE dte_dtn ADD CONSTRAINT dte_dtn_script_cc CHECK (
    (dtn_type IN ('S', 'T', 'B') AND dtn_script IS NOT NULL) OR (dtn_type NOT IN ('S', 'T', 'B') AND dtn_script IS NULL)
);
ALTER TABLE dte_dtn ADD CONSTRAINT dte_dtn_btn_cc CHECK (
    (dtn_type = 'D' AND DTN_BTN_ID IS NOT NULL) OR (dtn_type <> 'D' AND DTN_BTN_ID IS NULL)
);
ALTER TABLE dte_dtn ADD CONSTRAINT dte_dtn_dtn_cc CHECK (
    (dtn_type = 'R' AND DTN_DTN_ID IS NOT NULL) OR (dtn_type <> 'R' AND DTN_DTN_ID IS NULL)
);
ALTER TABLE dte_dtn ADD CONSTRAINT dte_dtn_negated_cc CHECK (
    (dtn_type IN ('D', 'B') AND dtn_negated IS NOT NULL) OR (dtn_type NOT IN ('D', 'B') AND dtn_negated IS NULL)
);
ALTER TABLE dte_dtn ADD CONSTRAINT dte_dtn_shared_cc CHECK (
    (dtn_type IN ('C', 'S', 'T', 'B') AND dtn_shared IS NOT NULL) OR (dtn_type NOT IN ('C', 'S', 'T', 'B') AND dtn_shared IS NULL)
);