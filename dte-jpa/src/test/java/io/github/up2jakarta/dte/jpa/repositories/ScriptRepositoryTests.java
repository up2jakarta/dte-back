package io.github.up2jakarta.dte.jpa.repositories;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import io.github.up2jakarta.dte.jpa.TestConfiguration;
import io.github.up2jakarta.dte.jpa.views.Key;
import io.github.up2jakarta.dte.jpa.views.VCScript;
import io.github.up2jakarta.dte.jpa.views.VDScript;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, inheritLocations = false)
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql, classpath:computers.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:computers_repo;MODE=PostgreSQL;"
})
public class ScriptRepositoryTests {

    private static final int DTN = 2;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ScriptRepository repo;

    @Test
    public void searchScriptedDecider() {
        // WHEN
        final List<VDScript> list = repo.search(DTN, Collections.emptyList());

        // THEN
        Assert.assertNotNull(list);
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void searchScriptedDeciderEmpty() {
        // WHEN
        final List<VDScript> list = repo.search(DTN, Collections.singletonList("404"));

        // THEN
        Assert.assertNotNull(list);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void searchScriptedComputer() {
        // WHEN
        final List<VCScript> list = repo.search(DTN, Collections.emptyList(), Collections.emptyList());

        // THEN
        Assert.assertNotNull(list);
        Assert.assertEquals(6, list.size());
    }

    @Test
    public void searchScriptedComputerEmpty1() {
        // WHEN
        final List<VCScript> list = repo.search(DTN, Collections.singletonList("404"), Collections.emptyList());

        // THEN
        Assert.assertNotNull(list);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void searchScriptedComputerEmpty2() {
        // WHEN
        final List<VCScript> list = repo.search(DTN, Collections.emptyList(), Collections.singletonList("404"));

        // THEN
        Assert.assertNotNull(list);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void findScriptedDecider() {
        // WHEN
        final VDScript entity = repo.find(new Key(21, 'S'));
        entityManager.detach(entity);

        // THEN
        assertThat(entity).isNotNull()
                .matches(e -> new Key(21, 'S').compareTo(e.getId()) == 0)
                .hasFieldOrPropertyWithValue("id.id", 21L)
                .hasFieldOrPropertyWithValue("id.type", 'S')
                .hasFieldOrPropertyWithValue("templateId", 0)
                .hasFieldOrPropertyWithValue("shared", true)
                .hasFieldOrPropertyWithValue("label", "TND")
                .hasFieldOrPropertyWithValue("script", "currency == \"TND\"")
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("groupId", 2)
                .hasFieldOrPropertyWithValue("description", "")
                .hasFieldOrPropertyWithValue("inputs.size", 0)
        ;
    }

    @Test
    public void findScriptedComputer() {
        // WHEN
        final VCScript entity = repo.find(102);
        entityManager.detach(entity);

        // THEN
        assertThat(entity).isNotNull()
                .hasFieldOrPropertyWithValue("id", 102L)
                .hasFieldOrPropertyWithValue("templateId", 0)
                .hasFieldOrPropertyWithValue("shared", false)
                .hasFieldOrPropertyWithValue("label", "Tunisia")
                .hasFieldOrPropertyWithValue("script", "country = \"Tunisia\"")
                .hasFieldOrPropertyWithValue("groupId", 2)
                .hasFieldOrPropertyWithValue("description", "")
                .hasFieldOrPropertyWithValue("inputs.size", 0)
                .hasFieldOrPropertyWithValue("outputs.size", 0)
        ;
    }

}
