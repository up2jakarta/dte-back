package io.github.up2jakarta.dte.jpa.validators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.jpa.TestConfiguration;
import io.github.up2jakarta.dte.jpa.models.Decider;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static io.github.up2jakarta.dte.jpa.TestUtil.BTreeWrapper;
import static io.github.up2jakarta.dte.jpa.TestUtil.last;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, inheritLocations = false)
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql, classpath:deciders.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:deciders_m_valid;MODE=PostgreSQL;"
})
public class BTreeMixedTests {

    private final Decider local = new Decider(Decider.Type.LOCAL);
    private final Decider link = new Decider(Decider.Type.DECIDER);
    private final Decider operator = new Decider(Decider.Type.OPERATOR);
    @Autowired
    private Validator validator;

    {
        operator.setLabel("OR");
        operator.setOperator(Operator.OR);
        operator.setNegated(false);
        operator.setOperands(Arrays.asList(link, local));

        link.setLabel("Link");
        link.setDeciderId(1000L);
        link.setNegated(false);

        local.setScript("a == 'Test'");
        local.setLabel("Test");
        local.setNegated(false);
        local.setShared(false);
        local.setTemplateId(0);
    }

    @Test
    public void shouldCheckLinkType() {
        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(link));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("type");
    }

    @Test
    public void shouldCheckOperatorType() {
        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(operator));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("type");
    }

    @Test
    public void shouldCheckLabel() {
        // Given
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            //setLabel("Test");
            root.setOperator(Operator.OR);
            root.setNegated(false);
            root.setShared(false);
            root.setGroupId(1);
            root.setOperands(Arrays.asList(operator, local));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("label");
    }

    @Test
    public void shouldCheckLabelWhenEmpty() {
        // Given
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setLabel("");
            root.setOperator(Operator.OR);
            root.setNegated(false);
            root.setShared(false);
            root.setGroupId(1);
            root.setOperands(Arrays.asList(operator, local));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("label");
    }

    @Test
    public void shouldCheckNegated() {
        // Given
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setLabel("Test");
            root.setOperator(Operator.OR);
            //setNegated(false);
            root.setShared(false);
            root.setGroupId(1);
            root.setOperands(Arrays.asList(operator, local));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("negated");
    }

    @Test
    public void shouldCheckShared() {
        // Given
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setLabel("Test");
            root.setOperator(Operator.OR);
            root.setNegated(false);
            //root.setShared(false);
            root.setGroupId(1);
            root.setOperands(Arrays.asList(operator, local));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("shared");
    }

    @Test
    public void shouldCheckGroupId() {
        // Given
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setLabel("Test");
            root.setOperator(Operator.OR);
            root.setNegated(false);
            root.setShared(false);
            //setGroupId(1);
            root.setOperands(Arrays.asList(operator, local));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("groupId");
    }

    @Test
    public void shouldCheckGroup() {
        // Given
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setLabel("Test");
            root.setOperator(Operator.OR);
            root.setNegated(false);
            root.setShared(false);
            root.setGroupId(Integer.MAX_VALUE);
            root.setOperands(Arrays.asList(operator, local));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("groupId");
    }

    @Test
    public void shouldCheckOperator() {
        // Given
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setLabel("Test");
            //setOperator(Operator.OR);
            root.setNegated(false);
            root.setShared(false);
            root.setGroupId(1);
            root.setOperands(Arrays.asList(operator, link));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("operator");
    }

    @Test
    public void shouldCheckOperands() {
        // Given
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setLabel("Test");
            root.setOperator(Operator.OR);
            root.setNegated(false);
            root.setShared(false);
            root.setGroupId(1);
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("operands");
    }

    @Test
    public void shouldCheckOperandsWhenEmpty() {
        // Given
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setLabel("Test");
            root.setOperator(Operator.OR);
            root.setNegated(false);
            root.setShared(false);
            root.setGroupId(1);
            root.setOperands(Collections.emptyList());
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("operands");
    }

    @Test
    public void shouldCheckOperandsWhenSingle() {
        // Given
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setLabel("Test");
            root.setOperator(Operator.OR);
            root.setNegated(false);
            root.setShared(false);
            root.setGroupId(1);
            root.setOperands(Collections.singletonList(operator));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("operands");
    }

    @Test
    public void shouldCheckUniqueKey() {
        // Given
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setLabel("Empty");
            root.setOperator(Operator.OR);
            root.setNegated(false);
            root.setShared(false);
            root.setGroupId(4);
            root.setOperands(Arrays.asList(operator, link));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("label");
    }

    @Test
    public void shouldSuccessWhenDistinctGroup() {
        // Given
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setLabel("XOR");
            root.setOperator(Operator.OR);
            root.setNegated(false);
            root.setShared(false);
            root.setGroupId(2);
            root.setOperands(Arrays.asList(operator, link));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(root));

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldSuccessWhenDistinctLabel() {
        // Given
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setLabel("XOR2");
            root.setOperator(Operator.OR);
            root.setNegated(false);
            root.setShared(false);
            root.setGroupId(4);
            root.setOperands(Arrays.asList(operator, link));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(root));

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldSuccessWhenSameId() {
        // Given
        final Decider root = new Decider(Decider.Type.MIXED);
        {
            root.setId(1000L);
            root.setLabel("XOR");
            root.setOperator(Operator.OR);
            root.setNegated(false);
            root.setShared(false);
            root.setGroupId(4);
            root.setOperands(Arrays.asList(operator, link));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(root));

        // Then
        assertThat(violations).hasSize(0);
    }
}
