------------------------------------------------- Decision Trees -------------------------------------------------------
-- Deciders
INSERT INTO dte_btn(btn_id, btn_type, btn_label, btn_desc, btn_script, btn_grp_id, btn_negated, btn_shared, btn_tpl_id) VALUES
                          (20, 'S', 'EUR', '', 'currency == "EUR"', 2, false, true, 0),
                          (21, 'S', 'TND', '', 'currency == "TND"', 2, false, true, 0)
    ;
INSERT INTO dte_btn(btn_id, btn_type, btn_label, btn_desc, btn_grp_id, btn_operator, btn_depth, btn_negated, btn_shared) VALUES
                          (22, 'C', 'Empty', 'Empty', 2, 'O', 0, false, false)
    ;
-- Simple Computers
INSERT INTO dte_dtn(dtn_id, dtn_type, dtn_label, dtn_desc, dtn_script, dtn_grp_id, dtn_shared, dtn_tpl_id) VALUES
                            (101, 'S', 'Europe' , '', 'country = "Europe"'                  , 2, false, 0),
                            (102, 'S', 'Tunisia', '', 'country = "Tunisia"'                 , 2, false, 0),
                            (103, 'S', 'Unknown', '', 'country = "Unknown"'                 , 2, false, 0),
                            (104, 'S', 'Warning', '', 'log = "Unknown currency " + currency', 2, false, 0),
                            (105, 'S', 'EU'     , '', 'country = "European Union"'          , 2, false, 0),
                            (150, 'S', 'Maghreb'  , '', 'country = "Maghreb"'                   , 2, true, 0)
    ;
-- Empty computers
INSERT INTO dte_dtn (dtn_id, dtn_type, dtn_label, dtn_desc, dtn_depth, dtn_grp_id, dtn_shared) VALUES
                     (1,   'C', 'Empty' , '' , 0, 2, false),
                     (2,   'C', 'Dummy' , '' , 0, 2, false)
    ;
-- Deprecated computer
INSERT INTO dte_dtn (dtn_id, dtn_type, dtn_label, dtn_desc, dtn_depth, dtn_grp_id, dtn_shared) VALUES
                     (10,  'C', 'Deprecated' , '' , 0, 2, false);
INSERT INTO dte_dtn (dtn_id, dtn_type, dtn_depth, dtn_order, dtn_btn_id, dtn_negated, dtn_dtn_id, dtn_parent_id, dtn_root_id, dtn_label) VALUES
                     (11,  'D', 1, 0, 20 , false, null, 10,  10  , 'EUR Decision'),
                     (12,  'R', 2, 0, null, null , 101 , 11,  10 , 'Europe Cumputer' ),
                     (13,  'D', 1, 1, 22 , true, null, 10,  10   , 'Empty Decision'),
                     (14,  'R', 2, 0, null, null , 1   , 13,  10 , 'Empty Computer' ),
                     (15,  'E', 1, 2, null, null , null, 10,  10 , 'Default Decision' )
     ;
-- Pay computer
INSERT INTO dte_dtn (dtn_id, dtn_type, dtn_label, dtn_desc, dtn_depth, dtn_grp_id, dtn_shared) VALUES
                     (1000, 'C', 'Pay', '' , 0, 2, false);
INSERT INTO dte_dtn (dtn_id, dtn_type, dtn_depth, dtn_order, dtn_btn_id, dtn_negated, dtn_dtn_id, dtn_parent_id, dtn_root_id, dtn_label) VALUES
                     (1001,  'D', 1, 1,   20 , false, null,   1000,  1000            , 'EUR Decision'), -- Decision
                        (1002,  'R', 2, 0,   null , null, 101,   1001,  1000         , 'Europe Computer'),
                        (1003,  'R', 3, 0,   null , null, 105,   1002,  1000         , 'EU Computer'),
                     (1004,  'D', 1, 2,   21 , false, null,   1000,  1000            , 'TND Decision'), -- Decision
		                (1005,  'R', 2, 0,   null , null, 102,   1004,  1000 , 'Tunisia Computer'),
                     (1006,  'E', 1, 3,   null, null , null,   1000,  1000           , 'Else Decision'),  -- Else
                        (1007,  'R', 2, 0,   null , null, 103,   1006,  1000         , 'Unknown'),
                        (1008,  'R', 3, 0,   null , null, 104,   1007,  1000         , 'Warning')
	;
-- Invalid computers
INSERT INTO dte_dtn (dtn_id, dtn_type, dtn_label, dtn_desc, dtn_depth, dtn_grp_id, dtn_shared) VALUES
                     (2000, 'C', 'INVALID1', '' , 0, 2, false),
                     (3000, 'C', 'INVALID2', '' , 0, 2, false),
                     (4000, 'C', 'INVALID3', '' , 0, 2, false);
INSERT INTO dte_dtn (dtn_id, dtn_type, dtn_depth, dtn_order, dtn_btn_id, dtn_negated, dtn_dtn_id, dtn_parent_id, dtn_root_id, dtn_label) VALUES
                     (2001,  'D', 1, 1,   20 , false, null,  2000,  2000,  'D020' ), -- Decision
                     (2002,  'R', 1, 0,   null , null, 101,   2000,  2000, 'R101' ), -- Rule

                     (3002,  'R', 1, 0,   null , null, 102,   3000,  3000, 'R102' ), -- Rule
                     (3001,  'D', 1, 1,   21 , false, null,  3000,  3000,  'D021' ), -- Decision

                     (4001,  'E', 1, 0,   null, null , null,  4000,  4000, 'E000' ), -- Empty
                     (4002,  'D', 1, 1,   22 , false, null,  4000,  4000,  'D022' )  -- Decision
    ;
