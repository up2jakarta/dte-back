-- DROP
DROP FUNCTION IF EXISTS dte_version();

-- RECREATE

-- @return the database version
CREATE FUNCTION dte_version() RETURNS varchar AS $$
    DECLARE db_version varchar;
    BEGIN
        SELECT version FROM dte_vrs
            WHERE version IS NOT NULL
            ORDER BY string_to_array(version, '.') DESC LIMIT 1
            INTO db_version;
        RETURN db_version;
    END;
$$ LANGUAGE 'plpgsql';