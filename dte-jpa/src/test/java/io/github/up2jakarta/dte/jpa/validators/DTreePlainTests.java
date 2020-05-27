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
import io.github.up2jakarta.dte.jpa.models.Output;
import io.github.up2jakarta.dte.jpa.repositories.TypeRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static io.github.up2jakarta.dte.jpa.TestUtil.DTreeWrapper;
import static io.github.up2jakarta.dte.jpa.TestUtil.last;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, inheritLocations = false)
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:computers_p_valid;MODE=PostgreSQL;"
})
@Transactional
public class DTreePlainTests {

    private final DTreeComputer.Output outputString = new DTreeComputer.Output();

    @Autowired
    private Validator validator;
    @Autowired
    private TypeRepository types;

    @Before
    public void initialize() {
        final Type string = types.find(2);
        outputString.setDescription("Test");
        outputString.setLabel("Test");
        outputString.setOptional(false);
        outputString.setType(string);
    }

    @Test
    public void shouldCheckLabel() {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setScript("a = 'Test'");
            model.setGroupId(1);
            model.setShared(false);
            model.setTemplateId(0);
            model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(model));

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
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setScript("a = 'Test'");
            model.setLabel("");
            model.setGroupId(1);
            model.setShared(false);
            model.setTemplateId(0);
            model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(model));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("label");
    }

    @Test
    public void shouldCheckScript() {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setScript(null);
            model.setLabel("Test");
            model.setGroupId(1);
            model.setShared(false);
            model.setTemplateId(0);
            model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(model));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("script");

    }

    @Test
    public void shouldCheckScriptWhenEmpty() {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setScript("");
            model.setLabel("Test");
            model.setGroupId(1);
            model.setShared(false);
            model.setTemplateId(0);
            model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(model));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("script");
    }

    @Test
    public void shouldCheckScriptWhenSyntax() {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setScript("+&2/'AA'");
            model.setLabel("Test");
            model.setGroupId(1);
            model.setShared(false);
            model.setTemplateId(0);
            model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(model));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("script");
    }

    @Test
    public void shouldCheckGroupId() {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setScript("a = 'Test'");
            model.setLabel("Test");
            model.setGroupId(null);
            model.setShared(false);
            model.setTemplateId(0);
            model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(model));

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
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setScript("a = 'Test'");
            model.setLabel("Test");
            model.setGroupId(Integer.MAX_VALUE);
            model.setShared(false);
            model.setTemplateId(0);
            model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(model));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("groupId");
    }

    @Test
    public void shouldCheckTemplateId() {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setScript("a = 'Test'");
            model.setLabel("Test");
            model.setGroupId(1);
            model.setShared(false);
            model.setTemplateId(null);
            model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(model));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("templateId");
    }

    @Test
    public void shouldCheckTemplate() {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setScript("a = 'Test'");
            model.setLabel("Test");
            model.setGroupId(1);
            model.setShared(false);
            model.setTemplateId(Integer.MAX_VALUE);
            model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(model));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("templateId");
    }

    @Test
    public void shouldCheckShared() {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setScript("a = 'Test'");
            model.setLabel("Test");
            model.setGroupId(1);
            model.setShared(null);
            model.setTemplateId(0);
            model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(model));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("shared");
    }

    @Test
    public void shouldCheckLocalType() {
        // Given
        final Computer model = new Computer(Computer.Type.LOCAL);
        {
            model.setScript("a == 'Test'");
            model.setLabel("Test");
            model.setNegated(false);
            model.setShared(false);
            model.setTemplateId(0);
            model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(model));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("type");
    }

    @Test
    public void shouldCheckDecisionType() {
        // Given
        final Computer model = new Computer(Computer.Type.DECISION);
        {
            model.setScript("a == 'Test'");
            model.setLabel("Test");
            model.setNegated(false);
            model.setShared(false);
            model.setTemplateId(0);
            model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(model));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("type");
    }

    @Test
    public void shouldSuccess() {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setScript("a = 'Test'");
            model.setLabel("Test");
            model.setGroupId(1);
            model.setShared(false);
            model.setTemplateId(0);
            model.setOutputs(Collections.singletonList(new Output("a", outputString)));
        }

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(model));

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldType() {
        // Given
        final Computer model = new Computer(null);

        //When
        final Set<ConstraintViolation<DTreeWrapper>> violations = validator.validate(new DTreeWrapper(model));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("type");
    }
}
