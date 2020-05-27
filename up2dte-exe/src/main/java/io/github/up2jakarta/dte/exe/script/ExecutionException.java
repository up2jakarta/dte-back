package io.github.up2jakarta.dte.exe.script;

/**
 * Superclass of any exception thrown during the execution of the script.
 *
 * @author A.ABBESSI
 */
public class ExecutionException extends RuntimeException {

    private final String script;

    /**
     * Public constructor for ExecutionException.
     *
     * @param script  the script name
     * @param message the message details
     */
    public ExecutionException(final String script, final String message) {
        super(message);
        this.script = script;
    }

    /**
     * Public constructor for ExecutionException.
     *
     * @param script the script name
     * @param cause  the cause exception
     */
    public ExecutionException(final String script, final Exception cause) {
        super(cause);
        this.script = script;
    }

    /**
     * @return the name of script that this exception is thrown in.
     */
    public String getScript() {
        return script;
    }
}
