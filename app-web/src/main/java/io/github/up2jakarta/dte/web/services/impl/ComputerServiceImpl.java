package io.github.up2jakarta.dte.web.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.up2jakarta.dte.jpa.entities.DTreeComputer;
import io.github.up2jakarta.dte.jpa.entities.dtn.DTreePlainComputer;
import io.github.up2jakarta.dte.jpa.managers.ComputerManager;
import io.github.up2jakarta.dte.jpa.models.Computer;
import io.github.up2jakarta.dte.jpa.repositories.ComputerRepository;
import io.github.up2jakarta.dte.web.exceptions.BadRequestException;
import io.github.up2jakarta.dte.web.exceptions.NotFoundException;
import io.github.up2jakarta.dte.web.services.ComputerService;

import java.util.List;
import java.util.Optional;

/**
 * API Computer Service implementation.
 */
@Service
@Transactional
@Primary
public class ComputerServiceImpl implements ComputerService {

    private final ComputerRepository repository;
    private final ComputerManager manager;

    /**
     * Public constructor for ComputerServiceImpl.
     *
     * @param repository the repository
     * @param manager    the manager
     */
    @Autowired
    public ComputerServiceImpl(final ComputerRepository repository, final ComputerManager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    @Transactional(readOnly = true)
    @Override
    public Computer find(final Long id) {
        final DTreeComputer entity = repository.find(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        return manager.convert(entity);
    }

    @Override
    public void delete(final Long id) {
        final DTreeComputer entity = repository.find(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        repository.remove(entity);
    }

    @Override
    public Computer update(final Long id, final Computer model) {
        final DTreeComputer entity = repository.find(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        if (entity instanceof DTreePlainComputer ^ model.getType() == Computer.Type.PLAIN) {
            throw new BadRequestException("type", "attempt to modify type");
        }
        manager.update(entity, model);
        return manager.convert(repository.merge(entity));
    }

    @Override
    public Computer create(final Computer model) {
        final DTreeComputer entity = manager.create(model);
        repository.persist(entity);
        return manager.convert(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public Long find(final int groupId, final String label) {
        return Optional.ofNullable(repository.find(groupId, label))
                .map(DTreeComputer::getId)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Computer> search(final int groupId, final List<String> inputs, final List<String> outputs) {
        return manager.convert(repository.search(groupId, inputs, outputs));
    }
}
