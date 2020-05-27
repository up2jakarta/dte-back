package io.github.up2jakarta.dte.web.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;
import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.jpa.models.Decider;
import io.github.up2jakarta.dte.jpa.models.Input;
import io.github.up2jakarta.dte.web.TestConfiguration;
import io.github.up2jakarta.dte.web.config.ExceptionHandlerAdvice;
import io.github.up2jakarta.dte.web.controllers.DeciderController;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@WebAppConfiguration
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql, classpath:deciders.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:deciders_valid;MODE=PostgreSQL;"
})
@Transactional
public class DeciderValidatorTests {

    private final Input input = new Input();
    private MockMvc mvc;
    @Autowired
    private DeciderController controller;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private Validator validator;

    @Before
    public void initialize() {
        this.mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionHandlerAdvice())
                .setValidator(validator)
                .build();
        {
            input.setVariable("a");
            input.setLabel("Test");
            input.setDescription("Test");
            input.setOptional(false);
            input.setTypeId(2);
        }
    }

    @Test
    public void shouldValidateShared() throws Exception {
        // Given
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setLabel("NEW");
            model.setScript("true");
            model.setDescription("NEW");
            model.setGroupId(Integer.MAX_VALUE);
            model.setNegated(false);
            //model.setShared(false);
            model.setGroupId(1);
            model.setTemplateId(0);

        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("shared")))
                .andExpect(jsonPath("$[0].message").isNotEmpty())
        ;
    }

    @Test
    public void shouldValidateTemplateId() throws Exception {
        // Given
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setLabel("NEW");
            model.setScript("true");
            model.setDescription("NEW");
            model.setGroupId(Integer.MAX_VALUE);
            model.setNegated(false);
            model.setShared(false);
            model.setGroupId(1);
            //model.setTemplateId(0);

        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("templateId")))
                .andExpect(jsonPath("$[0].message").isNotEmpty())
        ;
    }

    @Test
    public void shouldValidateGroupId() throws Exception {
        // Given
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setLabel("NEW");
            model.setScript("true");
            model.setDescription("NEW");
            //model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            model.setTemplateId(0);

        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("groupId")))
                .andExpect(jsonPath("$[0].message").isNotEmpty())
        ;
    }

    @Test
    public void shouldValidateOperandsWhenNull() throws Exception {
        final Decider model = new Decider(Decider.Type.MIXED);
        {
            model.setLabel("Test validation");
            model.setDescription("Test validation");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            model.setOperator(Operator.XOR);
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("operands")));
    }

    @Test
    public void shouldValidateOperandsWhenEmpty() throws Exception {
        final Decider model = new Decider(Decider.Type.MIXED);
        {
            model.setLabel("Test validation");
            model.setDescription("Test validation");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            model.setOperator(Operator.XOR);
            model.setOperands(Collections.emptyList());
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("operands")));
    }

    @Test
    public void shouldValidateOperandsRecursivelyWhenOneOperand() throws Exception {
        // Given
        final Decider operand3 = new Decider(Decider.Type.DECIDER);
        {
            operand3.setLabel("21");
            operand3.setDeciderId(21L);
            operand3.setNegated(false);
        }
        final Decider operand2 = new Decider(Decider.Type.DECIDER);
        {
            operand2.setLabel("21");
            operand2.setDeciderId(21L);
            operand2.setNegated(false);
        }
        final Decider operand1 = new Decider(Decider.Type.OPERATOR);
        {
            operand1.setLabel("op");
            operand1.setOperator(Operator.XOR);
            operand1.setNegated(false);
            operand1.setOperands(Collections.singletonList(operand3));
        }
        final Decider model = new Decider(Decider.Type.MIXED);
        {
            model.setLabel("Test validation");
            model.setDescription("Test validation");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            model.setOperator(Operator.XOR);
            model.setOperands(Arrays.asList(operand1, operand2));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("operands[0].operands")));
    }

    @Test
    public void shouldValidateNegated() throws Exception {
        // Given
        final Decider left = new Decider(Decider.Type.DECIDER);
        {
            left.setLabel("left");
            left.setDeciderId(21L);
            left.setNegated(false);
        }
        final Decider right = new Decider(Decider.Type.LOCAL);
        {
            right.setLabel("right");
            right.setScript("a");
            right.setDescription("Right operand");
            right.setGroupId(1);
            right.setNegated(true);
            right.setShared(false);
            right.setTemplateId(0);
            right.setInputs(Collections.singletonList(input));
        }
        final Decider model = new Decider(Decider.Type.MIXED);
        {
            model.setLabel("Test validation");
            model.setDescription("Test validation");
            model.setGroupId(1);
            //model.setNegated(false);
            model.setShared(false);
            model.setOperator(Operator.XOR);
            model.setOperands(Arrays.asList(left, right));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("negated")));
    }

    @Test
    public void shouldValidateOperator() throws Exception {
        // Given
        final Decider left = new Decider(Decider.Type.DECIDER);
        {
            left.setLabel("left");
            left.setDeciderId(21L);
            left.setNegated(false);
        }
        final Decider right = new Decider(Decider.Type.LOCAL);
        {
            right.setLabel("right");
            right.setScript("a");
            right.setDescription("Right operand");
            right.setGroupId(1);
            right.setNegated(true);
            right.setShared(false);
            right.setTemplateId(0);
            right.setInputs(Collections.singletonList(input));
        }
        final Decider model = new Decider(Decider.Type.MIXED);
        {
            model.setLabel("Test validation");
            model.setDescription("Test validation");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            //model.setOperator(Operator.XOR);
            model.setOperands(Arrays.asList(left, right));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("operator")));
    }

    @Test
    public void shouldValidateLabelWhenNull() throws Exception {
        // Given
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            //model.setLabel("Test validation");
            model.setDescription("Test validation");
            model.setScript("a");
            model.setNegated(true);
            model.setShared(false);
            model.setGroupId(1);
            model.setTemplateId(0);
            model.setInputs(Collections.singletonList(input));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("label")));
    }

    @Test
    public void shouldValidateLabelWhenEmpty() throws Exception {
        // Given
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setLabel("");
            model.setDescription("Test validation");
            model.setScript("a");
            model.setNegated(true);
            model.setShared(false);
            model.setGroupId(1);
            model.setTemplateId(0);
            model.setInputs(Collections.singletonList(input));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("label")));
    }

    @Test
    public void shouldValidateScriptWhenNull() throws Exception {
        // Given
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setLabel("Test");
            model.setDescription("Test validation");
            //model.setScript("test");
            model.setNegated(true);
            model.setShared(false);
            model.setGroupId(1);
            model.setTemplateId(0);
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("script")));
    }

    @Test
    public void shouldValidateScriptWhenEmpty() throws Exception {
        // Given
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setLabel("Test");
            model.setDescription("Test validation");
            model.setScript("");
            model.setNegated(true);
            model.setShared(false);
            model.setGroupId(1);
            model.setTemplateId(0);
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("script")));
    }

    @Test
    public void shouldValidateGroup() throws Exception {
        // Given
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setLabel("Test");
            model.setDescription("Test validation");
            model.setScript("a");
            model.setNegated(true);
            model.setShared(false);
            model.setGroupId(Integer.MIN_VALUE);
            model.setTemplateId(0);
            model.setInputs(Collections.singletonList(input));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("groupId")));
    }

    @Test
    public void shouldValidateTemplate() throws Exception {
        // Given
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setLabel("Test");
            model.setDescription("Test validation");
            model.setScript("test");
            model.setNegated(true);
            model.setShared(false);
            model.setGroupId(1);
            model.setTemplateId(Integer.MIN_VALUE);
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("templateId")));
    }

    @Test
    public void shouldValidateType() throws Exception {
        // Given
        final Decider model = new Decider(Decider.Type.LOCAL);
        {
            model.setLabel("Test");
            model.setDescription("Test validation");
            model.setScript("test");
            model.setNegated(true);
            model.setShared(false);
            model.setGroupId(1);
            model.setTemplateId(0);
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("type")));
    }

    @Test
    public void shouldValidateDeciderId() throws Exception {
        // Given
        final Decider left = new Decider(Decider.Type.DECIDER);
        {
            left.setLabel("left");
            //left.setDeciderId(21L);
            left.setNegated(false);
        }
        final Decider right = new Decider(Decider.Type.LOCAL);
        {
            right.setLabel("right");
            right.setScript("a");
            right.setDescription("Right operand");
            right.setGroupId(1);
            right.setNegated(true);
            right.setShared(false);
            right.setTemplateId(0);
            right.setInputs(Collections.singletonList(input));
        }
        final Decider model = new Decider(Decider.Type.MIXED);
        {
            model.setLabel("Test validation");
            model.setDescription("Test validation");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            model.setOperator(Operator.XOR);
            model.setOperands(Arrays.asList(left, right));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("operands[0].deciderId")));
    }

    @Test
    public void shouldValidateDeciderGroup() throws Exception {
        // Given
        final Decider left = new Decider(Decider.Type.DECIDER);
        {
            left.setLabel("left");
            left.setDeciderId(22L);
            left.setNegated(false);
        }
        final Decider right = new Decider(Decider.Type.LOCAL);
        {
            right.setLabel("right");
            right.setScript("a");
            right.setDescription("Right operand");
            right.setGroupId(1);
            right.setNegated(true);
            right.setShared(false);
            right.setTemplateId(0);
            right.setInputs(Collections.singletonList(input));
        }
        final Decider model = new Decider(Decider.Type.MIXED);
        {
            model.setLabel("Test validation");
            model.setDescription("Test validation");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            model.setOperator(Operator.XOR);
            model.setOperands(Arrays.asList(left, right));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("operands[0].deciderId")));
    }

    @Test
    public void shouldValidateDecider() throws Exception {
        // Given
        final Decider left = new Decider(Decider.Type.DECIDER);
        {
            left.setLabel("left");
            left.setDeciderId(Long.MIN_VALUE);
            left.setNegated(false);
        }
        final Decider right = new Decider(Decider.Type.LOCAL);
        {
            right.setLabel("right");
            right.setScript("a");
            right.setDescription("Right operand");
            right.setGroupId(1);
            right.setNegated(true);
            right.setShared(false);
            right.setTemplateId(0);
            right.setInputs(Collections.singletonList(input));
        }
        final Decider model = new Decider(Decider.Type.MIXED);
        {
            model.setLabel("Test validation");
            model.setDescription("Test validation");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(false);
            model.setOperator(Operator.XOR);
            model.setOperands(Arrays.asList(left, right));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("operands[0].deciderId")));
    }

    @Test
    public void shouldValidateNullType() throws Exception {
        // Given
        final Decider model = new Decider(null);
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("type")));
    }

    @Test
    public void shouldValidateRequestBody() throws Exception {
        // Given
        final String json = mapper.writeValueAsString(null);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("Request")));
    }

    @Test
    public void shouldCompilationErrors() throws Exception {
        // Given
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setLabel("Test validation");
            model.setDescription("Test validation");
            model.setNegated(false);
            model.setScript("&v = +;");
            model.setGroupId(1);
            model.setShared(true);
            model.setTemplateId(0);
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("script")));
    }

}
