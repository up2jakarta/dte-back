package io.github.up2jakarta.dte.jpa.managers;

import io.github.up2jakarta.dte.jpa.entities.dtn.*;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import io.github.up2jakarta.dte.jpa.TestConfiguration;
import io.github.up2jakarta.dte.jpa.TestUtil;
import io.github.up2jakarta.dte.jpa.entities.DTreeComputer;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.Template;
import io.github.up2jakarta.dte.jpa.entities.btn.BTreePlainDecider;
import io.github.up2jakarta.dte.jpa.entities.dtn.*;
import io.github.up2jakarta.dte.jpa.models.Computer;
import io.github.up2jakarta.dte.jpa.repositories.ComputerRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static io.github.up2jakarta.dte.jpa.TestUtil.DTN_SPACE;
import static io.github.up2jakarta.dte.jpa.TestUtil.setId;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql, classpath:computers.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:computers_mng;MODE=PostgreSQL;"
})
@Transactional
@SuppressWarnings("unchecked")
public class ComputerManagerTests {

    private static final Template TEMPLATE = TestUtil.BASE_TEMPLATE;
    private static final Group GROUP = DTN_SPACE;
    private static final BTreePlainDecider REF_DECIDER = new BTreePlainDecider(GROUP);
    private static final DTreePlainComputer PLAIN = new DTreePlainComputer(GROUP);
    private static final DTreeComputerNode COMPUTER = new DTreeComputerNode();
    private static final DTreeLocalNode LOCAL = new DTreeLocalNode(GROUP);
    private static final DTreeDefaultNode ANY = new DTreeDefaultNode();
    private static final DTreeDecisionNode DECISION = new DTreeDecisionNode(GROUP);
    private static final DTreeDeciderNode DECIDER = new DTreeDeciderNode();
    private static final DTreeMixedComputer MIXED = new DTreeMixedComputer(GROUP);

    @Autowired
    private ComputerManager manager;
    @Autowired
    private ComputerRepository repository;

    @BeforeClass
    public static void init() {
        {
            setId(GROUP, 2);
            setId(PLAIN, 1);
            setId(MIXED, 2);
            setId(LOCAL, 3);
            setId(COMPUTER, 4);
            setId(ANY, 5);
            setId(DECISION, 6);
            setId(DECIDER, 7);
        }
        {
            REF_DECIDER.setScript("a == b");
            REF_DECIDER.setLabel("Decider");
            REF_DECIDER.setShared(true);
            REF_DECIDER.setTemplate(TEMPLATE);

            PLAIN.setScript("a = b");
            PLAIN.setLabel("Computer");
            PLAIN.setShared(false);
            PLAIN.setTemplate(TEMPLATE);

            COMPUTER.setComputer(PLAIN);
            COMPUTER.setLabel("Computer");

            LOCAL.setLabel("Local");
            LOCAL.setScript("c = d");
            LOCAL.setShared(false);
            LOCAL.setTemplate(TEMPLATE);

            ANY.setLabel("Default");

            DECISION.setLabel("Decision");
            DECISION.setScript("c == d");
            DECISION.setShared(false);
            DECISION.setTemplate(TEMPLATE);

            DECIDER.setLabel("Decider");
            DECIDER.setDecider(REF_DECIDER);

            MIXED.setLabel("Mixed");
            PLAIN.setShared(true);

            MIXED.addChild(DECIDER, 0);
            MIXED.addChild(DECISION, 1);
            MIXED.addChild(ANY, 2);

            DECIDER.addChild(COMPUTER, 0);
            DECISION.addChild(LOCAL, 0);
        }
    }

    @Test
    public void shouldConvertPlain() {
        // When
        final Computer model = manager.convert(PLAIN);

        // Then
        assertThat(model)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", PLAIN.getId())
                .hasFieldOrPropertyWithValue("label", PLAIN.getLabel())
                .hasFieldOrPropertyWithValue("description", PLAIN.getDescription())
                .hasFieldOrPropertyWithValue("groupId", PLAIN.getGroup().getId())
                .hasFieldOrPropertyWithValue("script", PLAIN.getScript())
                .hasFieldOrPropertyWithValue("templateId", PLAIN.getTemplate().getId())
                .hasFieldOrPropertyWithValue("shared", PLAIN.isShared())
                .hasFieldOrPropertyWithValue("type", Computer.Type.PLAIN);
    }

