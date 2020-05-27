package io.github.up2jakarta.dte.jpa.repositories;

import io.github.up2jakarta.dte.jpa.entities.btn.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.jpa.TestConfiguration;
import io.github.up2jakarta.dte.jpa.TestUtil;
import io.github.up2jakarta.dte.jpa.api.btree.IChildNode;
import io.github.up2jakarta.dte.jpa.entities.BTreeDecider;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.Template;
import io.github.up2jakarta.dte.jpa.entities.btn.*;
import io.github.up2jakarta.dte.jpa.views.VDecider;

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
        "spring.datasource.data=classpath:groups.sql, classpath:deciders.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:deciders_repo;MODE=PostgreSQL;"
})
public class DeciderRepositoryTests {

    private static final int BTN = 4;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DeciderRepository repo;

    @Test
    public void shouldSave() {
        // GIVEN
        final Group group = entityManager.getReference(Group.class, BTN);
        final Template template = entityManager.getReference(Template.class, 0);
        final String label = "S1";
        final String script = "a == b";
        final String description = "Simple equality decider";

        // WHEN
        final BTreePlainDecider bool = new BTreePlainDecider(group);
        bool.setScript(script);
        bool.setDescription(description);
        bool.setLabel(label);
        bool.setTemplate(template);
        bool.setShared(false);
        bool.setNegated(true);
        repo.persist(bool);
        entityManager.flush();
        entityManager.clear();

        // THEN
        final BTreePlainDecider persisted = (BTreePlainDecider) repo.find(bool.getId());

        Assert.assertEquals(label, persisted.getLabel());
        Assert.assertEquals(script, persisted.getScript());
        Assert.assertEquals(description, persisted.getDescription());
        Assert.assertFalse(persisted.isShared());
        Assert.assertTrue(persisted.isNegated());
        Assert.assertEquals(template.getId(), persisted.getTemplate().getId());
    }

    @Test
    public void shouldUpdate() {
        // GIVEN
        final String label = "S2";
        final String script = "a == b";
        final String description = "Simple equality decider";

        // WHEN
        final BTreePlainDecider bool = (BTreePlainDecider) repo.find(12);
        entityManager.detach(bool);

        bool.setLabel(label);
        bool.setScript(script);
        bool.setDescription(description);
        repo.merge(bool);
        entityManager.flush();
        entityManager.clear();

        // THEN
        final BTreePlainDecider persisted = (BTreePlainDecider) repo.find(bool.getId());

        Assert.assertEquals(label, persisted.getLabel());
        Assert.assertEquals(script, persisted.getScript());
        Assert.assertEquals(description, persisted.getDescription());
    }

    @Test
    public void shouldDelete() {
        // WHEN
        final BTreePlainDecider bool = (BTreePlainDecider) repo.find(11);
        repo.remove(bool);
        entityManager.flush();
        entityManager.clear();

        // THEN
        final BTreePlainDecider persisted = (BTreePlainDecider) repo.find(bool.getId());

        Assert.assertNull(persisted);
    }

    @Test
    public void shouldUpdateMixed() {
        // GIVEN
        final String label = "Complex";
        final String description = "Simple equality decider";
        final Operator operator = Operator.AND;

        // WHEN
        final BTreeMixedDecider bool = (BTreeMixedDecider) repo.find(100);
        entityManager.detach(bool);

        bool.setLabel(label);
        bool.setOperator(operator);
        bool.setDescription(description);
        repo.merge(bool);
        entityManager.flush();
        entityManager.clear();

        // THEN
        final BTreeMixedDecider persisted = (BTreeMixedDecider) repo.find(bool.getId());

        Assert.assertEquals(label, persisted.getLabel());
        Assert.assertEquals(operator, persisted.getOperator());
        Assert.assertEquals(description, persisted.getDescription());

        Assert.assertEquals(Integer.valueOf(0), persisted.getDepth());
    }

