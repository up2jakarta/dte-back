package io.github.up2jakarta.dte.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.github.up2jakarta.dte.jpa.views.Key;
import io.github.up2jakarta.dte.jpa.views.VCScript;
import io.github.up2jakarta.dte.jpa.views.VDScript;
import io.github.up2jakarta.dte.web.exceptions.BadRequestException;
import io.github.up2jakarta.dte.web.models.ItemModel;
import io.github.up2jakarta.dte.web.services.ScriptService;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * API endpoint for scripts.
 */
@RequestMapping(path = "/scripts")
@RestController
@Validated
public class ScriptController {

    private static final String GROUP = "groupId";
    private static final String INPUTS = "inputs";
    private static final String OUTPUTS = "outputs";

    private final ScriptService service;

    /**
     * Public constructor for ScriptController.
     *
     * @param service the transactional service
     */
    @Autowired
    public ScriptController(final ScriptService service) {
        this.service = service;
    }

    /**
     * @see ScriptService#find(Key)
     */
    @GetMapping(path = "/{key}/{id}", produces = APPLICATION_JSON_VALUE)
    public VDScript find(@NotNull @PathVariable(name = "id") final Long id,
                         @NotNull @PathVariable(name = "key") final Character key) {
        return service.find(new Key(id, key));
    }

    /**
     * @see ScriptService#find(long)
     */
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public VCScript find(@NotNull @PathVariable(name = "id") final Long id) {
        return service.find(id);
    }

    /**
     * @see ScriptService#search(int, List, List)
     */
    @GetMapping(path = "/computers", produces = APPLICATION_JSON_VALUE)
    public List<ItemModel<Long>> search(
            @NotNull @RequestParam(name = GROUP) final Integer groupId,
            @RequestParam(required = false, name = INPUTS) final List<@NotEmpty String> inputs,
            @RequestParam(required = false, name = OUTPUTS) final List<@NotEmpty String> outputs) {
        return service.search(groupId, inputs, outputs);
    }

    /**
     * @see ScriptService#search(int, List)
     */
    @GetMapping(path = "/deciders", produces = APPLICATION_JSON_VALUE)
    public List<ItemModel<Key>> search(
            @NotNull @RequestParam(name = GROUP) final Integer groupId,
            @RequestParam(required = false, name = INPUTS) final List<@NotEmpty String> inputs) {
        return service.search(groupId, inputs);
    }

    /**
     * @see ScriptService#compile(int, String, Map)
     */
    @GetMapping(path = "/compile", produces = APPLICATION_JSON_VALUE)
    public Map<String, Integer> compile(
            @NotNull @RequestParam(value = "templateId") final Integer templateId,
            @NotEmpty @RequestParam(name = "script") final String script,
            @RequestParam(required = false, name = "inputs", defaultValue = "") final List<@NotEmpty String> inputs,
            @RequestParam(required = false, name = "types", defaultValue = "") final List<@NotNull Integer> types) {
        if (inputs.size() != types.size()) {
            throw new BadRequestException("types", "Cannot map [inputs/types]");
        }
        final Map<String, Integer> typing = new HashMap<>(inputs.size());
        final Iterator<Integer> it = types.iterator();
        for (final String input : inputs) {
            typing.put(input, it.next());
        }
        return service.compile(templateId, script, typing);
    }

}
