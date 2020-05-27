package io.github.up2jakarta.dte.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.up2jakarta.dte.web.models.MetaModel;
import io.github.up2jakarta.dte.web.models.UserModel;
import io.github.up2jakarta.dte.web.services.MetaService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

/**
 * API endpoint for metadata.
 *
 * @author A.ABBESSI
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("")
public class MetaController {

    private final MetaService service;

    /**
     * Public constructor for MetaController.
     *
     * @param service the transactional service
     */
    @Autowired
    public MetaController(final MetaService service) {
        this.service = service;
    }

    /**
     * @see MetaService#about()
     */
    @GetMapping(value = {"", "/version"}, produces = APPLICATION_JSON_VALUE)
    public MetaModel about() {
        return service.about();
    }

    /**
     * @see MetaService#status()
     */
    @GetMapping(value = {"/heartbeat", "/status"}, produces = {TEXT_PLAIN_VALUE, APPLICATION_JSON_VALUE})
    public String status() throws Throwable {
        return service.status();
    }

    /**
     * Return the current logged API user.
     *
     * @param current the logged user
     * @return the connected user
     */
    @RequestMapping("/me")
    @Secured("ROLE_USER")
    public UserModel user(final Authentication current) {
        return new UserModel(current);
    }
}
