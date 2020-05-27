package io.github.up2jakarta.dte.web.exceptions;

import org.springframework.http.HttpStatus;

/**
 * API HTTP Exception.
 */
public class HttpException extends RuntimeException {

    private final HttpStatus status;
    private final String origin;

    /**
     * Public constructor HttpException.
     *
     * @param origin  the origin of the error
     * @param message the detail message
     * @param status  the HTTP status
     */
    public HttpException(final HttpStatus status, final String origin, final String message) {
        super(message);
        this.status = status;
        this.origin = origin;
    }

    /**
     * @return the origin of the error.
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * @return the HTTP status
     */
    public HttpStatus getStatus() {
        return status;
    }
}
