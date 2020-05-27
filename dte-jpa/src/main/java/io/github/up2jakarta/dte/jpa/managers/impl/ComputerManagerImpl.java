package io.github.up2jakarta.dte.jpa.managers.impl;

import io.github.up2jakarta.dte.jpa.api.IComputer;
import io.github.up2jakarta.dte.jpa.api.IDecider;
import io.github.up2jakarta.dte.jpa.api.dtree.IChildNode;
import io.github.up2jakarta.dte.jpa.api.dtree.IMixedComputer;
import io.github.up2jakarta.dte.jpa.api.dtree.IParentNode;
import io.github.up2jakarta.dte.jpa.entities.*;
import io.github.up2jakarta.dte.jpa.entities.dtn.*;
import io.github.up2jakarta.dte.jpa.managers.ComputerManager;
import io.github.up2jakarta.dte.jpa.models.Computer;
import io.github.up2jakarta.dte.jpa.models.Input;
import io.github.up2jakarta.dte.jpa.models.Output;
import io.github.up2jakarta.dte.jpa.views.VComputer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.Supplier;

import static io.github.up2jakarta.dte.jpa.models.Computer.Type.*;
import static java.util.stream.Collectors.toList;

/**
 * {@link ComputerManager} implementation.
 */
@Component
public class ComputerManagerImpl implements ComputerManager {

    private final EntityManager entityManager;

    /**
     * Public constructor for DTreeManagerImpl.
     *
     * @param entityManager the persistence context
     */
    @Autowired
    public ComputerManagerImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Set the outputs property of the given {@code model} from the specified source {@code typing}..
     *
     * @param model  the decision tree
     * @param typing the persistent outputs
     */
    private static void fillOutputs(final Computer model, final Map<String, DTreeNode.Output> typing) {
        if (typing != null && !typing.isEmpty()) {
            final List<Output> outputs = typing.entrySet().stream()
                    .map(e -> new Output(e.getKey(), e.getValue()))
                    .collect(toList());
            model.setOutputs(outputs);
        } else {
            model.setInputs(Collections.emptyList());
        }
    }

    /**
     * Set the inputs property of the given {@code model} from the specified source {@code typing}..
     *
     * @param model  the decision tree
     * @param typing the persistent inputs
     */
    private static void fillInputs(final Computer model, final Map<String, DTreeNode.Input> typing) {
        if (typing != null && !typing.isEmpty()) {
            final List<Input> inputs = typing.entrySet().stream()
                    .map(e -> new Input(e.getKey(), e.getValue()))
                    .collect(toList());
            model.setInputs(inputs);
        } else {
            model.setInputs(Collections.emptyList());
        }
    }

    /**
     * Search a child with the specified identifier and check the child class {@link #clearNodes}.
     * Return the found one if its type is verified, otherwise create another one.
     *
     * @param node    the parent entity
     * @param id      the child identifier
     * @param creator the child creator
     * @param <C>     the child class type
     * @return the found child, otherwise the supplied child
     */
    @SuppressWarnings("unchecked")
    private static <C extends IChildNode> C getChild(final IParentNode node, final Long id, final Supplier<C> creator) {
        if (id == null) {
            return creator.get();
        }
        return node.getRoot().getNodes().stream()
                .filter(e -> id.equals(e.getId()))
                .findFirst()
                .map(f -> (C) f)
                .orElse(creator.get());
    }

