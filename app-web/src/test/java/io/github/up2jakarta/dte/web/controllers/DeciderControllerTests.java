package io.github.up2jakarta.dte.web.controllers;

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

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@WebAppConfiguration
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql, classpath:deciders.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:deciders_rest;MODE=PostgreSQL;"
})
@Transactional
public class DeciderControllerTests {

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
    public void shouldGet() throws Exception {
        // Given
        final int id = 10;

        // When
        final ResultActions result = this.mvc.perform(get("/deciders/{id}", id).accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.type", is(Decider.Type.PLAIN.name())))
                .andExpect(jsonPath("$.label", is("AeqB")))
                .andExpect(jsonPath("$.description", is("a eq b")))
                .andExpect(jsonPath("$.script", is("a = b")))
                .andExpect(jsonPath("$.groupId", is(1)))
                .andExpect(jsonPath("$.negated", is(false)))
                .andExpect(jsonPath("$.shared", is(false)))
                .andExpect(jsonPath("$.templateId", is(0)))
                .andExpect(jsonPath("$.operator").doesNotExist())
                .andExpect(jsonPath("$.deciderId").doesNotExist())
                .andExpect(jsonPath("$.operands").doesNotExist())
        ;
    }

    @Test
    public void shouldGetNotFound() throws Exception {
        // Given
        final int id = 0;

        // When
        final ResultActions result = this.mvc.perform(get("/deciders/{id}", id));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldPost() throws Exception {
        // Given
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setLabel("NEW");
            model.setScript("true");
            model.setDescription("NEW");
            model.setGroupId(1);
            model.setNegated(true);
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
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.type", is(Decider.Type.PLAIN.name())))
                .andExpect(jsonPath("$.label", is(model.getLabel())))
                .andExpect(jsonPath("$.description", is(model.getDescription())))
                .andExpect(jsonPath("$.script", is(model.getScript())))
                .andExpect(jsonPath("$.groupId", is(1)))
                .andExpect(jsonPath("$.negated", is(true)))
                .andExpect(jsonPath("$.shared", is(true)))
                .andExpect(jsonPath("$.templateId", is(0)))
                .andExpect(jsonPath("$.operator").doesNotExist())
                .andExpect(jsonPath("$.deciderId").doesNotExist())
                .andExpect(jsonPath("$.operands").doesNotExist())
        ;
    }

    @Test
    public void shouldPut() throws Exception {
        // Given
        final int id = 11;
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setLabel("PUT11");
            model.setScript("true");
            model.setDescription("PUT 11");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(true);
            model.setTemplateId(0);
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                put("/deciders/{id}", id)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.type", is(Decider.Type.PLAIN.name())))
                .andExpect(jsonPath("$.label", is(model.getLabel())))
                .andExpect(jsonPath("$.description", is(model.getDescription())))
                .andExpect(jsonPath("$.script", is(model.getScript())))
                .andExpect(jsonPath("$.groupId", is(1)))
                .andExpect(jsonPath("$.negated", is(false)))
                .andExpect(jsonPath("$.shared", is(true)))
                .andExpect(jsonPath("$.templateId", is(0)))
                .andExpect(jsonPath("$.operator").doesNotExist())
                .andExpect(jsonPath("$.deciderId").doesNotExist())
                .andExpect(jsonPath("$.operands").doesNotExist());
    }

    @Test
    public void shouldNotPut() throws Exception {
        // Given
        final int id = 50;
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setLabel("PUT_50");
            model.setScript("true");
            model.setDescription("PUT 11");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(true);
            model.setTemplateId(0);
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                put("/deciders/{id}", id)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("type")))
                .andExpect(jsonPath("$[0].message").isNotEmpty())
        ;
    }

