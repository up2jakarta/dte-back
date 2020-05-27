package io.github.up2jakarta.dte.jpa.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import io.github.up2jakarta.dte.dsl.BaseScript;
import io.github.up2jakarta.dte.jpa.TestConfiguration;
import io.github.up2jakarta.dte.jpa.entities.Template;
import io.github.up2jakarta.dte.jpa.entities.tpl.BasicTemplate;
import io.github.up2jakarta.dte.jpa.views.VTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, inheritLocations = false)
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:templates_repo;MODE=PostgreSQL;"
})
public class TemplateRepositoryTests {

    @Autowired
    private TemplateRepository repo;

    @Test
    public void shouldGetDefault() {
        // WHEN
        final Template entity = repo.getDefault();

        // THEN
        assertThat(entity).isNotNull()
                .isInstanceOf(BasicTemplate.class)
                .hasFieldOrPropertyWithValue("id", 0)
                .matches(e -> "DTE/Default".equals(entity.toString()))
                .hasFieldOrPropertyWithValue("baseClass", BaseScript.class)
                .hasFieldOrPropertyWithValue("label", "Default")
                .hasFieldOrPropertyWithValue("description", "Default")
                .hasFieldOrPropertyWithValue("group.id", 0)
        ;
    }

    @Test
    public void shouldFindById() {
        // WHEN
        final Template entity = repo.find(0);

        // THEN
        assertThat(entity).isNotNull()
                .isInstanceOf(BasicTemplate.class)
                .hasFieldOrPropertyWithValue("id", 0)
                .hasFieldOrPropertyWithValue("baseClass", BaseScript.class)
                .hasFieldOrPropertyWithValue("label", "Default")
                .hasFieldOrPropertyWithValue("description", "Default")
                .hasFieldOrPropertyWithValue("group.id", 0)
        ;
    }

    @Test
    public void shouldSearch() {
        // GIVEN
        final int groupId = 0;

        // WHEN
        final List<VTemplate> types = repo.search(groupId);

        // THEN
        assertThat(types).isNotNull()
                .hasSize(1)
                .matches(l -> l.stream().allMatch(t -> t.getGroupId() == groupId))
        ;
    }

    @Test
    public void shouldSearchByGroup() {
        // GIVEN
        final int groupId = 1;

        // WHEN
        final List<VTemplate> types = repo.search(groupId);

        // THEN
        assertThat(types).isNotNull()
                .hasSize(1)
                .matches(l -> l.stream().allMatch(t -> t.getGroupId() == 0))
        ;
    }

}
