package io.github.up2jakarta.dte.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.github.up2jakarta.dte.jpa.models.Decider;
import io.github.up2jakarta.dte.jpa.validators.BinaryTree;
import io.github.up2jakarta.dte.web.services.DeciderService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * API endpoint for deciders.
 */
@RequestMapping(path = "/deciders")
@RestController
@Validated
public class DeciderController {

    private final DeciderService service;

    /**
     * Public constructor for DeciderController.
     *
     * @param service the transactional service
     */
    @Autowired
    public DeciderController(final DeciderService service) {
        this.service = service;
    }

    /**
     * @see DeciderService#find(Long)
     */
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Decider find(@NotNull @PathVariable(name = "id") final Long id) {
        return service.find(id);
    }

    /**
     * @see DeciderService#delete(Long)
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void delete(@NotNull @PathVariable(name = "id") final Long id) {
        service.delete(id);
    }

    /**
     * @see DeciderService#update(Long, Decider)
     */
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Decider update(@NotNull @PathVariable(name = "id") final Long id,
                          @Valid @BinaryTree @RequestBody final Decider model) {
        return service.update(id, model);
    }

    /**
     * @see DeciderService#create(Decider)
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Decider create(@Valid @BinaryTree @RequestBody final Decider model) {
        return service.create(model);
    }

    /**
     * @see DeciderService#find(int, String)
     */
    @GetMapping(path = "exists", produces = APPLICATION_JSON_VALUE)
    public Long exists(@NotNull @RequestParam(name = "groupId") final Integer groupId,
                       @NotEmpty @RequestParam(name = "label") final String label) {
        return service.find(groupId, label);
    }

    /**
     * @see DeciderService#search(int, List)
     */
    @GetMapping(path = "search", produces = APPLICATION_JSON_VALUE)
    public List<Decider> search(@NotNull @RequestParam(name = "groupId") final Integer groupId,
                                @RequestParam(required = false, name = "inputs") final List<String> inputs) {
        return service.search(groupId, inputs);
    }

}
