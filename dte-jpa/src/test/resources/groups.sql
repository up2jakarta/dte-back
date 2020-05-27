-- Groups
INSERT INTO dte_grp (grp_id, grp_type, grp_label, grp_parent_id, grp_icon, grp_color, grp_desc) VALUES
                      (0, 'M', 'DTE', 0, 'logo', 'green', 'Decision Tree Engine'),
                      (1, 'S', 'TST', 0, 'test', 'green', 'Test'),
                      (2, 'G', 'PAY', 1, 'test', 'green', 'Pay processes'),
                      (3, 'G', 'DTN', 1, 'test', 'green', 'Decision trees'),
                      (4, 'G', 'BTN', 1, 'test', 'green', 'Binary trees')
            ;
-- Templates
INSERT INTO dte_tpl(tpl_id, tpl_type, tpl_grp_id, tpl_label, tpl_desc, tpl_name) VALUES
            (0, 'B', 0, 'Default', 'Default', 'io.github.up2jakarta.dte.dsl.BaseScript')
    ;
-- Types
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