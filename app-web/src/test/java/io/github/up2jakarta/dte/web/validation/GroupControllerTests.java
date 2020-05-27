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
import io.github.up2jakarta.dte.web.TestConfiguration;
import io.github.up2jakarta.dte.web.config.ExceptionHandlerAdvice;
import io.github.up2jakarta.dte.web.controllers.GroupController;
import io.github.up2jakarta.dte.web.models.GroupModel;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@WebAppConfiguration
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:groups_valid;MODE=PostgreSQL;"
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
    public void shouldCheckUniqueLabel() throws Exception {
        // Given
        final GroupModel group = new GroupModel();
        {
            group.setLabel("G001");
            group.setDescription("Test Label Unique");
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("label")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    public void shouldCheckLabel() throws Exception {
        // Given
        final GroupModel group = new GroupModel();
        {
            //group.setLabel("G001");
            group.setDescription("Test Label Unique");
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("label")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    public void shouldCheckEmptyLabel() throws Exception {
        // Given
        final GroupModel group = new GroupModel();
        {
            group.setLabel("");
            group.setDescription("Test Label Unique");
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("label")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    public void shouldCheckDescription() throws Exception {
        // Given
        final GroupModel group = new GroupModel();
        {
            group.setLabel("G001");
            //group.setDescription("Test Label Unique");
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("description")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    public void shouldCheckEmptyDescription() throws Exception {
        // Given
        final GroupModel group = new GroupModel();
        {
            group.setLabel("TST");
            group.setDescription("");
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("description")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    public void shouldCheckNullParentId() throws Exception {
        // Given
        final GroupModel group = new GroupModel();
        {
            group.setLabel("TST");
            group.setDescription("Test");
            group.setIcon("test");
            group.setColor("white");
            //group.setParentId(0);
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("parentId")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    public void shouldCheckUnknownParent() throws Exception {
        // Given
        final GroupModel group = new GroupModel();
        {
            group.setLabel("TST");
            group.setDescription("Test");
            group.setIcon("test");
            group.setColor("white");
            group.setParentId(Integer.MIN_VALUE);
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("parentId")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    public void shouldCheckInvalidParent() throws Exception {
        // Given
        final GroupModel group = new GroupModel();
        {
            group.setLabel("T22");
            group.setDescription("Test");
            group.setIcon("test");
            group.setColor("white");
            group.setParentId(3);
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("parentId")))
                .andExpect(jsonPath("$[0].message", is("invalid parent reference")));
    }
}