    @Test
    public void shouldDeleteMixed() {
        // WHEN
        final BTreeMixedDecider bool = (BTreeMixedDecider) repo.find(300);
        repo.remove(bool);
        entityManager.flush();
        entityManager.clear();

        // THEN
        final BTreeMixedDecider persisted = (BTreeMixedDecider) repo.find(bool.getId());

        Assert.assertNull(persisted);
    }

    @Test
    public void search() {
        // WHEN
        //entityManager.createNativeQuery("select * from information_schema.views;").getResultList();
        final List<VDecider> list = repo.search(BTN, Collections.emptyList());

        // THEN
        Assert.assertNotNull(list);
        Assert.assertEquals(9, list.size());
    }

    @Test
    public void shouldSaveMixed() {
        // Given
        final Template template = entityManager.getReference(Template.class, 0);
        final Group group = entityManager.getReference(Group.class, BTN);
        final BTreeLocalNode c = new BTreeLocalNode(group);
        {
            c.setNegated(false);
            c.setScript("c");
            c.setLabel("C");
            c.setDescription("Boolean c");
            c.setTemplate(template);
        }

        final BTreeLocalNode d = new BTreeLocalNode(group);
        {
            d.setNegated(false);
            d.setScript("d");
            d.setLabel("D");
            d.setDescription("Boolean d");
            d.setTemplate(template);
        }

        final BTreeOperatorNode left = new BTreeOperatorNode();
        {
            left.setOperator(Operator.OR);
            left.setNegated(true);
        }

        final BTreeDeciderNode a = new BTreeDeciderNode();
        {
            a.setNegated(false);
            a.setDecider(repo.find(12));
        }

        final BTreeDeciderNode b = new BTreeDeciderNode();
        {
            b.setNegated(false);
            b.setDecider(repo.find(13));
        }

        final BTreeOperatorNode right = new BTreeOperatorNode();
        {
            right.setOperator(Operator.AND);
            right.setNegated(true);
        }

        final BTreeMixedDecider bool = new BTreeMixedDecider(group);
        {
            bool.addOperand(left, 0);
            bool.addOperand(right, 1);
            bool.setLabel("!(c && d) ^ (b || a)");
            bool.setDescription("not(c and d xor not(a or b)");
            bool.setNegated(false);
            bool.setOperator(Operator.XOR);
        }

        left.addOperand(c, 0);
        left.addOperand(d, 1);
        right.addOperand(a, 0);
        right.addOperand(b, 1);

        // When
        repo.persist(bool);
        entityManager.flush();
        entityManager.clear();

        // Then
        final BTreeMixedDecider tree = (BTreeMixedDecider) repo.find(bool.getId());

        assertThat(tree).isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("label", bool.getLabel())
                .hasFieldOrPropertyWithValue("description", bool.getDescription())
                .hasFieldOrPropertyWithValue("negated", bool.isNegated())
                .hasFieldOrPropertyWithValue("operator", bool.getOperator())
                .hasFieldOrPropertyWithValue("depth", 0)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        Iterator<IChildNode> it = tree.getOperands().iterator();
        final IChildNode leftOperand = it.next();
        final IChildNode rightOperand = it.next();
        assertThat(leftOperand)
                .isInstanceOf(BTreeOperatorNode.class)
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("negated", left.isNegated())
                .hasFieldOrPropertyWithValue("operator", left.getOperator())
                .hasFieldOrPropertyWithValue("depth", 1)
                .hasFieldOrPropertyWithValue("order", 0)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        assertThat(rightOperand)
                .isInstanceOf(BTreeOperatorNode.class)
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("negated", right.isNegated())
                .hasFieldOrPropertyWithValue("operator", right.getOperator())
                .hasFieldOrPropertyWithValue("depth", 1)
                .hasFieldOrPropertyWithValue("order", 1)
                .hasFieldOrPropertyWithValue("operands.size", 2);

        it = ((BTreeOperatorNode) leftOperand).getOperands().iterator();
        assertThat(it.next())
                .isInstanceOf(BTreeLocalNode.class)
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("negated", c.isNegated())
                .hasFieldOrPropertyWithValue("script", c.getScript())
                .hasFieldOrPropertyWithValue("label", c.getLabel())
                .hasFieldOrPropertyWithValue("description", c.getDescription())
                .hasFieldOrPropertyWithValue("group.id", d.getGroup().getId())
                .hasFieldOrPropertyWithValue("depth", 2)
                .hasFieldOrPropertyWithValue("order", 0);

        assertThat(it.next())
                .isInstanceOf(BTreeLocalNode.class)
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("negated", d.isNegated())
                .hasFieldOrPropertyWithValue("script", d.getScript())
                .hasFieldOrPropertyWithValue("label", d.getLabel())
                .hasFieldOrPropertyWithValue("description", d.getDescription())
                .hasFieldOrPropertyWithValue("group.id", d.getGroup().getId())
                .hasFieldOrPropertyWithValue("depth", 2)
                .hasFieldOrPropertyWithValue("order", 1);

        it = ((BTreeOperatorNode) rightOperand).getOperands().iterator();
        assertThat(it.next())
                .isInstanceOf(BTreeDeciderNode.class)
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("negated", a.isNegated())
                .hasFieldOrPropertyWithValue("decider.id", a.getDecider().getId())
                .hasFieldOrPropertyWithValue("depth", 2)
                .hasFieldOrPropertyWithValue("order", 0);

        assertThat(it.next())
                .isInstanceOf(BTreeDeciderNode.class)
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("negated", b.isNegated())
                .hasFieldOrPropertyWithValue("decider.id", b.getDecider().getId())
                .hasFieldOrPropertyWithValue("depth", 2)
                .hasFieldOrPropertyWithValue("order", 1);
    }

