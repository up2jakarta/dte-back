package io.github.up2jakarta.dte.jpa.validators;

import io.github.up2jakarta.dte.jpa.DynamicEngine;
import io.github.up2jakarta.dte.jpa.api.IDecider;
import io.github.up2jakarta.dte.jpa.models.Decider;
import io.github.up2jakarta.dte.jpa.models.Input;
import io.github.up2jakarta.dte.jpa.repositories.DeciderRepository;
import io.github.up2jakarta.dte.jpa.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static io.github.up2jakarta.dte.jpa.models.Decider.Type.*;

/**
 * {@link Decider} validator.
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class BinaryTreeValidator extends AbstractValidator implements ConstraintValidator<BinaryTree, Decider> {

    private List<Decider.Type> allowedTypes;

    @Autowired
    private EntityManager manager;
    @Autowired
    private DeciderRepository repository;
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
    public void initialize(final BinaryTree constraint) {
        this.allowedTypes = Collections.unmodifiableList(Arrays.asList(constraint.types()));
    }

    @Override
    public boolean isValid(final Decider model, final ConstraintValidatorContext context) {
        final Decider.Type type = model.getType();
        if (model.getType() == null || !allowedTypes.contains(model.getType())) {
            if (model.getType() == null) {
                error(context, "type", "{javax.validation.constraints.NotEmpty.message}", null);
            } else {
                error(context, "type", "{dte.validation.constraints.type.message}", null);
            }
            context.disableDefaultConstraintViolation();
            return false;
        }
        final Decider root = getRoot(model);
        final AtomicBoolean isValid = new AtomicBoolean(true);
        notNull(context, "negated", model.getNegated(), isValid);
        final boolean notEmpty = checkLabel(context, model.getLabel(), isValid);

        if (type == PLAIN || type == MIXED || type == LOCAL) {
            notNull(context, "shared", model.getShared(), isValid);
            if (notEmpty && root.getGroupId() != null && type != LOCAL) {
                final IDecider found = repository.find(root.getGroupId(), model.getLabel());
                if (found != null && !found.getId().equals(model.getId())) {
                    error(context, "label", "{dte.validation.constraints.label.message}", isValid);
                }
            }
            if (type == MIXED) {
                checkInputs(context, "inputs", model.getInputs(), isValid);
            }
        }
        if (type == PLAIN || type == MIXED) {
            checkGroup(context, model.getGroupId(), root.getGroupId(), isValid);
        }
        if (type == DECIDER) {
            checkDecider(context, root.getGroupId(), model.getDeciderId(), getTyping(root), isValid);
        }
        if (type == PLAIN || type == LOCAL) {
            boolean compile = checkTemplate(context, model.getTemplateId(), isValid);
            if (compile) {
                checkScript(context, model, getTyping(root), isValid);
            }
        }
        if (type == OPERATOR || type == MIXED) {
            if (model.getOperator() == null) {
                error(context, "operator", "{javax.validation.constraints.NotEmpty.message}", isValid);
            }
            if (model.getOperands() == null || model.getOperands().size() < 2) {
                error(context, "operands", "{dte.validation.constraints.operands.message}", isValid);
            }
        }
        return isValid.get();
    }

    /**
     * Return the context typing (inputs and outputs) of the given {@code node}.
     *
     * @param root the root node
     * @return the JPA context typing (name/typeId)
     */
    private Map<String, Input> getTyping(final Decider root) {
        if (root.getInputs() == null || root.getInputs().isEmpty()) {
            return Collections.emptyMap();
        }
        return root.getInputs().stream()
                .filter(i -> i.getTypeId() != null)
                .collect(Collectors.toMap(Input::getVariable, e -> e));
    }
}
