package io.github.up2jakarta.dte.web.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * API Error Model.
 */
@SuppressWarnings("unused")
@JsonPropertyOrder({"type", "origin", "message"})
public final class ErrorModel {

    private final Type type;
    private final String origin;
    private final String message;

    /**
     * Public constructor for ApiError.
     *
     * @param type   the error type
     * @param origin the error origin
     * @param msg    the message details
     */
    public ErrorModel(final Type type, final String origin, final String msg) {
        this.origin = origin;
        this.message = msg;
        this.type = type;
    }

    /**
     * Public constructor for ApiError.
     *
     * @param type the error type
     * @param ex   the java exception
     */
    public ErrorModel(final Type type, final Throwable ex) {
        this(type, ex.getClass().getName(), ex.getMessage());
    }

    /**
     * @return the origin of the error
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * @return the message details.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the error type
     */
    public Type getType() {
        return type;
    }

    /**
     * API Error Type.
     */
    public enum Type {
        /**
         * Network.
         */
        NET,
        /**
         * DB Connection.
         */
        DBC,
        /**
         * Server.
         */
        SRV,
        /**
         * Web API.
         */
        API
    }
}
