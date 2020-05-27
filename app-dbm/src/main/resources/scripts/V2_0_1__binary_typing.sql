CREATE TABLE dte_bip (
    bip_btn_id bigint NOT NULL,
    bip_name character varying(31) NOT NULL,
    bip_label character varying(63) NOT NULL,
    bip_optional boolean NOT NULL,
    bip_tpe_id integer NOT NULL,
    bip_desc text NOT NULL,

    CONSTRAINT dte_bip_pk PRIMARY KEY (bip_btn_id, bip_name),
    CONSTRAINT bip_btn_id FOREIGN KEY (bip_btn_id) REFERENCES dte_btn (btn_id),
    CONSTRAINT bip_tpe_fk FOREIGN KEY (bip_tpe_id) REFERENCES dte_tpe (tpe_id),

    CONSTRAINT bip_label_uc UNIQUE (bip_btn_id, bip_label)
);
