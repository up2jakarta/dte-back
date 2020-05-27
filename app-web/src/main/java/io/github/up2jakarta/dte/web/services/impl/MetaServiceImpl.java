package io.github.up2jakarta.dte.web.services.impl;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.web.models.MetaModel;
import io.github.up2jakarta.dte.web.services.MetaService;

import javax.persistence.EntityManagerFactory;

/**
 * API Meta Service implementation (No transaction).
 */
@Service
@Primary
public class MetaServiceImpl implements MetaService {

    private final JdbcTemplate jdbcTemplate;
    private final String checkQuery;
    private final boolean isTest;

    /**
     * Public constructor for MetaServiceImpl.
     *
     * @param emFactory   the entity manager factory
     * @param sqlTemplate the spring DBC template
     * @param testQuery   the data base validation SQL query
     */
    @Autowired
    public MetaServiceImpl(final EntityManagerFactory emFactory, final JdbcTemplate sqlTemplate,
                           @Value("${spring.datasource.tomcat.validation-query}") final String testQuery) {
        this.jdbcTemplate = sqlTemplate;
        this.checkQuery = testQuery;
        isTest = emFactory.unwrap(SessionFactoryImplementor.class).getJdbcServices().getDialect() instanceof H2Dialect;
    }

    @Override
    public MetaModel about() {
        if (isTest) {
            return new MetaModel(StaticEngine.version().getBuildVersion());
        }
        try {
            final String version = jdbcTemplate.queryForObject("select DTE_VERSION()", String.class);
            return new MetaModel(version);
        } catch (DataAccessException ex) {
            return new MetaModel(ex.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public String status() throws Throwable {
        try {
            jdbcTemplate.queryForObject(checkQuery, Object.class);
            return "OK";
        } catch (DataAccessException ex) {
            throw ex.getMostSpecificCause();
        }
    }
}
