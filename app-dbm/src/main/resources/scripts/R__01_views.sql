-- DROP VIEWS
DROP VIEW IF EXISTS dte_computers;
DROP VIEW IF EXISTS dte_deciders;
DROP VIEW IF EXISTS dte_jpa_btn;
DROP VIEW IF EXISTS dte_jpa_grp;
DROP VIEW IF EXISTS dte_jpa_bip;

-- CREATE VIEWS
CREATE VIEW dte_jpa_grp AS
    WITH RECURSIVE grp_tree AS (
        SELECT grp_id, ARRAY[grp_id] AS grp_groups
            FROM dte_grp g
            WHERE grp_type = 'M'
        UNION ALL
        SELECT c.grp_id, p.grp_groups || c.grp_id
            FROM dte_grp c
            INNER JOIN grp_tree p ON p.grp_id = c.grp_parent_id
            WHERE grp_type <> 'M'
    )
    SELECT t.grp_id AS grp_pid, unnest(t.grp_groups) AS grp_did FROM grp_tree t
    ;

CREATE VIEW dte_jpa_btn AS
    SELECT btn_id, btn_type, btn_label, btn_negated, btn_shared, btn_desc, btn_script, btn_tpl_id, btn_grp_id
        FROM dte_btn
        WHERE btn_type IN ('C', 'S', 'T')
    UNION ALL
    SELECT dtn_id, dtn_type, dtn_label, dtn_negated, dtn_shared, dtn_desc, dtn_script, dtn_tpl_id, dtn_grp_id
        FROM dte_dtn
        WHERE dtn_type = 'B'
    ;

CREATE VIEW dte_jpa_bip AS
    SELECT bip_btn_id, btn_type AS bip_type, bip_name, bip_tpe_id
        FROM dte_btn
        INNER JOIN dte_bip ON bip_btn_id = btn_id
        WHERE btn_type IN ('C', 'S', 'T')
    UNION ALL
    SELECT dip_dtn_id, dtn_type, dip_name, dip_tpe_id
        FROM dte_dtn
        INNER JOIN dte_dip ON dip_dtn_id = dtn_id
        WHERE dtn_type = 'B'
    ;

CREATE VIEW dte_computers AS
    WITH RECURSIVE dtn_tree AS (
    SELECT  dtn_label AS dtn_root, dtn_grp_id AS dtn_group, '/' AS dtn_path, r.*
        FROM dte_dtn r
        WHERE dtn_type IN ('C', 'S')
    UNION ALL
    SELECT p.dtn_root, p.dtn_group, p.dtn_path || lpad('' || n.dtn_order, 2, '0') || '/', n.*
        FROM dte_dtn n
        INNER JOIN dtn_tree p ON p.dtn_id = n.dtn_parent_id
    )
    SELECT t.dtn_id, t.dtn_group, t.dtn_root, dtn_path ,
            CASE WHEN t.dtn_type = 'E' THEN '[E] Default'
                 WHEN t.dtn_type = 'D' THEN ('[D] ') || c.btn_label
                 WHEN t.dtn_type = 'R' THEN ('[R] ') || f.dtn_label
                 WHEN t.dtn_type = 'B' THEN ('[B] ') || replace(t.dtn_script, E'\n', E' \\ ')
                 WHEN t.dtn_type = 'T' THEN ('[T] ') || replace(t.dtn_script, E'\n', E' \\ ')
                 ELSE NULL END AS DTN_NODE
        FROM dtn_tree t
        LEFT JOIN dte_btn c ON c.btn_id = t.dtn_btn_id
        LEFT JOIN dte_dtn f ON f.dtn_id = t.dtn_dtn_id
    ORDER BY dtn_group, dtn_root, dtn_path, t.dtn_order
    ;

CREATE VIEW dte_deciders AS
    WITH RECURSIVE btn_tree AS (
    SELECT  btn_label AS btn_root, btn_grp_id AS btn_group, '/' AS btn_path, r.*
        FROM dte_btn r
        WHERE btn_type IN ('C', 'S')
    UNION ALL
    SELECT p.btn_root, p.btn_group, p.btn_path || lpad('' || n.btn_order, 2, '0') || '/', n.*
        FROM dte_btn n
        INNER JOIN btn_tree p ON p.btn_id = n.btn_parent_id
    )
    SELECT t.btn_id, t.btn_group, t.btn_root, btn_path, t.btn_operator, t.btn_negated,
            CASE WHEN t.btn_type = 'L' THEN ('[L] ') || c.btn_label
                 WHEN t.btn_type = 'T' THEN ('[T] ') || replace(t.btn_script, E'\n', E' \\ ')
                 ELSE NULL
            END AS btn_node
        FROM btn_tree t
        LEFT JOIN dte_btn c ON c.btn_id = t.btn_btn_id
    ORDER BY btn_group, btn_root, btn_path, t.btn_order
    ;
