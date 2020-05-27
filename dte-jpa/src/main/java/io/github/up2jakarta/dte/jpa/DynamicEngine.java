package io.github.up2jakarta.dte.jpa;

import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.api.Computer;
import io.github.up2jakarta.dte.exe.api.Decider;
import io.github.up2jakarta.dte.exe.api.Template;
import io.github.up2jakarta.dte.exe.loader.Finder;
import io.github.up2jakarta.dte.exe.loader.LoadingException;
import io.github.up2jakarta.dte.exe.script.CompilationException;
import io.github.up2jakarta.dte.jpa.repositories.TemplateRepository;
import io.github.up2jakarta.dte.jpa.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Engine entry point for executing trees and validating decision trees.
 *
 * @author A.ABBESSI
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class DynamicEngine extends StaticEngine {

    private final TemplateRepository templates;
    private final TypeRepository types;

    /**
     * Public constructor for DynamicEngine.
     *
     * @param computers the computer finder
     * @param deciders  the decider finder
     * @param types     the type repository
     * @param templates the template repository
     */
    @Autowired
    public DynamicEngine(final Finder<Computer> computers, final Finder<Decider> deciders,
                         final TypeRepository types, final TemplateRepository templates) {
        super(computers, deciders);
        this.types = types;
        this.templates = templates;
    }

    /**
     * Check the syntax and types of the given {@code script} within DTE extensions.
     * Extract the output variables typing and return them.
     *
     * @param templateId the template identifier
     * @param script     the script source code
     * @param inputs     the input typing
     * @return the output typing
     * @throws CompilationException if the script does not compile
     * @throws LoadingException     if the template or one of types is not found
     */
    public Map<String, Integer> compile(final int templateId, final String script, final Map<String, Integer> inputs)
            throws CompilationException, LoadingException {
        final Template template = templates.find(templateId);
        if (template == null) {
            throw new LoadingException("template", templateId);
        }
        final Map<String, Class<?>> outputs = compile(template.getBaseClass(), script, types.serialize(inputs));
        return types.deserialize(outputs);
    }

    /**
     * Check the syntax and types of the given {@code script} within DTE extensions.
     *
     * @param templateId the template identifier
     * @param script     the script source code
     * @param typing     the variables(inputs and outputs) typing
     * @throws CompilationException if the script does not compile
     * @throws LoadingException     if the template or one of types is not found
     */
    public void validate(final int templateId, final String script, final Map<String, Integer> typing)
            throws CompilationException, LoadingException {
        final Template template = templates.find(templateId);
        if (template == null) {
            throw new LoadingException("template", templateId);
        }
        validate(template.getBaseClass(), script, types.serialize(typing));
    }
}
