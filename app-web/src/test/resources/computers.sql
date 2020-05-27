-- Plain deciders
INSERT INTO dte_btn(btn_id, btn_type, btn_label, btn_desc, btn_script, btn_grp_id, btn_negated, btn_shared, btn_tpl_id) VALUES
            (10, 'S', 'EUR', 'EUR', 'currency == "EUR"', 2, false, false, 0)
;

-- Plain computers
INSERT INTO dte_dtn(dtn_id, dtn_type, dtn_label, dtn_desc, dtn_script, dtn_grp_id, dtn_shared, dtn_tpl_id) VALUES
            (10, 'S', 'EU_M1' , 'EU M1', 'country = "Europe"'                  , 2, false, 0),
            (11, 'S', 'EU_M2' , 'EU M2', 'country = "European Union"'          , 2, false, 0),
            (12, 'S', '??_M1' , '?? M1', 'country = "Unknown"'                 , 2, false, 0),
            (13, 'S', '??_M2' , '?? M2', 'log = "Unknown currency " + currency', 2, false, 0),
            (14, 'S', 'T_MOD' , 'T MOD', 'test = "Update"'                     , 2, false, 0),
            (15, 'S', 'T_DEL' , 'T DEL', 'test = "Delete"'                     , 2, false, 0),
            (16, 'S', 'T_GP1' , 'GRP 1', 'test = "group_1"'                    , 1, false, 0)
;

-- Mixed computers
INSERT INTO dte_dtn (dtn_id, dtn_type, dtn_grp_id, dtn_label, dtn_depth, dtn_order, dtn_btn_id, dtn_negated, dtn_dtn_id, dtn_parent_id, dtn_root_id, dtn_script, dtn_desc, dtn_shared, dtn_tpl_id) VALUES
         (20, 'C', 2,   'MIX', 0, null, null, null  , null, null, null, null,                'MIXED', false, null),

         (21, 'D', null, 'EUR',  1, 1,    10   , false, null, 20, 20, null,                   null  , null, null),
         (22, 'R', null, 'EU1',  2, 0,    null , null , 10  , 21, 20, null,                   null  , null, null),
         (23, 'R', null, 'EU2',  2, 1,    null , null , 11  , 21, 20, null,                   null  , null, null),

         (24, 'B', 2, 'TND',  1, 2,    null , true , null, 20, 20, 'currency == "TND"',    'TND' , false, 0),
         (25, 'T', 2, 'TN' ,  2, 0,    null , null , null, 24, 20, 'country = "Tunisia"',  'TN'  , false, 0),

         (26, 'E', null, 'DEF',  1, 3,    null , null , null, 20, 20, null,                   null  , null, null),
         (27, 'R', null, '?M1',  2, 0,    null , null , 12  , 26, 20, null,                   null  , null, null),
         (28, 'R', null, '?M2',  2, 1,    null , null , 13  , 26, 20, null,                   null  , null, null)
;

INSERT INTO dte_dtn (dtn_id, dtn_type, dtn_grp_id, dtn_label, dtn_depth, dtn_order, dtn_btn_id, dtn_negated, dtn_dtn_id, dtn_parent_id, dtn_root_id, dtn_script, dtn_desc, dtn_shared) VALUES
         (30, 'C', 2,    'PUT', 0, null, null, null , null, null, null, null, 'Test PUT', false),
         (31, 'R', null, 'E_1', 1, 0,    null, null , 10  , 30  , 30  , null, null, null),
         (32, 'R', null, 'E_2', 1, 1,    null, null , 11  , 30  , 30  , null, null, null)
;

INSERT INTO dte_dtn (dtn_id, dtn_type, dtn_grp_id, dtn_label, dtn_depth, dtn_order, dtn_btn_id, dtn_negated, dtn_dtn_id, dtn_parent_id, dtn_root_id, dtn_script, dtn_desc, dtn_shared) VALUES
                    (40, 'C', 2,    'DEL', 0, null, null, null , null, null, null, null, 'Test DEL', false),
                    (41, 'R', null, 'E_1', 1, 0,    null, null , 10  , 40  , 40  , null, null, null),
                    (42, 'R', null, 'E_2', 1, 1,    null, null , 11  , 40  , 40  , null, null, null)
;