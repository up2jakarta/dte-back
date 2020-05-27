package io.github.up2jakarta.dte.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.github.up2jakarta.dte.web.markers.Post;
import io.github.up2jakarta.dte.web.markers.Put;
import io.github.up2jakarta.dte.web.models.GroupModel;
import io.github.up2jakarta.dte.web.services.GroupService;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * API endpoint for groups.
 */
@RequestMapping(path = "/groups")
@RestController
@Validated
public class GroupController {

    private final GroupService service;

    /**
     * Public constructor for GroupController.
     *
     * @param service the transactional service
     */
    @Autowired
    public GroupController(final GroupService service) {
        this.service = service;
    }

    /**
     * @see GroupService#find(int)
     */
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public GroupModel find(@NotNull @PathVariable(name = "id") final Integer id) {
        return service.find(id);
    }

    /**
     * @see GroupService#delete(int)
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void delete(@NotNull @PathVariable(name = "id") final Integer id) {
        service.delete(id);
    }

    /**
     * @see GroupService#update(int, GroupModel)
     */
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public GroupModel update(@NotNull @PathVariable(name = "id") final Integer id,
                             @Validated(Put.class) @RequestBody final GroupModel model) {
        return service.update(id, model);
    }

    /**
     * @see GroupService#create(GroupModel)
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    // TODO create security extension like canCreateGroupOn('#model.parentId')
    public GroupModel create(@Validated(Post.class) @RequestBody final GroupModel model) {
        return service.create(model);
    }

    /**
     * @see GroupService#search(Integer)
     */
    @GetMapping(path = "/search", produces = APPLICATION_JSON_VALUE)
    public List<GroupModel> search(@RequestParam(name = "parentId", required = false) final Integer parentId) {
        return service.search(parentId);
    }

    /**
     * @see GroupService#search(Integer)
     */
    @GetMapping(path = "/tree", produces = APPLICATION_JSON_VALUE)
    public List<GroupModel> tree(final Authentication user) {
        return service.tree();
    }

    /**
     * @see GroupService#lov() ()
     */
    @GetMapping(path = "", produces = APPLICATION_JSON_VALUE)
    public List<GroupModel> lov() {
        return service.lov();
    }

}
