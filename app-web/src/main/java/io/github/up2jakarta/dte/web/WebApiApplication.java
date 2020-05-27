package io.github.up2jakarta.dte.web;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Application Entry Point to launch the web API.
 *
 * @author A.ABBESSI
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class WebApiApplication extends SpringBootServletInitializer {

    /**
     * Main entry point.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        final SpringApplication app = new SpringApplication(WebApiApplication.class);
        app.setBannerMode(Banner.Mode.CONSOLE);
        app.run(args);
    }
}
