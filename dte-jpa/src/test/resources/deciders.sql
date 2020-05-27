-- Simple
INSERT INTO dte_btn(btn_id, btn_type, btn_label, btn_desc, btn_script, btn_grp_id, btn_negated, btn_shared, btn_tpl_id) VALUES
                          (10, 'S', 'A=B', 'A eq B', 'a = b', 4, false, true, 0),
                          (11, 'S', 'C=D', 'C eq D', 'c = d', 4, false, true, 0),
                          (12, 'S', 'A', 'A', 'a' , 4, false, true, 0),
                          (13, 'S', 'B', 'B', 'b' , 4, false, true, 0)
    ;
-- Empty complex decider
INSERT INTO dte_btn(btn_id, btn_type, btn_label, btn_desc, btn_grp_id, btn_operator, btn_depth, btn_negated, btn_shared) VALUES
                          (100, 'C', 'Empty', 'Empty', 4, 'O', 0, false, true)
    ;
-- XOR complex decider
INSERT INTO dte_btn(btn_id, btn_type, btn_label, btn_desc, btn_grp_id, btn_operator, btn_depth, btn_negated, btn_shared) VALUES
                          (200, 'C', 'A^B', 'A xor B', 4, 'O', 0, false, false);
INSERT INTO dte_btn(btn_id, btn_type, btn_depth , btn_order, btn_operator, btn_negated, btn_parent_id, btn_root_id, btn_label) VALUES
                          (201, 'P', 1, 0, 'A', false, 200, 200, '!A&B'),
                          (202, 'P', 1, 1, 'A', false, 200, 200, '!B&A');
INSERT INTO dte_btn(btn_id, btn_type, btn_depth, btn_order, btn_parent_id, btn_root_id, btn_btn_id, btn_negated, btn_label) VALUES
                          (203, 'L', 2, 0, 201, 200, 12, false, 'A'),
                          (204, 'L', 2, 1, 201, 200, 13, true , 'B'),
                          (205, 'L', 2, 0, 202, 200, 12, true , 'A'),
                          (206, 'L', 2, 1, 202, 200, 13, false, 'B')
    ;
-- AND complex decider
INSERT INTO dte_btn(btn_id, btn_type, btn_label, btn_desc, btn_grp_id, btn_operator, btn_depth, btn_negated, btn_shared) VALUES
                          (300, 'C', 'A&B', 'A and B', 4, 'A', 0, false, true);
INSERT INTO dte_btn(btn_id, btn_type, btn_depth, btn_order, btn_parent_id, btn_root_id, btn_btn_id, btn_negated, btn_label) VALUES
                          (301, 'L', 1, 0, 300, 300, 12, false, 'A'),
                          (302, 'L', 1, 1, 300, 300, 13, false, 'B')
    ;
-- NOT complex decider
INSERT INTO dte_btn(btn_id, btn_type, btn_label, btn_desc, btn_grp_id, btn_operator, btn_depth, btn_negated, btn_shared) VALUES
                          (400, 'C', '!A', 'not A', 4, 'A', 0, false, true);
INSERT INTO dte_btn(btn_id, btn_type, btn_depth, btn_order, btn_parent_id, btn_root_id, btn_btn_id, btn_negated, btn_label) VALUES
                          (401, 'L', 1, 0, 400, 400, 12, true, 'A')
    ;
-- AND complex decider
INSERT INTO dte_btn(btn_id, btn_type, btn_label, btn_desc, btn_grp_id, btn_operator, btn_depth, btn_negated, btn_shared) VALUES
                          (500, 'C', 'A|B', 'A or B', 4, 'O', 0, false, false);
INSERT INTO dte_btn(btn_id, btn_type, btn_depth, btn_order, btn_parent_id, btn_root_id, btn_btn_id, btn_negated, btn_label) VALUES
                          (501, 'L', 1, 0, 500, 500, 12, true, 'A');
INSERT INTO dte_btn(btn_id, btn_type, btn_depth, btn_order, btn_parent_id, btn_root_id, btn_negated, btn_label, btn_desc, btn_script, btn_shared, btn_tpl_id, btn_grp_id) VALUES
                          (502, 'T', 1, 1, 500, 500, false, 'LB', 'LB', 'b', false, 0, 4)
;
