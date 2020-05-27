package io.github.up2jakarta.dte.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(WebApiApplication.class)
@PropertySource(value = "classpath:dbc.properties")
public class TestConfiguration {
}
