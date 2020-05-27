package io.github.up2jakarta.dte.jpa.validators;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.jpa.TestConfiguration;
import io.github.up2jakarta.dte.jpa.entities.DTreeComputer;
import io.github.up2jakarta.dte.jpa.models.Decider;
import io.github.up2jakarta.dte.jpa.models.Input;
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
        "spring.datasource.data=classpath:groups.sql, classpath:deciders.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:deciders_o_valid;MODE=PostgreSQL;"
})
@Transactional
public class BTreeOperandTests {

    private final DTreeComputer.Input inputString = new DTreeComputer.Input();
    private final DTreeComputer.Input inputBoolean = new DTreeComputer.Input();
    private final Decider link = new Decider(Decider.Type.DECIDER);
    private final Decider local = new Decider(Decider.Type.LOCAL);

    @Autowired
    private Validator validator;
    @Autowired
    private TypeRepository types;

    @Before
    public void initialize() {
        {
            inputString.setDescription("Test");
            inputString.setLabel("Test");
            inputString.setOptional(false);
            inputString.setType(types.find(2));
        }
        {
            inputBoolean.setDescription("Test");
            inputBoolean.setLabel("Test");
            inputBoolean.setOptional(false);
            inputBoolean.setType(types.find(3));
        }
        {
            local.setScript("c == 'Test'");
            local.setLabel("Test");
            local.setNegated(false);
            local.setShared(false);
            local.setTemplateId(0);
            local.setInputs(singletonList(new Input("c", inputString)));
        }
        {
            link.setDeciderId(200L);
            link.setNegated(false);
            link.setLabel("Test");
            link.setInputs(asList(new Input("a", inputBoolean), new Input("b", inputBoolean)));
        }
    }