    /**
     * Convert the given entity {@code node} to decision tree model.
     *
     * @param node the child entity
     * @return the created decision tree
     */
    private static Computer convertChild(final IChildNode node) {
        if (node instanceof DTreeComputerNode) {
            final DTreeComputerNode entity = (DTreeComputerNode) node;
            final Computer model = new Computer(COMPUTER);
            model.setId(entity.getId());
            model.setComputerId(entity.getComputer().getId());
            model.setLabel(entity.getLabel());
            model.setDescription(entity.getComputer().getDescription());
            return model;
        }
        if (node instanceof DTreeLocalNode) {
            final DTreeLocalNode entity = (DTreeLocalNode) node;
            final Computer model = new Computer(LOCAL);
            fill(entity, model);
            fillOutputs(model, entity.getDeclaredTyping());
            return model;
        }
        if (node instanceof DTreeDefaultNode) {
            final Computer model = new Computer(DEFAULT);
            model.setId(node.getId());
            model.setLabel(((DTreeDefaultNode) node).getLabel());
            return model;
        }
        if (node instanceof DTreeDeciderNode) {
            final DTreeDeciderNode entity = (DTreeDeciderNode) node;
            final Computer model = new Computer(DECIDER);
            model.setId(entity.getId());
            model.setDeciderId(entity.getDecider().getId());
            model.setNegated(entity.isNegated());
            model.setLabel(entity.getLabel());
            model.setDescription(entity.getDecider().getDescription());
            return model;
        }
        final DTreeDecisionNode entity = (DTreeDecisionNode) node;
        final Computer model = new Computer(DECISION);
        fill(entity, model);
        model.setNegated(entity.isNegated());
        return model;
    }

    /**
     * Removes nodes from the specified {@code tree} that was removed from the given {@code model}.
     *
     * @param tree  the decision tree entity
     * @param model the decision tree
     */
    private static void clearNodes(final IMixedComputer tree, final Computer model) {
        final Map<Long, Class<DTreeChildNode>> remaining = getRemaining(model);
        remaining.forEach((id, type) -> {
            // Search children having same identifier
            final List<IChildNode> dtn = tree.getNodes().stream().filter(e -> id.equals(e.getId())).collect(toList());
            // Keep the first child that matches id and type equalities
            dtn.stream().filter(type::isInstance).findFirst().ifPresent(dtn::remove);
            // Remove duplicated children
            tree.getNodes().removeAll(dtn);
        });
        tree.getNodes().removeIf(n -> !remaining.containsKey(n.getId()));
    }

    /**
     * Get the remaining identifiers of all nodes of the specified model with their expected class.
     *
     * @param model the decision tree
     * @return the map of identifier/class
     */
    @SuppressWarnings("unchecked")
    private static Map<Long, Class<DTreeChildNode>> getRemaining(final Computer model) {
        if (model.getChildren() == null || model.getChildren().isEmpty()) {
            return Collections.emptyMap();
        }
        final Map<Long, Class<DTreeChildNode>> map = new HashMap<>();
        int index = 0;
        for (final Computer c : model.getChildren()) {
            if (c.getId() != null) {
                map.put(c.getId(), (Class<DTreeChildNode>) c.getType().getEntityClass());
            }
            if ((c.getType().isDecision() || index == 0)) {
                map.putAll(getRemaining(c));
            }
            index++;
        }
        return map;
    }

    /**
     * Update the common properties of the given {@code model} from the source {@code entity}.
     *
     * @param entity the entity
     * @param model  the script node
     */
    private static void fill(final DTreeScriptNode entity, final Computer model) {
        model.setId(entity.getId());
        model.setScript(entity.getScript());
        model.setLabel(entity.getLabel());
        model.setDescription(entity.getDescription());
        model.setShared(entity.isShared());
        model.setTemplateId(entity.getTemplate().getId());
        model.setGroupId(entity.getGroup().getId());
        fillInputs(model, entity.getTyping());
    }

    /**
     * Update the main properties of the given {@code model} from the source {@code entity}.
     *
     * @param entity the entity
     * @param model  the decision tree
     * @return the updated model
     */
    private static Computer fill(final DTreeComputer entity, final Computer model) {
        model.setId(entity.getId());
        model.setLabel(entity.getLabel());
        model.setDescription(entity.getDescription());
        model.setGroupId(entity.getGroup().getId());
        model.setShared(entity.isShared());
        fillInputs(model, entity.getTyping());
        fillOutputs(model, entity.getDeclaredTyping());
        return model;
    }

