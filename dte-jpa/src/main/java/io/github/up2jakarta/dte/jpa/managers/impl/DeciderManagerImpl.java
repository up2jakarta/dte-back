package io.github.up2jakarta.dte.jpa.managers.impl;

import io.github.up2jakarta.dte.jpa.api.IDecider;
import io.github.up2jakarta.dte.jpa.api.btree.IChildNode;
import io.github.up2jakarta.dte.jpa.api.btree.IMixedDecider;
import io.github.up2jakarta.dte.jpa.api.btree.IOperatorNode;
import io.github.up2jakarta.dte.jpa.entities.BTreeDecider;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.Template;
import io.github.up2jakarta.dte.jpa.entities.btn.*;
import io.github.up2jakarta.dte.jpa.managers.DeciderManager;
import io.github.up2jakarta.dte.jpa.models.Decider;
import io.github.up2jakarta.dte.jpa.models.Decider.Type;
import io.github.up2jakarta.dte.jpa.views.VDecider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

/**
 * {@link DeciderManager} implementation.
 */
@Component
public class DeciderManagerImpl implements DeciderManager {

    private final EntityManager entityManager;

    /**
     * Public constructor for DeciderManagerImpl.
     *
     * @param entityManager the persistence context
     */
    @Autowired
    public DeciderManagerImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Decider> convert(final List<VDecider> deciders) {
        return deciders.stream()
                .map(entity -> {
                    final Type type = (entity.getId().getType().equals('S')) ? Type.PLAIN : Type.MIXED;
                    final Decider model = new Decider(type);
                    model.setId(entity.getId().getId());
                    model.setLabel(entity.getLabel());
                    model.setShared(entity.getShared());
                    model.setDescription(entity.getDescription());
                    return model;
                })
                .collect(toList());
    }

    @Override
    public Decider convert(final BTreeDecider entity) {
        if (entity instanceof BTreePlainDecider) {
            final Decider model = new Decider(Type.PLAIN);
            final BTreePlainDecider plain = (BTreePlainDecider) entity;
            model.setScript(plain.getScript());
            model.setTemplateId(plain.getTemplate().getId());
            return fill(entity, model);
        }
        final Decider model = new Decider(Type.MIXED);
        final BTreeMixedDecider mixed = (BTreeMixedDecider) entity;
        this.fill(entity, model);
        model.setOperator(mixed.getOperator());
        setChildren(model, mixed.findOperands());
        return model;
    }

    @Override
    public BTreeDecider create(final Decider model) {
        final Group group = entityManager.getReference(Group.class, model.getGroupId());
        if (model.getType() == Type.PLAIN) {
            return update(new BTreePlainDecider(group), model);
        }
        return update(new BTreeMixedDecider(group), model);
    }

    @Override
    public BTreeDecider update(final BTreeDecider entity, final Decider model) {
        entity.setNegated(model.getNegated());
        entity.setShared(model.getShared());
        entity.setLabel(model.getLabel());
        entity.setDescription(model.getDescription());
        if (entity instanceof BTreeMixedDecider) {
            final BTreeMixedDecider mixed = (BTreeMixedDecider) entity;
            mixed.setOperator(model.getOperator());
            this.clearNodes(mixed, model);
            setChildren(mixed, model.getOperands());
        } else if (entity instanceof BTreePlainDecider) {
            final BTreePlainDecider plain = (BTreePlainDecider) entity;
            plain.setScript(model.getScript());
            plain.setTemplate(entityManager.getReference(Template.class, model.getTemplateId()));
        }
        return entity;
    }

    /**
     * Convert the given {@code node} to an entity node.
     *
     * @param parent the parent entity
     * @param model  the child node
     * @return the created entity
     */
    private IChildNode convertChild(final IOperatorNode parent, final Decider model) {
        switch (model.getType()) {
            case DECIDER:
                final IDecider decider = entityManager.getReference(BTreeDecider.class, model.getDeciderId());
                final BTreeDeciderNode link = getChild(parent, model.getId(), BTreeDeciderNode::new);
                link.setDecider(decider);
                link.setLabel(model.getLabel());
                link.setNegated(model.getNegated());
                return link;
            case LOCAL:
                final Supplier<BTreeLocalNode> creator = () -> {
                    final Group group = entityManager.getReference(Group.class, model.getGroupId());
                    return new BTreeLocalNode(group);
                };
                final BTreeLocalNode local = getChild(parent, model.getId(), creator);
                local.setScript(model.getScript());
                local.setNegated(model.getNegated());
                local.setLabel(model.getLabel());
                local.setDescription(model.getDescription());
                local.setShared(model.getShared());
                local.setTemplate(entityManager.getReference(Template.class, model.getTemplateId()));
                return local;
            default:
                final BTreeOperatorNode operator = getChild(parent, model.getId(), BTreeOperatorNode::new);
                operator.setOperator(model.getOperator());
                operator.setNegated(model.getNegated());
                operator.setLabel(model.getLabel());
                return operator;
        }
    }

