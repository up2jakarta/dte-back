CREATE SEQUENCE dte_tpe_seq START 1 INCREMENT 1;

CREATE TABLE dte_tpe (
    tpe_id integer NOT NULL,
    tpe_type character(1) NOT NULL,
    tpe_version integer NOT NULL DEFAULT 0,
    tpe_label character varying(63) NOT NULL,
    tpe_name character varying(127) NOT NULL,
    tpe_grp_id integer NOT NULL,
    tpe_base_id integer,
    tpe_desc text NOT NULL,

    CONSTRAINT dte_tpe_pk PRIMARY KEY (tpe_id),
    CONSTRAINT tpe_base_fk FOREIGN KEY (tpe_base_id) REFERENCES dte_tpe (tpe_id),
    CONSTRAINT tpe_grp_fk FOREIGN KEY (tpe_grp_id) REFERENCES dte_grp (grp_id)
);

ALTER TABLE dte_tpe ADD CONSTRAINT dte_tpe_base_cc CHECK (
    (tpe_type <> 'B' AND tpe_base_id IS NOT NULL) OR (tpe_type = 'B' AND tpe_base_id IS NULL)
);

CREATE TABLE dte_fld (
    fld_type_id integer NOT NULL,
    fld_name character varying(31) NOT NULL,
    fld_label character varying(63) NOT NULL,
    fld_array boolean NOT NULL,
    fld_shared boolean NOT NULL,
    fld_optional boolean NOT NULL,
    fld_tpe_id integer NOT NULL,
    fld_desc text NOT NULL,

    CONSTRAINT dte_fld_pk PRIMARY KEY (fld_type_id, fld_name),
    CONSTRAINT fld_tpe_fk FOREIGN KEY (fld_tpe_id) REFERENCES dte_tpe (tpe_id),
    CONSTRAINT fld_type_fk FOREIGN KEY (fld_type_id) REFERENCES dte_tpe (tpe_id),

    CONSTRAINT fld_label_uc UNIQUE (fld_type_id, fld_label)
);

INSERT INTO dte_tpe(tpe_id, tpe_type, tpe_name, tpe_grp_id, tpe_label, tpe_desc) VALUES
                    (0,  'B', 'java.lang.Object', 0, 'Default', 'Default'),
                    (1,  'B', 'java.lang.Character', 0, 'Character', 'Character'),
                    (2,  'B', 'java.lang.String', 0, 'String', 'String'),
                    (3,  'B', 'java.lang.Boolean', 0, 'Boolean', 'Boolean'),

                    (4,  'B', 'java.lang.Short', 0, 'Short', 'Short integer'),
                    (5,  'B', 'java.lang.Integer', 0, 'Integer', 'Integer'),
                    (6,  'B', 'java.lang.Long', 0, 'Long', 'Long integer'),
                    (7,  'B', 'java.math.BigInteger', 0, 'BigInteger', 'Big integer'),
                    (8,  'B', 'java.lang.Float', 0, 'Float', 'Decimal'),
                    (9,  'B', 'java.lang.Double', 0, 'Double', 'Double decimal'),
                    (10, 'B', 'java.math.BigDecimal', 0, 'BigDecimal', 'Big decimal'),

                    (11, 'B', 'java.time.DayOfWeek', 0, 'DayOfWeek', 'Day of week'),
                    (12, 'B', 'java.time.Duration', 0, 'Duration', 'Duration'),
                    (13, 'B', 'java.time.Instant', 0, 'Instant', 'Moment'),
                    (14, 'B', 'java.time.LocalDate', 0, 'LocalDate', 'Local date'),
                    (15, 'B', 'java.time.LocalDateTime', 0, 'LocalDateTime', 'Local date&time'),
                    (16, 'B', 'java.time.MonthDay', 0, 'MonthDay', 'Month&day'),
                    (17, 'B', 'java.time.Month', 0, 'Month', 'Month'),
                    (18, 'B', 'java.time.OffsetDateTime', 0, 'OffsetDateTime', 'Offset date&time'),
                    (19, 'B', 'java.time.OffsetTime', 0, 'OffsetTime', 'Offset time'),
                    (20, 'B', 'java.time.Period', 0, 'Period', 'Period'),

                    (21, 'B', 'java.time.Year', 0, 'Year', 'Year'),
                    (22, 'B', 'java.time.YearMonth', 0, 'YearMonth', 'Year&month'),
                    (23, 'B', 'java.time.ZonedDateTime', 0, 'ZonedDateTime', 'Zoned date&time'),
                    (24, 'B', 'java.time.ZoneId', 0, 'ZoneId', 'Zone id'),
                    (25, 'B', 'java.time.ZoneOffset', 0, 'ZoneOffset', 'Zone offset'),
                    (27, 'B', 'java.util.TimeZone', 0, 'TimeZone', 'Time zone'),
                    (26, 'B', 'java.util.Locale', 0, 'Locale', 'Locale&day')
    ;