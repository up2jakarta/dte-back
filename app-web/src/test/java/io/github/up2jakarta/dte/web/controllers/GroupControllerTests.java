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
import io.github.up2jakarta.dte.web.TestConfiguration;
import io.github.up2jakarta.dte.web.config.ExceptionHandlerAdvice;
import io.github.up2jakarta.dte.web.models.GroupModel;

import static org.hamcrest.core.Is.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@WebAppConfiguration
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:groups_rest;MODE=PostgreSQL;"
})
public class GroupControllerTests {

    private MockMvc mvc;
    @Autowired
    private GroupController controller;
    @Autowired
    private ObjectMapper mapper;

    @Before
    public void initialize() {
        this.mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionHandlerAdvice())
                .build();
    }

    @Test
    public void shouldPostWorkGroup() throws Exception {
        // Given
        final GroupModel group = new GroupModel();
        {
            group.setLabel("WG");
            group.setDescription("Test Description");
            group.setIcon("test");
            group.setColor("white");
            group.setParentId(1);
        }
        final String json = mapper.writeValueAsString(group);

        // When
        final ResultActions result = this.mvc.perform(
                post("/groups/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.label", is(group.getLabel())))
                .andExpect(jsonPath("$.description", is(group.getDescription())))
                .andExpect(jsonPath("$.icon", is(group.getIcon())))
                .andExpect(jsonPath("$.color", is(group.getColor())));
    }

    @Test
    public void shouldPostWorkspace() throws Exception {
        // Given
        final GroupModel group = new GroupModel();
        {
            group.setLabel("WS");
            group.setDescription("Test Description");
            group.setIcon("test");
            group.setColor("white");
            group.setParentId(0);
        }
        final String json = mapper.writeValueAsString(group);

        // When
        final ResultActions result = this.mvc.perform(
                post("/groups/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.label", is(group.getLabel())))
                .andExpect(jsonPath("$.description", is(group.getDescription())))
                .andExpect(jsonPath("$.icon", is(group.getIcon())))
                .andExpect(jsonPath("$.color", is(group.getColor())));
    }

    @Test
    public void shouldFind() throws Exception {
        // Given
        final int id = 1;

        // When
        final ResultActions result = this.mvc.perform(get("/groups/{id}", id).accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.label", is("G001")))
                .andExpect(jsonPath("$.description", is("WS 1")))
                .andExpect(jsonPath("$.icon", is("test")))
                .andExpect(jsonPath("$.color", is("green")));
    }

    @Test
    public void shouldNotFindMain() throws Exception {
        // Given
        final int id = 0;

        // When
        final ResultActions result = this.mvc.perform(get("/groups/{id}", id));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldPut() throws Exception {
        // Given
        final int id = 3;
        final GroupModel group = new GroupModel();
        {
            group.setLabel("WGT");
            group.setDescription("Test2");
            group.setIcon("test");
            group.setColor("white");
        }
        final String json = mapper.writeValueAsString(group);

        // When
        final ResultActions result = this.mvc.perform(
                put("/groups/{id}", id)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.label", is(group.getLabel())))
                .andExpect(jsonPath("$.description", is(group.getDescription())))
                .andExpect(jsonPath("$.icon", is(group.getIcon())))
                .andExpect(jsonPath("$.color", is(group.getColor())));
    }

    @Test
    public void shouldNotPutMain() throws Exception {
        // Given
        final int id = 0;
        final GroupModel group = new GroupModel();
        {
            group.setLabel("Test2");
            group.setDescription("Test2");
            group.setIcon("test");
            group.setColor("white");
        }
        final String json = mapper.writeValueAsString(group);

        // When
        final ResultActions result = this.mvc.perform(
                put("/groups/{id}", id)
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
        final int id = 3;

        // When
        final ResultActions result = this.mvc.perform(delete("/groups/{id}", id));

        // Then
        result.andExpect(status().isNoContent());
    }

    @Test
    public void shouldNotDeleteMain() throws Exception {
        // Given
        final int id = 0;

        // When
        final ResultActions result = this.mvc.perform(delete("/groups/{id}", id));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetAll() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(get("/groups").accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].label").isNotEmpty())
                .andExpect(jsonPath("$[0].icon").isNotEmpty())
                .andExpect(jsonPath("$[0].icon").isNotEmpty());
    }

}
