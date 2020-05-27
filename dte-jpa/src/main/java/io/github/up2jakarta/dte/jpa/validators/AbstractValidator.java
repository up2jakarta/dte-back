package io.github.up2jakarta.dte.jpa.validators;

import io.github.up2jakarta.dte.exe.script.CompilationException;
import io.github.up2jakarta.dte.jpa.DynamicEngine;
import io.github.up2jakarta.dte.jpa.api.IGroup;
import io.github.up2jakarta.dte.jpa.api.Shareable;
import io.github.up2jakarta.dte.jpa.entities.*;
import io.github.up2jakarta.dte.jpa.models.Input;
import io.github.up2jakarta.dte.jpa.models.Node;
import io.github.up2jakarta.dte.jpa.repositories.TypeRepository;
import org.apache.groovy.io.StringBuilderWriter;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;

import javax.persistence.EntityManager;
import javax.validation.ConstraintValidatorContext;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * JPA Repository utility.
 */
abstract class AbstractValidator {

    /**
     * Return the persistence context which is necessary for data base validation.
     *
     * @return the entity manager
     */
    abstract EntityManager getManager();

    /**
     * Return the type repository which is necessary for Type/Class serialization.
     *
     * @return the type repository
     */
    abstract TypeRepository getTypeRepository();

    /**
     * Return the tree engine which is necessary for script validation.
     *
     * @return the tree engine
     */
    abstract DynamicEngine getEngine();

    /**
     * Check the specified {@code inputs} are valid, i.e unique variable names and existing types.
     *
     * @param context  the validation context
     * @param property the property name
     * @param inputs   the input/output typing
     * @param valid    the valid flag
     * @return {@code true} if valid, otherwise {@code false}
     */
    boolean checkInputs(final ConstraintValidatorContext context, final String property,
                        final List<? extends Input> inputs, final AtomicBoolean valid) {
        boolean isValid = true;
        if (inputs != null && !inputs.isEmpty()) {
            final Set<String> names = new HashSet<>(inputs.size());
            int index = 0;
            for (final Input e : inputs) {
                if (names.contains(e.getVariable())) {
                    final String key = property + "[" + index + "].variable";
                    error(context, key, "{dte.validation.constraints.input.message}", valid);
                    isValid = false;
                } else {
                    names.add(e.getVariable());
                }
                index++;
            }
            final Map<Integer, List<Input>> map = inputs.stream().filter(e -> e.getTypeId() != null)
                    .collect(Collectors.groupingBy(Input::getTypeId));
            final List<Type> types = getTypeRepository().find(map.keySet());
            if (map.size() != types.size()) {
                isValid = false;
                types.forEach(e -> map.remove(e.getId()));
                for (final Map.Entry<Integer, List<Input>> entry : map.entrySet()) {
                    entry.getValue().forEach(put -> {
                        final String p = property + "[" + inputs.indexOf(put) + "].typeId";
                        error(context, p, "{dte.validation.constraints.reference.message}", valid);
                    });
                }
            }
            return isValid && inputs.stream().noneMatch(e -> e.getTypeId() == null);
        }
        return true;
    }

    /**
     * Check the specified decider {@code id} is not null, and the entity exists.
     *
     * @param context the validation context
     * @param id      the decider identifier
     * @param groupId the group identifier
     * @param typing  the declared typing
     * @param valid   the valid flag
     */
    void checkDecider(final ConstraintValidatorContext context, final Integer groupId, final Long id,
                      final Map<String, ? extends Input> typing, final AtomicBoolean valid) {
        if (id == null) {
            error(context, "deciderId", "{javax.validation.constraints.NotNull.message}", valid);
        } else {
            final BTreeDecider ref = getManager().find(BTreeDecider.class, id);
            if (checkGroup(context, "deciderId", groupId, ref, valid)) {
                checkSpecification(context, "deciderId", typing, ref.getTyping(), valid);
            }
        }
    }

    /**
     * Check the specified computer {@code id} is not null, and the entity exists.
     *
     * @param context the validation context
     * @param id      the decider identifier
     * @param groupId the group identifier
     * @param typing  the declared typing
     * @param valid   the valid flag
     */
    void checkComputer(final ConstraintValidatorContext context, final Integer groupId, final Long id,
                       final Map<String, ? extends Input> typing, final AtomicBoolean valid) {
        if (id == null) {
            error(context, "computerId", "{javax.validation.constraints.NotNull.message}", valid);
        } else {
            final DTreeComputer ref = getManager().find(DTreeComputer.class, id);
            if (checkGroup(context, "computerId", groupId, ref, valid)) {
                checkSpecification(context, "computerId", typing, ref.getTyping(), valid);
            }
        }
    }

    /**
     * Check the specified group {@code id} is not null, and the entity exists.
     *
     * @param c   the validation context
     * @param gid the expected group identifier
     * @param id  the group identifier
     * @param v   the valid flag
     */
    void checkGroup(final ConstraintValidatorContext c, final Integer id, final Integer gid, final AtomicBoolean v) {
        if (id == null) {
            error(c, "groupId", "{javax.validation.constraints.NotNull.message}", v);
        } else if (gid != null && !id.equals(gid)) {
            error(c, "groupId", "{dte.validation.constraints.root.message}", v);
        } else if (getManager().find(Group.class, id) == null) {
            error(c, "groupId", "{dte.validation.constraints.reference.message}", v);
        }
    }

