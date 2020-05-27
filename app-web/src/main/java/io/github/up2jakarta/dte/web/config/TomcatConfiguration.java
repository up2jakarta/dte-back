package io.github.up2jakarta.dte.web.config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * Tomcat configuration.
 */
@Configuration
@PropertySource(value = "classpath:web.properties", ignoreResourceNotFound = true)
public class TomcatConfiguration {

    @Value("${server.ajp.port:8009}")
    private Integer ajpPort;

    /**
     * @return tomcat container
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainer() {
        return server -> {
            server.addAdditionalTomcatConnectors(redirectConnector());
            server.setMimeMappings(mimeMappings());
        };
    }

    /**
     * @return mime mappings
     */
    private MimeMappings mimeMappings() {
        final MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("html", TEXT_HTML_VALUE + ";charset=" + UTF_8.name().toLowerCase());
        // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
        mappings.add("json", TEXT_HTML_VALUE + ";charset=" + UTF_8.name().toLowerCase());
        return mappings;
    }

    /**
     * @return AJP connector
     */
    private Connector redirectConnector() {
        final Connector connector = new Connector("AJP/1.3");
        connector.setScheme("http");
        connector.setPort(ajpPort);
        connector.setSecure(false);
        connector.setProperty("secretRequired", Boolean.FALSE.toString());
        connector.setAllowTrace(false);
        connector.setXpoweredBy(false);
        return connector;
    }
}
