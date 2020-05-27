package io.github.up2jakarta.dte.jpa.managers;

import io.github.up2jakarta.dte.jpa.entities.btn.*;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.jpa.TestConfiguration;
import io.github.up2jakarta.dte.jpa.TestUtil;
import io.github.up2jakarta.dte.jpa.api.btree.IChildNode;
import io.github.up2jakarta.dte.jpa.entities.BTreeDecider;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.Template;
import io.github.up2jakarta.dte.jpa.entities.btn.*;
import io.github.up2jakarta.dte.jpa.models.Decider;
import io.github.up2jakarta.dte.jpa.repositories.DeciderRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static io.github.up2jakarta.dte.jpa.TestUtil.setId;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql, classpath:deciders.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:deciders_mng;MODE=PostgreSQL;"
})
@Transactional
@SuppressWarnings("unchecked")
public class DeciderManagerTests {

    private static final Template TEMPLATE = TestUtil.BASE_TEMPLATE;
    private static final Group GROUP = TestUtil.BTN_SPACE;
    private static final BTreePlainDecider PLAIN = new BTreePlainDecider(GROUP);
    private static final BTreeDeciderNode LINK = new BTreeDeciderNode();
    private static final BTreeLocalNode LOCAL = new BTreeLocalNode(GROUP);
    private static final BTreeOperatorNode OPERAND = new BTreeOperatorNode();
    private static final BTreeMixedDecider MIXED = new BTreeMixedDecider(GROUP);

    @Autowired
    private DeciderManager manager;
    @Autowired
    private DeciderRepository repository;

    @BeforeClass
    public static void init() {
        {
            setId(GROUP, 1);
            setId(PLAIN, 1);
            setId(MIXED, 2);
            setId(LOCAL, 3);
            setId(LINK, 4);
            setId(OPERAND, 5);
        }
        {
            MIXED.setLabel("Mixed");
            MIXED.setOperator(Operator.OR);
            MIXED.addOperand(LINK, 0);
            MIXED.addOperand(LOCAL, 1);
            MIXED.addOperand(OPERAND, 2);

            PLAIN.setScript("a == b");
            PLAIN.setLabel("Plain");
            PLAIN.setTemplate(TEMPLATE);

            LINK.setDecider(PLAIN);

            LOCAL.setLabel("Local");
            LOCAL.setScript("c == d");
            LOCAL.setNegated(true);
            LOCAL.setTemplate(TEMPLATE);

            OPERAND.setOperator(Operator.OR);
        }
    }

    @Test
    public void shouldConvertPlain() {
        // When
        final Decider model = manager.convert(PLAIN);

        // Then
        assertThat(model)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", PLAIN.getId())
                .hasFieldOrPropertyWithValue("label", PLAIN.getLabel())
                .hasFieldOrPropertyWithValue("description", PLAIN.getDescription())
                .hasFieldOrPropertyWithValue("groupId", PLAIN.getGroup().getId())
                .hasFieldOrPropertyWithValue("script", PLAIN.getScript())
                .hasFieldOrPropertyWithValue("shared", PLAIN.isShared())
                .hasFieldOrPropertyWithValue("templateId", PLAIN.getTemplate().getId())
                .hasFieldOrPropertyWithValue("type", Decider.Type.PLAIN);
    }

    @Test
    public void shouldConvertMixed() {
        // When
        final Decider model = manager.convert(MIXED);

        // Then
        final AbstractListAssert listAssert = assertThat(model)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", MIXED.getId())
                .hasFieldOrPropertyWithValue("label", MIXED.getLabel())
                .hasFieldOrPropertyWithValue("description", MIXED.getDescription())
                .hasFieldOrPropertyWithValue("negated", MIXED.isNegated())
                .hasFieldOrPropertyWithValue("shared", MIXED.isShared())
                .hasFieldOrPropertyWithValue("type", Decider.Type.MIXED)
                .extracting(Decider::getOperands)
                .asList()
                .isNotNull()
                .hasSize(3);

        ((ObjectAssert) listAssert.element(0))
                .hasFieldOrPropertyWithValue("id", LINK.getId())
                .hasFieldOrPropertyWithValue("label", LINK.getLabel())
                .hasFieldOrPropertyWithValue("description", LINK.getDecider().getDescription())
                .hasFieldOrPropertyWithValue("deciderId", LINK.getDecider().getId())
                .hasFieldOrPropertyWithValue("negated", LINK.isNegated())
                .hasFieldOrPropertyWithValue("operands", null)
                .hasFieldOrPropertyWithValue("type", Decider.Type.DECIDER);
        ((ObjectAssert) listAssert.element(1))
                .hasFieldOrPropertyWithValue("id", LOCAL.getId())
                .hasFieldOrPropertyWithValue("label", LOCAL.getLabel())
                .hasFieldOrPropertyWithValue("description", LOCAL.getDescription())
                .hasFieldOrPropertyWithValue("script", LOCAL.getScript())
                .hasFieldOrPropertyWithValue("negated", LOCAL.isNegated())
                .hasFieldOrPropertyWithValue("shared", LOCAL.isShared())
                .hasFieldOrPropertyWithValue("templateId", LOCAL.getTemplate().getId())
                .hasFieldOrPropertyWithValue("operands", null)
                .hasFieldOrPropertyWithValue("type", Decider.Type.LOCAL);
        ((ObjectAssert) listAssert.element(2))
                .hasFieldOrPropertyWithValue("id", OPERAND.getId())
                .hasFieldOrPropertyWithValue("label", OPERAND.getLabel())
                .hasFieldOrPropertyWithValue("operator", OPERAND.getOperator())
                .hasFieldOrPropertyWithValue("negated", OPERAND.isNegated())
                .hasFieldOrPropertyWithValue("operands", null)
                .hasFieldOrPropertyWithValue("type", Decider.Type.OPERATOR);

    }