    @Override
    public List<Computer> convert(final List<VComputer> computers) {
        return computers.stream()
                .map(entity -> {
                    final Computer.Type type = (entity.getType().equals('S')) ? PLAIN : MIXED;
                    final Computer model = new Computer(type);
                    model.setId(entity.getId());
                    model.setLabel(entity.getLabel());
                    model.setShared(entity.getShared());
                    model.setDescription(entity.getDescription());
                    return model;
                })
                .collect(toList());
    }

    @Override
    public Computer convert(final DTreeComputer entity) {
        if (entity instanceof DTreePlainComputer) {
            final Computer model = new Computer(PLAIN);
            final DTreePlainComputer plain = (DTreePlainComputer) entity;
            model.setScript(plain.getScript());
            model.setTemplateId(plain.getTemplate().getId());
            return fill(entity, model);
        }
        final Computer model = new Computer(MIXED);
        final DTreeMixedComputer mixed = (DTreeMixedComputer) entity;
        fill(entity, model);
        setChildren(model, mixed.findChildren());
        return model;
    }

    @Override
    public DTreeComputer create(final Computer model) {
        final Group group = entityManager.getReference(Group.class, model.getGroupId());
        if (model.getType() == PLAIN) {
            return update(new DTreePlainComputer(group), model);
        }
        return update(new DTreeMixedComputer(group), model);
    }

    @Override
    public DTreeComputer update(final DTreeComputer entity, final Computer model) {
        entity.setLabel(model.getLabel());
        entity.setDescription(model.getDescription());
        entity.setShared(model.getShared());
        if (entity instanceof DTreeMixedComputer) {
            final DTreeMixedComputer mixed = (DTreeMixedComputer) entity;
            clearNodes(mixed, model);
            setChildren(mixed, model.getChildren());
        } else if (entity instanceof DTreePlainComputer) {
            final DTreePlainComputer plain = (DTreePlainComputer) entity;
            plain.setScript(model.getScript());
            plain.setTemplate(entityManager.getReference(Template.class, model.getTemplateId()));
        }
        updateInputs(entity.getTyping(), model.getInputs());
        updateOutputs(entity.getDeclaredTyping(), model.getOutputs());
        return entity;
    }

    /**
     * Create the children node for the given {@code parent} from the given {@code children}.
     *
     * @param parent   the parent model
     * @param children the children entities
     */
    private void setChildren(final IParentNode parent, final List<Computer> children) {
        if (children == null || children.isEmpty()) {
            parent.getChildren().clear();
        } else {
            // Backup order @see unique constraint
            int order = parent.getChildren().stream()
                    .max(Comparator.comparingInt(IChildNode::getOrder))
                    .map(c -> c.getOrder() + 1)
                    .orElse(0);
            // Cleanup
            parent.getChildren().clear();
            // Sub-children
            for (final Computer child : children) {
                final DTreeChildNode node = convertParent(parent, child);
                parent.addChild(node, order++);
                setChildren(node, child.getChildren());
            }
        }
    }

    /**
     * Create the children node for the given {@code parent} from the given {@code children}.
     *
     * @param parent   the parent node
     * @param children the children entities
     */
    private void setChildren(final Computer parent, final Set<IChildNode> children) {
        if (!children.isEmpty()) {
            final List<IChildNode> list = new ArrayList<>(children);
            list.sort(Comparator.comparingInt(IChildNode::getOrder));
            final List<Computer> nodes = new ArrayList<>(children.size());
            parent.setChildren(nodes);
            for (final IChildNode child : list) {
                final Computer node = convertChild(child);
                nodes.add(node);
                setChildren(node, child.findChildren());
            }
        }
    }

