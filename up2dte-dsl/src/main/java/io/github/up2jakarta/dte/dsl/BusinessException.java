package io.github.up2jakarta.dte.dsl;

/**
 * Business Exception can be thrown during during engine execution.
 *
 * @author A.ABBESSI
 */
public class BusinessException extends RuntimeException {

    private final String script;

    /**
     * Public constructor for BusinessException.
     *
     * @param script  the script name
     * @param message the error message
     */
    public BusinessException(final String script, final String message) {
        super(message);
        this.script = script;
    }

    /**
     * @return the name of script that this exception is thrown in.
     */
    public String getScript() {
        return script;
    }

}
