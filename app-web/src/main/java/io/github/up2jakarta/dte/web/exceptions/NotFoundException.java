package io.github.up2jakarta.dte.web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * API Resource Not Found Exception.
 */
public class NotFoundException extends HttpException {

    /**
     * Public constructor for NotFoundException.
     */
    public NotFoundException() {
        super(HttpStatus.NOT_FOUND, null, "resource not found");
    }
}
