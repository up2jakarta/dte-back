package io.github.up2jakarta.dte.jpa.validators;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import io.github.up2jakarta.dte.jpa.TestConfiguration;
import io.github.up2jakarta.dte.jpa.entities.DTreeComputer;
import io.github.up2jakarta.dte.jpa.entities.Type;
import io.github.up2jakarta.dte.jpa.models.Computer;
import io.github.up2jakarta.dte.jpa.models.Input;
import io.github.up2jakarta.dte.jpa.models.Output;
import io.github.up2jakarta.dte.jpa.repositories.TypeRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static io.github.up2jakarta.dte.jpa.TestUtil.last;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, inheritLocations = false)
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql, classpath:computers.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:computers_c_valid;MODE=PostgreSQL;"
})
@Transactional
public class DTreeChildTests {

    private final DTreeComputer.Input inputString = new DTreeComputer.Input();
    private final DTreeComputer.Output outputString = new DTreeComputer.Output();

    private final Computer rule = new Computer(Computer.Type.LOCAL);
    private final Computer decision = new Computer(Computer.Type.DECISION);

    @Autowired
    private Validator validator;
    @Autowired
    private TypeRepository types;

    @Before
    public void initialize() {
        final Type string = types.find(2);
        {
            inputString.setDescription("Test");
            inputString.setLabel("Test");
            inputString.setOptional(false);
            inputString.setType(string);
        }
        {
            outputString.setDescription("Test");
            outputString.setLabel("Test");
            outputString.setOptional(false);
            outputString.setType(string);
        }
        {
            rule.setScript("a = 'Test'");
            rule.setLabel("Test");
            rule.setShared(false);
            rule.setTemplateId(0);
            rule.setOutputs(singletonList(new Output("a", outputString)));
        }
        {
            decision.setScript("c == 'Test'");
            decision.setLabel("Test");
            decision.setNegated(true);
            decision.setShared(false);
            decision.setTemplateId(0);
            decision.setChildren(singletonList(rule));
            decision.setInputs(singletonList(new Input("c", inputString)));
        }
    }

