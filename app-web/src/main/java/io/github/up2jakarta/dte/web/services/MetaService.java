package io.github.up2jakarta.dte.web.services;

import io.github.up2jakarta.dte.web.models.MetaModel;

/**
 * Service responsible for providing application meta data.
 */
public interface MetaService {

    /**
     * Returns the database version ans some other details.
     *
     * @return the application details
     */
    MetaModel about();

    /**
     * Check the server status.
     *
     * @return the application status
     * @throws Throwable if the database connection fails
     */
    String status() throws Throwable;
}