    @Test
    public void shouldCheckLocalLabel() {
        // Given
        final Decider child = new Decider(Decider.Type.LOCAL);
        {
            child.setScript("c == 'Test'");
            //setLabel("Test");
            child.setNegated(false);
            child.setShared(true);
            child.setTemplateId(0);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setInputs(singletonList(new Input("c", inputString)));
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

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
        final Decider child = new Decider(Decider.Type.LOCAL);
        {
            child.setScript("c == 'Test'");
            child.setLabel("");
            child.setNegated(false);
            child.setShared(true);
            child.setTemplateId(0);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setInputs(singletonList(new Input("c", inputString)));
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("label");
    }

    @Test
    public void shouldCheckLocalShared() {
        // Given
        final Decider child = new Decider(Decider.Type.LOCAL);
        {
            child.setScript("c == 'Test'");
            child.setLabel("Test");
            child.setNegated(false);
            //child.setShared(true);
            child.setTemplateId(0);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setInputs(singletonList(new Input("c", inputString)));
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("shared");
    }

    @Test
    public void shouldCheckLocalTemplate() {
        // Given
        final Decider child = new Decider(Decider.Type.LOCAL);
        {
            child.setScript("c == 'Test'");
            child.setLabel("Test");
            child.setNegated(false);
            child.setShared(true);
            //child.setTemplateId(0);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setInputs(singletonList(new Input("c", inputString)));
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("templateId");
    }

    @Test
    public void shouldCheckLocalNegated() {
        // Given
        final Decider child = new Decider(Decider.Type.LOCAL);
        {
            child.setScript("c == 'Test'");
            child.setLabel("Test");
            //setNegated(false);
            child.setShared(true);
            child.setTemplateId(0);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setInputs(singletonList(new Input("c", inputString)));
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("negated");
    }

    @Test
    public void shouldCheckLocalScript() {
        // Given
        final Decider child = new Decider(Decider.Type.LOCAL);
        {
            //setScript("c == 'Test'");
            child.setLabel("Test");
            child.setNegated(false);
            child.setShared(true);
            child.setTemplateId(0);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

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
        final Decider child = new Decider(Decider.Type.LOCAL);
        {
            child.setScript("");
            child.setLabel("Test");
            child.setNegated(false);
            child.setShared(true);
            child.setTemplateId(0);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

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
        final Decider child = new Decider(Decider.Type.LOCAL);
        {
            child.setScript(" a+/'HH");
            child.setLabel("Test");
            child.setNegated(false);
            child.setShared(true);
            child.setTemplateId(0);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("script");
    }

    @Test
    public void shouldLocalSuccess() {
        // Given
        final Decider child = new Decider(Decider.Type.LOCAL);
        {
            child.setScript("a == 'Test'");
            child.setLabel("XOR");
            child.setGroupId(4);
            child.setNegated(false);
            child.setShared(true);
            child.setTemplateId(0);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setInputs(singletonList(new Input("a", inputString)));
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldLocalSuccess2() {
        // Given
        final Decider child = new Decider(Decider.Type.LOCAL);
        {
            child.setScript("a == 'Test'");
            child.setLabel("XOR");
            child.setNegated(false);
            child.setShared(true);
            child.setTemplateId(0);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setInputs(singletonList(new Input("a", inputString)));
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldLocalSuccess3() {
        // Given
        final Decider child = new Decider(Decider.Type.LOCAL);
        {
            child.setScript("a == 'Test'");
            child.setLabel("Test");
            child.setNegated(false);
            child.setGroupId(4);
            child.setShared(true);
            child.setTemplateId(0);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setInputs(singletonList(new Input("a", inputString)));
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldCheckDeciderLabel() {
        // Given
        final Decider child = new Decider(Decider.Type.DECIDER);
        {
            child.setDeciderId(200L);
            child.setNegated(false);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

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
        final Decider child = new Decider(Decider.Type.DECIDER);
        {
            child.setDeciderId(200L);
            child.setLabel("Test");
            //setNegated(false);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("negated");
    }

    @Test
    public void shouldCheckDeciderId() {
        // Given
        final Decider child = new Decider(Decider.Type.DECIDER);
        {
            //setDeciderId(200L);
            child.setLabel("Test");
            child.setNegated(false);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("deciderId");
    }

    @Test
    public void shouldCheckDecider() {
        // Given
        final Decider child = new Decider(Decider.Type.DECIDER);
        {
            child.setDeciderId(Long.MIN_VALUE);
            child.setLabel("Test");
            child.setNegated(false);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("deciderId");
    }

    @Test
    public void shouldCheckDeciderGroup() {
        // Given
        final Decider child = new Decider(Decider.Type.DECIDER);
        {
            child.setDeciderId(200L);
            child.setLabel("Test");
            child.setNegated(false);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setGroupId(Integer.MAX_VALUE);
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("deciderId");
    }

    @Test
    public void shouldDeciderSuccess() {
        // Given
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(link));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldDeciderSuccessWhenSharedDecider() {
        // Given
        final Decider child = new Decider(Decider.Type.DECIDER);
        {
            child.setDeciderId(100L);
            child.setLabel("Test");
            child.setNegated(false);
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setGroupId(Integer.MAX_VALUE);
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldDeciderSuccessWhenSameGroup() {
        // Given
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setGroupId(4);
            root.setOperands(singletonList(link));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldCheckOperatorLabel() {
        // Given
        final Decider child = new Decider(Decider.Type.OPERATOR);
        {
            child.setOperator(Operator.XOR);
            child.setNegated(false);
            //child.setLabel("Test");
            child.setOperands(asList(local, link));
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(child));
            root.setInputs(asList(new Input("c", inputString), new Input("a", inputBoolean), new Input("b", inputBoolean)));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("label");
    }

    @Test
    public void shouldCheckOperatorNegated() {
        // Given
        final Decider child = new Decider(Decider.Type.OPERATOR);
        {
            child.setOperator(Operator.XOR);
            //setNegated(false);
            child.setLabel("Test");
            child.setOperands(asList(local, link));
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(child));
            root.setInputs(asList(new Input("c", inputString), new Input("a", inputBoolean), new Input("b", inputBoolean)));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("negated");
    }

    @Test
    public void shouldCheckOperatorOperator() {
        // Given
        final Decider child = new Decider(Decider.Type.OPERATOR);
        {
            //setOperator(Operator.XOR);
            child.setNegated(false);
            child.setLabel("Test");
            child.setOperands(asList(local, link));
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(child));
            root.setInputs(asList(new Input("c", inputString), new Input("a", inputBoolean), new Input("b", inputBoolean)));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("operator");
    }

    @Test
    public void shouldCheckOperatorOperands() {
        // Given
        final Decider child = new Decider(Decider.Type.OPERATOR);
        {
            child.setOperator(Operator.XOR);
            child.setNegated(false);
            child.setLabel("Test");
            //setOperands(Arrays.asList(local, link));
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("operands");
    }

    @Test
    public void shouldCheckOperatorOperandsWhenEmpty() {
        // Given
        final Decider child = new Decider(Decider.Type.OPERATOR);
        {
            child.setOperator(Operator.XOR);
            child.setNegated(false);
            child.setLabel("Test");
            child.setOperands(Collections.emptyList());
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("operands");
    }

    @Test
    public void shouldCheckOperatorOperandsWhenSingle() {
        // Given
        final Decider child = new Decider(Decider.Type.OPERATOR);
        {
            child.setOperator(Operator.XOR);
            child.setNegated(false);
            child.setLabel("Test");
            child.setOperands(singletonList(local));
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(child));
            root.setInputs(singletonList(new Input("c", inputString)));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("operands");
    }

    @Test
    public void shouldOperatorSuccess() {
        // Given
        final Decider child = new Decider(Decider.Type.OPERATOR);
        {
            child.setOperator(Operator.XOR);
            child.setNegated(false);
            child.setLabel("Test");
            child.setOperands(asList(local, link));
        }
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(child));
            root.setInputs(asList(new Input("c", inputString), new Input("a", inputBoolean), new Input("b", inputBoolean)));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldCheckPlainType() {
        // Given
        final Decider child = new Decider(Decider.Type.PLAIN);
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

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
        final Decider child = new Decider(Decider.Type.MIXED);
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setOperands(singletonList(child));
        }

        //When
        final Set<ConstraintViolation<Decider>> violations = validator.validateProperty(root, "operands");

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("type");
    }
}