    /**
     * Convert the given entity {@code node} to binary tree.
     *
     * @param node the child entity
     * @return the created binary tree
     */
    private Decider convertChild(final IChildNode node) {
        if (node instanceof BTreeDeciderNode) {
            final BTreeDeciderNode entity = (BTreeDeciderNode) node;
            final Decider model = convertChild(Type.DECIDER, entity);
            model.setDeciderId(entity.getDecider().getId());
            model.setDescription(entity.getDecider().getDescription());
            return model;
        }
        if (node instanceof BTreeLocalNode) {
            final BTreeLocalNode entity = (BTreeLocalNode) node;
            final Decider model = convertChild(Type.LOCAL, entity);
            model.setScript(entity.getScript());
            model.setDescription(entity.getDescription());
            model.setShared(entity.isShared());
            model.setTemplateId(entity.getTemplate().getId());
            return model;
        }
        final BTreeOperatorNode entity = (BTreeOperatorNode) node;
        final Decider model = convertChild(Type.OPERATOR, entity);
        model.setOperator(entity.getOperator());
        setChildren(model, entity.findOperands());
        return model;
    }

    /**
     * Create the children node for the given {@code parent} from the given {@code children}.
     *
     * @param parent   the parent model
     * @param children the children entities
     */
    private void setChildren(final IOperatorNode parent, final List<Decider> children) {
        if (children == null || children.isEmpty()) {
            parent.getOperands().clear();
        } else {
            // Backup order @see unique constraint
            int order = parent.getOperands().stream()
                    .max(Comparator.comparingInt(IChildNode::getOrder))
                    .map(c -> c.getOrder() + 1)
                    .orElse(0);
            // Cleanup
            parent.getOperands().clear();
            // Sub-children
            for (final Decider child : children) {
                final IChildNode node = convertChild(parent, child);
                parent.addOperand(node, order++);
                if (node instanceof IOperatorNode) {
                    setChildren((IOperatorNode) node, child.getOperands());
                }
            }
        }
    }

    /**
     * Update the main properties of the given {@code model} from the source {@code entity}.
     *
     * @param entity the entity
     * @param model  the binary tree
     * @return the updated model
     */
    private Decider fill(final BTreeDecider entity, final Decider model) {
        model.setId(entity.getId());
        model.setLabel(entity.getLabel());
        model.setDescription(entity.getDescription());
        model.setGroupId(entity.getGroup().getId());
        model.setNegated(entity.isNegated());
        model.setShared(entity.isShared());
        return model;
    }

    /**
     * Create un model node from the given {@code entity}.
     *
     * @param t the model type
     * @param e the source entity
     * @return the created model
     */
    private Decider convertChild(final Type t, final IChildNode e) {
        final Decider model = new Decider(t);
        model.setId(e.getId());
        model.setNegated(e.isNegated());
        model.setLabel(e.getLabel());
        return model;
    }

    /**
     * Create the children node for the given {@code parent} from the given {@code children}.
     *
     * @param parent   the parent model
     * @param children the children entities
     */
    private void setChildren(final Decider parent, final Set<IChildNode> children) {
        if (!children.isEmpty()) {
            final List<IChildNode> list = new ArrayList<>(children);
            list.sort(Comparator.comparingInt(IChildNode::getOrder));
            final List<Decider> operands = new ArrayList<>(children.size());
            parent.setOperands(operands);
            for (final IChildNode child : list) {
                operands.add(convertChild(child));
            }
        }
    }

    /**
     * Search a child with the specified identifier and check the child class {@link #clearNodes}.
     * Return the found one if its type is verified, otherwise create another one.
     *
     * @param parent  the parent entity
     * @param id      the child identifier
     * @param creator the child creator
     * @param <C>     the child class type
     * @return the found child, otherwise the supplied child
     */
    @SuppressWarnings("unchecked")
    private <C extends IChildNode> C getChild(final IOperatorNode parent, final Long id, final Supplier<C> creator) {
        if (id == null) {
            return creator.get();
        }
        return parent.getRoot().getNodes().stream()
                .filter(e -> id.equals(e.getId()))
                .findFirst()
                .map(f -> (C) f)
                .orElse(creator.get());
    }

    /**
     * Removes nodes from the specified {@code tree} that was removed from the given {@code model}.
     *
     * @param tree  the binary tree entity
     * @param model the binary tree
     */
    private void clearNodes(final IMixedDecider tree, final Decider model) {
        final Map<Long, Class<BTreeChildNode>> remaining = this.getRemaining(model);
        remaining.forEach((id, type) -> {
            // Search operands having same identifier
            final List<IChildNode> btn = tree.getNodes().stream().filter(e -> id.equals(e.getId())).collect(toList());
            // Keep the first operand that matches id and type equalities
            btn.stream().filter(type::isInstance).findFirst().ifPresent(btn::remove);
            // Remove duplicated operands
            tree.getNodes().removeAll(btn);
        });
        tree.getNodes().removeIf(n -> !remaining.containsKey(n.getId()));
    }

    /**
     * Get the remaining identifiers of all nodes of the specified model with their expected class.
     *
     * @param model the binary tree
     * @return the map of identifier/class
     */
    @SuppressWarnings("unchecked")
    private Map<Long, Class<BTreeChildNode>> getRemaining(final Decider model) {
        if (model.getOperands() == null || model.getOperands().isEmpty()) {
            return Collections.emptyMap();
        }
        final Map<Long, Class<BTreeChildNode>> map = new HashMap<>();
        model.getOperands().forEach(c -> {
            if (c.getId() != null) {
                map.put(c.getId(), (Class<BTreeChildNode>) c.getType().getEntityClass());
            }
            if (c.getType() == Type.OPERATOR) {
                map.putAll(getRemaining(c));
            }
        });
        return map;
    }

}