    @Test
    public void shouldCheckLocalShared() {
        // Given
        final Computer local = new Computer(Computer.Type.LOCAL);
        {
            local.setScript("a = 'Test'");
            local.setLabel("Test");
            //local.setShared(false);
            local.setTemplateId(0);
            local.setOutputs(singletonList(new Output("a", outputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(local));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("shared");
    }

    @Test
    public void shouldCheckLocalTemplateId() {
        // Given
        final Computer local = new Computer(Computer.Type.LOCAL);
        {
            local.setScript("a = 'Test'");
            local.setLabel("Test");
            local.setShared(false);
            //local.setTemplateId(0);
            local.setOutputs(singletonList(new Output("a", outputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(local));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("templateId");
    }

    @Test
    public void shouldCheckLocalLabel() {
        // Given
        final Computer child = new Computer(Computer.Type.LOCAL);
        {
            child.setScript("c = 'Test'");
            //child.setLabel("Test");
            child.setShared(false);
            child.setTemplateId(0);
            child.setChildren(Collections.emptyList());
            child.setOutputs(singletonList(new Output("c", outputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("label");
    }

    @Test
    public void shouldCheckLocalLabelWhenEmpty() {
        // Given
        final Computer child = new Computer(Computer.Type.LOCAL);
        {
            child.setScript("c = 'Test'");
            child.setLabel("");
            child.setShared(false);
            child.setTemplateId(0);
            child.setOutputs(singletonList(new Output("c", outputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("label");
    }

    @Test
    public void shouldCheckLocalScript() {
        // Given
        final Computer child = new Computer(Computer.Type.LOCAL);
        {
            //child.setScript("c = 'Test'");
            child.setLabel("Test");
            child.setShared(false);
            child.setTemplateId(0);
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("script");
    }

    @Test
    public void shouldCheckLocalScriptWhenEmpty() {
        // Given
        final Computer child = new Computer(Computer.Type.LOCAL);
        {
            child.setScript("");
            child.setLabel("Test");
            child.setShared(false);
            child.setTemplateId(0);
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("script");
    }

    @Test
    public void shouldCheckLocalScriptSyntax() {
        // Given
        final Computer child = new Computer(Computer.Type.LOCAL);
        {
            child.setScript("a+/'HH");
            child.setLabel("Test");
            child.setShared(false);
            child.setTemplateId(0);
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("script");
    }

    @Test
    public void shouldCheckLocalChildren() {
        // Given
        final Computer local = new Computer(Computer.Type.LOCAL);
        {
            local.setScript("a = 'Test'");
            local.setLabel("Test");
            local.setShared(false);
            local.setTemplateId(0);
            local.setChildren(singletonList(rule));
            local.setOutputs(singletonList(new Output("a", outputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(local));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("children");
    }

    @Test
    public void shouldLocalSuccess() {
        // Given
        final Computer local = new Computer(Computer.Type.LOCAL);
        {
            local.setScript("a = 'Test'");
            local.setLabel("Test");
            local.setShared(false);
            local.setTemplateId(0);
            local.setOutputs(singletonList(new Output("a", outputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(local));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldLocalSuccessWithDecisions() {
        // Given
        final Computer any = new Computer(Computer.Type.DEFAULT);
        {
            any.setLabel("Any");
            any.setChildren(singletonList(rule));
        }
        final Computer local = new Computer(Computer.Type.LOCAL);
        {
            local.setScript("a = 'Test'");
            local.setLabel("Test");
            local.setShared(false);
            local.setTemplateId(0);
            local.setChildren(asList(decision, any));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(local));
            model.setInputs(singletonList(new Input("c", inputString)));
            model.setOutputs(singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldCheckComputerLabel() {
        // Given
        final Computer child = new Computer(Computer.Type.COMPUTER);
        {
            //child.setLabel("Computer");
            child.setComputerId(1000L);
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("label");
    }

    @Test
    public void shouldCheckLinkComputerId() {
        // Given
        final Computer child = new Computer(Computer.Type.COMPUTER);
        {
            child.setLabel("Computer");
            //setComputerId(1000L);
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("computerId");
    }

    @Test
    public void shouldCheckComputerComputerExists() {
        // Given
        final Computer child = new Computer(Computer.Type.COMPUTER);
        {
            child.setLabel("Computer");
            child.setComputerId(Long.MIN_VALUE);
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("computerId");
    }

    @Test
    public void shouldCheckComputerComputerGroup() {
        // Given
        final Computer child = new Computer(Computer.Type.COMPUTER);
        {
            child.setComputerId(1000L);
            child.setLabel("Computer");
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setGroupId(Integer.MAX_VALUE);
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("computerId");
    }

    @Test
    public void shouldCheckComputerChildren() {
        // Given
        final Computer link = new Computer(Computer.Type.COMPUTER);
        {
            link.setComputerId(1000L);
            link.setLabel("Computer");
            link.setChildren(singletonList(rule));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(link));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("children");
    }

    @Test
    public void shouldComputerSuccess() {
        // Given
        final Computer link = new Computer(Computer.Type.COMPUTER);
        {
            link.setLabel("Computer");
            link.setComputerId(1000L);
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(link));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldComputerSuccessWhenSharedComputer() {
        // Given
        final Computer child = new Computer(Computer.Type.COMPUTER);
        {
            child.setComputerId(150L);
            child.setLabel("Computer");
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setGroupId(Integer.MAX_VALUE);
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldComputerSuccessWithDecisions() {
        // Given
        final Computer link = new Computer(Computer.Type.COMPUTER);
        {
            link.setLabel("Computer");
            link.setComputerId(1000L);
            link.setChildren(singletonList(decision));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(link));
            model.setInputs(singletonList(new Input("c", inputString)));
            //model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldComputerSuccessWhenSameGroup() {
        // Given
        final Computer link = new Computer(Computer.Type.COMPUTER);
        {
            link.setLabel("Computer");
            link.setComputerId(1000L);
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setGroupId(2);
            model.setChildren(singletonList(link));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldCheckDecisionShared() {
        // Given
        final Computer child = new Computer(Computer.Type.DECISION);
        {
            child.setScript("c == 'Test'");
            child.setLabel("Test");
            child.setNegated(true);
            //child.setShared(false);
            child.setTemplateId(0);
            child.setChildren(singletonList(rule));
            //child.setInputs(Collections.singletonList(new Input("c", inputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
            model.setInputs(singletonList(new Input("c", inputString)));
            model.setOutputs(singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("shared");
    }

    @Test
    public void shouldCheckDecisionNegated() {
        // Given
        final Computer child = new Computer(Computer.Type.DECISION);
        {
            child.setScript("c == 'Test'");
            child.setLabel("Test");
            //setNegated(true);
            child.setShared(false);
            child.setTemplateId(0);
            child.setChildren(singletonList(rule));
            //child.setInputs(Collections.singletonList(new Input("c", inputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
            model.setInputs(singletonList(new Input("c", inputString)));
            //model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("negated");
    }

    @Test
    public void shouldCheckDecisionLabel() {
        // Given
        final Computer child = new Computer(Computer.Type.DECISION);
        {
            child.setScript("c == 'Test'");
            //setLabel("Test");
            child.setNegated(true);
            child.setShared(false);
            child.setTemplateId(0);
            child.setChildren(singletonList(rule));
            //child.setInputs(Collections.singletonList(new Input("c", inputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
            model.setInputs(singletonList(new Input("c", inputString)));
            //model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("label");
    }

    @Test
    public void shouldCheckDecisionLabelWhenEmpty() {
        // Given
        final Computer child = new Computer(Computer.Type.DECISION);
        {
            child.setScript("c == 'Test'");
            child.setLabel("");
            child.setNegated(true);
            child.setShared(false);
            child.setTemplateId(0);
            child.setChildren(singletonList(rule));
            //child.setInputs(Collections.singletonList(new Input("c", inputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
            model.setInputs(singletonList(new Input("c", inputString)));
            //model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("label");
    }

    @Test
    public void shouldCheckDecisionScript() {
        // Given
        final Computer child = new Computer(Computer.Type.DECISION);
        {
            //setScript("c == 'Test'");
            child.setLabel("Test");
            child.setNegated(true);
            child.setShared(false);
            child.setTemplateId(0);
            child.setChildren(singletonList(rule));
            //child.setOutputs(Collections.singletonList(new Output("c", outputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
            model.setInputs(singletonList(new Input("c", inputString)));
            //model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("script");
    }

    @Test
    public void shouldCheckDecisionScriptWhenEmpty() {
        // Given
        final Computer child = new Computer(Computer.Type.DECISION);
        {
            child.setScript("");
            child.setLabel("Test");
            child.setNegated(true);
            child.setShared(false);
            child.setTemplateId(0);
            child.setChildren(singletonList(rule));
            child.setOutputs(singletonList(new Output("c", outputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("script");
    }

    @Test
    public void shouldCheckDecisionScriptSyntax() {
        // Given
        final Computer child = new Computer(Computer.Type.DECISION);
        {
            child.setScript("a+/'HH");
            child.setLabel("Test");
            child.setNegated(true);
            child.setShared(false);
            child.setTemplateId(0);
            child.setChildren(singletonList(rule));
            child.setOutputs(singletonList(new Output("a", outputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("script");
    }

    @Test
    public void shouldCheckDecisionTemplate() {
        // Given
        final Computer child = new Computer(Computer.Type.DECISION);
        {
            child.setScript("a");
            child.setLabel("Test");
            child.setNegated(true);
            child.setShared(false);
            //child.setTemplateId(0);
            child.setChildren(singletonList(rule));
            child.setInputs(singletonList(new Input("a", inputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("templateId");
    }

    @Test
    public void shouldCheckDecisionChildren() {
        // Given
        final Computer child = new Computer(Computer.Type.DECISION);
        {
            child.setScript("a == 'Test'");
            child.setLabel("Test");
            child.setNegated(true);
            child.setShared(false);
            child.setTemplateId(0);
            //child.setInputs(Collections.singletonList(new Input("a", inputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
            model.setInputs(singletonList(new Input("a", inputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("children");
    }

    @Test
    public void shouldCheckDecisionChildrenWhenEmpty() {
        // Given
        final Computer child = new Computer(Computer.Type.DECISION);
        {
            child.setScript("a == 'Test'");
            child.setLabel("Test");
            child.setNegated(true);
            child.setShared(false);
            child.setTemplateId(0);
            child.setChildren(Collections.emptyList());
            //child.setInputs(Collections.singletonList(new Input("a", inputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
            model.setInputs(singletonList(new Input("a", inputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("children");
    }

    @Test
    public void shouldDecisionSuccess() {
        // Given
        final Computer child = new Computer(Computer.Type.DECISION);
        {
            child.setScript("a == 'Test'");
            child.setLabel("Test");
            child.setNegated(true);
            child.setShared(false);
            child.setTemplateId(0);
            child.setChildren(singletonList(rule));
            //child.setInputs(Collections.singletonList(new Input("a", inputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
            model.setInputs(singletonList(new Input("a", inputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldDecisionSuccessWithDecisions() {
        // Given
        final Computer child = new Computer(Computer.Type.DECISION);
        {
            child.setScript("a == 'Test'");
            child.setLabel("Test");
            child.setNegated(true);
            child.setShared(false);
            child.setTemplateId(0);
            child.setChildren(singletonList(decision));
            //child.setInputs(Collections.singletonList(new Input("a", inputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
            model.setInputs(asList(new Input("a", inputString), new Input("c", inputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldCheckDeciderLabel() {
        // Given
        final Computer child = new Computer(Computer.Type.DECIDER);
        {
            child.setDeciderId(22L);
            child.setNegated(true);
            //child.setLabel("Test");
            child.setChildren(singletonList(rule));
            child.setInputs(singletonList(new Input("a", inputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("label");
    }

    @Test
    public void shouldCheckDeciderNegated() {
        // Given
        final Computer child = new Computer(Computer.Type.DECIDER);
        {
            child.setDeciderId(22L);
            child.setLabel("Test");
            //child.setNegated(true);
            child.setChildren(singletonList(rule));
            child.setInputs(singletonList(new Input("a", inputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("negated");
    }

    @Test
    public void shouldCheckDeciderReference() {
        // Given
        final Computer child = new Computer(Computer.Type.DECIDER);
        {
            //child.setDeciderId(102L);
            child.setNegated(false);
            child.setLabel("Test");
            child.setChildren(singletonList(rule));
            child.setInputs(singletonList(new Input("a", inputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("deciderId");
    }

    @Test
    public void shouldCheckDeciderReferenceExists() {
        // Given
        final Computer child = new Computer(Computer.Type.DECIDER);
        {
            child.setDeciderId(Long.MIN_VALUE);
            child.setNegated(true);
            child.setLabel("Test");
            child.setChildren(singletonList(rule));
            child.setInputs(singletonList(new Input("a", inputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("deciderId");
    }

    @Test
    public void shouldCheckDeciderReferenceGroupId() {
        // Given
        final Computer child = new Computer(Computer.Type.DECIDER);
        {
            child.setDeciderId(102L);
            child.setLabel("Test");
            child.setNegated(true);
            child.setChildren(singletonList(rule));
            child.setInputs(singletonList(new Input("a", inputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setGroupId(Integer.MAX_VALUE);
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("deciderId");
    }

    @Test
    public void shouldCheckDeciderChildren() {
        // Given
        final Computer child = new Computer(Computer.Type.DECIDER);
        {
            child.setDeciderId(22L);
            child.setLabel("Test");
            child.setNegated(true);
            child.setInputs(singletonList(new Input("a", inputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("children");
    }

    @Test
    public void shouldCheckDeciderChildrenWhenEmpty() {
        // Given
        final Computer child = new Computer(Computer.Type.DECIDER);
        {
            child.setDeciderId(22L);
            child.setNegated(true);
            child.setLabel("Test");
            child.setChildren(Collections.emptyList());
            child.setInputs(singletonList(new Input("a", inputString)));
        }
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(root, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("children");
    }

    @Test
    public void shouldDeciderSuccess() {
        // Given
        final Computer child = new Computer(Computer.Type.DECIDER);
        {
            child.setDeciderId(22L);
            child.setNegated(true);
            child.setLabel("Test");
            child.setChildren(singletonList(rule));
            child.setInputs(singletonList(new Input("a", inputString)));
        }
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(root, "children");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldDeciderSuccessWhenShareDecider() {
        // Given
        final Computer child = new Computer(Computer.Type.DECIDER);
        {
            child.setDeciderId(21L);
            child.setNegated(true);
            child.setLabel("Test");
            child.setChildren(singletonList(rule));
            child.setInputs(singletonList(new Input("a", inputString)));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setGroupId(Integer.MAX_VALUE);
            model.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(model, "children");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldDeciderSuccessWhenSameGroup() {
        // Given
        final Computer child = new Computer(Computer.Type.DECIDER);
        {
            child.setDeciderId(22L);
            child.setNegated(true);
            child.setLabel("Test");
            child.setChildren(singletonList(rule));
            child.setInputs(singletonList(new Input("a", inputString)));
        }
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setGroupId(2);
            root.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(root, "children");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldDeciderSuccessWithDecisions() {
        // Given
        final Computer child = new Computer(Computer.Type.DECIDER);
        {
            child.setDeciderId(22L);
            child.setNegated(true);
            child.setLabel("Test");
            child.setChildren(singletonList(decision));
            //child.setInputs(Collections.singletonList(new Input("a", inputString)));
        }
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setChildren(singletonList(child));
            root.setInputs(singletonList(new Input("c", inputString)));
            root.setOutputs(singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(root, "children");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldCheckDefaultLabel() {
        // Given
        final Computer any = new Computer(Computer.Type.DEFAULT);
        {
            any.setLabel("");
            any.setChildren(singletonList(rule));
        }
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setChildren(asList(decision, any));
            root.setInputs(singletonList(new Input("c", inputString)));
            root.setOutputs(singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(root, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("label");
    }

    @Test
    public void shouldCheckDefaultChildren() {
        // Given
        final Computer any = new Computer(Computer.Type.DEFAULT);
        {
            any.setLabel("Any");
        }
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setChildren(asList(decision, any));
            root.setInputs(singletonList(new Input("c", inputString)));
            root.setOutputs(singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(root, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("children");
    }

    @Test
    public void shouldCheckDefaultChildrenWhenEmpty() {
        // Given
        final Computer any = new Computer(Computer.Type.DEFAULT);
        {
            any.setLabel("Any");
            any.setChildren(Collections.emptyList());
        }
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setChildren(asList(decision, any));
            root.setInputs(singletonList(new Input("c", inputString)));
            root.setOutputs(singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(root, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("children");
    }

    @Test
    public void shouldDefaultSuccess() {
        // Given
        final Computer any = new Computer(Computer.Type.DEFAULT);
        {
            any.setLabel("Any");
            any.setChildren(singletonList(rule));
        }
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setChildren(asList(decision, any));
            root.setInputs(singletonList(new Input("c", inputString)));
            root.setOutputs(singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(root, "children");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldDefaultSuccessWithDecisions() {
        // Given
        final Computer any = new Computer(Computer.Type.DEFAULT);
        {
            any.setLabel("Any");
            any.setChildren(singletonList(decision));
        }
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setChildren(asList(decision, any));
            root.setInputs(singletonList(new Input("c", inputString)));
            root.setOutputs(singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(root, "children");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldCheckPlainType() {
        // Given
        final Computer child = new Computer(Computer.Type.PLAIN);
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(root, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("type");
    }

    @Test
    public void shouldCheckMixedType() {
        // Given
        final Computer child = new Computer(Computer.Type.MIXED);
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setChildren(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Computer>> violations = validator.validateProperty(root, "children");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("type");
    }
}
