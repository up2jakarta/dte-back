-- DROP triggers
DROP TRIGGER IF EXISTS dte_btn_check ON dte_btn;
DROP TRIGGER IF EXISTS dte_dtn_check ON dte_dtn;
DROP TRIGGER IF EXISTS dte_grp_check ON dte_grp;
-- DROP trigger functions
DROP FUNCTION IF EXISTS dte_btn_check();
DROP FUNCTION IF EXISTS dte_dtn_check();
DROP FUNCTION IF EXISTS dte_grp_check();
-- DROP utility functions
DROP FUNCTION IF EXISTS dte_btn_check_ref(integer, bigint);
DROP FUNCTION IF EXISTS dte_dtn_check_ref(integer, bigint);

-- CREATE utility functions
CREATE FUNCTION dte_btn_check_ref(btn_group integer, btn_ref_id bigint) RETURNS void AS $$
    DECLARE ref_type character;
    DECLARE ref_groups integer[];
    DECLARE ref_shared boolean;
    BEGIN
        SELECT btn_id, btn_type, btn_shared, array_agg(grp_ref_id) as btn_groups
            FROM dte_btn INNER JOIN dte_jpa_grp ON grp_id = btn_grp_id
            WHERE btn_id = btn_ref_id GROUP BY btn_id
            INTO btn_ref_id, ref_type, ref_shared, ref_groups;
        IF (FOUND) THEN
            IF (ref_type NOT IN ('C', 'S')) THEN
                RAISE SQLSTATE '23503' USING
                    MESSAGE = 'insert or update violates decider foreign constraints',
                    DETAIL  = 'decider of type [' || ref_type || '] cannot be referenced';
            ELSIF (NOT ref_shared AND btn_group = ANY (ref_groups)) THEN
                 RAISE SQLSTATE '23503' USING
                    MESSAGE = 'insert or update violates decider foreign constraints',
                    DETAIL  = 'decider [' || btn_ref_id || '] cannot be referenced in group [' || btn_group || ']';
            END IF;
        END IF;
    END;
$$ LANGUAGE 'plpgsql';

CREATE FUNCTION dte_dtn_check_ref(dtn_group integer, dtn_ref_id bigint) RETURNS void AS $$
    DECLARE ref_type character;
    DECLARE ref_groups integer[];
    DECLARE ref_shared boolean;
    BEGIN
        SELECT dtn_id, dtn_type, dtn_shared, array_agg(grp_ref_id) as btn_groups
            FROM dte_dtn INNER JOIN dte_jpa_grp ON grp_id = dtn_grp_id
            WHERE dtn_id = dtn_ref_id GROUP BY dtn_id
            INTO dtn_ref_id, ref_type, ref_shared, ref_groups;
        IF (FOUND) THEN
            IF (ref_type NOT IN ('C', 'S')) THEN
                RAISE SQLSTATE '23503' USING
                    MESSAGE = 'insert or update violates computer foreign constraints',
                    DETAIL  = 'computer of type [' || ref_type || '] cannot be referenced';
            ELSIF (NOT ref_shared AND dtn_group = ANY (ref_groups)) THEN
                 RAISE SQLSTATE '23503' USING
                    MESSAGE = 'insert or update violates computer foreign constraints',
                    DETAIL  = 'computer [' || dtn_ref_id || '] cannot be referenced in group [' || dtn_group || ']';
            END IF;
        END IF;
    END;
$$ LANGUAGE 'plpgsql';

