package io.github.up2jakarta.dte.jpa.ddl;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class H2GeneratorTest extends AbstractGeneratorTest {

    @Before
    public void setUp() {
        super.setUp(H2Dialect.class);
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void generateDdlCreate() {
        super.testGenerateDdl(SchemaExport.Action.CREATE, "h2-create.sql");
    }

    @Test
    public void generateDdlDrop() {
        super.testGenerateDdl(SchemaExport.Action.DROP, "h2-drop.sql");
    }

}
