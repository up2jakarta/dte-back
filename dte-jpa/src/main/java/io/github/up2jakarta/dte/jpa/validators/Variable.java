package io.github.up2jakarta.dte.jpa.validators;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * The annotated {@link String} element has to be not null and valid variable identifier.
 */
@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VariableValidator.class)
@NotNull
@Documented
public @interface Variable {

    /**
     * @return {@link NotNull} message.
     */
    @OverridesAttribute(constraint = NotNull.class, name = "message")
    String message() default "{javax.validation.constraints.NotNull.message}";

    /**
     * @return groups.
     */
    @OverridesAttribute(constraint = NotNull.class, name = "groups")
    Class<?>[] groups() default {};

    /**
     * @return payloads.
     */
    @OverridesAttribute(constraint = NotNull.class, name = "payload")
    Class<? extends Payload>[] payload() default {};
}