    @Test(expected = ClassCastException.class)
    public void shouldNotConvert() {
        // Given
        final BTreeDecider entity = new BTreeDecider(GROUP, 0) {
        };

        // When
        manager.convert(entity);
    }

    @Test
    public void shouldNotUpdateChildGroup() {
        // Given
        final Decider model = new Decider(Decider.Type.DECIDER);
        {
            model.setLabel("Decider");
            model.setNegated(false);
            model.setShared(false);
            model.setGroupId(1);
        }
        final BTreeDecider entity = new BTreeDecider(null, 0) {
        };

        // When
        manager.update(entity, model);

        // Then
        Assert.assertNull(entity.getGroup());
    }

    @Test
    public void shouldCreatePlain() {
        // Given
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setScript("a == b");
            model.setLabel("Plain");
            model.setNegated(false);
            model.setShared(false);
            model.setTemplateId(0);
            model.setGroupId(1);
        }

        // When
        final BTreeDecider entity = manager.create(model);

        // Then
        assertThat(entity)
                .isNotNull()
                .isInstanceOf(BTreePlainDecider.class)
                .hasFieldOrPropertyWithValue("label", model.getLabel())
                .hasFieldOrPropertyWithValue("description", model.getDescription())
                .hasFieldOrPropertyWithValue("script", model.getScript())
                .hasFieldOrPropertyWithValue("negated", model.getNegated())
                .hasFieldOrPropertyWithValue("shared", model.getShared())
                .hasFieldOrPropertyWithValue("template.id", model.getTemplateId())
                .hasFieldOrPropertyWithValue("group.id", model.getGroupId());
    }

    @Test
    public void shouldCreateMixed() {
        // Given
        final Decider local = new Decider(Decider.Type.LOCAL);
        {
            local.setScript("a == b");
            local.setLabel("Plain");
            local.setNegated(false);
            local.setShared(false);
            local.setTemplateId(0);
            local.setGroupId(4);
        }
        final Decider link = new Decider(Decider.Type.DECIDER);
        {
            link.setLabel("Decider");
            link.setDeciderId(11L);
            link.setNegated(false);
            link.setGroupId(4);
        }
        final Decider operator = new Decider(Decider.Type.OPERATOR);
        {
            operator.setLabel("OR");
            operator.setDeciderId(11L);
            operator.setOperator(Operator.OR);
            operator.setNegated(false);
            operator.setGroupId(4);
        }

        final Decider model = new Decider(Decider.Type.MIXED);
        {
            model.setLabel("Test");
            model.setDescription("Test Post");
            model.setOperator(Operator.OR);
            model.setNegated(false);
            model.setShared(false);
            model.setGroupId(4);
            model.setOperands(Arrays.asList(link, local, operator));
        }

        // When
        final BTreeDecider entity = manager.create(model);

        // Then
        final AbstractListAssert listAssert = assertThat(entity)
                .isNotNull()
                .isInstanceOf(BTreeMixedDecider.class)
                .extracting(e -> (BTreeMixedDecider) e)
                .hasFieldOrPropertyWithValue("label", model.getLabel())
                .hasFieldOrPropertyWithValue("description", model.getDescription())
                .hasFieldOrPropertyWithValue("negated", model.getNegated())
                .hasFieldOrPropertyWithValue("shared", model.getShared())
                .hasFieldOrPropertyWithValue("group.id", model.getGroupId())
                .hasFieldOrPropertyWithValue("operator", model.getOperator())
                .extracting(e -> new ArrayList(e.getOperands()))
                .asList()
                .hasSize(3);

        ((ObjectAssert<ObjectAssert>) listAssert.element(0))
                .isNotNull()
                .isInstanceOf(BTreeDeciderNode.class)
                .hasFieldOrPropertyWithValue("decider.id", link.getDeciderId())
                .hasFieldOrPropertyWithValue("negated", link.getNegated())
                .hasFieldOrPropertyWithValue("label", link.getLabel());

        ((ObjectAssert<ObjectAssert>) listAssert.element(1)).isNotNull()
                .isInstanceOf(BTreeLocalNode.class)
                .hasFieldOrPropertyWithValue("label", local.getLabel())
                .hasFieldOrPropertyWithValue("description", local.getDescription())
                .hasFieldOrPropertyWithValue("script", local.getScript())
                .hasFieldOrPropertyWithValue("negated", local.getNegated())
                .hasFieldOrPropertyWithValue("shared", local.getShared())
                .hasFieldOrPropertyWithValue("template.id", local.getTemplateId());

        ((ObjectAssert<ObjectAssert>) listAssert.element(2)).isNotNull()
                .isInstanceOf(BTreeOperatorNode.class)
                .hasFieldOrPropertyWithValue("operator", operator.getOperator())
                .hasFieldOrPropertyWithValue("negated", operator.getNegated())
                .hasFieldOrPropertyWithValue("label", operator.getLabel());

    }

    @Test
    public void shouldUpdateMixed() {
        // Given
        final Decider local = new Decider(Decider.Type.LOCAL);
        {
            local.setId(204L);
            local.setScript("a == b");
            local.setLabel("Plain");
            local.setNegated(false);
            local.setShared(false);
            local.setTemplateId(0);
        }
        final Decider link = new Decider(Decider.Type.DECIDER);
        {
            link.setLabel("Decider");
            link.setId(203L);
            link.setDeciderId(11L);
            link.setNegated(false);
        }

        final Decider model = new Decider(Decider.Type.MIXED);
        {
            model.setLabel("Test");
            model.setDescription("Test Post");
            model.setOperator(Operator.OR);
            model.setNegated(false);
            model.setShared(false);
            model.setOperands(Arrays.asList(link, local));
            local.setGroupId(4);
        }
        final BTreeDecider entity = repository.find(200L);

        // When
        manager.update(entity, model);

        // Then
        final AbstractListAssert listAssert = assertThat(entity)
                .isInstanceOf(BTreeMixedDecider.class)
                .extracting(e -> (BTreeMixedDecider) e)
                .hasFieldOrPropertyWithValue("label", model.getLabel())
                .hasFieldOrPropertyWithValue("description", model.getDescription())
                .hasFieldOrPropertyWithValue("negated", model.getNegated())
                .hasFieldOrPropertyWithValue("shared", model.getShared())
                .hasFieldOrPropertyWithValue("operator", model.getOperator())
                .extracting(e -> new ArrayList<>(e.getOperands()))
                .asList()
                .hasSize(2);

        ((ObjectAssert<ObjectAssert>) listAssert.element(0))
                .isNotNull()
                .isInstanceOf(BTreeDeciderNode.class)
                .hasFieldOrPropertyWithValue("id", link.getId())
                .hasFieldOrPropertyWithValue("decider.id", link.getDeciderId())
                .hasFieldOrPropertyWithValue("negated", link.getNegated())
                .hasFieldOrPropertyWithValue("label", link.getLabel());

        ((ObjectAssert<ObjectAssert>) listAssert.element(1)).isNotNull()
                .isInstanceOf(BTreeLocalNode.class)
                .hasFieldOrPropertyWithValue("id", null)
                .hasFieldOrPropertyWithValue("label", local.getLabel())
                .hasFieldOrPropertyWithValue("description", local.getDescription())
                .hasFieldOrPropertyWithValue("script", local.getScript())
                .hasFieldOrPropertyWithValue("negated", local.getNegated())
                .hasFieldOrPropertyWithValue("shared", local.getShared())
                .hasFieldOrPropertyWithValue("template.id", local.getTemplateId());

    }

    @Test(expected = ClassCastException.class)
    public void shouldNotConvertChild() {
        // Given
        final IChildNode child = new BTreeChildNode() {
        };
        final BTreeMixedDecider root = new BTreeMixedDecider(GROUP);
        {
            root.setOperator(Operator.OR);
            root.addOperand(child, 0);
        }

        // When
        setId((BTreeNode) child, 101L);
        setId(root, 100L);
        manager.convert(root);
    }

    @Test
    public void shouldCleanChildrenWhenEmpty() {
        // Given
        final Decider model = new Decider(Decider.Type.MIXED);
        {
            model.setLabel("Test");
            model.setOperator(Operator.OR);
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            model.setTemplateId(0);
            model.setOperands(Collections.emptyList());
        }
        final BTreeMixedDecider entity = new BTreeMixedDecider(GROUP);
        {
            entity.setOperator(Operator.OR);
            entity.addOperand(new BTreeDeciderNode(), 0);
        }

        // When
        manager.update(entity, model);

        //Then
        Assert.assertEquals(0, entity.getOperands().size());
    }

    @Test
    public void shouldCleanChildrenWhenNull() {
        // Given
        final Decider model = new Decider(Decider.Type.MIXED);
        {
            model.setLabel("Test");
            model.setOperator(Operator.OR);
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            model.setTemplateId(0);
            model.setOperands(null);
        }
        final BTreeMixedDecider entity = new BTreeMixedDecider(GROUP);
        {
            entity.setOperator(Operator.OR);
            entity.addOperand(new BTreeDeciderNode(), 0);
        }

        // When
        manager.update(entity, model);

        //Then
        Assert.assertEquals(0, entity.getOperands().size());
    }

}
