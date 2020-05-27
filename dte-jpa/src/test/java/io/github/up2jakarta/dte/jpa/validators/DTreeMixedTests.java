package io.github.up2jakarta.dte.jpa.validators;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import io.github.up2jakarta.dte.jpa.TestConfiguration;
import io.github.up2jakarta.dte.jpa.entities.DTreeComputer;
import io.github.up2jakarta.dte.jpa.entities.Type;
import io.github.up2jakarta.dte.jpa.models.Computer;
import io.github.up2jakarta.dte.jpa.models.Input;
import io.github.up2jakarta.dte.jpa.models.Output;
import io.github.up2jakarta.dte.jpa.repositories.TypeRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static io.github.up2jakarta.dte.jpa.TestUtil.DTreeWrapper;
import static io.github.up2jakarta.dte.jpa.TestUtil.last;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, inheritLocations = false)
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql, classpath:computers.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:computers_valid;MODE=PostgreSQL;"
})
public class DTreeMixedTests {
    private final DTreeComputer.Input inputString = new DTreeComputer.Input();
    private final DTreeComputer.Output outputString = new DTreeComputer.Output();

    private final Computer local = new Computer(Computer.Type.LOCAL);
    private final Computer link = new Computer(Computer.Type.COMPUTER);
    private final Computer decider = new Computer(Computer.Type.DECISION);
    private final Computer decision = new Computer(Computer.Type.DECISION);
    private final Computer any = new Computer(Computer.Type.DEFAULT);

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
            any.setLabel("Test");
        }
        {
            local.setScript("a = 'Test'");
            local.setLabel("Test");
            local.setShared(false);
            local.setTemplateId(0);
            local.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }
        {
            link.setComputerId(1000L);
            link.setLabel("Test");
        }
        {
            decider.setNegated(false);
            decider.setDeciderId(22L);
            decider.setLabel("Test");
        }
        {
            decision.setScript("a == 'Test'");
            decision.setLabel("Test");
            decision.setNegated(false);
            decision.setShared(false);
            decision.setTemplateId(0);
            decision.setInputs(Collections.singletonList(new Input("a", inputString)));
        }
    }

    @Test
    public void shouldCheckLinkType() {
        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(link));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("type");
    }

    @Test
    public void shouldCheckLocalType() {
        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(local));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("type");
    }

    @Test
    public void shouldCheckDecisionType() {
        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(decision));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("type");
    }

    @Test
    public void shouldCheckDeciderType() {
        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(decider));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("type");
    }

    @Test
    public void shouldCheckDefaultType() {
        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(any));

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
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            //setLabel("Test");
            root.setGroupId(1);
            root.setShared(false);
            root.setChildren(Collections.singletonList(local));
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("label");
    }

    @Test
    public void shouldCheckShared() {
        // Given
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setLabel("Test");
            root.setGroupId(1);
            root.setShared(null);
            root.setChildren(Collections.singletonList(local));
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("shared");
    }

    @Test
    public void shouldCheckLabelWhenEmpty() {
        // Given
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setLabel("");
            root.setGroupId(1);
            root.setShared(false);
            root.setChildren(Collections.singletonList(local));
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("label");
    }

    @Test
    public void shouldCheckGroupId() {
        // Given
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setLabel("Test");
            //setGroupId(1);
            root.setShared(false);
            root.setChildren(Collections.singletonList(link));
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

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
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setLabel("Test");
            root.setGroupId(Integer.MAX_VALUE);
            root.setShared(false);
            root.setChildren(Collections.singletonList(link));
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("groupId");
    }

    @Test
    public void shouldCheckChildren() {
        // Given
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setLabel("Test");
            root.setGroupId(1);
            root.setShared(false);
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("children");
    }

    @Test
    public void shouldCheckChildrenWhenEmpty() {
        // Given
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setLabel("Test");
            root.setGroupId(1);
            root.setShared(false);
            root.setChildren(Collections.emptyList());
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("children");
    }

    @Test
    public void shouldCheckChildrenWhenOnlyDefault() {
        // Given
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setLabel("Test");
            root.setShared(false);
            root.setGroupId(1);
            root.setChildren(Collections.singletonList(any));
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("children");
    }

    @Test
    public void shouldCheckChildrenWhenLastDefault() {
        // Given
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setLabel("Test");
            root.setShared(false);
            root.setGroupId(1);
            root.setChildren(Arrays.asList(any, decision));
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("children");
    }

    @Test
    public void shouldCheckChildrenWhenSingleDefault() {
        // Given
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setLabel("Test");
            root.setShared(false);
            root.setGroupId(1);
            root.setChildren(Arrays.asList(any, any));
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("children");
    }

    @Test
    public void shouldCheckChildrenWhenDistinctType() {
        // Given
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setLabel("Test");
            root.setShared(false);
            root.setGroupId(1);
            root.setChildren(Arrays.asList(decision, local));
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("children");
    }

    @Test
    public void shouldCheckUniqueKey() {
        // Given
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setLabel("Pay");
            root.setShared(false);
            root.setGroupId(2);
            root.setChildren(Arrays.asList(decision, decider, any));
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

        // Then
        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("label");
    }

    @Test
    public void shouldSuccessWhenRules() {
        // Given
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setLabel("Test");
            root.setShared(false);
            root.setGroupId(1);
            root.setChildren(Arrays.asList(local, link));
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldSuccessWhenDecisions() {
        // Given
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setLabel("Test");
            root.setShared(false);
            root.setGroupId(1);
            root.setChildren(Arrays.asList(decision, decider, any));
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldSuccessWhenDistinctGroup() {
        // Given
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setLabel("SIMPLE");
            root.setShared(false);
            root.setGroupId(1);
            root.setChildren(Arrays.asList(decision, decider, any));
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldSuccessWhenDistinctLabel() {
        // Given
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setLabel("SIMPLE2");
            root.setShared(false);
            root.setGroupId(2);
            root.setChildren(Arrays.asList(decision, decider, any));
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldSuccessWhenSameId() {
        // Given
        final Computer root = new Computer(Computer.Type.MIXED);
        {
            root.setId(1000L);
            root.setLabel("SIMPLE");
            root.setShared(false);
            root.setGroupId(2);
            root.setChildren(Arrays.asList(decision, decider, any));
            root.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(root));

        // Then
        assertThat(violations).hasSize(0);
    }
}