    @Test
    public void shouldConvertMixed() {
        // When
        final Computer model = manager.convert(MIXED);

        // Then
        final AbstractListAssert listAssert = assertThat(model)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", MIXED.getId())
                .hasFieldOrPropertyWithValue("label", MIXED.getLabel())
                .hasFieldOrPropertyWithValue("description", MIXED.getDescription())
                .hasFieldOrPropertyWithValue("type", Computer.Type.MIXED)
                .hasFieldOrPropertyWithValue("shared", MIXED.isShared())
                .hasFieldOrPropertyWithValue("groupId", MIXED.getGroup().getId())
                .extracting(Computer::getChildren)
                .asList()
                .isNotNull()
                .hasSize(3);

        ((ObjectAssert) listAssert.element(0))
                .hasFieldOrPropertyWithValue("id", DECIDER.getId())
                .hasFieldOrPropertyWithValue("label", DECIDER.getLabel())
                .hasFieldOrPropertyWithValue("description", DECIDER.getDecider().getDescription())
                .hasFieldOrPropertyWithValue("deciderId", DECIDER.getDecider().getId())
                .hasFieldOrPropertyWithValue("negated", DECIDER.isNegated())
                .hasFieldOrPropertyWithValue("type", Computer.Type.DECIDER)
                .hasFieldOrPropertyWithValue("children.size", 1)

                .extracting(e -> ((Computer) e).getChildren().get(0))
                .hasFieldOrPropertyWithValue("id", COMPUTER.getId())
                .hasFieldOrPropertyWithValue("label", COMPUTER.getLabel())
                .hasFieldOrPropertyWithValue("description", COMPUTER.getComputer().getDescription())
                .hasFieldOrPropertyWithValue("computerId", COMPUTER.getComputer().getId())
                .hasFieldOrPropertyWithValue("children", null)
                .hasFieldOrPropertyWithValue("type", Computer.Type.COMPUTER);

        ((ObjectAssert) listAssert.element(1))
                .hasFieldOrPropertyWithValue("id", DECISION.getId())
                .hasFieldOrPropertyWithValue("label", DECISION.getDecider().getLabel())
                .hasFieldOrPropertyWithValue("description", DECISION.getDecider().getDescription())
                .hasFieldOrPropertyWithValue("negated", DECISION.isNegated())
                .hasFieldOrPropertyWithValue("type", Computer.Type.DECISION)
                .hasFieldOrPropertyWithValue("script", DECISION.getScript())
                .hasFieldOrPropertyWithValue("templateId", DECISION.getTemplate().getId())
                .hasFieldOrPropertyWithValue("shared", DECISION.isShared())
                .hasFieldOrPropertyWithValue("children.size", 1)

                .extracting(e -> ((Computer) e).getChildren().get(0))
                .hasFieldOrPropertyWithValue("id", LOCAL.getId())
                .hasFieldOrPropertyWithValue("label", LOCAL.getLabel())
                .hasFieldOrPropertyWithValue("description", LOCAL.getDescription())
                .hasFieldOrPropertyWithValue("script", LOCAL.getScript())
                .hasFieldOrPropertyWithValue("templateId", LOCAL.getTemplate().getId())
                .hasFieldOrPropertyWithValue("shared", LOCAL.isShared())
                .hasFieldOrPropertyWithValue("children", null)
                .hasFieldOrPropertyWithValue("type", Computer.Type.LOCAL);

        ((ObjectAssert) listAssert.element(2))
                .hasFieldOrPropertyWithValue("id", ANY.getId())
                .hasFieldOrPropertyWithValue("type", Computer.Type.DEFAULT)
                .hasFieldOrPropertyWithValue("children", null)
                .hasFieldOrPropertyWithValue("label", ANY.getLabel());

    }

    @Test(expected = ClassCastException.class)
    public void shouldNotConvert() {
        // Given
        final DTreeComputer entity = new DTreeComputer(null) {
        };

        // When
        manager.convert(entity);
    }

    @Test
    public void shouldNotUpdateChildGroup() {
        // Given
        final Computer model = new Computer(Computer.Type.COMPUTER);
        {
            model.setGroupId(1);
            model.setShared(false);
        }
        final DTreeComputer entity = new DTreeComputer(null) {
        };

        // When
        manager.update(entity, model);

        // Then
        Assert.assertNull(entity.getGroup());
    }

    @Test
    public void shouldCreatePlain() {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setScript("a == b");
            model.setLabel("Plain");
            model.setGroupId(1);
            model.setShared(true);
            model.setTemplateId(0);
        }

