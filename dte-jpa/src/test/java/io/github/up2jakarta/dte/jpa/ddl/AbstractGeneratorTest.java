package io.github.up2jakarta.dte.jpa.ddl;

import io.github.up2jakarta.dte.jpa.entities.btn.*;
import io.github.up2jakarta.dte.jpa.entities.dtn.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import io.github.up2jakarta.dte.jpa.entities.btn.*;
import io.github.up2jakarta.dte.jpa.entities.dtn.*;
import io.github.up2jakarta.dte.jpa.entities.grp.MainGroup;
import io.github.up2jakarta.dte.jpa.entities.grp.WorkGroup;
import io.github.up2jakarta.dte.jpa.entities.grp.Workspace;
import io.github.up2jakarta.dte.jpa.entities.tpe.BasicType;
import io.github.up2jakarta.dte.jpa.entities.tpe.GeneratedType;
import io.github.up2jakarta.dte.jpa.entities.tpe.UserType;
import io.github.up2jakarta.dte.jpa.entities.tpl.BasicTemplate;
import io.github.up2jakarta.dte.jpa.entities.tpl.UserTemplate;
import io.github.up2jakarta.dte.jpa.views.VCScript;
import io.github.up2jakarta.dte.jpa.views.VComputer;
import io.github.up2jakarta.dte.jpa.views.VDScript;
import io.github.up2jakarta.dte.jpa.views.VDecider;

import java.util.EnumSet;

import static org.junit.Assert.assertTrue;

public abstract class AbstractGeneratorTest {

    private static final String BASE_DIR = "target/sql/";

    private ServiceRegistry serviceRegistry;
    private MetadataImplementor metadata;

    public void setUp(final Class<? extends Dialect> dialect) {
        //System.setProperty("hibernate.dialect", H2Dialect.class.getTypeName());
        serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(Environment.getProperties())
                .applySetting("hibernate.dialect", dialect.getTypeName())
                .applySetting("hibernate.hbm2ddl.schema_filter_provider", DTSchemaFilterProvider.class.getName())
                .build();
        metadata = (MetadataImplementor) new MetadataSources(serviceRegistry)
                .addResource("META-INF/checks.hbm.xml")
                .addResource("META-INF/views.hbm.xml")
                // Groups
                .addAnnotatedClass(MainGroup.class)
                .addAnnotatedClass(Workspace.class)
                .addAnnotatedClass(WorkGroup.class)
                // Types
                .addAnnotatedClass(BasicType.class)
                .addAnnotatedClass(GeneratedType.class)
                .addAnnotatedClass(UserType.class)
                // Templates
                .addAnnotatedClass(BasicTemplate.class)
                .addAnnotatedClass(UserTemplate.class)
                // Deciders
                .addAnnotatedClass(BTreePlainDecider.class)
                .addAnnotatedClass(BTreeMixedDecider.class)
                .addAnnotatedClass(BTreeDeciderNode.class)
                .addAnnotatedClass(BTreeLocalNode.class)
                .addAnnotatedClass(BTreeOperatorNode.class)
                // Computers
                .addAnnotatedClass(DTreePlainComputer.class)
                .addAnnotatedClass(DTreeMixedComputer.class)
                .addAnnotatedClass(DTreeDeciderNode.class)
                .addAnnotatedClass(DTreeDefaultNode.class)
                .addAnnotatedClass(DTreeComputerNode.class)
                .addAnnotatedClass(DTreeLocalNode.class)
                .addAnnotatedClass(DTreeDecisionNode.class)
                // Views
                .addAnnotatedClass(VDecider.class)
                .addAnnotatedClass(VComputer.class)
                .addAnnotatedClass(VDScript.class)
                .addAnnotatedClass(VCScript.class)
                .buildMetadata();
        //this.annotatedClasses(Operand.class.getPackage()).forEach(e -> me)
        metadata.validate();
    }

    public void tearDown() {
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
        serviceRegistry = null;
    }

    void testGenerateDdl(final SchemaExport.Action action, final String fileName) {
        final SchemaExport schemaExport = new SchemaExport();
        schemaExport.setFormat(true);
        schemaExport.setDelimiter(";");

        final java.io.File file = new java.io.File(BASE_DIR + fileName);
        if (file.exists()) {
            final boolean delete = file.delete();
            System.out.println("## " + delete + ": " + file.getAbsolutePath());
        }

        schemaExport.setOutputFile(file.getPath());
        schemaExport.execute(EnumSet.of(TargetType.SCRIPT), action, metadata);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }
}
