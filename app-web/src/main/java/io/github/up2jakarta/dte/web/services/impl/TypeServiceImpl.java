package io.github.up2jakarta.dte.web.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.up2jakarta.dte.jpa.repositories.TypeRepository;
import io.github.up2jakarta.dte.web.managers.TypeManager;
import io.github.up2jakarta.dte.web.models.TypeModel;
import io.github.up2jakarta.dte.web.services.TypeService;

import java.util.List;

/**
 * API Type Service implementation.
 */
@Service
@Transactional
@Primary
public class TypeServiceImpl implements TypeService {

    private final TypeRepository repository;
    private final TypeManager manager;

    /**
     * Public constructor for TypeServiceImpl.
     *
     * @param repo the repository
     * @param mng  the manager
     */
    @Autowired
    public TypeServiceImpl(final TypeRepository repo, final TypeManager mng) {
        this.repository = repo;
        this.manager = mng;
    }


    @Transactional(readOnly = true)
    @Override
    public List<TypeModel> search(final int groupId) {
        return manager.convert(repository.search(groupId));
    }
}
