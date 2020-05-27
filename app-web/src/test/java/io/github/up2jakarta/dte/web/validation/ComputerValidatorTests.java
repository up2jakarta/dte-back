package io.github.up2jakarta.dte.web.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
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
import io.github.up2jakarta.dte.web.controllers.ComputerController;

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
        "spring.datasource.data=classpath:groups.sql, classpath:computers.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:computers_valid;MODE=PostgreSQL;"
})
@Transactional
public class ComputerValidatorTests {

    private final Input input = new Input();
    private final Output output = new Output();
    private MockMvc mvc;
    @Autowired
    private ComputerController controller;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private Validator validator;
    @Autowired
    private HttpMessageConverter<?>[] messageConverters;

    @Before
    public void initialize() {
        this.mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionHandlerAdvice())
                .setValidator(validator)
                .setMessageConverters(messageConverters)
                .build();
        {
            input.setVariable("test");
            input.setLabel("Test");
            input.setDescription("Test");
            input.setOptional(false);
            input.setTypeId(2);

            output.setVariable("test");
            output.setLabel("Test");
            output.setDescription("Test");
            output.setOptional(false);
            output.setShared(true);
            output.setTypeId(2);
        }
    }

    @Test
    public void shouldValidateShared() throws Exception {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setLabel("NEW 1");
            model.setDescription("NEW");
            model.setScript("test = 'NEW'");
            model.setGroupId(1);
            // model.setShared(true);
            model.setTemplateId(0);
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("shared")))
                .andExpect(jsonPath("$[0].message").isNotEmpty())
        ;
    }

    @Test
    public void shouldValidateTemplateId() throws Exception {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setLabel("NEW 2");
            model.setDescription("NEW");
            model.setScript("test = 'NEW'");
            model.setGroupId(1);
            model.setShared(true);
            // model.setTemplateId(0);
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("templateId")))
                .andExpect(jsonPath("$[0].message").isNotEmpty())
        ;
    }

    @Test
    public void shouldValidateGroupId() throws Exception {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setLabel("NEW 3");
            model.setDescription("NEW");
            model.setScript("test = 'NEW'");
            // model.setGroupId(1);
            model.setShared(true);
            model.setTemplateId(0);
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("groupId")))
                .andExpect(jsonPath("$[0].message").isNotEmpty())
        ;
    }

    @Test
    public void shouldValidateChildrenWhenNull() throws Exception {
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("NEW 4");
            model.setDescription("Test Children");
            model.setGroupId(1);
            model.setShared(true);
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("children")));
    }

    @Test
    public void shouldValidateChildrenRecursively() throws Exception {
        // Given
        final Computer decision = new Computer(Computer.Type.DECIDER);
        {
            decision.setLabel("NEW 5");
            decision.setDeciderId(10L);
            decision.setNegated(false);
            decision.setShared(true);
            decision.setChildren(Collections.emptyList());
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("NEW 6");
            model.setDescription("Test validation");
            model.setGroupId(2);
            model.setChildren(Collections.singletonList(decision));
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("children[0].children")));
    }

    @Test
    public void shouldValidateNegated() throws Exception {
        // Given
        final Computer task = new Computer(Computer.Type.LOCAL);
        {
            task.setLabel("NEW 7");
            task.setDescription("Local task");
            task.setScript("test = \"T1\"");
            task.setShared(true);
            task.setTemplateId(0);
            task.setOutputs(Collections.singletonList(output));
        }
        final Computer decision = new Computer(Computer.Type.DECIDER);
        {
            decision.setLabel("NEW 8");
            decision.setDeciderId(10L);
            //decision.setNegated(false);
            decision.setChildren(Collections.singletonList(task));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("NEW 9");
            model.setDescription("Test validation");
            model.setGroupId(2);
            model.setShared(true);
            model.setChildren(Collections.singletonList(decision));
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("children[0].negated")));
    }

    @Test
    public void shouldValidateLabel() throws Exception {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            //setLabel("Test validation");
            model.setDescription("Test validation");
            model.setScript("test = 'Test'");
            model.setShared(true);
            model.setTemplateId(0);
            model.setGroupId(1);
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("label")));
    }

    @Test
    public void shouldValidateScript() throws Exception {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setLabel("NEW 10");
            model.setDescription("Test validation");
            // model.setScript("test = 'Test'");
            model.setShared(true);
            model.setTemplateId(0);
            model.setGroupId(1);
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("script")));
    }

    @Test
    public void shouldValidateTemplate() throws Exception {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setLabel("NEW 11");
            model.setDescription("Test validation");
            model.setScript("test = 'Test'");
            model.setShared(true);
            model.setTemplateId(Integer.MIN_VALUE);
            model.setGroupId(1);
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("templateId")));
    }

    @Test
    public void shouldValidateGroup() throws Exception {
        // Given
        final Computer model = new Computer(Computer.Type.PLAIN);
        {
            model.setLabel("NEW 12");
            model.setDescription("Test validation");
            model.setScript("test = 'Test'");
            model.setShared(true);
            model.setTemplateId(0);
            model.setGroupId(Integer.MIN_VALUE);
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("groupId")));
    }

    @Test
    public void shouldValidateType() throws Exception {
        // Given
        final Computer model = new Computer(Computer.Type.LOCAL);
        {
            model.setLabel("NEW 13");
            model.setDescription("Test validation");
            model.setScript("test = 'Test'");
            model.setShared(true);
            model.setGroupId(1);
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("type")));
    }

    @Test
    public void shouldValidateDeciderId() throws Exception {
        // Given
        final Computer local = new Computer(Computer.Type.LOCAL);
        {
            local.setLabel("NEW 14");
            local.setDescription("Local task");
            local.setScript("test = \"T1\"");
            local.setShared(true);
            local.setTemplateId(0);
            local.setOutputs(Collections.singletonList(output));
        }
        final Computer decider = new Computer(Computer.Type.DECIDER);
        {
            decider.setLabel("NEW 15");
            //decider.setDeciderId(10L);
            decider.setNegated(false);
            decider.setChildren(Collections.singletonList(local));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("NEW 16");
            model.setDescription("Test validation");
            model.setGroupId(1);
            model.setShared(true);
            model.setChildren(Collections.singletonList(decider));
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("children[0].deciderId")));
    }

    @Test
    public void shouldValidateDecider() throws Exception {
        // Given
        final Computer local = new Computer(Computer.Type.LOCAL);
        {
            local.setLabel("NEW 17");
            local.setDescription("Local task");
            local.setScript("test = \"T1\"");
            local.setShared(true);
            local.setTemplateId(0);
            local.setOutputs(Collections.singletonList(output));
        }
        final Computer decider = new Computer(Computer.Type.DECIDER);
        {
            decider.setLabel("NEW 18");
            decider.setDeciderId(Long.MIN_VALUE);
            decider.setNegated(false);
            decider.setChildren(Collections.singletonList(local));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("NEW 19");
            model.setDescription("Test validation");
            model.setGroupId(1);
            model.setShared(true);
            model.setChildren(Collections.singletonList(decider));
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("children[0].deciderId")));
    }

    @Test
    public void shouldValidateComputerId() throws Exception {
        // Given
        final Computer computer = new Computer(Computer.Type.COMPUTER);
        {
            computer.setLabel("NEW 20");
            //computer.setComputerId(10L);
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("NEW 21");
            model.setDescription("Test validation");
            model.setGroupId(1);
            model.setShared(true);
            model.setChildren(Collections.singletonList(computer));
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("children[0].computerId")));
    }

    @Test
    public void shouldValidateComputerGroup() throws Exception {
        // Given
        final Computer computer = new Computer(Computer.Type.COMPUTER);
        {
            computer.setLabel("NEW 22");
            computer.setComputerId(16L);
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("NEW 23");
            model.setDescription("Test validation");
            model.setGroupId(2);
            model.setShared(true);
            model.setChildren(Collections.singletonList(computer));
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("children[0].computerId")));
    }

    @Test
    public void shouldValidateComputer() throws Exception {
        // Given
        final Computer computer = new Computer(Computer.Type.COMPUTER);
        {
            computer.setLabel("NEW 24");
            computer.setComputerId(Long.MIN_VALUE);
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("NEW 25");
            model.setDescription("Test validation");
            model.setGroupId(1);
            model.setShared(true);
            model.setChildren(Collections.singletonList(computer));
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].message").isNotEmpty());
    }

    @Test
    public void shouldValidateUnnecessaryDefault() throws Exception {
        // Given
        final Computer local = new Computer(Computer.Type.LOCAL);
        {
            local.setLabel("NEW 6");
            local.setDescription("Local task");
            local.setScript("test = \"T1\"");
            local.setShared(true);
            local.setTemplateId(0);
            local.setOutputs(Collections.singletonList(output));
        }
        final Computer any = new Computer(Computer.Type.DEFAULT);
        {
            any.setLabel("NEW 27");
            any.setChildren(Collections.singletonList(local));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("NEW 28");
            model.setDescription("Test validation");
            model.setGroupId(1);
            model.setShared(true);
            model.setChildren(Collections.singletonList(any));
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("children")));
    }

    @Test
    public void shouldValidateManyDefault() throws Exception {
        // Given
        final Computer local = new Computer(Computer.Type.LOCAL);
        {
            local.setLabel("NEW 29");
            local.setDescription("Local task");
            local.setScript("test = \"T1\"");
            local.setShared(true);
            local.setTemplateId(0);
            local.setOutputs(Collections.singletonList(output));
        }
        final Computer any = new Computer(Computer.Type.DEFAULT);
        {
            any.setLabel("NEW 30");
            any.setChildren(Collections.singletonList(local));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("NEW 31");
            model.setDescription("Test validation");
            model.setGroupId(1);
            model.setShared(true);
            model.setChildren(Arrays.asList(any, any));
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("children")));
    }

    @Test
    public void shouldValidateDefaultLast() throws Exception {
        // Given
        final Computer task = new Computer(Computer.Type.LOCAL);
        {
            task.setLabel("Task");
            task.setDescription("Local task");
            task.setScript("test = \"T1\"");
            task.setShared(false);
            task.setTemplateId(0);
            task.setOutputs(Collections.singletonList(output));
        }
        final Computer any = new Computer(Computer.Type.DEFAULT);
        {
            any.setLabel("Default");
            any.setChildren(Collections.singletonList(task));
        }
        final Computer decision = new Computer(Computer.Type.DECIDER);
        {
            decision.setLabel("Decider");
            decision.setDeciderId(10L);
            decision.setNegated(false);
            decision.setChildren(Collections.singletonList(task));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("NEW 32");
            model.setDescription("Test validation");
            model.setGroupId(2);
            model.setShared(true);
            model.setChildren(Arrays.asList(any, decision));
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("children")));
    }

    @Test
    public void shouldValidateChildrenClasses() throws Exception {
        // Given
        final Computer task = new Computer(Computer.Type.LOCAL);
        {
            task.setLabel("Task");
            task.setDescription("Local task");
            task.setScript("test = \"T1\"");
            task.setShared(false);
            task.setTemplateId(0);
            task.setOutputs(Collections.singletonList(output));
        }
        final Computer decision = new Computer(Computer.Type.DECIDER);
        {
            decision.setLabel("Decider");
            decision.setDeciderId(10L);
            decision.setNegated(false);
            decision.setChildren(Collections.singletonList(task));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("NEW 33");
            model.setDescription("Test validation");
            model.setShared(false);
            model.setGroupId(2);
            model.setChildren(Arrays.asList(task, decision));
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("children")));
    }

    @Test
    public void shouldValidateRuleChildrenClasses() throws Exception {
        // Given
        final Computer task1 = new Computer(Computer.Type.LOCAL);
        {
            task1.setLabel("Task");
            task1.setDescription("Local task");
            task1.setScript("test = \"T1\"");
            task1.setShared(false);
            task1.setTemplateId(0);
            task1.setOutputs(Collections.singletonList(output));
        }
        final Computer task0 = new Computer(Computer.Type.LOCAL);
        {
            task0.setLabel("Task");
            task0.setDescription("Local task");
            task0.setScript("test = \"T1\"");
            task0.setShared(false);
            task0.setTemplateId(0);
            task0.setChildren(Collections.singletonList(task1));
            task0.setOutputs(Collections.singletonList(output));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("NEW 34");
            model.setDescription("Test validation");
            model.setShared(false);
            model.setGroupId(1);
            model.setChildren(Collections.singletonList(task0));
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("children[0].children")));
    }

    @Test
    public void shouldValidateDecisionChildrenRecursively() throws Exception {
        // Given
        final Computer child0 = new Computer(Computer.Type.LOCAL);
        {
            child0.setLabel("Task 01");
            child0.setDescription("Local task");
            child0.setScript("test = \"T1\"");
            child0.setShared(false);
            child0.setTemplateId(0);
            child0.setChildren(Collections.emptyList());
            child0.setOutputs(Collections.singletonList(output));
        }
        final Computer child1 = new Computer(Computer.Type.DECIDER);
        {
            child1.setLabel("Decider");
            child1.setDeciderId(10L);
            child1.setNegated(false);
        }
        final Computer child2 = new Computer(Computer.Type.DECIDER);
        {
            child2.setLabel("Decider");
            child2.setDeciderId(10L);
            child2.setNegated(true);
            child2.setChildren(Collections.singletonList(child0));
        }
        final Computer task = new Computer(Computer.Type.LOCAL);
        {
            task.setLabel("Task 01");
            task.setDescription("Local task ");
            task.setScript("test = \"T1\"");
            task.setShared(true);
            task.setTemplateId(0);
            task.setChildren(Arrays.asList(child1, child2));
            task.setOutputs(Collections.singletonList(output));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("NEW 35");
            model.setDescription("Test validation");
            model.setShared(false);
            model.setGroupId(2);
            model.setChildren(Collections.singletonList(task));
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("children[0].children[0].children")));
    }

    @Test
    public void shouldValidateRuleChildren() throws Exception {
        // Given
        final Computer child0 = new Computer(Computer.Type.LOCAL);
        {
            child0.setLabel("Task");
            child0.setDescription("Local task");
            child0.setScript("test = \"T1\"");
            child0.setShared(false);
            child0.setTemplateId(0);
            child0.setChildren(Collections.emptyList());
            child0.setOutputs(Collections.singletonList(output));
        }
        final Computer child1 = new Computer(Computer.Type.DEFAULT);
        {
            child1.setLabel("Default");
            child1.setChildren(Collections.singletonList(child0));
        }
        final Computer child2 = new Computer(Computer.Type.DECIDER);
        {
            child2.setLabel("Decider");
            child2.setDeciderId(10L);
            child2.setNegated(false);
            child2.setChildren(Collections.singletonList(child0));
        }

        final Computer task = new Computer(Computer.Type.LOCAL);
        {
            task.setLabel("Task");
            task.setDescription("Local task");
            task.setScript("test = \"T1\"");
            task.setShared(false);
            task.setTemplateId(0);
            task.setChildren(Arrays.asList(child1, child2));
            task.setOutputs(Collections.singletonList(output));
        }
        final Computer model = new Computer(Computer.Type.MIXED);
        {
            model.setLabel("NEW 36");
            model.setDescription("Test validation");
            model.setShared(false);
            model.setGroupId(2);
            model.setChildren(Collections.singletonList(task));
            model.setOutputs(Collections.singletonList(output));
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
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").isNotEmpty())
                .andExpect(jsonPath("$[0].type", is("API")))
                .andExpect(jsonPath("$[0].origin", is("children[0].children")));
    }

    @Test
    public void shouldValidateNullType() throws Exception {
        // Given
        final Computer model = new Computer(null);
        final String json = mapper.writeValueAsString(model);

        // When
        final ResultActions result = this.mvc.perform(
                post("/computers/")
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
                post("/computers/")
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
    public void shouldValidateTypeEnum() throws Exception {
        // Given
        final String json = "{\"type\": \"unknown\", \"label\": \"Test\", \"script\": \"test\", \"groupId\": 1}";

        // When
        final ResultActions result = this.mvc.perform(
                post("/computers/")
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
}
