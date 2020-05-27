package io.github.up2jakarta.dte.web.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;
import org.springframework.web.bind.annotation.RequestParam;
import io.github.up2jakarta.dte.jpa.DynamicEngine;
import io.github.up2jakarta.dte.jpa.managers.ComputerManager;
import io.github.up2jakarta.dte.jpa.repositories.ComputerRepository;
import io.github.up2jakarta.dte.jpa.repositories.DeciderRepository;
import io.github.up2jakarta.dte.jpa.repositories.TemplateRepository;
import io.github.up2jakarta.dte.jpa.repositories.TypeRepository;
import io.github.up2jakarta.dte.jpa.validators.BinaryTreeValidator;
import io.github.up2jakarta.dte.web.services.ComputerService;

import javax.validation.ParameterNameProvider;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service layer configuration.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackageClasses = {ComputerService.class, ComputerManager.class, BinaryTreeValidator.class})
public class ServiceConfiguration {

    /**
     * Create and return an engine which is necessary for scripts validation and execution.
     *
     * @param dtn the computer finder
     * @param btn the decider finder
     * @param tpe the type repository
     * @param tpl the template repository
     * @return an engine
     */
    @Bean
    public DynamicEngine engine(final ComputerRepository dtn, final DeciderRepository btn,
                                final TypeRepository tpe, final TemplateRepository tpl) {
        return new DynamicEngine(dtn, btn, tpe, tpl);
    }

    /**
     * Return JSR-303 validator that supports {@link org.springframework.beans.factory.annotation.Autowired}.
     *
     * @param beanFactory spring bean factory
     * @return hibernate validator
     */
    @Bean
    public Validator validator(final AutowireCapableBeanFactory beanFactory) {
        return Validation.byProvider(HibernateValidator.class)
                .configure().constraintValidatorFactory(new SpringConstraintValidatorFactory(beanFactory))
                .parameterNameProvider(new SpringParameterNameProvider())
                .failFast(false)
                .buildValidatorFactory()
                .getValidator();
    }

    /**
     * An extended {@link ParameterNameProvider} implementation which returns parameter names Java reflection API.
     * Within support of the spring annotation {@link RequestParam}.
     */
    public class SpringParameterNameProvider implements ParameterNameProvider {

        @Override
        public List<String> getParameterNames(final Constructor<?> constructor) {
            return doGetParameterNames(constructor);
        }

        @Override
        public List<String> getParameterNames(final Method method) {
            return doGetParameterNames(method);
        }

        private List<String> doGetParameterNames(final Executable executable) {
            final Parameter[] parameters = executable.getParameters();
            final List<String> parameterNames = new ArrayList<>(parameters.length);
            for (final Parameter parameter : parameters) {
                String parameterName = parameter.getName();
                if (parameter.isAnnotationPresent(RequestParam.class)) {
                    final RequestParam requestParamAnnotation = parameter.getAnnotation(RequestParam.class);
                    String name = requestParamAnnotation.value();
                    if (name.isEmpty()) {
                        parameterName = requestParamAnnotation.name();
                    }
                    if (!name.isEmpty()) {
                        parameterName = name;
                    }
                }
                parameterNames.add(parameterName);
            }
            return Collections.unmodifiableList(parameterNames);
        }
    }
}
