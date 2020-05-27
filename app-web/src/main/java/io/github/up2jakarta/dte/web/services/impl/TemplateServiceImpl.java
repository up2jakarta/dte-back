package io.github.up2jakarta.dte.web.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.up2jakarta.dte.jpa.repositories.TemplateRepository;
import io.github.up2jakarta.dte.web.managers.TemplateManager;
import io.github.up2jakarta.dte.web.models.TemplateModel;
import io.github.up2jakarta.dte.web.services.TemplateService;

import java.util.List;

/**
 * API Template Service implementation.
 */
@Service
@Transactional
@Primary
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository repository;
    private final TemplateManager manager;

    /**
     * Public constructor for TemplateServiceImpl.
     *
     * @param repo the repository
     * @param mng  the manager
     */
    @Autowired
    public TemplateServiceImpl(final TemplateRepository repo, final TemplateManager mng) {
        this.repository = repo;
        this.manager = mng;
    }


    @Transactional(readOnly = true)
    @Override
    public List<TemplateModel> search(final int groupId) {
        return manager.convert(repository.search(groupId));
    }
}
