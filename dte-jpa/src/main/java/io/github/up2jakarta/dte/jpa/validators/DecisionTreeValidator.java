package io.github.up2jakarta.dte.jpa.validators;

import io.github.up2jakarta.dte.jpa.DynamicEngine;
import io.github.up2jakarta.dte.jpa.api.IComputer;
import io.github.up2jakarta.dte.jpa.entities.DTreeComputer;
import io.github.up2jakarta.dte.jpa.models.Computer;
import io.github.up2jakarta.dte.jpa.models.Input;
import io.github.up2jakarta.dte.jpa.models.Output;
import io.github.up2jakarta.dte.jpa.repositories.ComputerRepository;
import io.github.up2jakarta.dte.jpa.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static io.github.up2jakarta.dte.jpa.models.Computer.Type.*;

/**
 * {@link Computer} validator.
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class DecisionTreeValidator extends AbstractValidator implements ConstraintValidator<DecisionTree, Computer> {

    private List<Computer.Type> allowedTypes;

    @Autowired
    private EntityManager manager;
    @Autowired
    private ComputerRepository repository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private DynamicEngine engine;

    @Override
    public EntityManager getManager() {
        return manager;
    }

    @Override
    public TypeRepository getTypeRepository() {
        return typeRepository;
    }

    @Override
    DynamicEngine getEngine() {
        return engine;
    }

    @Override
    public void initialize(final DecisionTree constraint) {
        this.allowedTypes = Collections.unmodifiableList(Arrays.asList(constraint.types()));
    }

    @Override
    public boolean isValid(final Computer model, final ConstraintValidatorContext context) {
        final Computer.Type type = model.getType();
        if (model.getType() == null || !allowedTypes.contains(model.getType())) {
            if (model.getType() == null) {
                error(context, "type", "{javax.validation.constraints.NotEmpty.message}", null);
            } else {
                error(context, "type", "{dte.validation.constraints.type.message}", null);
            }
            context.disableDefaultConstraintViolation();
            return false;
        }
        final Computer root = getRoot(model);
        final AtomicBoolean isValid = new AtomicBoolean(true);
        final boolean notEmpty = checkLabel(context, model.getLabel(), isValid);
        if (type == DECIDER || type == DECISION) {
            notNull(context, "negated", model.getNegated(), isValid);
        }
        if (type == PLAIN || type == MIXED || type == LOCAL || type == DECISION) {
            notNull(context, "shared", model.getShared(), isValid);
            if (notEmpty && root.getGroupId() != null && (type == PLAIN || type == MIXED)) {
                final IComputer found = repository.find(root.getGroupId(), model.getLabel());
                if (found != null && !found.getId().equals(model.getId())) {
                    error(context, "label", "{dte.validation.constraints.label.message}", isValid);
                }
            }
        }
        if (type == PLAIN || type == MIXED) {
            checkGroup(context, model.getGroupId(), root.getGroupId(), isValid);
            if (model.getOutputs() == null || model.getOutputs().isEmpty()) {
                error(context, "outputs", "{javax.validation.constraints.NotEmpty.message}", isValid);
            }
            if (type == MIXED) {
                checkInputs(context, "inputs", model.getInputs(), isValid);
                checkOutputs(context, model, getTyping(root, model), isValid);
            }
        }
        if (type == COMPUTER) {
            checkComputer(context, root.getGroupId(), model.getComputerId(), getTyping(root, model), isValid);
        }
        if (type == DECIDER) {
            checkDecider(context, root.getGroupId(), model.getDeciderId(), getTyping(root, model), isValid);
        }
        if (type == PLAIN || type == LOCAL || type == DECISION) {
            final Map<String, Input> typing = getTyping(root, model);
            boolean compile = checkTemplate(context, model.getTemplateId(), isValid);
            compile = compile && checkInputs(context, "inputs", model.getInputs(), isValid);
            compile = compile && checkOutputs(context, model, typing, isValid);
            if (compile) {
                checkScript(context, model, typing, isValid);
            }
        }
        if (type != PLAIN) {
            final List<Computer> children = model.getChildren();
            if (type.isDecision() || type == MIXED) {
                if (children == null || children.size() == 0) {
                    error(context, "children", "{javax.validation.constraints.NotEmpty.message}", isValid);
                } else {
                    checkChildren(context, children, isValid);
                }
            } else if (children != null && children.size() != 0) {
                checkDecisions(context, children, isValid);
            }
        }
        return isValid.get();
    }

    /**
     * Check the specified {@code typings} are valid, i.e unique variable names and existing types.
     *
     * @param ctx    the validation context
     * @param model  the comp=ter toe checked
     * @param typing the declared typing
     * @param v      the valid flag
     * @return {@code true} if valid, otherwise {@code false}
     */
    private boolean checkOutputs(final ConstraintValidatorContext ctx, final Computer model,
                                 final Map<String, ? extends Input> typing, final AtomicBoolean v) {
        if (model.getOutputs() == null || model.getOutputs().isEmpty()) {
            return true;
        }
        boolean isValid = checkInputs(ctx, "outputs", model.getOutputs(), v);
        int index = 0;
        for (final Output output : model.getOutputs()) {
            if (output.getTypeId() != null) {
                final Input input = typing.get(output.getVariable());
                if (input != null && !output.getTypeId().equals(input.getTypeId())) {
                    isValid = false;
                    final String p = "outputs[" + index + "].typeId";
                    error(ctx, p, "{dte.validation.constraints.output.message}", v);
                }
            }
            index++;
        }
        return isValid;
    }

    /**
     * Check if the children nodes have the same type.
     *
     * @param c the validation context.
     * @param l the list of children nodes.
     * @param v the valid flag
     */
    private void checkChildren(final ConstraintValidatorContext c, final List<Computer> l, final AtomicBoolean v) {
        final List<Boolean> isDecisions = new ArrayList<>(l.size());
        final List<Integer> defPositions = new ArrayList<>(l.size());
        int i = 0;
        for (final Computer child : l) {
            final Boolean isDecision = Optional.of(child.getType()).map(Computer.Type::isDecision).orElse(null);
            isDecisions.add(isDecision);
            if (child.getType() == DEFAULT) {
                defPositions.add(i);
            }
            i++;
        }
        if (isDecisions.contains(Boolean.TRUE) && isDecisions.contains(Boolean.FALSE)) {
            error(c, "children", "{dte.validation.constraints.children.message}", v);
        } else {
            check(c, v, defPositions, l.size());
        }
    }

    /**
     * Check if the all children nodes are decisions.
     *
     * @param c the validation context.
     * @param l the list of children nodes.
     * @param v the valid flag
     */
    private void checkDecisions(final ConstraintValidatorContext c, final List<Computer> l, final AtomicBoolean v) {
        final List<Integer> defPositions = new ArrayList<>(l.size());
        int i = 0;
        for (final Computer child : l) {
            final boolean isDecision = Optional.of(child.getType()).map(Computer.Type::isDecision).orElse(false);
            if (!isDecision) {
                error(c, "children", "{dte.validation.constraints.decisions.message}", v);
                return;
            } else if (child.getType() == DEFAULT) {
                defPositions.add(i);
            }
            i++;
        }
        check(c, v, defPositions, l.size());
    }

    /**
     * Check the default node and add the most specific error message.
     *
     * @param c the validation context.
     * @param v the valid flag
     * @param p the positions of default decision
     * @param s the size of the children
     */
    private void check(final ConstraintValidatorContext c, final AtomicBoolean v, final List<Integer> p, final int s) {
        if (p.isEmpty()) {
            return;
        }
        if (s == 1) {
            error(c, "children", "{dte.validation.constraints.default.one.message}", v);
        } else if (p.size() == 1 && p.get(0) != s - 1) {
            error(c, "children", "{dte.validation.constraints.default.last.message}", v);
        } else if (p.size() > 1) {
            error(c, "children", "{dte.validation.constraints.default.many.message}", v);
        }
    }

    /**
     * Return the context typing (inputs and outputs) of the given {@code node}.
     *
     * @param root the root node
     * @param node the tree node
     * @return the JPA context typing (name/typeId)
     */
    private Map<String, Input> getTyping(final Computer root, final Computer node) {
        final Map<String, Input> typing = new HashMap<>();
        if (root.getInputs() != null) {
            final Map<String, Input> spec = root.getInputs().stream()
                    .filter(i -> i.getTypeId() != null)
                    .collect(Collectors.toMap(Input::getVariable, e -> e));
            typing.putAll(spec);
        }
        Computer parent = node;
        while (parent != root) {
            parent = parent.getParent();
            List<Output> outputs = null;
            if (parent.getType() == COMPUTER) {
                final DTreeComputer ref = Optional.ofNullable(parent.getComputerId())
                        .map(repository::find)
                        .orElse(null);
                if (ref != null) {
                    outputs = ref.getDeclaredTyping().entrySet().stream()
                            .map(e -> new Output(e.getKey(), e.getValue()))
                            .collect(Collectors.toList());
                }
            } else if (parent.getType() == LOCAL) {
                outputs = parent.getOutputs();
            }
            if (outputs != null) {
                outputs.stream().filter(i -> i.getTypeId() != null).forEach((o) -> {
                    final Input i = typing.get(o.getVariable());
                    assert (i == null || i.getTypeId().equals(o.getTypeId())) : "Incompatible types " + root.getId();
                    if (o.getShared() && (i == null || (i.getOptional() && !o.getOptional()))) {
                        typing.put(o.getVariable(), o);
                    }
                });
            }
        }
        return typing;
    }
}
