-- Simple
INSERT INTO dte_btn(btn_id, btn_type, btn_label, btn_desc, btn_script, btn_grp_id, btn_negated, btn_shared, btn_tpl_id) VALUES
                          (10, 'S', 'AeqB', 'a eq b', 'a = b', 1, false, false, 0),
                          (11, 'S', 'CeqB', 'C', 'c = b', 1, false, false, 0),
                          (12, 'S', 'DeqB', 'D', 'd = b', 1, false, false, 0),
                          (20, 'S', 'A', 'A', 'a', 1, false, false, 0),
                          (21, 'S', 'B', 'B', 'b', 1, false, false, 0),
                          (22, 'S', 'G', 'Group 2', 'g', 2, false, false, 0);


-- AND
INSERT INTO dte_btn(btn_id, btn_type, btn_label, btn_desc, btn_grp_id, btn_operator, btn_depth, btn_negated, btn_shared) VALUES
                          (30, 'C', 'A&B', 'A and B', 1, 'A', 0, false, false);
INSERT INTO dte_btn(btn_id, btn_type, btn_depth, btn_order, btn_parent_id, btn_root_id, btn_btn_id, btn_negated, btn_label) VALUES
                          (31, 'L', 1, 0, 30, 30, 20, true, 'A');
INSERT INTO dte_btn(btn_id, btn_type, btn_depth, btn_order, btn_parent_id, btn_root_id, btn_negated, btn_label, btn_desc, btn_script, btn_shared, btn_tpl_id, btn_grp_id) VALUES
                          (32, 'T', 1, 1, 30, 30, false, 'LB', 'LB', 'b', false, 0, 1 );

-- OR
INSERT INTO dte_btn(btn_id, btn_type, btn_label, btn_desc, btn_grp_id, btn_operator, btn_depth, btn_negated, btn_shared) VALUES
                        (40, 'C', 'A|B', 'a or b', 1, 'O', 0, false, false);
INSERT INTO dte_btn(btn_id, btn_type, btn_depth, btn_order, btn_parent_id, btn_root_id, btn_btn_id, btn_negated, btn_label) VALUES
                        (41, 'L', 1, 0, 40, 40, 20, true, 'A'),
                        (42, 'L', 1, 1, 40, 40, 21, true, 'B');

-- XOR
INSERT INTO dte_btn(btn_id, btn_type, btn_label, btn_desc, btn_grp_id, btn_operator, btn_depth, btn_negated, btn_shared) VALUES
                        (50, 'C', 'AxB', 'a xor b', 1, 'O', 0, false, false);
INSERT INTO dte_btn(btn_id, btn_type, btn_depth, btn_order, btn_parent_id, btn_root_id, btn_btn_id, btn_negated, btn_label) VALUES
                        (51, 'L', 1, 0, 50, 50, 20, true, 'A'),
                        (52, 'L', 1, 1, 50, 50, 21, true, 'B');