package io.github.up2jakarta.dte.web.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import io.github.up2jakarta.dte.web.TestConfiguration;
import io.github.up2jakarta.dte.web.models.MetaModel;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:meta_service;MODE=PostgreSQL;",
        "spring.datasource.tomcat.validation-query=SELECT unknown from unknowns"
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MetaServiceTests {

    @Autowired
    private MetaService service;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void initialize() throws NoSuchFieldException, IllegalAccessException {
        final Field isTest = service.getClass().getDeclaredField("isTest");
        isTest.setAccessible(true);
        isTest.set(service, false);
    }

    @Test(expected = SQLException.class)
    @Order(0)
    public void statusShouldFails() throws Throwable {
        service.status();
    }

    @Test
    @Order(1)
    public void aboutShouldSuccessWhenError() {
        // When
        final MetaModel model = service.about();

        // Then
        assertThat(model)
                .isNotNull()
                .extracting("dataSource")
                .isNotNull()
                .matches(ds -> ds.toString().contains("DTE_VERSION"));
    }

    @Test
    @Order(99)
    @Transactional
    public void aboutShouldSuccess() {
        // Given
        final String version = "DTE.H2.FINAL";
        final String fn = "String version(){\n\treturn \"" + version + "\";\n}";

        // When
        entityManager.createNativeQuery("CREATE ALIAS DTE_VERSION AS $$\n " + fn + "\n$$;")
                .executeUpdate();
        final MetaModel model = service.about();

        // Then
        assertThat(model)
                .isNotNull()
                .hasFieldOrPropertyWithValue("dataSource", version);
    }


}
