package io.github.up2jakarta.dte.jpa.repositories;

import io.github.up2jakarta.dte.jpa.entities.dtn.*;
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
import io.github.up2jakarta.dte.jpa.api.dtree.IChildNode;
import io.github.up2jakarta.dte.jpa.entities.DTreeComputer;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.Template;
import io.github.up2jakarta.dte.jpa.entities.btn.BTreePlainDecider;
import io.github.up2jakarta.dte.jpa.entities.dtn.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, inheritLocations = false)
@Transactional
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql, classpath:computers.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:computers_repo;MODE=PostgreSQL;"
})
public class ComputerRepositoryTests {

    private static final int DTN = 2;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ComputerRepository repo;

    @Test
    public void shouldSave() {
        // GIVEN
        final Template template = entityManager.getReference(Template.class, 0);
        final Group group = entityManager.getReference(Group.class, DTN);
        final String label = "S1";
        final String script = "a = b";
        final String description = "Simple computer";

        // WHEN
        final DTreePlainComputer calc = new DTreePlainComputer(group);
        calc.setDescription(description);
        calc.setLabel(label);
        calc.setScript(script);
        calc.setTemplate(template);
        calc.setShared(false);
        repo.persist(calc);
        entityManager.flush();
        entityManager.clear();

        // THEN
        final DTreePlainComputer persisted = (DTreePlainComputer) repo.find(calc.getId());

        Assert.assertEquals(label, persisted.getLabel());
        Assert.assertEquals(script, persisted.getScript());
        Assert.assertEquals(description, persisted.getDescription());
        Assert.assertFalse(persisted.isShared());
        Assert.assertEquals(template.getId(), persisted.getTemplate().getId());
    }

    @Test
    public void shouldUpdate() {
        // GIVEN
        final String label = "S2";
        final String script = "a == b";

        // WHEN
        final DTreePlainComputer calc = (DTreePlainComputer) repo.find(150);
        entityManager.detach(calc);

        calc.setLabel(label);
        calc.setScript(script);
        repo.merge(calc);
        entityManager.flush();
        entityManager.clear();

        // THEN
        final DTreePlainComputer persisted = (DTreePlainComputer) repo.find(calc.getId());

        Assert.assertEquals(label, persisted.getLabel());
        Assert.assertEquals(script, persisted.getScript());
    }

    @Test
    public void shouldDelete() {
        // WHEN
        final DTreePlainComputer calc = (DTreePlainComputer) repo.find(150);
        repo.remove(calc);
        entityManager.flush();
        entityManager.clear();

        // THEN
        final DTreePlainComputer persisted = (DTreePlainComputer) repo.find(calc.getId());

        Assert.assertNull(persisted);
    }

    @Test
    public void shouldUpdateMixed() {
        // GIVEN
        final String label = "Complex";

        // WHEN
        final DTreeMixedComputer calc = (DTreeMixedComputer) repo.find(2);
        entityManager.detach(calc);

        calc.setLabel(label);
        repo.merge(calc);
        entityManager.flush();
        entityManager.clear();

        // THEN
        final DTreeMixedComputer persisted = (DTreeMixedComputer) repo.find(calc.getId());

        Assert.assertEquals(label, persisted.getLabel());

        Assert.assertEquals(Integer.valueOf(0), persisted.getDepth());
    }

    @Test
    public void shouldDeleteMixed() {
        // WHEN
        final DTreeMixedComputer calc = (DTreeMixedComputer) repo.find(1000);
        repo.remove(calc);
        entityManager.flush();
        entityManager.clear();

        // THEN
        final DTreeMixedComputer persisted = (DTreeMixedComputer) repo.find(calc.getId());

        Assert.assertNull(persisted);
    }

    @Test
    public void search() {
        // WHEN
        final List list = repo.search(DTN, Collections.emptyList(), Collections.emptyList());

        // THEN
        Assert.assertNotNull(list);
        Assert.assertEquals(13, list.size());
    }

