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
import io.github.up2jakarta.dte.jpa.models.Computer;
import io.github.up2jakarta.dte.jpa.models.Input;
import io.github.up2jakarta.dte.jpa.models.Output;
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
        "spring.datasource.data=classpath:groups.sql, classpath:computers.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:computers_rest;MODE=PostgreSQL;"
})
@Transactional
public class ComputerControllerTests {

    private final Input aInput = new Input();
    private final Output aOutput = new Output();
    private MockMvc mvc;
    @Autowired
    private ComputerController controller;
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
            aInput.setVariable("test");
            aInput.setLabel("Test");
            aInput.setDescription("Test");
            aInput.setOptional(false);
            aInput.setTypeId(2);

            aOutput.setVariable("test");
            aOutput.setLabel("Test");
            aOutput.setDescription("Test");
            aOutput.setOptional(false);
            aOutput.setShared(true);
            aOutput.setTypeId(2);
        }
    }

    @Test
    public void shouldGet() throws Exception {
        // Given
        final int id = 10;

        // When
        final ResultActions result = this.mvc.perform(get("/computers/{id}", id).accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.type", is(Computer.Type.PLAIN.name())))
                .andExpect(jsonPath("$.label", is("EU_M1")))
                .andExpect(jsonPath("$.description", is("EU M1")))
                .andExpect(jsonPath("$.script", is("country = \"Europe\"")))
                .andExpect(jsonPath("$.shared", is(false)))
                .andExpect(jsonPath("$.templateId", is(0)))
                .andExpect(jsonPath("$.groupId", is(2)))
                .andExpect(jsonPath("$.negated").doesNotExist())
                .andExpect(jsonPath("$.computerId").doesNotExist())
                .andExpect(jsonPath("$.deciderId").doesNotExist())
                .andExpect(jsonPath("$.children").doesNotExist())
        ;
    }

    @Test
    public void shouldGetNotFound() throws Exception {
        // Given
        final int id = 0;

        // When
        final ResultActions result = this.mvc.perform(get("/computers/{id}", id));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldPost() throws Exception {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setLabel("NEW");
            model.setDescription("NEW");
            model.setScript("test = 'NEW'");
            model.setShared(true);
            model.setTemplateId(0);
            model.setGroupId(2);
            model.setOutputs(Collections.singletonList(aOutput));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/computers/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.type", is(Computer.Type.PLAIN.name())))
                .andExpect(jsonPath("$.label", is("NEW")))
                .andExpect(jsonPath("$.description", is("NEW")))
                .andExpect(jsonPath("$.script", is("test = 'NEW'")))
                .andExpect(jsonPath("$.shared", is(true)))
                .andExpect(jsonPath("$.templateId", is(0)))
                .andExpect(jsonPath("$.groupId", is(2)))
                .andExpect(jsonPath("$.negated").doesNotExist())
                .andExpect(jsonPath("$.computerId").doesNotExist())
                .andExpect(jsonPath("$.deciderId").doesNotExist())
                .andExpect(jsonPath("$.children").doesNotExist())
        ;
    }

    @Test
    public void shouldPut() throws Exception {
        // Given
        final int id = 14;
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setLabel("PUT14");
            model.setDescription("PUT 14");
            model.setScript("test = 'PUT'");
            model.setShared(true);
            model.setTemplateId(0);
            model.setGroupId(2);
            model.setOutputs(Collections.singletonList(aOutput));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                put("/computers/{id}", id)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.type", is(Computer.Type.PLAIN.name())))
                .andExpect(jsonPath("$.label", is("PUT14")))
                .andExpect(jsonPath("$.description", is("PUT 14")))
                .andExpect(jsonPath("$.script", is("test = 'PUT'")))
                .andExpect(jsonPath("$.shared", is(true)))
                .andExpect(jsonPath("$.templateId", is(0)))
                .andExpect(jsonPath("$.groupId", is(2)))
                .andExpect(jsonPath("$.negated").doesNotExist())
                .andExpect(jsonPath("$.computerId").doesNotExist())
                .andExpect(jsonPath("$.deciderId").doesNotExist())
                .andExpect(jsonPath("$.children").doesNotExist());
    }

    @Test
    public void shouldNotPut() throws Exception {
        // Given
        final int id = 30; // Mixed
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setLabel("PUT_30");
            model.setDescription("PUT 30");
            model.setScript("test = 'PUT'");
            model.setShared(true);
            model.setTemplateId(0);
            model.setGroupId(2);
            model.setOutputs(Collections.singletonList(aOutput));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                put("/computers/{id}", id)
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
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setLabel("PUT101");
            model.setDescription("PUT 101");
            model.setScript("test = 'PUT'");
            model.setShared(true);
            model.setTemplateId(0);
            model.setGroupId(2);
            model.setOutputs(Collections.singletonList(aOutput));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                put("/computers/{id}", id)
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
        final int id = 15;

        // When
        final ResultActions result = this.mvc.perform(delete("/computers/{id}", id));

        // Then
        result.andExpect(status().isNoContent());
    }

    @Test
    public void shouldDeleteNotFound() throws Exception {
        // Given
        final int id = 0;

        // When
        final ResultActions result = this.mvc.perform(delete("/computers/{id}", id));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldSearchByGroup() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(get("/computers/search")
                .param("groupId", "2")
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
        final ResultActions result = this.mvc.perform(get("/computers/search")
                //.param("groupId", "2")
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
        final int id = 20;

        // When
        final ResultActions result = this.mvc.perform(get("/computers/{id}", id).accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.type", is(Computer.Type.MIXED.name())))
                .andExpect(jsonPath("$.label", is("MIX")))
                .andExpect(jsonPath("$.description", is("MIXED")))
                .andExpect(jsonPath("$.script").doesNotExist())
                .andExpect(jsonPath("$.groupId", is(2)))
                .andExpect(jsonPath("$.shared", is(false)))
                .andExpect(jsonPath("$.templateId").doesNotExist())
                .andExpect(jsonPath("$.negated").doesNotExist())
                .andExpect(jsonPath("$.computerId").doesNotExist())
                .andExpect(jsonPath("$.deciderId").doesNotExist())
                .andExpect(jsonPath("$.children").isNotEmpty())

                .andExpect(jsonPath("$.children[0].id", is(21)))
                .andExpect(jsonPath("$.children[0].type", is(Computer.Type.DECIDER.name())))
                .andExpect(jsonPath("$.children[0].label", is("EUR")))
                .andExpect(jsonPath("$.children[0].description", is("EUR")))
                .andExpect(jsonPath("$.children[0].deciderId", is(10)))
                .andExpect(jsonPath("$.children[0].negated", is(false)))
                .andExpect(jsonPath("$.children[0].shared").doesNotExist())
                .andExpect(jsonPath("$.children[0].templateId").doesNotExist())
                .andExpect(jsonPath("$.children[0].script").doesNotExist())
                .andExpect(jsonPath("$.children[0].groupId").doesNotExist())
                .andExpect(jsonPath("$.children[0].computerId").doesNotExist())
                .andExpect(jsonPath("$.children[0].children").isNotEmpty())

                .andExpect(jsonPath("$.children[0].children[0].id", is(22)))
                .andExpect(jsonPath("$.children[0].children[0].type", is(Computer.Type.COMPUTER.name())))
                .andExpect(jsonPath("$.children[0].children[0].label", is("EU1")))
                .andExpect(jsonPath("$.children[0].children[0].description", is("EU M1")))
                .andExpect(jsonPath("$.children[0].children[0].computerId", is(10)))
                .andExpect(jsonPath("$.children[0].children[0].shared").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[0].templateId").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[0].negated").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[0].script").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[0].groupId").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[0].deciderId").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[0].children").doesNotExist())

                .andExpect(jsonPath("$.children[0].children[1].id", is(23)))
                .andExpect(jsonPath("$.children[0].children[1].type", is(Computer.Type.COMPUTER.name())))
                .andExpect(jsonPath("$.children[0].children[1].label", is("EU2")))
                .andExpect(jsonPath("$.children[0].children[1].description", is("EU M2")))
                .andExpect(jsonPath("$.children[0].children[1].computerId", is(11)))
                .andExpect(jsonPath("$.children[0].children[1].shared").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[1].templateId").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[1].negated").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[1].script").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[1].groupId").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[1].deciderId").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[1].children").doesNotExist())

                .andExpect(jsonPath("$.children[1].id", is(24)))
                .andExpect(jsonPath("$.children[1].type", is(Computer.Type.DECISION.name())))
                .andExpect(jsonPath("$.children[1].label", is("TND")))
                .andExpect(jsonPath("$.children[1].description", is("TND")))
                .andExpect(jsonPath("$.children[1].deciderId").doesNotExist())
                .andExpect(jsonPath("$.children[1].negated", is(true)))
                .andExpect(jsonPath("$.children[1].script", is("currency == \"TND\"")))
                .andExpect(jsonPath("$.children[1].shared", is(false)))
                .andExpect(jsonPath("$.children[1].templateId", is(0)))
                .andExpect(jsonPath("$.children[1].groupId", is(2)))
                .andExpect(jsonPath("$.children[1].computerId").doesNotExist())
                .andExpect(jsonPath("$.children[1].children").isNotEmpty())

                .andExpect(jsonPath("$.children[1].children[0].id", is(25)))
                .andExpect(jsonPath("$.children[1].children[0].type", is(Computer.Type.LOCAL.name())))
                .andExpect(jsonPath("$.children[1].children[0].label", is("TN")))
                .andExpect(jsonPath("$.children[1].children[0].description", is("TN")))
                .andExpect(jsonPath("$.children[1].children[0].script", is("country = \"Tunisia\"")))
                .andExpect(jsonPath("$.children[1].children[0].shared", is(false)))
                .andExpect(jsonPath("$.children[1].children[0].templateId", is(0)))
                .andExpect(jsonPath("$.children[1].children[0].negated").doesNotExist())
                .andExpect(jsonPath("$.children[1].children[0].computerId").doesNotExist())
                .andExpect(jsonPath("$.children[1].children[0].groupId", is(2)))
                .andExpect(jsonPath("$.children[1].children[0].deciderId").doesNotExist())
                .andExpect(jsonPath("$.children[1].children[0].children").doesNotExist())

                .andExpect(jsonPath("$.children[2].id", is(26)))
                .andExpect(jsonPath("$.children[2].type", is(Computer.Type.DEFAULT.name())))
                .andExpect(jsonPath("$.children[2].label", is("DEF")))
                .andExpect(jsonPath("$.children[2].description").doesNotExist())
                .andExpect(jsonPath("$.children[2].shared").doesNotExist())
                .andExpect(jsonPath("$.children[2].templateId").doesNotExist())
                .andExpect(jsonPath("$.children[2].script").doesNotExist())
                .andExpect(jsonPath("$.children[2].negated").doesNotExist())
                .andExpect(jsonPath("$.children[2].groupId").doesNotExist())
                .andExpect(jsonPath("$.children[2].computerId").doesNotExist())
                .andExpect(jsonPath("$.children[2].deciderId").doesNotExist())
                .andExpect(jsonPath("$.children[2].children").isNotEmpty())

                .andExpect(jsonPath("$.children[2].children[0].id", is(27)))
                .andExpect(jsonPath("$.children[2].children[0].type", is(Computer.Type.COMPUTER.name())))
                .andExpect(jsonPath("$.children[2].children[0].label", is("?M1")))
                .andExpect(jsonPath("$.children[2].children[0].description", is("?? M1")))
                .andExpect(jsonPath("$.children[2].children[0].computerId", is(12)))
                .andExpect(jsonPath("$.children[2].children[0].templateId").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[0].script").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[0].negated").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[0].script").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[0].groupId").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[0].deciderId").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[0].children").doesNotExist())

                .andExpect(jsonPath("$.children[2].children[1].id", is(28)))
                .andExpect(jsonPath("$.children[2].children[1].type", is(Computer.Type.COMPUTER.name())))
                .andExpect(jsonPath("$.children[2].children[1].label", is("?M2")))
                .andExpect(jsonPath("$.children[2].children[1].description", is("?? M2")))
                .andExpect(jsonPath("$.children[2].children[1].computerId", is(13)))
                .andExpect(jsonPath("$.children[2].children[1].shared").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[1].templateId").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[1].negated").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[1].script").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[1].groupId").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[1].deciderId").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[1].children").doesNotExist())
        ;
    }

    @Test
    public void shouldPostMixed() throws Exception {
        // Given
        final Computer link10 = new Computer(Computer.Type.COMPUTER);
        {
            link10.setLabel("P1/Link 10");
            link10.setComputerId(10L);
        }
        final Computer link12 = new Computer(Computer.Type.COMPUTER);
        {
            link12.setLabel("P1/Link 12");
            link12.setComputerId(12L);
        }
        final Computer local = new Computer(Computer.Type.LOCAL);
        {
            local.setLabel("P1/TN_TEST");
            local.setScript("test = \"Tunisia\"");
            local.setDescription("Tunisia");
            local.setShared(false);
            local.setTemplateId(0);
            local.setOutputs(Collections.singletonList(aOutput));
        }

        final Computer linkDecision = new Computer(Computer.Type.DECIDER);
        {
            linkDecision.setLabel("P1/Link 10");
            linkDecision.setDeciderId(10L);
            linkDecision.setNegated(false);
            linkDecision.setChildren(Collections.singletonList(link10));
        }
        final Computer localDecision = new Computer(Computer.Type.DECISION);
        {
            localDecision.setLabel("P1/TND_TEST");
            localDecision.setScript("test == \"TND\"");
            localDecision.setDescription("Tunisian Dinar");
            localDecision.setGroupId(1);
            localDecision.setNegated(true);
            localDecision.setShared(true);
            localDecision.setTemplateId(0);
            localDecision.setChildren(Collections.singletonList(local));
            localDecision.setInputs(Collections.singletonList(aInput));
        }

        final Computer any = new Computer(Computer.Type.DEFAULT);
        {
            any.setLabel("Any");
            any.setChildren(Collections.singletonList(link12));
        }

        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("P1/NEW_TEST");
            model.setDescription("Test New");
            model.setShared(false);
            model.setGroupId(2);
            model.setChildren(Arrays.asList(linkDecision, localDecision, any));
            model.setOutputs(Collections.singletonList(aOutput));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/computers/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.type", is(Computer.Type.MIXED.name())))
                .andExpect(jsonPath("$.label", is("P1/NEW_TEST")))
                .andExpect(jsonPath("$.description", is("Test New")))
                .andExpect(jsonPath("$.groupId", is(2)))
                .andExpect(jsonPath("$.shared", is(false)))
                .andExpect(jsonPath("$.templateId").doesNotExist())
                .andExpect(jsonPath("$.script").doesNotExist())
                .andExpect(jsonPath("$.negated").doesNotExist())
                .andExpect(jsonPath("$.computerId").doesNotExist())
                .andExpect(jsonPath("$.deciderId").doesNotExist())
                .andExpect(jsonPath("$.children").isNotEmpty())

                .andExpect(jsonPath("$.children[0].id").isNotEmpty())
                .andExpect(jsonPath("$.children[0].type", is(Computer.Type.DECIDER.name())))
                .andExpect(jsonPath("$.children[0].label", is("P1/Link 10")))
                .andExpect(jsonPath("$.children[0].description", is("EUR")))
                .andExpect(jsonPath("$.children[0].deciderId", is(10)))
                .andExpect(jsonPath("$.children[0].negated", is(false)))
                .andExpect(jsonPath("$.children[0].shared").doesNotExist())
                .andExpect(jsonPath("$.children[0].templateId").doesNotExist())
                .andExpect(jsonPath("$.children[0].script").doesNotExist())
                .andExpect(jsonPath("$.children[0].groupId").doesNotExist())
                .andExpect(jsonPath("$.children[0].computerId").doesNotExist())
                .andExpect(jsonPath("$.children[0].children").isNotEmpty())

                .andExpect(jsonPath("$.children[0].children[0].id").isNotEmpty())
                .andExpect(jsonPath("$.children[0].children[0].type", is(Computer.Type.COMPUTER.name())))
                .andExpect(jsonPath("$.children[0].children[0].label", is("P1/Link 10")))
                .andExpect(jsonPath("$.children[0].children[0].description", is("EU M1")))
                .andExpect(jsonPath("$.children[0].children[0].computerId", is(10)))
                .andExpect(jsonPath("$.children[0].shared").doesNotExist())
                .andExpect(jsonPath("$.children[0].templateId").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[0].negated").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[0].script").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[0].groupId").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[0].deciderId").doesNotExist())
                .andExpect(jsonPath("$.children[0].children[0].children").doesNotExist())

                .andExpect(jsonPath("$.children[1].id").isNotEmpty())
                .andExpect(jsonPath("$.children[1].type", is(Computer.Type.DECISION.name())))
                .andExpect(jsonPath("$.children[1].label", is("P1/TND_TEST")))
                .andExpect(jsonPath("$.children[1].description", is("Tunisian Dinar")))
                .andExpect(jsonPath("$.children[1].deciderId").doesNotExist())
                .andExpect(jsonPath("$.children[1].negated", is(true)))
                .andExpect(jsonPath("$.children[1].script", is("test == \"TND\"")))
                .andExpect(jsonPath("$.children[1].shared", is(true)))
                .andExpect(jsonPath("$.children[1].templateId", is(0)))
                .andExpect(jsonPath("$.children[1].groupId", is(2)))
                .andExpect(jsonPath("$.children[1].computerId").doesNotExist())
                .andExpect(jsonPath("$.children[1].children").isNotEmpty())

                .andExpect(jsonPath("$.children[1].children[0].id").isNotEmpty())
                .andExpect(jsonPath("$.children[1].children[0].type", is(Computer.Type.LOCAL.name())))
                .andExpect(jsonPath("$.children[1].children[0].label", is("P1/TN_TEST")))
                .andExpect(jsonPath("$.children[1].children[0].description", is("Tunisia")))
                .andExpect(jsonPath("$.children[1].children[0].script", is("test = \"Tunisia\"")))
                .andExpect(jsonPath("$.children[1].children[0].shared", is(false)))
                .andExpect(jsonPath("$.children[1].children[0].templateId", is(0)))
                .andExpect(jsonPath("$.children[1].children[0].negated").doesNotExist())
                .andExpect(jsonPath("$.children[1].children[0].computerId").doesNotExist())
                .andExpect(jsonPath("$.children[1].children[0].groupId", is(2)))
                .andExpect(jsonPath("$.children[1].children[0].deciderId").doesNotExist())
                .andExpect(jsonPath("$.children[1].children[0].children").doesNotExist())

                .andExpect(jsonPath("$.children[2].id").isNotEmpty())
                .andExpect(jsonPath("$.children[2].type", is(Computer.Type.DEFAULT.name())))
                .andExpect(jsonPath("$.children[2].label", is("Any")))
                .andExpect(jsonPath("$.children[2].description").doesNotExist())
                .andExpect(jsonPath("$.children[2].shared").doesNotExist())
                .andExpect(jsonPath("$.children[2].templateId").doesNotExist())
                .andExpect(jsonPath("$.children[2].script").doesNotExist())
                .andExpect(jsonPath("$.children[2].negated").doesNotExist())
                .andExpect(jsonPath("$.children[2].groupId").doesNotExist())
                .andExpect(jsonPath("$.children[2].computerId").doesNotExist())
                .andExpect(jsonPath("$.children[2].deciderId").doesNotExist())
                .andExpect(jsonPath("$.children[2].children").isNotEmpty())

                .andExpect(jsonPath("$.children[2].children[0].id").isNotEmpty())
                .andExpect(jsonPath("$.children[2].children[0].type", is(Computer.Type.COMPUTER.name())))
                .andExpect(jsonPath("$.children[2].children[0].label", is("P1/Link 12")))
                .andExpect(jsonPath("$.children[2].children[0].description", is("?? M1")))
                .andExpect(jsonPath("$.children[2].children[0].computerId", is(12)))
                .andExpect(jsonPath("$.children[2].children[0].templateId").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[0].script").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[0].negated").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[0].script").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[0].groupId").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[0].deciderId").doesNotExist())
                .andExpect(jsonPath("$.children[2].children[0].children").doesNotExist())
        ;
    }

    @Test
    public void shouldPutMixed() throws Exception {
        // Given
        final int id = 30;
        final Computer country = new Computer(Computer.Type.LOCAL);
        {
            country.setLabel("TN_TEST");
            country.setDescription("Tunisia");
            country.setScript("test = \"Tunisia\"");
            country.setShared(false);
            country.setTemplateId(0);
            country.setOutputs(Collections.singletonList(aOutput));
        }
        final Computer continent = new Computer(Computer.Type.LOCAL);
        {
            continent.setLabel("AF_TEST");
            continent.setDescription("Africa");
            continent.setScript("test = \"Africa\"");
            continent.setShared(false);
            continent.setTemplateId(0);
            continent.setOutputs(Collections.singletonList(aOutput));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("CD_TEST");
            model.setDescription("CD Test");
            model.setGroupId(2);
            model.setShared(true);
            model.setChildren(Arrays.asList(country, continent));
            model.setOutputs(Collections.singletonList(aOutput));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                put("/computers/{id}", id)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.type", is(Computer.Type.MIXED.name())))
                .andExpect(jsonPath("$.label", is("CD_TEST")))
                .andExpect(jsonPath("$.description", is("CD Test")))
                .andExpect(jsonPath("$.groupId", is(2)))
                .andExpect(jsonPath("$.shared", is(true)))
                .andExpect(jsonPath("$.templateId").doesNotExist())
                .andExpect(jsonPath("$.script").doesNotExist())
                .andExpect(jsonPath("$.negated").doesNotExist())
                .andExpect(jsonPath("$.computerId").doesNotExist())
                .andExpect(jsonPath("$.deciderId").doesNotExist())
                .andExpect(jsonPath("$.children").isNotEmpty())

                .andExpect(jsonPath("$.children[0].id").isNotEmpty())
                .andExpect(jsonPath("$.children[0].type", is(Computer.Type.LOCAL.name())))
                .andExpect(jsonPath("$.children[0].label", is("TN_TEST")))
                .andExpect(jsonPath("$.children[0].description", is("Tunisia")))
                .andExpect(jsonPath("$.children[0].script", is("test = \"Tunisia\"")))
                .andExpect(jsonPath("$.children[0].shared", is(false)))
                .andExpect(jsonPath("$.children[0].templateId", is(0)))
                .andExpect(jsonPath("$.children[0].negated").doesNotExist())
                .andExpect(jsonPath("$.children[0].computerId").doesNotExist())
                .andExpect(jsonPath("$.children[0].groupId", is(2)))
                .andExpect(jsonPath("$.children[0].deciderId").doesNotExist())
                .andExpect(jsonPath("$.children[0].children").doesNotExist())

                .andExpect(jsonPath("$.children[1].id").isNotEmpty())
                .andExpect(jsonPath("$.children[1].type", is(Computer.Type.LOCAL.name())))
                .andExpect(jsonPath("$.children[1].label", is("AF_TEST")))
                .andExpect(jsonPath("$.children[1].description", is("Africa")))
                .andExpect(jsonPath("$.children[1].script", is("test = \"Africa\"")))
                .andExpect(jsonPath("$.children[1].shared", is(false)))
                .andExpect(jsonPath("$.children[1].templateId", is(0)))
                .andExpect(jsonPath("$.children[1].negated").doesNotExist())
                .andExpect(jsonPath("$.children[1].computerId").doesNotExist())
                .andExpect(jsonPath("$.children[1].groupId", is(2)))
                .andExpect(jsonPath("$.children[1].deciderId").doesNotExist())
                .andExpect(jsonPath("$.children[1].children").doesNotExist())
        ;
    }

    @Test
    public void shouldNotPutMixed() throws Exception {
        // Given
        final int id = 14; // Plain
        final Computer country = new Computer(Computer.Type.LOCAL);
        {
            country.setLabel("TN_TEST");
            country.setDescription("Tunisia");
            country.setScript("test = \"Tunisia\"");
            country.setShared(false);
            country.setTemplateId(0);
            country.setOutputs(Collections.singletonList(aOutput));
        }
        final Computer continent = new Computer(Computer.Type.LOCAL);
        {
            continent.setLabel("AF_TEST");
            continent.setDescription("Africa");
            continent.setScript("test = \"Africa\"");
            continent.setShared(false);
            continent.setTemplateId(0);
            continent.setOutputs(Collections.singletonList(aOutput));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("PUT_14");
            model.setDescription("CD Test");
            model.setGroupId(2);
            model.setShared(true);
            model.setChildren(Arrays.asList(country, continent));
            model.setOutputs(Collections.singletonList(aOutput));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                put("/computers/{id}", id)
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
        final int id = 40;

        // When
        final ResultActions result = this.mvc.perform(delete("/computers/{id}", id));

        // Then
        result.andExpect(status().isNoContent());
    }

    @Test
    public void shouldLabelUnique() throws Exception {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setLabel("MIX");
            model.setScript("test = 'UC'");
            model.setDescription("Test Label Unique");
            model.setShared(true);
            model.setTemplateId(0);
            model.setGroupId(1);
            model.setOutputs(Collections.singletonList(aOutput));
        }
        final String json = mapper.writeValueAsString(model);

        // When
        this.mvc.perform(
                post("/computers/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        final ResultActions result = this.mvc.perform(
                post("/computers/")
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
