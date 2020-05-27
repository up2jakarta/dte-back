package io.github.up2jakarta.dte.web.controllers;

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
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.Version;
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
        "spring.datasource.data=classpath:groups.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:meta_rest;MODE=PostgreSQL;"
})
public class MetaControllerTests {

    private MockMvc mvc;

    @Autowired
    private MetaController controller;

    @Before
    public void initialize() {
        this.mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionHandlerAdvice())
                .build();
    }

    @Test
    public void shouldGetVersion() throws Exception {
        // Given
        final Version version = StaticEngine.version();

        // When
        final ResultActions result = this.mvc.perform(get("/version").accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.project").isNotEmpty())
                .andExpect(jsonPath("$.startDate").isNotEmpty())
                .andExpect(jsonPath("$.startTime").isNotEmpty())
                .andExpect(jsonPath("$.dataSource", is(version.getBuildVersion())));

    }

    @Test
    public void shouldGetStatus() throws Exception {
        // Given
        final String status = "OK";

        // When
        final ResultActions result = this.mvc.perform(get("/status").accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", is(status)));

    }


    @Test
    public void notFount() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(get("/not_found").accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().is(404));
    }

}