    @Test
    public void shouldSaveMixed() {
        // Given
        final Template template = entityManager.getReference(Template.class, 0);
        final Group group = entityManager.getReference(Group.class, DTN);
        final DTreeLocalNode local1 = new DTreeLocalNode(group);
        {
            local1.setScript("c = 'Test'");
            local1.setLabel("C");
            local1.setDescription("Test c");
            local1.setTemplate(template);
        }

        final DTreeLocalNode local2 = new DTreeLocalNode(group);
        {
            local2.setScript("d = 'Test'");
            local2.setLabel("D");
            local2.setDescription("Test d");
            local2.setTemplate(template);
        }

        final DTreeComputerNode link = new DTreeComputerNode();
        {
            link.setComputer(repo.find(101));
        }

        final DTreeDefaultNode defDecision = new DTreeDefaultNode();

        final DTreeDeciderNode linkDecision = new DTreeDeciderNode();
        {
            linkDecision.setNegated(false);
            linkDecision.setDecider(entityManager.getReference(BTreePlainDecider.class, 20L));
        }

        final DTreeDecisionNode localDecision = new DTreeDecisionNode(group);
        {
            localDecision.setNegated(false);
            localDecision.setScript("a");
            localDecision.setLabel("Local a");
            localDecision.setDescription("Local a");
            localDecision.setTemplate(template);
        }

        final DTreeMixedComputer calc = new DTreeMixedComputer(group);
        {
            calc.setLabel("DTree");
            calc.setDescription("Decision tree");
            calc.addChild(linkDecision, 0);
            calc.addChild(localDecision, 1);
            calc.addChild(defDecision, 2);
        }

        linkDecision.addChild(link, 0);
        localDecision.addChild(local1, 0);
        defDecision.addChild(local2, 0);

        // When
        repo.persist(calc);
        entityManager.flush();
        entityManager.clear();

        // Then
        final DTreeMixedComputer tree = (DTreeMixedComputer) repo.find(calc.getId());

        assertThat(tree).isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("label", calc.getLabel())
                .hasFieldOrPropertyWithValue("depth", 0)
                .hasFieldOrPropertyWithValue("children.size", 3);

        final Iterator<IChildNode> it = tree.getChildren().iterator();
        assertThat(it.next())
                .isInstanceOf(DTreeDeciderNode.class)
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("negated", linkDecision.isNegated())
                .hasFieldOrPropertyWithValue("decider.id", linkDecision.getDecider().getId())
                .hasFieldOrPropertyWithValue("depth", 1)
                .hasFieldOrPropertyWithValue("order", 0)
                .hasFieldOrPropertyWithValue("children.size", 1)

                .extracting(d -> d.getChildren().iterator().next())
                .isInstanceOf(DTreeComputerNode.class)
                .hasFieldOrPropertyWithValue("computer.id", link.getComputer().getId())
                .hasFieldOrPropertyWithValue("depth", 2)
                .hasFieldOrPropertyWithValue("order", 0)
                .hasFieldOrPropertyWithValue("children.size", 0);

        assertThat(it.next())
                .isInstanceOf(DTreeDecisionNode.class)
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("negated", localDecision.isNegated())
                //.hasFieldOrPropertyWithValue("script", localDecision.getScript())
                .hasFieldOrPropertyWithValue("label", localDecision.getLabel())
                //.hasFieldOrPropertyWithValue("description", localDecision.getDescription())
                .hasFieldOrPropertyWithValue("group.id", localDecision.getGroup().getId())
                //.hasFieldOrPropertyWithValue("decider.script", localDecision.getScript())
                .hasFieldOrPropertyWithValue("depth", 1)
                .hasFieldOrPropertyWithValue("order", 1)
                .hasFieldOrPropertyWithValue("children.size", 1)

                .extracting(d -> d.getChildren().iterator().next())
                .isInstanceOf(DTreeLocalNode.class)
                //.hasFieldOrPropertyWithValue("script", local1.getScript())
                .hasFieldOrPropertyWithValue("label", local1.getLabel())
                //.hasFieldOrPropertyWithValue("description", local1.getDescription())
                .hasFieldOrPropertyWithValue("group.id", local1.getGroup().getId())
                //.hasFieldOrPropertyWithValue("computer.script", local1.getScript())
                .hasFieldOrPropertyWithValue("depth", 2)
                .hasFieldOrPropertyWithValue("order", 0)
                .hasFieldOrPropertyWithValue("children.size", 0);

        assertThat(it.next())
                .isInstanceOf(DTreeDefaultNode.class)
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("depth", 1)
                .hasFieldOrPropertyWithValue("order", 2)
                .hasFieldOrPropertyWithValue("children.size", 1)

                .extracting(d -> d.getChildren().iterator().next())
                .isInstanceOf(DTreeLocalNode.class)
                //.hasFieldOrPropertyWithValue("script", local2.getScript())
                .hasFieldOrPropertyWithValue("label", local2.getLabel())
                //.hasFieldOrPropertyWithValue("description", local2.getDescription())
                .hasFieldOrPropertyWithValue("group.id", local2.getGroup().getId())
                //.hasFieldOrPropertyWithValue("computer.script", local2.getScript())
                .hasFieldOrPropertyWithValue("depth", 2)
                .hasFieldOrPropertyWithValue("order", 0)
                .hasFieldOrPropertyWithValue("children.size", 0);
    }