    @Test
    public void shouldPutNotFound() throws Exception {
        // Given
        final long id = Long.MAX_VALUE;
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setLabel("PUT102");
            model.setScript("true");
            model.setDescription("PUT 102");
            model.setGroupId(1);
            model.setNegated(false);
            model.setShared(true);
            model.setTemplateId(0);
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                put("/deciders/{id}", id)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldDelete() throws Exception {
        // Given
        final int id = 12;

        // When
        final ResultActions result = this.mvc.perform(delete("/deciders/{id}", id));

        // Then
        result.andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteNotFound() throws Exception {
        // Given
        final int id = 0;

        // When
        final ResultActions result = this.mvc.perform(delete("/deciders/{id}", id));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldSearchByGroup() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(get("/deciders/search")
                .param("groupId", "1")
                .accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].label").isNotEmpty())
                .andExpect(jsonPath("$[0].description").isNotEmpty());
    }

    @Test
    public void shouldSearchValidateGroupId() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(get("/deciders/search")
                //.param("groupId", "1")
                .accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("groupId")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    public void shouldGetMixed() throws Exception {
        // Given
        final int id = 30;

        // When
        final ResultActions result = this.mvc.perform(get("/deciders/{id}", id).accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.type", is(Decider.Type.MIXED.name())))
                .andExpect(jsonPath("$.label", is("A&B")))
                .andExpect(jsonPath("$.description", is("A and B")))
                .andExpect(jsonPath("$.negated", is(false)))
                .andExpect(jsonPath("$.operator", is(Operator.AND.name())))
                .andExpect(jsonPath("$.groupId", is(1)))
                .andExpect(jsonPath("$.shared", is(false)))
                .andExpect(jsonPath("$.templateId").doesNotExist())
                .andExpect(jsonPath("$.script").doesNotExist())
                .andExpect(jsonPath("$.deciderId").doesNotExist())
                .andExpect(jsonPath("$.operands").isNotEmpty())

                .andExpect(jsonPath("$.operands[0].id", is(31)))
                .andExpect(jsonPath("$.operands[0].type", is(Decider.Type.DECIDER.name())))
                .andExpect(jsonPath("$.operands[0].label", is("A")))
                .andExpect(jsonPath("$.operands[0].description", is("A")))
                .andExpect(jsonPath("$.operands[0].deciderId", is(20)))
                .andExpect(jsonPath("$.operands[0].negated", is(true)))
                .andExpect(jsonPath("$.operands[0].shared").doesNotExist())
                .andExpect(jsonPath("$.operands[0].templateId").doesNotExist())
                .andExpect(jsonPath("$.operands[0].script").doesNotExist())
                .andExpect(jsonPath("$.operands[0].groupId").doesNotExist())
                .andExpect(jsonPath("$.operands[0].operator").doesNotExist())
                .andExpect(jsonPath("$.operands[0].operands").doesNotExist())

                .andExpect(jsonPath("$.operands[1].id", is(32)))
                .andExpect(jsonPath("$.operands[1].type", is(Decider.Type.LOCAL.name())))
                .andExpect(jsonPath("$.operands[1].label", is("LB")))
                .andExpect(jsonPath("$.operands[1].description", is("LB")))
                .andExpect(jsonPath("$.operands[1].script", is("b")))
                .andExpect(jsonPath("$.operands[1].negated", is(false)))
                .andExpect(jsonPath("$.operands[1].shared", is(false)))
                .andExpect(jsonPath("$.operands[1].templateId", is(0)))
                .andExpect(jsonPath("$.operands[1].groupId").doesNotExist())
                .andExpect(jsonPath("$.operands[1].operator").doesNotExist())
                .andExpect(jsonPath("$.operands[1].deciderId").doesNotExist())
                .andExpect(jsonPath("$.operands[1].operands").doesNotExist())
        ;
    }

    @Test
    public void shouldPostMixed() throws Exception {
        // Given
        final Decider left = new Decider(Decider.Type.DECIDER);
        {
            left.setLabel("Left");
            left.setDeciderId(21L);
            left.setNegated(false);
        }
        final Decider right = new Decider(Decider.Type.LOCAL);
        {
            right.setLabel("Right");
            right.setScript("a");
            right.setDescription("Right operand");
            right.setTemplateId(0);
            right.setNegated(true);
            right.setShared(false);
            right.setInputs(Collections.singletonList(input));
        }
        final Decider model = new Decider(Decider.Type.MIXED);
        {
            model.setLabel("a^!b");
            model.setDescription("a xor not b");
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
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.type", is(model.getType().name())))
                .andExpect(jsonPath("$.label", is(model.getLabel())))
                .andExpect(jsonPath("$.description", is(model.getDescription())))
                .andExpect(jsonPath("$.negated", is(model.getNegated())))
                .andExpect(jsonPath("$.operator", is(model.getOperator().name())))
                .andExpect(jsonPath("$.groupId", is(1)))
                .andExpect(jsonPath("$.shared", is(false)))
                .andExpect(jsonPath("$.operands").isNotEmpty())

                .andExpect(jsonPath("$.operands[0].id").isNotEmpty())
                .andExpect(jsonPath("$.operands[0].type", is(left.getType().name())))
                .andExpect(jsonPath("$.operands[0].deciderId", is(left.getDeciderId().intValue())))
                .andExpect(jsonPath("$.operands[0].negated", is(left.getNegated())))
                .andExpect(jsonPath("$.operands[0].label", is(left.getLabel())))

                .andExpect(jsonPath("$.operands[1].id").isNotEmpty())
                .andExpect(jsonPath("$.operands[1].type", is(right.getType().name())))
                .andExpect(jsonPath("$.operands[1].label", is(right.getLabel())))
                .andExpect(jsonPath("$.operands[1].description", is(right.getDescription())))
                .andExpect(jsonPath("$.operands[1].script", is(right.getScript())))
                .andExpect(jsonPath("$.operands[1].negated", is(right.getNegated())))
                .andExpect(jsonPath("$.operands[1].shared", is(right.getShared())))
                .andExpect(jsonPath("$.operands[1].templateId", is(right.getTemplateId())));
    }

    @Test
    public void shouldPutMixed() throws Exception {
        // Given
        final int id = 50;
        final Decider left = new Decider(Decider.Type.DECIDER);
        {
            left.setLabel("Left 50");
            left.setId(51L);
            left.setDeciderId(21L);
            left.setNegated(false);
        }
        final Decider right = new Decider(Decider.Type.LOCAL);
        {
            right.setLabel("Right 50");
            right.setScript("a");
            right.setDescription("Right operand");
            right.setNegated(true);
            right.setShared(true);
            right.setTemplateId(0);
            right.setInputs(Collections.singletonList(input));
        }
        final Decider model = new Decider(Decider.Type.MIXED);
        {
            model.setLabel("Test 50");
            model.setDescription("Test 50");
            model.setGroupId(1);
            model.setNegated(true);
            model.setShared(true);
            model.setOperator(Operator.AND);
            model.setOperands(Arrays.asList(left, right));
        }

        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                put("/deciders/{id}", id)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.type", is(model.getType().name())))
                .andExpect(jsonPath("$.label", is(model.getLabel())))
                .andExpect(jsonPath("$.groupId", is(model.getGroupId())))
                .andExpect(jsonPath("$.negated", is(model.getNegated())))
                .andExpect(jsonPath("$.operator", is(model.getOperator().name())))
                .andExpect(jsonPath("$.description", is(model.getDescription())))
                .andExpect(jsonPath("$.shared", is(model.getShared())))
                .andExpect(jsonPath("$.operands").isNotEmpty())

                .andExpect(jsonPath("$.operands[0].id").isNotEmpty())
                .andExpect(jsonPath("$.operands[0].label", is(left.getLabel())))
                .andExpect(jsonPath("$.operands[0].type", is(left.getType().name())))
                .andExpect(jsonPath("$.operands[0].deciderId", is(left.getDeciderId().intValue())))
                .andExpect(jsonPath("$.operands[0].negated", is(left.getNegated())))

                .andExpect(jsonPath("$.operands[1].id").isNotEmpty())
                .andExpect(jsonPath("$.operands[1].type", is(right.getType().name())))
                .andExpect(jsonPath("$.operands[1].label", is(right.getLabel())))
                .andExpect(jsonPath("$.operands[1].description", is(right.getDescription())))
                .andExpect(jsonPath("$.operands[1].script", is(right.getScript())))
                .andExpect(jsonPath("$.operands[1].negated", is(right.getNegated())))
                .andExpect(jsonPath("$.operands[1].shared", is(right.getShared())))
                .andExpect(jsonPath("$.operands[1].templateId", is(right.getTemplateId())))
                .andExpect(jsonPath("$.operands[1].groupId").doesNotExist());
    }

    @Test
    public void shouldNotPutMixed() throws Exception {
        // Given
        final int id = 11;
        final Decider left = new Decider(Decider.Type.DECIDER);
        {
            left.setLabel("Left 11");
            left.setId(51L);
            left.setDeciderId(21L);
            left.setNegated(false);
        }
        final Decider right = new Decider(Decider.Type.LOCAL);
        {
            right.setLabel("Right 11");
            right.setScript("a");
            right.setDescription("Right operand");
            right.setNegated(true);
            right.setShared(true);
            right.setTemplateId(0);
            right.setInputs(Collections.singletonList(input));
        }
        final Decider model = new Decider(Decider.Type.MIXED);
        {
            model.setLabel("PUT_11");
            model.setDescription("Test 50");
            model.setGroupId(1);
            model.setNegated(true);
            model.setShared(true);
            model.setOperator(Operator.AND);
            model.setOperands(Arrays.asList(left, right));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                put("/deciders/{id}", id)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("type")))
                .andExpect(jsonPath("$[0].message").isNotEmpty())
        ;
    }

    @Test
    public void shouldDeleteMixed() throws Exception {
        // Given
        final Integer id = 40;

        // When
        final ResultActions result = this.mvc.perform(delete("/deciders/{id}", id));

        // Then
        result.andExpect(status().isNoContent());
    }

    @Test
    public void shouldLabelUnique() throws Exception {
        // Given
        final Decider model = new Decider(Decider.Type.PLAIN);
        {
            model.setLabel("TEST_UC");
            model.setScript("true");
            model.setDescription("Test Label Unique");
            model.setGroupId(1);
            model.setNegated(true);
            model.setShared(true);
            model.setTemplateId(0);
        }
        final String json = mapper.writeValueAsString(model);

        // When
        this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        final ResultActions result = this.mvc.perform(
                post("/deciders/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("label")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

}
