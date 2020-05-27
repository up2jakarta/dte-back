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
import io.github.up2jakarta.dte.jpa.TestUtil;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.grp.MainGroup;
import io.github.up2jakarta.dte.jpa.entities.grp.WorkGroup;
import io.github.up2jakarta.dte.jpa.entities.grp.Workspace;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, inheritLocations = false)
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:groups;MODE=PostgreSQL;"
})
public class GroupRepositoryTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private GroupRepository repo;

    @Test
    public void shouldFind() {
        // When
        final Group stored = repo.find(4);

        // Then
        assertThat(stored)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 4)
                .hasFieldOrPropertyWithValue("label", "BTN")
                .hasFieldOrPropertyWithValue("description", "Binary trees")
                .hasFieldOrPropertyWithValue("color", "green")
                .hasFieldOrPropertyWithValue("icon", "test")
                .hasFieldOrProperty("types")
                .hasFieldOrProperty("templates")
                .hasFieldOrProperty("parameters")
                .hasFieldOrProperty("typing")
                .hasFieldOrProperty("declaredTyping");
    }

    @Test
    public void shouldNotFindMain() {
        // When
        final Group stored = repo.find(0);

        // Then
        assertThat(stored).isNull();
    }

    @Test
    public void shouldFindByKey() {
        // When
        final Group stored = repo.find(1, "BTN");

        // Then
        assertThat(stored)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 4)
                .hasFieldOrPropertyWithValue("label", "BTN")
                .hasFieldOrPropertyWithValue("description", "Binary trees")
                .hasFieldOrPropertyWithValue("color", "green")
                .hasFieldOrPropertyWithValue("icon", "test");
    }

    @Test
    public void shouldNotFindByNull() {
        // When
        final Group stored = repo.find(0, null);

        // Then
        assertThat(stored).isNull();
    }

    @Test
    public void shouldSave() {
        // Given
        final Workspace group = new Workspace(TestUtil.MAIN_GROUP);
        {
            group.setLabel("Test");
            group.setDescription("Test");
            group.setIcon("DTN");
            group.setColor("red");
        }

        // When
        repo.persist(group);
        entityManager.flush();
        entityManager.clear();
        final Group stored = repo.find(group.getId());

        // Then
        assertThat(stored)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", group.getId())
                .hasFieldOrPropertyWithValue("label", group.getLabel())
                .hasFieldOrPropertyWithValue("description", group.getDescription())
                .hasFieldOrPropertyWithValue("color", group.getColor())
                .hasFieldOrPropertyWithValue("icon", group.getIcon())
                .hasFieldOrPropertyWithValue("parent.id", group.getParent().getId())
                .hasFieldOrPropertyWithValue("groups.size", group.getGroups().size())
                .hasFieldOrPropertyWithValue("types.size", group.getTypes().size())
                .hasFieldOrPropertyWithValue("templates.size", group.getTemplates().size())
                .hasFieldOrPropertyWithValue("parameters.size", group.getParameters().size())
                .hasFieldOrPropertyWithValue("typing.size", group.getTyping().size())
                .hasFieldOrPropertyWithValue("declaredTyping.size", group.getDeclaredTyping().size());

    }

    @Test
    public void shouldUpdate() {
        // GIVEN
        final String label = "G2";
        final String color = "blue";
        final String icon = "blue";
        final String description = "Simple equality decider";

        // WHEN
        final Group group = repo.find(1);
        entityManager.detach(group);

        group.setLabel(label);
        group.setDescription(description);
        group.setColor(color);
        group.setIcon(icon);
        repo.merge(group);
        entityManager.flush();
        entityManager.clear();

        // THEN
        final Group persisted = repo.find(1);

        Assert.assertEquals(label, persisted.getLabel());
        Assert.assertEquals(color, persisted.getColor());
        Assert.assertEquals(icon, persisted.getIcon());
        Assert.assertEquals(description, persisted.getDescription());
    }

    @Test
    public void shouldDelete() {
        // WHEN
        final Group group = entityManager.getReference(Group.class, 3);
        repo.remove(group);
        entityManager.flush();
        entityManager.clear();

        // THEN
        final Group persisted = repo.find(3);
        Assert.assertNull(persisted);
    }

    @Test
    public void shouldFindWorkspaces() {
        // WHEN
        final List list = repo.search(null);

        // THEN
        Assert.assertNotNull(list);
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void shouldFindWorkGroups() {
        // WHEN
        final List list = repo.search(1);

        // THEN
        Assert.assertNotNull(list);
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void shouldGetMainGroup() {
        // WHEN
        final MainGroup main = repo.getMain();

        // THEN
        Assert.assertNotNull(main);
        Assert.assertEquals(1, main.getSpaces().size());
    }

    @Test
    public void testEquality() {
        // WHEN
        final Workspace group = TestUtil.BTN_SPACE;
        final Workspace copy = new Workspace(group.getParent());
        {
            copy.setLabel(group.getLabel());
            copy.setDescription(group.getDescription());
            copy.setIcon(group.getIcon());
            copy.setColor(group.getColor());
        }
        final Group other = new WorkGroup(group);
        {
            other.setLabel(group.getLabel());
            other.setDescription("Decision tree engine");
            other.setIcon("BTN");
            other.setColor("green");
        }

        // THEN
        Assert.assertEquals(0, TestUtil.MAIN_GROUP.hashCode());
        Assert.assertEquals(group.hashCode(), group.hashCode());
        Assert.assertEquals(group, group);
        Assert.assertEquals(group, copy);
        Assert.assertNotEquals(group, other);
        Assert.assertNotEquals(group, group.getParent());
        Assert.assertNotEquals(group.getParent(), group);
        Assert.assertNotEquals(group, new Object());
    }
}
