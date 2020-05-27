package io.github.up2jakarta.dte.web.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.up2jakarta.dte.jpa.entities.BTreeDecider;
import io.github.up2jakarta.dte.jpa.entities.btn.BTreePlainDecider;
import io.github.up2jakarta.dte.jpa.managers.DeciderManager;
import io.github.up2jakarta.dte.jpa.models.Decider;
import io.github.up2jakarta.dte.jpa.repositories.DeciderRepository;
import io.github.up2jakarta.dte.web.exceptions.BadRequestException;
import io.github.up2jakarta.dte.web.exceptions.NotFoundException;
import io.github.up2jakarta.dte.web.services.DeciderService;

import java.util.List;
import java.util.Optional;

/**
 * API Decider Service implementation.
 */
@Service
@Transactional
@Primary
public class DeciderServiceImpl implements DeciderService {

    private final DeciderRepository repository;
    private final DeciderManager manager;

    /**
     * Public constructor for DeciderServiceImpl.
     *
     * @param repository the repository
     * @param manager    the manager
     */
    @Autowired
    public DeciderServiceImpl(final DeciderRepository repository, final DeciderManager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    @Transactional(readOnly = true)
    @Override
    public Decider find(final Long id) {
        final BTreeDecider entity = repository.find(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        return manager.convert(entity);
    }

    @Override
    public void delete(final Long id) {
        final BTreeDecider entity = repository.find(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        repository.remove(entity);
    }

    @Override
    public Decider update(final Long id, final Decider model) {
        final BTreeDecider entity = repository.find(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        if (entity instanceof BTreePlainDecider ^ model.getType() == Decider.Type.PLAIN) {
            throw new BadRequestException("type", "attempt to modify type");
        }
        manager.update(entity, model);
        return manager.convert(repository.merge(entity));
    }

    @Override
    public Decider create(final Decider model) {
        final BTreeDecider entity = manager.create(model);
        repository.persist(entity);
        return manager.convert(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public Long find(final int groupId, final String label) {
        return Optional.ofNullable(repository.find(groupId, label))
                .map(BTreeDecider::getId)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Decider> search(final int groupId, final List<String> inputs) {
        return manager.convert(repository.search(groupId, inputs));
    }
}