    @Test
    public void shouldFindByKey() {
        // When
        final BTreeDecider stored = repo.find(4, "A&B");

        // Then
        assertThat(stored)
                .isNotNull()
                .hasFieldOrPropertyWithValue("group.id", 4)
                .hasFieldOrPropertyWithValue("label", "A&B");
    }

    @Test
    public void shouldFindNull() {
        // When
        final BTreeDecider stored = repo.find(4, null);

        // Then
        assertThat(stored).isNull();
    }

    @Test
    public void shouldNotFindLocalByKey() {
        // When
        final BTreeDecider stored = repo.find(4, "LB");

        // Then
        assertThat(stored).isNull();
    }

    @Test
    public void shouldNotFindLocal() {
        // When
        final BTreeDecider stored = repo.find(502);

        // Then
        assertThat(stored).isNull();
    }

    @Test
    public void testEquality() {
        // WHEN
        final Group group = TestUtil.BTN_SPACE;
        final Group group2 = TestUtil.DTN_SPACE;
        final BTreePlainDecider plain = new BTreePlainDecider(group);
        {
            plain.setLabel("Test");
        }

        // THEN
        Assert.assertEquals(plain, plain);
        Assert.assertEquals(plain.hashCode(), plain.hashCode());

        BTreePlainDecider other = new BTreePlainDecider(group);
        {
            plain.setLabel("Test2");
        }
        Assert.assertNotEquals(plain, other);

        other = new BTreePlainDecider(group2);
        {
            plain.setLabel("Test");
        }
        Assert.assertNotEquals(plain, other);
        Assert.assertNotEquals(plain, new BTreeMixedDecider(group2) {{
            setLabel("Test2");
        }});
        Assert.assertNotEquals(plain, new Object());
    }

    @Test
    public void testChildEquality() {
        // WHEN
        final Group group = entityManager.getReference(Group.class, BTN);
        final BTreeOperatorNode operand = new BTreeOperatorNode();
        final BTreeChildNode link = new BTreeDeciderNode();
        final BTreeChildNode local = new BTreeLocalNode(group);
        {
            operand.setOrder(0);

            link.setParent(operand);
            link.setOrder(0);

            link.setParent(operand);
            link.setOrder(0);
        }

        // THEN
        Assert.assertEquals(link, link);
        Assert.assertEquals(link.hashCode(), link.hashCode());
        //Assert.assertEquals(link, local);
        Assert.assertNotEquals(link, operand);
        //Assert.assertNotEquals(local, local1);
        Assert.assertNotEquals(local, new Object());
    }

}
