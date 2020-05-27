package io.github.up2jakarta.dte.jpa;

import io.github.up2jakarta.dte.jpa.repositories.*;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.managers.ComputerManager;
import io.github.up2jakarta.dte.jpa.repositories.*;
import io.github.up2jakarta.dte.jpa.views.VDecider;

import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackageClasses = {GroupRepository.class, ComputerManager.class})
@EntityScan(basePackageClasses = {Group.class, VDecider.class})
@EnableAutoConfiguration
@PropertySource(value = "classpath:dbc.properties", ignoreResourceNotFound = true)
public class TestConfiguration {

    @Bean
    public DynamicEngine engine(final ComputerRepository dtn, final DeciderRepository btn,
                                final TypeRepository tpe, final TemplateRepository tpl) {
        return new DynamicEngine(dtn, btn, tpe, tpl);
    }

    @Bean
    public Validator validator(final AutowireCapableBeanFactory beanFactory) {
        return Validation.byProvider(HibernateValidator.class)
                .configure().constraintValidatorFactory(new SpringConstraintValidatorFactory(beanFactory))
                .buildValidatorFactory()
                .getValidator();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.tomcat")
    @Primary
    public DataSource defaultDataSource() {
        return DataSourceBuilder.create().build();
    }
}