-- CREATE trigger functions
CREATE FUNCTION dte_btn_check() RETURNS trigger AS $$
     DECLARE btn_group integer;
    BEGIN
        IF (NEW.btn_root_id = NEW.btn_btn_ID) THEN
            RAISE SQLSTATE '23503' USING
                MESSAGE = 'insert or update violates decider foreign constraints',
                DETAIL  = 'decider [' || NEW.btn_root_id || '] cannot be referenced by itself';
        ELSIF (TG_OP = 'INSERT' AND NEW.btn_btn_ID IS NOT NULL) THEN
            SELECT btn_grp_id FROM dte_btn WHERE btn_id = NEW.btn_root_id INTO btn_group;
            EXECUTE dte_btn_check_ref(btn_group, NEW.btn_btn_ID);
        ELSIF (TG_OP = 'UPDATE' AND NEW.btn_btn_ID IS NOT NULL AND NEW.btn_btn_ID <> OLD.btn_btn_ID) THEN
            SELECT btn_grp_id FROM dte_btn WHERE btn_id = NEW.btn_root_id INTO btn_group;
            EXECUTE dte_btn_check_ref(btn_group, NEW.btn_btn_ID);
        END IF;
        RETURN NEW;
    END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION dte_dtn_check() RETURNS trigger AS $$
    DECLARE dtn_group integer;
    BEGIN
        IF (NEW.dtn_type NOT IN ('D', 'R')) THEN
             RETURN NEW;
        END IF;

        SELECT dtn_grp_id FROM dte_dtn WHERE dtn_id = NEW.dtn_root_id INTO dtn_group;

        IF (NEW.dtn_root_id = NEW.dtn_dtn_ID) THEN
            RAISE SQLSTATE '23503' USING
                MESSAGE = 'insert or update violates computer foreign constraints',
                DETAIL  = 'computer [' || NEW.dtn_root_id || '] cannot be referenced by itself';
        ELSIF (TG_OP = 'INSERT') THEN
           IF (NEW.dtn_btn_ID IS NOT NULL) THEN
               EXECUTE dte_btn_check_ref(dtn_group, NEW.dtn_btn_ID);
           ELSIF (NEW.dtn_dtn_ID IS NOT NULL) THEN
                EXECUTE dte_dtn_check_ref(dtn_group, NEW.dtn_dtn_ID);
           END IF;
        ELSIF (TG_OP = 'UPDATE') THEN
            IF (NEW.dtn_btn_ID IS NOT NULL AND NEW.dtn_btn_ID <> OLD.dtn_btn_ID) THEN
                EXECUTE dte_btn_check_ref(dtn_group, NEW.dtn_btn_ID);
            ELSIF (NEW.dtn_dtn_ID IS NOT NULL AND NEW.dtn_dtn_ID <> OLD.dtn_dtn_ID) THEN
                 EXECUTE dte_dtn_check_ref(dtn_group, NEW.dtn_dtn_ID);
            END IF;
        END IF;
        RETURN NEW;
    END;
$$ LANGUAGE plpgsql;

CREATE FUNCTION dte_grp_check() RETURNS trigger AS $$
     DECLARE ref_type char(1);
    BEGIN
        IF ((TG_OP = 'DELETE' AND OLD.grp_type = 'M') OR (TG_OP <> 'DELETE' AND NEW.grp_type = 'M')) THEN
            RAISE SQLSTATE '42846' USING MESSAGE = 'Cannot ' || lower(TG_OP) || ' technical group';
        ELSIF (TG_OP = 'INSERT' AND NEW.grp_parent_id IS NOT NULL)
              OR (TG_OP = 'UPDATE' AND NEW.grp_parent_id IS NOT NULL AND NEW.grp_parent_id <> OLD.grp_parent_id) THEN
            SELECT grp_type FROM dte_grp WHERE grp_id = NEW.grp_parent_id INTO ref_type;
            IF (FOUND AND (ref_type = 'G' OR (ref_type = 'M' AND NEW.grp_type <> 'S') OR (ref_type = 'S' AND NEW.grp_type <> 'G'))) THEN
                RAISE SQLSTATE '23503' USING
                    MESSAGE = 'insert or update violates decider foreign constraints',
                    DETAIL  = 'group of type [' || ref_type || '] cannot be referenced by group of type [' || NEW.grp_type || ']';
            END IF;
        END IF;
        IF (TG_OP = 'DELETE') THEN
            RETURN OLD;
        ELSE RETURN NEW;
        END IF;
    END;
$$ LANGUAGE plpgsql;

-- CREATE triggers
CREATE trigger dte_btn_check BEFORE INSERT OR UPDATE ON dte_btn FOR EACH ROW EXECUTE PROCEDURE dte_btn_check();
CREATE trigger dte_dtn_check BEFORE INSERT OR UPDATE ON dte_dtn FOR EACH ROW EXECUTE PROCEDURE dte_dtn_check();
CREATE trigger dte_grp_check BEFORE INSERT OR UPDATE OR DELETE ON dte_grp FOR EACH ROW EXECUTE PROCEDURE dte_grp_check();
-- TODO no ops on basic TPE WHERE group is system;
-- TODO trigger to set default/check nullable label & description for DTE_BIP & DTE_DIP & DTE_DOP