    /**
     * Check the specified group {@code id} is not null, and the entity exists.
     *
     * @param context the validation context
     * @param id      the template identifier
     * @param valid   the valid flag
     * @return {@code true} if valid, otherwise {@code false}
     */
    boolean checkTemplate(final ConstraintValidatorContext context, final Integer id, final AtomicBoolean valid) {
        if (id == null) {
            error(context, "templateId", "{javax.validation.constraints.NotNull.message}", valid);
            return false;
        } else if (getManager().find(Template.class, id) == null) {
            error(context, "templateId", "{dte.validation.constraints.reference.message}", valid);
            return false;
        }
        return true;
    }

    /**
     * Check the specified {@code label} is not empty and unique.
     *
     * @param ctx   the validation context
     * @param label the label value
     * @param valid the valid flag
     * @return {@code true} if valid, otherwise {@code false}
     */
    boolean checkLabel(final ConstraintValidatorContext ctx, final String label, final AtomicBoolean valid) {
        if (label == null || label.length() == 0) {
            error(ctx, "label", "{javax.validation.constraints.NotEmpty.message}", valid);
            return false;
        }
        return true;
    }

    /**
     * Check the specified {@code script} is not empty and does not have syntax errors.
     *
     * @param ctx    the validation context
     * @param tree   the tree script node
     * @param typing the declared typing
     * @param valid  the valid flag
     */
    void checkScript(final ConstraintValidatorContext ctx, final Node tree, final Map<String, ? extends Input> typing,
                     final AtomicBoolean valid) {
        final String script = tree.getScript();
        if (script == null || script.trim().length() == 0) {
            error(ctx, "script", "{javax.validation.constraints.NotEmpty.message}", valid);
        } else {
            try {
                final Map<String, Integer> types = typing.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getTypeId()));
                getEngine().validate(tree.getTemplateId(), script, types);
            } catch (CompilationException ex) {
                final MultipleCompilationErrorsException cause = (MultipleCompilationErrorsException) ex.getCause();
                for (int i = 0; i < cause.getErrorCollector().getErrorCount(); i++) {
                    final Writer message = new StringBuilderWriter();
                    cause.getErrorCollector().getError(i).write(new PrintWriter(message));
                    error(ctx, "script", message.toString(), valid);
                }
            } catch (RuntimeException ex) {
                error(ctx, "script", ex.getMessage(), valid);
            }
        }
    }

    /**
     * Check the specified {@code value} is not null.
     *
     * @param c     the validation context
     * @param name  the property name
     * @param value the value to compile
     * @param v     the valid flag
     */
    void notNull(final ConstraintValidatorContext c, final String name, final Object value, final AtomicBoolean v) {
        if (value == null) {
            error(c, name, "{javax.validation.constraints.NotNull.message}", v);
        }
    }

    /**
     * Add an error to the specified validation context.
     *
     * @param c the validation context
     * @param n the property name
     * @param m the error message
     * @param v the valid flag (optional)
     */
    void error(final ConstraintValidatorContext c, final String n, final String m, final AtomicBoolean v) {
        if (v != null) {
            v.set(false);
            c.disableDefaultConstraintViolation();
        }
        c.buildConstraintViolationWithTemplate(m).addPropertyNode(n).addConstraintViolation();
    }

    /**
     * Check the group reference within respecting the notion of sharing between the hierarchy of groups.
     *
     * @param context the validation context
     * @param name    the property name
     * @param id      the expected group identifier
     * @param entity  the shareable entity
     * @param valid   the valid flag
     * @return {@code true} if valid, otherwise {@code false}
     */
    private boolean checkGroup(final ConstraintValidatorContext context, final String name, final Integer id,
                               final Shareable entity, final AtomicBoolean valid) {
        if (entity == null) {
            error(context, name, "{dte.validation.constraints.reference.message}", valid);
            return false;
        } else if (!entity.isShared() && id != null && !id.equals(entity.getGroup().getId())) {
            IGroup g = entity.getGroup();
            while (g instanceof Group) {
                g = ((Group) g).getParent();
                if (id.equals(entity.getGroup().getId())) {
                    return true;
                }
            }
            error(context, name, "{dte.validation.constraints.group.message}", valid);
            return false;
        }
        return true;
    }

    /**
     * Check if the declared  {@code typing} matches the specified {@code specs}.
     *
     * @param ctx      the validation context
     * @param property the property name
     * @param typing   the declared typing
     * @param specs    the specification typing
     * @param valid    the valid flag
     */
    private void checkSpecification(final ConstraintValidatorContext ctx, final String property,
                                    final Map<String, ? extends Input> typing,
                                    final Map<String, ? extends DocumentedType> specs, final AtomicBoolean valid) {
        specs.forEach((name, type) -> {
            final Input input = typing.get(name);
            final String errorKey = property + '.' + name;
            if (!type.isOptional() && (input == null || input.getOptional())) {
                error(ctx, errorKey, "{dte.validation.constraints.required.message}", valid);
            }
            if (input != null && input.getTypeId() != null && input.getTypeId().equals(type.getType().getId())) {
                error(ctx, errorKey, "{dte.validation.constraints.mismatch.message}", valid);
            }
        });
    }

    /**
     * Return the root node of the given tree {@code node}.
     *
     * @param node the tree node
     * @param <T>  the concrete node type
     * @return the root node
     */
    protected <T extends Node<T>> T getRoot(final T node) {
        T parent = node;
        while (parent.getParent() != null) {
            parent = parent.getParent();
        }
        return parent;
    }
}
