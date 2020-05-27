package io.github.up2jakarta.dte.exe.loader;

/**
 * Superclass of any exception thrown parsing phase.
 *
 * @author A.ABBESSI
 */
public class ParsingException extends RuntimeException {

    /**
     * Public constructor for ParsingException.
     *
     * @param msg the message
     */
    public ParsingException(final String msg) {
        super(msg);
    }
}
