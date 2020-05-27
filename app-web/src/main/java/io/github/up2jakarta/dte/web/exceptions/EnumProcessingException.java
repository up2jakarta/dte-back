package io.github.up2jakarta.dte.web.exceptions;


import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Generic exception for enum validation.
 */
public class EnumProcessingException extends JsonProcessingException {

    private final String origin;

    /**
     * Public constructor for EnumProcessingException.
     *
     * @param msg    the message details
     * @param origin the origin of the error
     */
    public EnumProcessingException(final String msg, final String origin) {
        super(msg);
        this.origin = origin;
    }

    /**
     * @return the origin of the error, field name or the class name.
     */
    public String getOrigin() {
        return origin;
    }
}
