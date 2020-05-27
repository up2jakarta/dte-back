package io.github.up2jakarta.dte.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.github.up2jakarta.dte.web.models.TemplateModel;
import io.github.up2jakarta.dte.web.services.TemplateService;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * API endpoint for templates.
 */
@RequestMapping(path = "/templates")
@RestController
@Validated
public class TemplateController {

    private final TemplateService service;

    /**
     * Public constructor for TTemplateController.
     *
     * @param service the transactional service
     */
    @Autowired
    public TemplateController(final TemplateService service) {
        this.service = service;
    }

    /**
     * @see TemplateService#search(int)
     */
    @GetMapping(path = "", produces = APPLICATION_JSON_VALUE)
    public List<TemplateModel> search(@NotNull @RequestParam(name = "groupId") final Integer groupId) {
        return service.search(groupId);
    }

}
