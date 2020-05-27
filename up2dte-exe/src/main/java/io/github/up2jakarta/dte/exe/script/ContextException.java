package io.github.up2jakarta.dte.exe.script;

import groovy.lang.MissingPropertyException;

/**
 * RuntimeException thrown by scripts when a variable is not present in the script execution context.
 *
 * @author A.ABBESSI
 */
@SuppressWarnings("unused")
public class ContextException extends RuntimeException {

    private static final String FORMAT = "No such variable [%s]";
    private final String script;

    /**
     * Public constructor for MissingVariableException.
     *
     * @param script the script name
     * @param cause  the cause exception
     */
    public ContextException(final String script, final MissingPropertyException cause) {
        super(String.format(FORMAT, cause.getProperty()), cause);
        this.script = script;
    }

    /**
     * @return the variable name.
     */
    public String getVariableName() {
        return getCause().getProperty();
    }

    /**
     * @return the name of script that this exception is thrown in.
     */
    public String getScript() {
        return script;
    }

    @Override
    public MissingPropertyException getCause() {
        return (MissingPropertyException) super.getCause();
    }
}
