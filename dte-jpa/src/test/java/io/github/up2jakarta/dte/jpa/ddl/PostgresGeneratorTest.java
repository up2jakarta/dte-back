package io.github.up2jakarta.dte.jpa.ddl;

import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PostgresGeneratorTest extends AbstractGeneratorTest {

    @Before
    public void setUp() {
        super.setUp(PostgreSQL95Dialect.class);
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void generateDdlCreate() {
        super.testGenerateDdl(SchemaExport.Action.CREATE, "pg-create.sql");
    }

    @Test
    public void generateDdlDrop() {
        super.testGenerateDdl(SchemaExport.Action.DROP, "pg-drop.sql");
    }

}
