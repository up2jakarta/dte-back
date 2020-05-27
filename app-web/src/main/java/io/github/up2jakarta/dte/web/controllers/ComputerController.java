package io.github.up2jakarta.dte.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.github.up2jakarta.dte.jpa.models.Computer;
import io.github.up2jakarta.dte.jpa.validators.DecisionTree;
import io.github.up2jakarta.dte.web.services.ComputerService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * API endpoint for computers.
 */
@RequestMapping(path = "/computers")
@RestController
@Validated
public class ComputerController {

    private final ComputerService service;

    /**
     * Public constructor for ComputerController.
     *
     * @param service the transactional service
     */
    @Autowired
    public ComputerController(final ComputerService service) {
        this.service = service;
    }

    /**
     * @see ComputerService#find(Long)
     */
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Computer find(@NotNull @PathVariable(name = "id") final Long id) {
        return service.find(id);
    }

    /**
     * @see ComputerService#delete(Long)
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void delete(@NotNull @PathVariable(name = "id") final Long id) {
        service.delete(id);
    }

    /**
     * @see ComputerService#update(Long, Computer)
     */
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Computer update(@NotNull @PathVariable(name = "id") final Long id,
                           @Valid @DecisionTree @RequestBody final Computer model) {
        return service.update(id, model);
    }

    /**
     * @see ComputerService#create(Computer)
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Computer create(@Valid @DecisionTree @RequestBody final Computer model) {
        return service.create(model);
    }

    /**
     * @see ComputerService#find(int, String)
     */
    @GetMapping(path = "/exists", produces = APPLICATION_JSON_VALUE)
    public Long exists(@NotNull @RequestParam(name = "groupId") final Integer groupId,
                       @NotEmpty @RequestParam(name = "label") final String label) {
        return service.find(groupId, label);
    }

    /**
     * @see ComputerService#search(int, List, List)
     */
    @GetMapping(path = "/search", produces = APPLICATION_JSON_VALUE)
    public List<Computer> search(@NotNull @RequestParam(name = "groupId") final Integer groupId,
                                 @RequestParam(required = false, name = "inputs") final List<String> inputs,
                                 @RequestParam(required = false, name = "outputs") final List<String> outputs) {
        return service.search(groupId, inputs, outputs);
    }

}
