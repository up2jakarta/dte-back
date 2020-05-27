package io.github.up2jakarta.dte.web.exceptions;

import io.github.up2jakarta.dte.web.config.ExceptionHandlerAdvice;
import org.springframework.http.HttpStatus;

/**
 * API Bad Request Exception. *
 *
 * @see ExceptionHandlerAdvice#apiException
 */
public class BadRequestException extends HttpException {

    /**
     * Public constructor for BadRequestException.
     *
     * @param origin  the origin of the error
     * @param message the detail message
     */
    public BadRequestException(final String origin, final String message) {
        super(HttpStatus.BAD_REQUEST, origin, message);
    }
}
