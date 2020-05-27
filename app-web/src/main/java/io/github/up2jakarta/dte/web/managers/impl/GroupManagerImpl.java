package io.github.up2jakarta.dte.web.managers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.grp.AbstractGroup;
import io.github.up2jakarta.dte.jpa.entities.grp.WorkGroup;
import io.github.up2jakarta.dte.jpa.entities.grp.Workspace;
import io.github.up2jakarta.dte.jpa.repositories.GroupRepository;
import io.github.up2jakarta.dte.web.exceptions.BadRequestException;
import io.github.up2jakarta.dte.web.managers.GroupManager;
import io.github.up2jakarta.dte.web.models.GroupModel;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * {@link GroupManager} implementation.
 */
@Component
public class GroupManagerImpl implements GroupManager {

    private final GroupRepository repository;

    /**
     * Public constructor for GroupManagerImpl.
     *
     * @param repository the group repository
     */
    @Autowired
    public GroupManagerImpl(final GroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<GroupModel> build(final List<? extends Group> list) {
        final Iterator<? extends Group> it = list.iterator();
        final Map<Integer, GroupModel> map = new HashMap<>(list.size());
        final List<GroupModel> workspaces = new ArrayList<>();
        while (it.hasNext()) {
            final Group group = it.next();
            final GroupModel child = this.fill(group, new GroupModel());
            if (group instanceof Workspace) {
                workspaces.add(child);
            } else {
                // The ORDER BY guarantees that the parents are created before their children
                map.get(group.getParent().getId()).addChild(child);
            }
            map.put(child.getId(), child);
        }
        return workspaces;
    }

    @Override
    public List<GroupModel> convert(final List<? extends Group> list) {
        return list.stream()
                .map(e -> {
                    final GroupModel m = this.fill(e, new GroupModel());
                    m.setDescription(e.getDescription());
                    return m;
                })
                .collect(toList());
    }

    @Override
    public GroupModel convert(final Group entity) {
        final GroupModel model = new GroupModel();
        this.fill(entity, model);
        model.setDescription(entity.getDescription());
        model.setParentId(entity.getParent().getId());
        return model;
    }

    @Override
    public Group create(final GroupModel model) {
        if (model.getParentId() == null) {
            throw new BadRequestException("parentId", "must be not null");
        }
        if (repository.getMain().getId().equals(model.getParentId())) {
            return update(new Workspace(repository.getMain()), model);
        }
        final Group parent = repository.find(model.getParentId());
        if (parent == null) {
            throw new BadRequestException("parentId", "reference not found");
        }
        if (parent instanceof Workspace) {
            return update(new WorkGroup((Workspace) parent), model);
        }
        throw new BadRequestException("parentId", "invalid parent reference");
    }

    @Override
    public Group update(final Group entity, final GroupModel model) {
        final Group found = repository.find(entity.getParent().getId(), model.getLabel());
        if (found != null && !found.getId().equals(model.getId())) {
            throw new BadRequestException("label", "must be unique per group");
        }
        entity.setLabel(model.getLabel());
        entity.setColor(model.getColor());
        entity.setIcon(model.getIcon());
        entity.setDescription(model.getDescription());
        return entity;
    }

    @Override
    public GroupModel fill(final AbstractGroup entity, final GroupModel model) {
        model.setLocked(entity instanceof WorkGroup);
        model.setId(entity.getId());
        model.setLabel(entity.getLabel());
        model.setColor(entity.getColor());
        model.setIcon(entity.getIcon());
        return model;
    }
}
