package io.github.up2jakarta.dte.jpa.ddl;

import org.hibernate.boot.model.relational.Namespace;
import org.hibernate.boot.model.relational.Sequence;
import org.hibernate.mapping.Table;
import org.hibernate.tool.schema.spi.SchemaFilter;

/**
 * Hibernate {@link DTSchemaFilter} that ignores views while DDL generation.
 */
class DTSchemaFilter implements SchemaFilter {

    /**
     * Single instance.
     */
    static final DTSchemaFilter INSTANCE = new DTSchemaFilter();

    @Override
    public boolean includeNamespace(final Namespace namespace) {
        return true;
    }

    @Override
    public boolean includeTable(final Table table) {
        return !table.getName().toUpperCase().startsWith("DTE_JPA_");
    }

    @Override
    public boolean includeSequence(final Sequence sequence) {
        return true;
    }
}
