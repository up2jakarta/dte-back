package io.github.up2jakarta.dte.web.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.grp.AbstractGroup;
import io.github.up2jakarta.dte.jpa.repositories.GroupRepository;
import io.github.up2jakarta.dte.web.exceptions.NotFoundException;
import io.github.up2jakarta.dte.web.managers.GroupManager;
import io.github.up2jakarta.dte.web.models.GroupModel;
import io.github.up2jakarta.dte.web.services.GroupService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * API Group Service implementation.
 */
@Service
@Transactional
@Primary
public class GroupServiceImpl implements GroupService {

    private final GroupRepository repository;
    private final GroupManager manager;

    /**
     * Public constructor for GroupServiceImpl.
     *
     * @param repo the repository
     * @param mng  the manager
     */
    @Autowired
    public GroupServiceImpl(final GroupRepository repo, final GroupManager mng) {
        this.repository = repo;
        this.manager = mng;
    }

    @Transactional(readOnly = true)
    @Override
    public GroupModel find(final int id) {
        final Group entity = Optional.ofNullable(repository.find(id)).orElseThrow(NotFoundException::new);
        return manager.convert(entity);
    }

    @Override
    public void delete(final int id) {
        final Group entity = Optional.ofNullable(repository.find(id)).orElseThrow(NotFoundException::new);
        // todo check has D/B trees or types / sub-groups ...
        repository.remove(entity);
    }

    @Override
    public GroupModel update(final int id, final GroupModel model) {
        final Group entity = Optional.ofNullable(repository.find(id)).orElseThrow(NotFoundException::new);
        manager.update(entity, model);
        return manager.convert(repository.merge(entity));
    }

    @Override
    public GroupModel create(final GroupModel model) {
        final Group entity = manager.create(model);
        repository.persist(entity);
        return manager.convert(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GroupModel> search(final Integer parentId) {
        return manager.convert(repository.search(parentId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<GroupModel> tree() {
        return manager.build(repository.tree());
    }

    @Transactional(readOnly = true)
    @Override
    public List<GroupModel> lov() {
        final List<AbstractGroup> list = new ArrayList<>(repository.tree());
        list.add(repository.getMain());
        return list.stream().map(e -> manager.fill(e, new GroupModel())).collect(Collectors.toList());
    }
}
