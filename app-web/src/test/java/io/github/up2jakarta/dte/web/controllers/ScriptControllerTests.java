package io.github.up2jakarta.dte.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import io.github.up2jakarta.dte.web.TestConfiguration;
import io.github.up2jakarta.dte.web.config.ExceptionHandlerAdvice;

import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@WebAppConfiguration
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql, classpath:computers.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:computers_rest;MODE=PostgreSQL;"
})
public class ScriptControllerTests {

    private MockMvc mvc;
    @Autowired
    private ScriptController controller;
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
    }

    @Test
    public void shouldGetComputerScript() throws Exception {
        // Given
        final long id = 10L;

        // When
        final ResultActions result = this.mvc.perform(get("/scripts/{id}", id).accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.label", is("EU_M1")))
                .andExpect(jsonPath("$.description", is("EU M1")))
                .andExpect(jsonPath("$.script", is("country = \"Europe\"")))

                .andExpect(jsonPath("$.shared", is(false)))
                .andExpect(jsonPath("$.templateId", is(0)))
                .andExpect(jsonPath("$.groupId", is(2)))

                .andExpect(jsonPath("$.inputs").isEmpty())
                .andExpect(jsonPath("$.outputs").isEmpty())
        ;
    }

    @Test
    public void shouldGetRootScriptNotFound() throws Exception {
        // Given
        final int id = 20;

        // When
        final ResultActions result = this.mvc.perform(get("/scripts/{id}", id).accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetCalcScriptNotFound() throws Exception {
        // Given
        final int id = 24;

        // When
        final ResultActions result = this.mvc.perform(get("/scripts/{id}", id).accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetDeciderScriptNotFound() throws Exception {
        // Given
        final int id = 10;
        final char key = 'B';

        // When
        final ResultActions result = this.mvc.perform(get("/scripts/{k}/{i}", key, id)
                .accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetDeciderScript() throws Exception {
        // Given
        final int id = 10;
        final char key = 'S';

        // When
        final ResultActions result = this.mvc.perform(get("/scripts/{k}/{i}", key, id)
                .accept(APPLICATION_JSON_VALUE));

        // Then
        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id.id", is(id)))
                .andExpect(jsonPath("$.id.type", is(String.valueOf(key))))
                .andExpect(jsonPath("$.label", is("EUR")))
                .andExpect(jsonPath("$.description", is("EUR")))
                .andExpect(jsonPath("$.script", is("currency == \"EUR\"")))

                .andExpect(jsonPath("$.negated", is(false)))
                .andExpect(jsonPath("$.shared", is(false)))
                .andExpect(jsonPath("$.templateId", is(0)))
                .andExpect(jsonPath("$.groupId", is(2)))
                .andExpect(jsonPath("$.inputs").isEmpty())
        ;
    }

    @Test
    public void shouldSearchComputers() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(
                get("/scripts/computers")
                        .param("groupId", "2")
                        .accept(APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].label").isNotEmpty());
    }

    @Test
    public void shouldSearchComputersValidateGroupId() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(
                get("/scripts/computers")
                        //.param("groupId", "2")
                        .accept(APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("groupId")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    public void shouldSearchDeciders() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(
                get("/scripts/deciders")
                        .param("groupId", "2")
                        .accept(APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id.id").isNotEmpty())
                .andExpect(jsonPath("$[0].id.type").isNotEmpty())
                .andExpect(jsonPath("$[0].label").isNotEmpty());
    }

    @Test
    public void shouldSearchDecidersValidateGroupId() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(
                get("/scripts/deciders")
                        //.param("groupId", "2")
                        .accept(APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("groupId")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    public void shouldCompile() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(
                get("/scripts/compile")
                        .param("templateId", "0")
                        .param("script", "a = 1 + 2")
                        .accept(APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.a").exists())
                .andExpect(jsonPath("$.a", is(5)));
    }

    @Test
    public void shouldCompile1() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(
                get("/scripts/compile")
                        .param("templateId", "0")
                        .param("script", "a = b + 1")
                        .param("inputs", "b")
                        .param("types", "5")
                        .accept(APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.a").exists())
                .andExpect(jsonPath("$.a", is(5)));
    }

    @Test
    public void shouldCompile2() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(
                get("/scripts/compile")
                        .param("templateId", "0")
                        .param("script", "a = b.days.ago")
                        .param("inputs", "b")
                        .param("types", "5")
                        .accept(APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.a").exists())
                .andExpect(jsonPath("$.a", is(14)));
    }

    @Test
    public void shouldCompile3() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(
                get("/scripts/compile")
                        .param("templateId", "0")
                        .param("script", "a = b")
                        .param("inputs", "b")
                        .param("types", "14")
                        .accept(APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.a").exists())
                .andExpect(jsonPath("$.a", is(14)));
    }

    @Test
    public void shouldValidateTemplateId() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(
                get("/scripts/compile")
                        //.param("templateId", "0")
                        .param("script", "a = b")
                        .accept(APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("templateId")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    public void shouldValidateTemplate() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(
                get("/scripts/compile")
                        .param("templateId", "-1")
                        .param("script", "def a = 2")
                        .accept(APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("DBC")))
                .andExpect(jsonPath("$[0].origin", is("templateId")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    public void shouldValidateScript() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(
                get("/scripts/compile")
                        .param("templateId", "0")
                        .param("script", "")
                        .accept(APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("script")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    public void shouldValidateInputs() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(
                get("/scripts/compile")
                        .param("templateId", "0")
                        .param("script", "a = b")
                        .param("inputs", ",b")
                        .param("types", "1, 2")
                        .accept(APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("inputs[0]")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    public void shouldValidateTypes() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(
                get("/scripts/compile")
                        .param("templateId", "0")
                        .param("script", "a = b")
                        .param("inputs", "b")
                        .param("types", "?")
                        .accept(APPLICATION_JSON_VALUE)
        );

        // Then result.andReturn().getResponse().getContentAsString()
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("types")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }
}
