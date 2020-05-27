package io.github.up2jakarta.dte.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import io.github.up2jakarta.dte.web.config.ExceptionHandlerAdvice;
import io.github.up2jakarta.dte.web.controllers.MetaController;
import io.github.up2jakarta.dte.web.exceptions.HttpException;

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
        "spring.datasource.tomcat.url=jdbc:h2:mem:errors_rest;MODE=PostgreSQL;"
})
public class ErrorHandlerTests {

    private MockMvc mvc;

    @Before
    public void initialize() throws Throwable {
        final MetaController controller = Mockito.mock(MetaController.class);
        Mockito.when(controller.status()).thenThrow(new HttpException(HttpStatus.CONFLICT, "Test", "TEST"));
        Mockito.when(controller.about()).thenThrow(new NullPointerException("NPE"));
        this.mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionHandlerAdvice())
                .build();
    }


    @Test
    public void anyException() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(get("/").accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("NPE")))
                .andExpect(jsonPath("$.type", is("SRV")))
                .andExpect(jsonPath("$.origin", is(NullPointerException.class.getName())));
    }

    @Test
    public void apiException() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(get("/status").accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("TEST")))
                .andExpect(jsonPath("$.type", is("API")))
                .andExpect(jsonPath("$.origin", is(HttpException.class.getName())));
    }

    @Test
    public void notFoundException() throws Exception {
        // When
        final ResultActions result = this.mvc.perform(get("/not_found").accept(APPLICATION_JSON_VALUE));

        // Then
        result.andExpect(status().isNotFound());
    }

}
