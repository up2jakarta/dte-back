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
import io.github.up2jakarta.dte.jpa.models.Decider;
import io.github.up2jakarta.dte.jpa.models.Input;
import io.github.up2jakarta.dte.jpa.repositories.TypeRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static io.github.up2jakarta.dte.jpa.TestUtil.BTreeWrapper;
import static io.github.up2jakarta.dte.jpa.TestUtil.last;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class, inheritLocations = false)
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:deciders_p_valid;MODE=PostgreSQL;"
})
@Transactional
public class BTreePlainTests {

    private final DTreeComputer.Input inputString = new DTreeComputer.Input();

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
    }

    @Test
    public void shouldCheckLabel() {
        // Given
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setScript("a == 'Test'");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            model.setTemplateId(0);
            model.setInputs(Collections.singletonList(new Input("a", inputString)));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(model));

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
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setScript("a == 'Test'");
            model.setLabel("");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            model.setTemplateId(0);
            model.setInputs(Collections.singletonList(new Input("a", inputString)));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(model));

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
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setScript(null);
            model.setLabel("Test");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            model.setTemplateId(0);
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(model));

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
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setScript("");
            model.setLabel("Test");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            model.setTemplateId(0);
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(model));

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
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setScript("+&2/'AA'");
            model.setLabel("Test");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            model.setTemplateId(0);
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(model));

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
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setScript("a == 'Test'");
            model.setLabel("Test");
            model.setGroupId(null);
            model.setNegated(false);
            model.setShared(false);
            model.setTemplateId(0);
            model.setInputs(Collections.singletonList(new Input("a", inputString)));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(model));

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
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setScript("a == 'Test'");
            model.setLabel("Test");
            model.setGroupId(Integer.MAX_VALUE);
            model.setNegated(false);
            model.setShared(false);
            model.setTemplateId(0);
            model.setInputs(Collections.singletonList(new Input("a", inputString)));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(model));

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
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setScript("a == 'Test'");
            model.setLabel("Test");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            model.setTemplateId(null);
            model.setInputs(Collections.singletonList(new Input("a", inputString)));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(model));

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
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setScript("a == 'Test'");
            model.setLabel("Test");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            model.setTemplateId(Integer.MAX_VALUE);
            model.setInputs(Collections.singletonList(new Input("a", inputString)));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(model));

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
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setScript("a == 'Test'");
            model.setLabel("Test");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(null);
            model.setTemplateId(0);
            model.setInputs(Collections.singletonList(new Input("a", inputString)));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(model));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("shared");
    }

    @Test
    public void shouldCheckNegated() {
        // Given
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setScript("a == 'Test'");
            model.setLabel("Test");
            model.setGroupId(1);
            model.setNegated(null);
            model.setShared(false);
            model.setTemplateId(0);
            model.setInputs(Collections.singletonList(new Input("a", inputString)));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(model));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("negated");
    }

    @Test
    public void shouldSuccess() {
        // Given
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setScript("a == 'Test'");
            model.setLabel("Test");
            model.setGroupId(1);
            model.setNegated(true);
            model.setShared(false);
            model.setTemplateId(0);
            model.setInputs(Collections.singletonList(new Input("a", inputString)));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(model));

        // Then
        assertThat(violations).hasSize(0);
    }

    @Test
    public void shouldCheckLocalType() {
        // Given
        final Decider model = new Decider(Decider.Type.LOCAL);
        {
            model.setScript("a == 'Test'");
            model.setLabel("Test");
            model.setNegated(false);
            model.setShared(false);
            model.setTemplateId(0);
            model.setInputs(Collections.singletonList(new Input("a", inputString)));
        }

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(model));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("type");
    }

    @Test
    public void shouldType() {
        // Given
        final Decider model = new Decider(null);

        //When
        final Set<ConstraintViolation<BTreeWrapper>> violations = validator.validate(new BTreeWrapper(model));

        // Then
        assertThat(violations)
                .hasSize(1)
                .element(0)
                .extracting(v -> last(v.getPropertyPath()).getName())
                .isEqualTo("type");
    }
}
