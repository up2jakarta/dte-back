-- DROP TABLE dte_grp;
CREATE SEQUENCE dte_grp_seq START 1 INCREMENT 1;

CREATE TABLE dte_grp (
    grp_id integer NOT NULL,
    grp_type character(1) NOT NULL,
    grp_version integer NOT NULL DEFAULT 0,
    grp_label character varying(63) NOT NULL,
    grp_icon character varying(31) NOT NULL DEFAULT 'group_work',
    grp_color character varying(15),
    grp_parent_id integer NOT NULL,
    grp_desc text NOT NULL,

    CONSTRAINT dte_grp_pk PRIMARY KEY (grp_id),
    CONSTRAINT grp_parent_fk FOREIGN KEY (grp_parent_id) REFERENCES dte_grp (grp_id),
    CONSTRAINT dte_grp_label_uc UNIQUE (grp_label, grp_parent_id),
    CONSTRAINT dte_grp_type_cc CHECK (grp_type IN ('M', 'S', 'G'))
);

INSERT INTO dte_grp(grp_id, grp_type, grp_label, grp_icon, grp_parent_id, grp_desc) VALUES
    (
        0, 'M', 'Decision Tree Engine', 'logo', 0,
        'DTE is a business rules management system (BRMS) based on decision trees.' ||
        'DTE enables businesses to create and manage business logic independently from applications and processes.' ||
        ' Through business rules, your team can specify decision logic in simple terms, close to natural language.'||
        E'\n\n' ||
        'Because rules are easily integrated with other IT systems, your applications can scale and execute' ||
        ' automated decisions across multiple channels. When changes to business rules are required, business' ||
        ' users can quickly update them, providing the agility and speed needed to meet changing business demands'
    );