    @Test
    public void shouldFindByKey() {
        // When
        final DTreeComputer stored = repo.find(2, "Pay");

        // Then
        assertThat(stored)
                .isNotNull()
                .hasFieldOrPropertyWithValue("group.id", 2)
                .hasFieldOrPropertyWithValue("label", "Pay");
    }

    @Test
    public void shouldFindNull() {
        // When
        final DTreeComputer stored = repo.find(4, null);

        // Then
        assertThat(stored).isNull();
    }

    @Test
    public void shouldNotFindLocalByKey() {
        // When
        final DTreeComputer stored = repo.find(2, "TND");

        // Then
        assertThat(stored).isNull();
    }

    @Test
    public void shouldNotFindLocal() {
        // When
        final DTreeComputer stored = repo.find(24);

        // Then
        assertThat(stored).isNull();
    }

    @Test
    public void testEquality() {
        // WHEN
        final Group group = TestUtil.BTN_SPACE;
        final Group group2 = TestUtil.DTN_SPACE;
        final DTreePlainComputer plain = new DTreePlainComputer(group);
        {
            plain.setLabel("Test");
        }

        // THEN
        Assert.assertEquals(plain.hashCode(), plain.hashCode());
        final DTreeMixedComputer m1 = new DTreeMixedComputer(group);
        final DTreeMixedComputer m2 = new DTreeMixedComputer(group2);
        final DTreeMixedComputer m3 = new DTreeMixedComputer(group2);
        {
            plain.setLabel("Test");
            plain.setLabel("Test2");
            plain.setLabel("Test2");
        }

        assertThat(plain)
                .isEqualTo(plain)
                .isNotEqualTo(m1)
                .isNotEqualTo(m2)
                .isNotEqualTo(m3)
                .isNotEqualTo(new Object());
    }

    @Test
    public void testChildEquality() {
        // WHEN
        final Group group = entityManager.getReference(Group.class, DTN);
        final DTreeMixedComputer root = new DTreeMixedComputer(group);
        final DTreeDecisionNode operand = new DTreeDecisionNode(group);
        {
            operand.setOrder(0);
            root.addChild(operand, 0);
        }
        final DTreeChildNode link = new DTreeComputerNode();
        {
            link.setParent(operand);
            link.setOrder(0);
        }
        final DTreeChildNode local = new DTreeLocalNode(group);
        {
            local.setParent(operand);
            local.setOrder(0);
        }

        final DTreeChildNode local1 = new DTreeLocalNode(group);
        {
            local1.setParent(operand);
            local1.setOrder(1);
        }

        // THEN
        Assert.assertEquals(link, link);
        Assert.assertEquals(link.hashCode(), link.hashCode());
        Assert.assertEquals(link, local);
        Assert.assertNotEquals(link, operand);
        Assert.assertNotEquals(local, local1);
        Assert.assertNotEquals(local, new Object());
    }
}
