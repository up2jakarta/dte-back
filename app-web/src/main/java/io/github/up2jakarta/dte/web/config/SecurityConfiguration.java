package io.github.up2jakarta.dte.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import io.github.up2jakarta.dte.web.models.ErrorModel;

import static org.springframework.http.ResponseEntity.status;
import static io.github.up2jakarta.dte.web.models.ErrorModel.Type.API;

/**
 * Security layer configuration within Open ID Connect.
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends ResourceServerConfigurerAdapter {

    private final String[] ignoredPaths = new String[]{"/", "/status", "/about", "/heartbeat"};

    @Value("${security.oauth2.resource.user-info-uri}")
    private String userInfoUri;

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(ignoredPaths).permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable().headers().frameOptions().sameOrigin();
    }

    @Override
    public void configure(final ResourceServerSecurityConfigurer resources) {
        final UserInfoTokenServices service = new UserInfoTokenServices(userInfoUri, clientId);
        service.setPrincipalExtractor(m -> m.get("preferred_username"));
        resources.tokenServices(service)
                .authenticationEntryPoint(new EnhancedAuthenticationEntryPoint())
                .stateless(true)
                .resourceId(clientId);
    }

    /**
     * Enhanced {@link OAuth2AuthenticationEntryPoint}.
     */
    private static class EnhancedAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {

        @Override
        protected ResponseEntity<?> enhanceResponse(final ResponseEntity<?> result, final Exception ex) {
            if (ex.getCause() instanceof ClientAuthenticationException) {
                final String msg = ((InvalidTokenException) ex.getCause()).getOAuth2ErrorCode().replace('_', ' ');
                return status(result.getStatusCode())
                        .headers(result.getHeaders())
                        .body(new ErrorModel(API, "security", msg));
            }
            return result;
        }

    }
}