    /**
     * Convert the given {@code node} to an entity node.
     *
     * @param parent the parent entity
     * @param model  the child model
     * @return the created/updated entity
     */
    private DTreeChildNode convertParent(final IParentNode parent, final Computer model) {
        switch (model.getType()) {
            case COMPUTER:
                final IComputer computer = entityManager.getReference(DTreeComputer.class, model.getComputerId());
                final DTreeComputerNode computerNode = getChild(parent, model.getId(), DTreeComputerNode::new);
                computerNode.setComputer(computer);
                computerNode.setLabel(model.getLabel());
                return computerNode;
            case LOCAL:
                final Supplier<DTreeLocalNode> lCreator = () -> {
                    final Group group = entityManager.getReference(Group.class, model.getGroupId());
                    return new DTreeLocalNode(group);
                };
                final DTreeLocalNode localNode = getChild(parent, model.getId(), lCreator);
                update(localNode, model);
                updateOutputs(localNode.getDeclaredTyping(), model.getOutputs());
                return localNode;
            case DECIDER:
                final IDecider decider = entityManager.getReference(BTreeDecider.class, model.getDeciderId());
                final DTreeDeciderNode deciderNode = getChild(parent, model.getId(), DTreeDeciderNode::new);
                deciderNode.setDecider(decider);
                deciderNode.setLabel(model.getLabel());
                deciderNode.setNegated(model.getNegated());
                return deciderNode;
            case DECISION:
                final Supplier<DTreeDecisionNode> dCreator = () -> {
                    final Group group = entityManager.getReference(Group.class, model.getGroupId());
                    return new DTreeDecisionNode(group);
                };
                final DTreeDecisionNode decisionNode = getChild(parent, model.getId(), dCreator);
                update(decisionNode, model);
                decisionNode.setNegated(model.getNegated());
                return decisionNode;
            default:
                final DTreeDefaultNode defaultNode = getChild(parent, model.getId(), DTreeDefaultNode::new);
                defaultNode.setLabel(model.getLabel());
                return defaultNode;
        }
    }

    /**
     * Update the common properties of the given {@code entity} from the source {@code model}.
     *
     * @param entity the entity
     * @param model  the script node
     */
    private void update(final DTreeScriptNode entity, final Computer model) {
        entity.setScript(model.getScript());
        entity.setLabel(model.getLabel());
        entity.setDescription(model.getDescription());
        entity.setShared(model.getShared());
        entity.setTemplate(entityManager.getReference(Template.class, model.getTemplateId()));
        updateInputs(entity.getTyping(), model.getInputs());
    }

    /**
     * Update the given {@code inputs} list from the specified {@code typing}.
     *
     * @param inputs the persistent inputs
     * @param typing the API inputs typing
     */
    private void updateInputs(final Map<String, DTreeNode.Input> inputs, final List<Input> typing) {
        if (typing == null || typing.isEmpty()) {
            inputs.clear();
            return;
        }
        inputs.keySet().stream().filter(n -> typing.stream().noneMatch(t -> n.equals(t.getVariable())))
                .collect(toList())
                .forEach(inputs::remove);

        for (final Input input : typing) {
            final DTreeNode.Input entity = inputs.computeIfAbsent(input.getVariable(), (k) -> new DTreeNode.Input());
            entity.setLabel(input.getLabel());
            entity.setDescription(input.getDescription());
            entity.setOptional(input.getOptional());
            entity.setType(entityManager.getReference(io.github.up2jakarta.dte.jpa.entities.Type.class, input.getTypeId()));
        }
    }

    /**
     * Update the given {@code outputs} list from the specified {@code typing}.
     *
     * @param outputs the persistent outputs
     * @param typing  the API outputs typing
     */
    private void updateOutputs(final Map<String, DTreeNode.Output> outputs, final List<Output> typing) {
        if (typing == null || typing.isEmpty()) {
            outputs.clear();
            return;
        }
        outputs.keySet().stream().filter(n -> typing.stream().noneMatch(t -> n.equals(t.getVariable())))
                .collect(toList())
                .forEach(outputs::remove);

        for (final Output out : typing) {
            final DTreeNode.Output entity = outputs.computeIfAbsent(out.getVariable(), (k) -> new DTreeNode.Output());
            entity.setLabel(out.getLabel());
            entity.setDescription(out.getDescription());
            entity.setOptional(out.getOptional());
            entity.setShared(out.getShared());
            entity.setType(entityManager.getReference(Type.class, out.getTypeId()));
        }
    }
}
