package io.github.up2jakarta.dte.jpa.ddl;

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OracleGeneratorTest extends AbstractGeneratorTest {

    @Before
    public void setUp() {
        super.setUp(Oracle10gDialect.class);
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void generateDdlCreate() {
        super.testGenerateDdl(SchemaExport.Action.CREATE, "ora-create.sql");
    }

    @Test
    public void generateDdlDrop() {
        super.testGenerateDdl(SchemaExport.Action.DROP, "ora-drop.sql");
    }

}
