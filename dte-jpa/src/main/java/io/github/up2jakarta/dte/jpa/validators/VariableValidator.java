package io.github.up2jakarta.dte.jpa.validators;

import org.springframework.stereotype.Component;

import javax.lang.model.SourceVersion;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link Variable} validator.
 */
@Component
public class VariableValidator implements ConstraintValidator<Variable, String> {

    private static final Set<String> KEYWORDS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("as", "def", "in", "trait", "var", "it", "binding", "metaClass"))
    );

    @Override
    public boolean isValid(final String name, final ConstraintValidatorContext context) {
        if (KEYWORDS.contains(name) || SourceVersion.isKeyword(name) || !SourceVersion.isIdentifier(name)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{dte.validation.constraints.variable.message}")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
