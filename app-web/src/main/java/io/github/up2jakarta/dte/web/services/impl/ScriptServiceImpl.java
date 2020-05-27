package io.github.up2jakarta.dte.web.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.up2jakarta.dte.jpa.DynamicEngine;
import io.github.up2jakarta.dte.jpa.repositories.ScriptRepository;
import io.github.up2jakarta.dte.jpa.views.Key;
import io.github.up2jakarta.dte.jpa.views.VCScript;
import io.github.up2jakarta.dte.jpa.views.VDScript;
import io.github.up2jakarta.dte.web.exceptions.NotFoundException;
import io.github.up2jakarta.dte.web.models.ItemModel;
import io.github.up2jakarta.dte.web.services.ScriptService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * API Computer Service implementation.
 */
@Service
@Transactional(readOnly = true)
public class ScriptServiceImpl implements ScriptService {

    private final ScriptRepository repository;
    private final DynamicEngine engine;

    /**
     * Public constructor for ScriptServiceImpl.
     *
     * @param repository the script repository
     * @param engine     the JPA engine
     */
    @Autowired
    public ScriptServiceImpl(final ScriptRepository repository, final DynamicEngine engine) {
        this.repository = repository;
        this.engine = engine;
    }

    @Override
    public VDScript find(final Key id) {
        final VDScript entity = repository.find(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        return entity;
    }

    @Override
    public VCScript find(final long id) {
        final VCScript entity = repository.find(id);
        if (entity == null) {
            throw new NotFoundException();
        }
        return entity;
    }

    @Override
    public List<ItemModel<Key>> search(final int groupId, final List<String> inputs) {
        return repository.search(groupId, inputs).stream()
                .map(i -> new ItemModel<>(i.getId(), i.getLabel()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemModel<Long>> search(final int groupId, final List<String> inputs, final List<String> outputs) {
        return repository.search(groupId, inputs, outputs).stream()
                .map(i -> new ItemModel<>(i.getId(), i.getLabel()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> compile(final int templateId, final String script, final Map<String, Integer> inputs) {
        return engine.compile(templateId, script, inputs);
    }
}
