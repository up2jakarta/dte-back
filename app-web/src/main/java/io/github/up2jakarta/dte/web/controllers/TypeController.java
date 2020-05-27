package io.github.up2jakarta.dte.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.github.up2jakarta.dte.web.models.TypeModel;
import io.github.up2jakarta.dte.web.services.TypeService;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * API endpoint for types.
 */
@RequestMapping(path = "/types")
@RestController
@Validated
public class TypeController {

    private final TypeService service;

    /**
     * Public constructor for TypeController.
     *
     * @param service the transactional service
     */
    @Autowired
    public TypeController(final TypeService service) {
        this.service = service;
    }

    /**
     * @see TypeService#search(int)
     */
    @GetMapping(path = "", produces = APPLICATION_JSON_VALUE)
    public List<TypeModel> search(@NotNull @RequestParam(name = "groupId") final Integer groupId) {
        return service.search(groupId);
    }

}