        // When
        final DTreeComputer entity = manager.create(model);

        // Then
        assertThat(entity)
                .isNotNull()
                .isInstanceOf(DTreePlainComputer.class)
                .hasFieldOrPropertyWithValue("label", model.getLabel())
                .hasFieldOrPropertyWithValue("description", model.getDescription())
                .hasFieldOrPropertyWithValue("script", model.getScript())
                .hasFieldOrPropertyWithValue("group.id", model.getGroupId())
                .hasFieldOrPropertyWithValue("template.id", model.getTemplateId())
                .hasFieldOrPropertyWithValue("shared", model.getShared());
    }

    @Test
    public void shouldCreateMixed() {
        // Given
        final Computer local = new Computer(Computer.Type.LOCAL);
        {
            local.setScript("a == b");
            local.setLabel("Plain");
            local.setShared(true);
            local.setTemplateId(0);
            local.setGroupId(1);
        }
        final Computer link = new Computer(Computer.Type.COMPUTER);
        {
            link.setComputerId(150L);
            link.setLabel("Link");
        }
        final Computer decider = new Computer(Computer.Type.DECIDER);
        {
            decider.setDeciderId(22L);
            decider.setNegated(false);
            decider.setLabel("Decider");
            decider.setChildren(Collections.singletonList(link));
        }
        final Computer decision = new Computer(Computer.Type.DECISION);
        {
            decision.setLabel("Local");
            decision.setScript("c == d");
            decision.setNegated(true);
            decision.setShared(true);
            decision.setTemplateId(0);
            decision.setGroupId(1);
            decision.setChildren(Collections.singletonList(local));
        }
        final Computer any = new Computer(Computer.Type.DEFAULT);

        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("Test");
            model.setDescription("Test Post");
            model.setGroupId(1);
            model.setShared(false);
            model.setChildren(Arrays.asList(decider, decision, any));
        }

        // When
        final DTreeComputer entity = manager.create(model);

        // Then
        final AbstractListAssert listAssert = assertThat(entity)
                .isNotNull()
                .isInstanceOf(DTreeMixedComputer.class)
                .extracting(e -> (DTreeMixedComputer) e)
                .hasFieldOrPropertyWithValue("label", model.getLabel())
                .hasFieldOrPropertyWithValue("description", model.getDescription())
                .hasFieldOrPropertyWithValue("group.id", model.getGroupId())
                .hasFieldOrPropertyWithValue("shared", model.getShared())
                .extracting(e -> new ArrayList(e.getChildren()))
                .asList()
                .hasSize(3);

        ((ObjectAssert<ObjectAssert<?>>) listAssert.element(0))
                .isNotNull()
                .isInstanceOf(DTreeDeciderNode.class)
                .hasFieldOrPropertyWithValue("decider.id", decider.getDeciderId())
                .hasFieldOrPropertyWithValue("negated", decider.getNegated())
                .hasFieldOrPropertyWithValue("label", decider.getLabel())
                .hasFieldOrPropertyWithValue("children.size", 1);

        ((ObjectAssert<ObjectAssert>) listAssert.element(1)).isNotNull()
                .isInstanceOf(DTreeDecisionNode.class)
                .hasFieldOrPropertyWithValue("label", decision.getLabel())
                .hasFieldOrPropertyWithValue("description", decision.getDescription())
                .hasFieldOrPropertyWithValue("script", decision.getScript())
                .hasFieldOrPropertyWithValue("template.id", decision.getTemplateId())
                .hasFieldOrPropertyWithValue("shared", decision.getShared())
                .hasFieldOrPropertyWithValue("negated", decision.getNegated())
                .hasFieldOrPropertyWithValue("children.size", 1);

        ((ObjectAssert<ObjectAssert>) listAssert.element(2)).isNotNull()
                .isInstanceOf(DTreeDefaultNode.class)
                .hasFieldOrPropertyWithValue("children.size", 0)
                .hasFieldOrPropertyWithValue("label", any.getLabel());

    }

    @Test
    public void shouldUpdateMixed() {
        // Given
        final Computer local = new Computer(Computer.Type.LOCAL);
        {
            local.setScript("a == b");
            local.setLabel("Plain");
            local.setShared(false);
            local.setTemplateId(0);
            local.setGroupId(2);
        }
        final Computer link = new Computer(Computer.Type.COMPUTER);
        {
            link.setLabel("Link");
            link.setId(1002L);
            link.setComputerId(150L);
            link.setChildren(Arrays.asList(local, local));
        }
        final Computer decider = new Computer(Computer.Type.DECIDER);
        {
            decider.setLabel("Decider");
            decider.setDeciderId(22L);
            decider.setNegated(false);
            decider.setChildren(Collections.singletonList(link));
        }
        final Computer decision = new Computer(Computer.Type.DECISION);
        {
            decision.setLabel("Local");
            decision.setScript("c == d");
            decision.setNegated(true);
            decision.setShared(false);
            decision.setTemplateId(0);
            decision.setGroupId(2);
            decision.setChildren(Collections.singletonList(local));
        }
        final Computer any = new Computer(Computer.Type.DEFAULT);
        {
            decision.setLabel("Any");
            any.setId(1006L);
        }

        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("Test");
            model.setDescription("Test Post");
            model.setGroupId(2);
            model.setShared(false);
            model.setChildren(Arrays.asList(decider, decision, any));
        }
        final DTreeComputer entity = repository.find(1000L);

        // When
        manager.update(entity, model);

        // Then
        final AbstractListAssert listAssert = assertThat(entity)
                .isNotNull()
                .isInstanceOf(DTreeMixedComputer.class)
                .extracting(e -> (DTreeMixedComputer) e)
                .hasFieldOrPropertyWithValue("label", model.getLabel())
                .hasFieldOrPropertyWithValue("description", model.getDescription())
                .hasFieldOrPropertyWithValue("group.id", model.getGroupId())
                .hasFieldOrPropertyWithValue("shared", model.getShared())
                .extracting(e -> new ArrayList(e.getChildren()))
                .asList()
                .hasSize(3);

        ((ObjectAssert<ObjectAssert<?>>) listAssert.element(0))
                .isNotNull()
                .isInstanceOf(DTreeDeciderNode.class)
                .hasFieldOrPropertyWithValue("decider.id", decider.getDeciderId())
                .hasFieldOrPropertyWithValue("negated", decider.getNegated())
                .hasFieldOrPropertyWithValue("label", decider.getLabel())
                .hasFieldOrPropertyWithValue("children.size", 1);

        ((ObjectAssert<ObjectAssert>) listAssert.element(1)).isNotNull()
                .isInstanceOf(DTreeDecisionNode.class)
                .hasFieldOrPropertyWithValue("label", decision.getLabel())
                .hasFieldOrPropertyWithValue("description", decision.getDescription())
                .hasFieldOrPropertyWithValue("script", decision.getScript())
                .hasFieldOrPropertyWithValue("negated", decision.getNegated())
                .hasFieldOrPropertyWithValue("shared", decision.getShared())
                .hasFieldOrPropertyWithValue("template.id", decision.getTemplateId())
                .hasFieldOrPropertyWithValue("children.size", 1);

        ((ObjectAssert<ObjectAssert>) listAssert.element(2)).isNotNull()
                .isInstanceOf(DTreeDefaultNode.class)
                .hasFieldOrPropertyWithValue("label", any.getLabel())
                .hasFieldOrPropertyWithValue("children.size", 0);

    }

    @Test
    public void shouldNotUpdateChild() {
        // Given
        final Computer model = new Computer(Computer.Type.DEFAULT);
        {
            model.setLabel("Default");
            model.setGroupId(1);
            model.setShared(false);
        }
        final DTreeComputer entity = new DTreeComputer(null) {
        };

        // When
        manager.update(entity, model);

        // Then
        Assert.assertNull(entity.getGroup());
    }

    @Test
    public void shouldCleanChildrenWhenEmpty() {
        // Given
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("Test");
            model.setGroupId(1);
            model.setShared(false);
            model.setChildren(Collections.emptyList());
        }
        final DTreeMixedComputer entity = new DTreeMixedComputer(GROUP);
        {
            entity.addChild(new DTreeDecisionNode(GROUP), 1);
        }

        // When
        manager.update(entity, model);

        //Then
        Assert.assertEquals(0, entity.getChildren().size());
    }

    @Test
    public void shouldCleanChildrenWhenNull() {
        // Given
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("Test");
            model.setGroupId(1);
            model.setShared(false);
            model.setChildren(null);
        }
        final DTreeMixedComputer entity = new DTreeMixedComputer(GROUP);
        {
            entity.addChild(new DTreeDecisionNode(GROUP), 1);
        }

        // When
        manager.update(entity, model);

        //Then
        Assert.assertEquals(0, entity.getChildren().size());
    }
}
